package au.org.ala.keys

/**
 * Created by a on 9/12/14.
 */
class ValuesService {
    def searchTaxonLsidService

    /**
     * Get all Value text for a scientificName (or LSID).
     *
     * If it does not find a result it will search Values by scientificName and LSID.
     *
     * @param params map containing "scientificName" (that can be an LSID) and optional "inheritedOnly" to restrict
     *               output to only inherited values
     * @return map with the "record" as NameSearchResult containing the searched LSIDs, if any, and
     *         "attributes" as map of Attribute label (it will merge duplicates) : [String values]
     */
    def findAllValues(params = null) {
        def scientificNameOrLsid = params.get("scientificName")
        def record = searchTaxonLsidService.getRecord(scientificNameOrLsid)
        def inheritedOnly = (params.containsKey("inheritedOnly") ? params.get("inheritedOnly") : false).asBoolean()

        def values = []

        //traverse and build
        Map attributes = [:]

        if (record != null && record.rankClassification != null) {
            def cid = Value.getForLsid(record.rankClassification.cid)
            def kid = Value.getForLsid(record.rankClassification.kid)
            def pid = Value.getForLsid(record.rankClassification.pid)
            def oid = Value.getForLsid(record.rankClassification.oid)
            def gid = Value.getForLsid(record.rankClassification.gid)
            def fid = Value.getForLsid(record.rankClassification.fid)
            def sid = Value.getForLsid(record.rankClassification.sid)

            if (record.rankClassification.kid != record.lsid || !inheritedOnly) mergeValues(attributes, kid)
            if (record.rankClassification.pid != record.lsid || !inheritedOnly) mergeValues(attributes, pid)
            if (record.rankClassification.cid != record.lsid || !inheritedOnly) mergeValues(attributes, cid)
            if (record.rankClassification.oid != record.lsid || !inheritedOnly) mergeValues(attributes, oid)
            if (record.rankClassification.fid != record.lsid || !inheritedOnly) mergeValues(attributes, fid)
            if (record.rankClassification.gid != record.lsid || !inheritedOnly) mergeValues(attributes, gid)
            if (record.rankClassification.sid != record.lsid || !inheritedOnly) mergeValues(attributes, sid)
        } else {
            mergeValues(attributes, Value.findByScientificName(scientificName))
            mergeValues(attributes, Value.findByLsid(scientificName))
        }

        Map m = [:]
        m.put("record", record)
        m.put("attributes", attributes)

        return m
    }

    /**
     * Get all Values for a scientificName (or LSID).
     *
     * If it does not find a result it will search Values by scientificName and LSID.
     *
     * @param params map containing "scientificName" (that can be an LSID) and optional "inheritedOnly" to restrict
     *               output to only inherited values
     * @return List of values
     */
    def findAllValuesAsList(params = null) {
        def scientificNameOrLsid = params.get("scientificName")
        def record = searchTaxonLsidService.getRecord(scientificNameOrLsid)
        def inheritedOnly = (params.containsKey("inheritedOnly") ? params.get("inheritedOnly") : false).asBoolean()

        def values = []

        //traverse and build
        Map attributes = [:]

        if (record != null && record.rankClassification != null) {
            def cid = Value.getForLsid(record.rankClassification.cid)
            def kid = Value.getForLsid(record.rankClassification.kid)
            def pid = Value.getForLsid(record.rankClassification.pid)
            def oid = Value.getForLsid(record.rankClassification.oid)
            def gid = Value.getForLsid(record.rankClassification.gid)
            def fid = Value.getForLsid(record.rankClassification.fid)
            def sid = Value.getForLsid(record.rankClassification.sid)

            if (record.rankClassification.kid != record.lsid || !inheritedOnly) values.addAll(kid)
            if (record.rankClassification.pid != record.lsid || !inheritedOnly) values.addAll(pid)
            if (record.rankClassification.cid != record.lsid || !inheritedOnly) values.addAll(cid)
            if (record.rankClassification.oid != record.lsid || !inheritedOnly) values.addAll(oid)
            if (record.rankClassification.fid != record.lsid || !inheritedOnly) values.addAll(fid)
            if (record.rankClassification.gid != record.lsid || !inheritedOnly) values.addAll(gid)
            if (record.rankClassification.sid != record.lsid || !inheritedOnly) values.addAll(sid)
        } else {
            values.addAll(Value.findByScientificName(scientificName))
            values.addAll(Value.findByLsid(scientificName))
        }

        return values
    }

    private def mergeValues(map, values) {
        if (values != null && values.size() > 0) {
            values.each() { Value value ->
                def list = map.get(value.attribute.label)
                if (list == null) {
                    list = []
                }
                list.add(value.asText([noLabel: true]))

                map.put(value.attribute.label, list)
            }
        }
    }
}
