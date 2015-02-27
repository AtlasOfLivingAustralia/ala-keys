package au.org.ala.keys

import grails.converters.JSON
import org.hibernate.criterion.CriteriaSpecification

class SearchController {

    def searchTaxonLsidService
    def valuesService
    def importService

    //permit search by lsid, scientificName or 'attribute|number' pairs
    def taxon() {
        def m = valuesService.findAllValues(params)

        render m as JSON
    }

    def tree() {
        def left = params.get("left")
        def right = params.get("right")
        def rank = params.get("rank")

        if ("null".equals(left) || left.length() == 0) left = null
        if ("null".equals(right) || right.length() == 0) right = null
        if ("null".equals(rank) || rank.length() == 0) rank = null

        //defaults are for Kingdoms
        def groupName = "kidName"
        def groupId = "kid"
        def groupRank = "kingdom"
        def rankCurrentName = null

        def ranks = ["KINGDOM", "PHYLUM", "CLASS", "ORDER", "FAMILY", "GENUS", "SPECIES"]
        def rankGroupId = ["kid", "pid", "cid", "oid", "fid", "gid", "sid"]
        def rankGroupName = ["kidName", "pidName", "cidName", "oidName", "fidName", "gidName", "sidName"]
        ranks.eachWithIndex() { rnk, i ->
            if (rnk.equalsIgnoreCase(rank)) {
                if (i + 1 < ranks.size()) {
                    groupRank = ranks[i + 1]
                    groupId = rankGroupId[i + 1]
                    groupName = rankGroupName[i + 1]
                    rankCurrentName = rankGroupName[i]
                } else {
                    groupRank = null
                    groupId = null
                    groupName = null
                    rankCurrentName = null
                }
            }
        }

        def nodes = Taxon.withCriteria() {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            projections {
                countDistinct("id", "count")
                if (groupName != null) {
                    groupProperty(groupName, "name")
                    groupProperty(groupId, "lsid")
                    if (rankCurrentName != null) {
                        groupProperty(rankCurrentName, "parentName")
                    }
                    min("leftt", "left")
                    max("rightt", "right")
                } else {
                    groupProperty("scientificName", "name")
                    groupProperty("lsid", "name")
                }
            }
            and {
                /*or {
                    eq("rank", groupRank)
                    isNull("rank")
                }*/

                if (left != null && right != null) {
                    gte("leftt", left.toInteger())
                    lte("rightt", right.toInteger())
                }
            }
        }

        //check for end node
        nodes.each() { node ->
            if (node.count == 1) {
                def taxons = Taxon.withCriteria() {
                    and {
                        if (left != null && right != null) {
                            gte("leftt", left.toInteger())
                            lte("rightt", right.toInteger())
                        }
                        if (node.name == null) {
                            isNull(groupName)
                        }
                    }
                }
                if (taxons != null && taxons.size() == 1) {
                    def taxon = taxons.get(0)
                    if ((rank == null && taxon.rank == null) ||
                            (getTaxonValue(taxon, rankGroupId) == null)) {
                        //end
                        node.put("taxonId", taxon.id)
                        node.put("name", taxon.scientificName)
                        node.put("rank", taxon.rank)
                        node.put("lsid", taxon.lsid)
                    }
                }
            }
        }

        def m = [rank: groupRank, nodes: nodes]
        render m as JSON
    }

    def getMapOfValues(Taxon t) {
        def map = [:]

        t.values.each() { a ->
            if (a.attribute.isText) {
                map.put(a.attribute.label, a.text)
            } else if (a.attribute.isNumeric) {
                if (a.min == a.max) {
                    map.put(a.attribute.label, (a.min + " " + a.attribute.units).trim())
                } else {
                    map.put(a.attribute.label, (a.min + "-" + a.max + " " + a.attribute.units).trim())
                }
            }
        }

        if (t.parent != null) {
            map.putAll(getMapOfValues(t.parent))
        }
    }

    private def getTaxonValue(taxon, idColumn) {
        if ("sid".equals(idColumn)) {
            return taxon.sid
        }
        if ("gid".equals(idColumn)) {
            return taxon.gid
        }
        if ("fid".equals(idColumn)) {
            return taxon.fid
        }
        if ("oid".equals(idColumn)) {
            return taxon.oid
        }
        if ("cid".equals(idColumn)) {
            return taxon.cid
        }
        if ("pid".equals(idColumn)) {
            return taxon.pid
        }
        if ("kid".equals(idColumn)) {
            return taxon.kid
        }
    }

    def attribute() {
        if (params.max == null) params.max = 100
        if (params.sort == null) params.sort = "id"
        if (params.order == null) params.order = "asc"
        if (params.primaryOnly == null) params.primaryOnly = "true"

        def c = Attribute.createCriteria()
        def list = c.list(params) {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            projections {
                property("id", "id")
                property("label", "label")
                property("units", "units")
                property("isNumeric", "isNumeric")
                property("isText", "isText")
            }

            and {
                if (params.q != null) {
                    or {
                        ilike("label", "%" + params.q + "%")
                        ilike("units", "%" + params.q + "%")
                    }
                }

                if (params.primaryOnly != null && "true".equalsIgnoreCase(params.primaryOnly)) {
                    eq("primaryy", true)
                }
            }
        }

        def m = [attributes: list, totalCount: list.totalCount]
        render m as JSON
    }

