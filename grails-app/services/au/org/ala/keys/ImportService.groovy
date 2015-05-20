package au.org.ala.keys

import grails.transaction.Transactional
import org.apache.commons.io.FileUtils

import static grails.async.Promises.onComplete
import static grails.async.Promises.task

/**
 * Created by a on 8/12/14.
 */
@Transactional
class ImportService {
    static transactional = false

    def importDeltaFileService
    def importLucidXmlService
    def importSddXmlService
    def importTextService
    def importStatusService
    def searchTaxonLsidService
    def zipService
    def authorisationService
    def keyService
    def taxonService

    private def importKey(Key key, boolean updateLsids = true) {
        try {
            //create
            if (key.filename.toLowerCase().endsWith(".dlt")) {
                importDeltaFileService.importDlt(key)
            } else if (key.filename.toLowerCase().endsWith(".xml")) {
                if (importLucidXmlService.importXml(key)) {

                } else if (importSddXmlService.importXml(key)) {

                }
            } else if (key.filename.toLowerCase().endsWith(".txt") || key.filename.toLowerCase().endsWith(".csv")) {
                importTextService.importText(key)

            }

            key.status = "loaded"
            importStatusService.put(key.id, ["loaded", 100])

        } catch (e) {
            key.status = "failed"
            importStatusService.put(key.id, ["failed", 0])

            log.error("failed to import: " + keyService.getFilePath(key), e)
        }

        //update status
        def i = Key.executeUpdate("update Key set status='" + key.status + "' where id=" + key.id)
    }

    /**
     * creates a new Key that returns immediately and runs the import in the background
     *
     * @param file
     * @return
     */
    def importFile(Project project, Key existingKey, File file, String name) {

        def key
        if (existingKey == null) {
            key = new Key(filename: name, status: "loading", project: project)
        } else {
            key = existingKey
            key.filename = name
            key.status = "loading"
        }

        def path = keyService.getFilePath(key)
        FileUtils.copyFile(file, new File(path))

        if (!key.save(flush: true)) {
            key.errors.each() {
                log.error("failed saving datasource: " + it)
            }
        }

        importStatusService.put(key.id, ["loading", 0])

        //do other datasource creation here
        def keys
        if (key.filename.toLowerCase().endsWith(".zip")) {
            keys = zipService.getKeys(key)
        }

        //thread
        def importTask = task {
            //wait for key creation from parent thread transaction
            while (Key.get(key.id) == null) {
                Thread.sleep(100);
            }

            if (key.filename.toLowerCase().endsWith(".zip")) {

                keys.eachWithIndex { ds, i ->
                    importStatusService.put(key.id, ["loading zipped file: " + ds.filename, i / (double) keys.size() * 100])
                    importKey(ds, false)
                }

                searchTaxonLsidService.updateTaxon();

                importStatusService.put(key.id, ["loaded", 100])

                //update status
                def i = Key.executeUpdate("update Key set status='" + key.status + "' where id=" + key.id)
            } else {
                importKey(key)
            }
        }

        //continue
        onComplete([importTask]) { List result ->
            log.debug("finished task: " + result.get(0))
        }


        return key
    }

    /**
     * upload a file for import, or json list of taxon/attribute/value
     *
     *{
     *      apiKey: "123",
     *      alaUserId: "123",
     *      data: [{
     *              lsid: "urn:lsid:123",
     *              attributeId: 123,       //optional
     *              attributeLabel: "text", //optional
     *              attributeUnits: "mm",   //optional
     *              value: "something"
     *            }]
     *}
     *
     * - if no attributeId or Label is supplied, defaults to attributeLabel="attribute"
     * - value can be
     *      - number (can be parsed as a number)
     *      - range (two numbers delimited by '-')
     *      - string
     */
    def importJson(json) {

        def project = Project.get(json.get("projectId"), null);

        def key = Key.findByProjectAndAlaUserIdAndFilename(project, json.get("alaUserId"), null)
        if (key == null) {
            key = new Key(alaUserId: json.get("alaUserId"), status: "adhoc", project: project)
            if (!key.save()) {
                key.errors.each() {
                    log.error(it)
                }
            }
        }

        def values = []
        json.data.each() { data ->
            def attributeId = data.containsKey("attributeId") ? data.get("attributeId") : null

            //default label is ""
            def attributeLabel = data.containsKey("attributeLabel") ? data.get("attributeLabel") : "attribute"

            def attributeUnits = data.containsKey("attributeUnits") ? data.get("attributeUnits") : null
            def value = data.containsKey("value") ? data.get("value") : null
            def lsid = data.containsKey("lsid") ? data.get("lsid") : null

            def attribute
            if (attributeId != null) {
                attribute = Attribute.get(attributeId)
            } else {
                attribute = Attribute.create(attributeLabel)
                attribute.units = attributeUnits
                if (!attribute.save()) {
                    attribute.errors.each() {
                        log.error(it)
                    }
                }
            }
            def r = searchTaxonLsidService.getRecord(lsid)
            def taxon = taxonService.get(lsid, r == null || r.rankClass == null ? null : r.rankClass.scientificName)
            searchTaxonLsidService.updateTaxon(taxon)

            //is it a single number
            def min = parseMin(value)
            def max = parseMax(value)
            def text = parseText(value)

            def newValue = new Value(key: key, taxon: taxon, attribute: attribute, min: min, max: max, text: text)
            if (!newValue.save()) {
                newValue.errors.each() {
                    log.error(it)
                }
            }
            values.add(newValue)
        }

        return values
    }

    def parseMin(value) {
        if (isDouble(value)) {
            return Double.parseDouble(value)
        } else if (isRange(value)) {
            def startN = value.startsWith('-')
            def split = (startN ? value.substring(1) : value).split('-')
            if (split.length == 2 || (split.length == 3 && split[1].trim().length == 0)) {
                return Double.parseDouble(split[0].trim()) * (startN ? -1 : 1)
            }
        } else {
            return null
        }
    }

    def parseMax(value) {
        if (isDouble(value)) {
            return Double.parseDouble(value)
        } else if (isRange(value)) {
            def startN = value.startsWith('-')
            def split = (startN ? value.substring(1) : value).split('-')
            if (split.length == 2 || (split.length == 3 && split[1].trim().length == 0)) {
                return Double.parseDouble(split[(split.length == 3 ? 2 : 1)].trim()) * (split.length == 3 ? -1 : 1)
            }
        } else {
            return null
        }
    }

    def parseText(value) {
        if (isDouble(value) || isRange(value)) {
            return null
        } else {
            return value
        }
    }

    def isDouble(value) {
        try {
            def d = Double.parseDouble(value.trim())
            return true
        } catch (e) {
        }
        return false
    }

    def isRange(value) {
        try {
            def split = (value.startsWith('-') ? value.substring(1) : value).split('-')
            if (split.length == 2 || (split.length == 3 && split[1].trim().length == 0)) {
                def d1 = Double.parseDouble(split[0].trim())
                def d2 = Double.parseDouble(split[(split.length == 3 ? 2 : 1)].trim())
                return true
            }
        } catch (e) {
        }
        return false
    }
}
