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
    def importStatusService
    def searchTaxonLsidService
    def zipService
    def authorisationService

    private def importDataSource(DataSource dataSource, boolean updateLsids = true) {
        try {
            //create
            if (dataSource.filename.toLowerCase().endsWith(".dlt")) {
                importDeltaFileService.importDlt(dataSource)
            } else if (dataSource.filename.toLowerCase().endsWith(".xml")) {
                if (importLucidXmlService.importXml(dataSource)) {

                } else if (importSddXmlService.importXml(dataSource)) {

                }
            }

            //lsid lookup
            if (updateLsids) {
                importStatusService.put(dataSource.id, ["retrieving LSIDs", 75])

                searchTaxonLsidService.updateTaxon()
            }

            dataSource.status = "loaded"
            importStatusService.put(dataSource.id, ["loaded", 100])

        } catch (e) {
            dataSource.status = "failed"
            importStatusService.put(dataSource.id, ["failed", 0])

            log.error("failed to import: " + dataSource.getFilePath(), e)
        }

        //update status
        def i = DataSource.executeUpdate("update DataSource set status='" + dataSource.status + "' where id=" + dataSource.id)
    }

    /**
     * creates a new DataSource that returns immediately and runs the import in the background
     *
     * @param file
     * @return
     */
    def importFile(Project project, File file, String name) {

        def dataSource = new DataSource(filename: name, status: "loading", project: project)
        def path = dataSource.getFilePath()
        FileUtils.copyFile(file, new File(path))

        if (!dataSource.save(flush: true)) {
            dataSource.errors.each() {
                log.error("failed saving datasource: " + it)
            }
        }

        importStatusService.put(dataSource.id, ["loading", 0])

        //do other datasource creation here
        def dataSources
        if (dataSource.filename.toLowerCase().endsWith(".zip")) {
            dataSources = zipService.getDataSources(dataSource)
        }

        //thread
        def importTask = task {
            //wait for dataSource creation from parent thread transaction
            while (DataSource.get(dataSource.id) == null) {
                Thread.sleep(100);
            }

            if (dataSource.filename.toLowerCase().endsWith(".zip")) {

                dataSources.eachWithIndex { ds, i ->
                    importStatusService.put(dataSource.id, ["loading zipped file: " + ds.filename, i / (double) dataSources.size() * 100])
                    importDataSource(ds, false)
                }

                searchTaxonLsidService.updateTaxon();

                importStatusService.put(dataSource.id, ["loaded", 100])

                //update status
                def i = DataSource.executeUpdate("update DataSource set status='" + dataSource.status + "' where id=" + dataSource.id)
            } else {
                importDataSource(dataSource)
            }
        }

        //continue
        onComplete([importTask]) { List result ->
            log.debug("finished task: " + result.get(0))
        }


        return dataSource
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

        def project = Project.findById(json.get("projectId"), null);

        def dataSource = DataSource.findByProjectAndAlaUserIdAndFilename(project, json.get("alaUserId"), null)
        if (dataSource == null) {
            dataSource = new DataSource(alaUserId: json.get("alaUserId"), status: "adhoc", project: project)
            if (!dataSource.save()) {
                dataSource.errors.each() {
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
                attribute = Attribute.createNewOrChild(attributeLabel)
                attribute.units = attributeUnits
                if (!attribute.save()) {
                    attribute.errors.each() {
                        log.error(it)
                    }
                }
            }
            def r = searchTaxonLsidService.getRecord(lsid)
            def taxon = Taxon.findOrCreateWithLsid(lsid, r == null || r.rankClass == null ? null : r.rankClass.scientificName)
            searchTaxonLsidService.updateTaxon(taxon)

            //is it a single number
            def min = parseMin(value)
            def max = parseMax(value)
            def text = parseText(value)

            def newValue = new Value(createdBy: dataSource, taxon: taxon, attribute: attribute, min: min, max: max, text: text)
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