    def value() {
        if (params.max == null) params.max = 100
        if (params.sort == null) params.sort = "id"
        if (params.order == null) params.order = "asc"
        if (params.exactMatch == null) params.exactMatch = "false"
        if (params.includeTaxon == null) params.includeTaxon = "true"
        if (params.includeChildrenAttributes == null) params.includeChildrenAttributes = "true"

        def q = params.q
        def min = q != null ? importService.parseMin(q) : null
        def max = q != null ? importService.parseMax(q) : null
        def text = q != null ? importService.parseText(q) : null

        def attribute = params.attributeId != null ? Attribute.get(params.attributeId.toLong()) : null

        def c = Value.createCriteria()
        def list = c.list(params) {
            createAlias('attribute', 'attrib', CriteriaSpecification.LEFT_JOIN)

            and {
                if (params.q != null) {
                    or {
                        println "text: " + text
                        if (text != null && !"null".equals(text)) {
                            if ("true".equalsIgnoreCase(params.exactMatch)) {
                                eq("text", text)
                            } else {
                                ilike("text", "%" + text + "%")
                            }
                        }
                    }
                }
                println min + ":" + max
                if (min != null && max != null) {
                    if ("true".equalsIgnoreCase(params.exactMatch)) {
                        eq("min", min)
                        eq("max", max)
                    } else {
                        lte("min", max)
                        gte("max", min)
                    }
                }
                or {
                    if (attribute != null) {
                        eq("attribute", attribute)

                        if ("true".equalsIgnoreCase(params.includeChildrenAttributes)) {
                            eq("attrib.parent", attribute)
                        }
                    }
                }
            }
        }

        def newList = []
        def group = [:]
        list.each() { value ->
            def key = value.getAttributeId() + " " + value.asText()
            def taxon = [ taxon_id: value.getTaxonId(), lsid: value.taxon.lsid,
                          scientificName: value.taxon.scientificName, rank: value.taxon.rank]
            def v = group.get(key)
            if ("false".equalsIgnoreCase(params.includeTaxon)) {
                if (v == null) {
                    group.put(key, [attribute_id: value.getAttributeId(), text: value.asText(), taxonCount: 1,textRaw: value.text, minRaw: value.min, maxRaw: value.max])
                } else {
                    v.taxonCount++
                }
            } else {
                if (v == null) {
                    group.put(key, [id   : value.id, attribute_id: value.getAttributeId(), text: value.asText(), taxonCount: 1,
                                textRaw: value.text, minRaw: value.min, maxRaw: value.max,
                                    taxon: [taxon]])
                } else {
                    v.taxon.add(taxon)
                    v.taxonCount++
                }
            }
        }
        group.each() { key, value ->
            newList.add(value)
        }

        def m = [values: newList, totalCount: list.totalCount]
        render m as JSON
    }

    def dataSource() {
        if (params.max == null) params.max = 100
        if (params.sort == null) params.sort = "id"
        if (params.order == null) params.order = "asc"

        //query
        def q = params.containsKey("q") ? params.q : null
        def list
        def count
        if (q != null) {
            def c = DataSource.createCriteria()

            list = c.list(params) {
                createAlias('project', 'p', CriteriaSpecification.LEFT_JOIN)
                resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
                projections {
                    property("id", "id")
                    property("created", "created")
                    property("alaUserId", "alaUserId")
                    property("filename", "filename")
                    property("description", "description")
                    property("status", "status")
                    property("p.name", "projectName")
                }
                or {
                    ilike("filename", "%" + q + "%")
                    ilike("alaUserId", "%" + q + "%")
                    ilike("status", "%" + q + "%")
                }
            }

            count = list.totalCount
        } else {
            list = DataSource.list(params)
            count = DataSource.count()
        }

        def m = [dataSources: list, totalCount: count]
        render m as JSON
    }

    def project() {
        if (params.max == null) params.max = 100
        if (params.sort == null) params.sort = "id"
        if (params.order == null) params.order = "asc"

        //query
        def q = params.containsKey("q") ? params.q : null
        def list
        def count
        if (q != null) {
            def c = Project.createCriteria()

            list = c.list(params) {
                resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
                projections {
                    property("id", "id")
                    property("name", "name")
                    property("description", "description")
                }
                or {
                    ilike("name", "%" + q + "%")
                    ilike("description", "%" + q + "%")
                }
            }

            count = list.totalCount
        } else {
            list = Project.list(params)
            count = Project.count()
        }

        def m = [projects: list, totalCount: count]
        render m as JSON
    }
}