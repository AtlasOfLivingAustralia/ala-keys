package au.org.ala.keys

import grails.converters.JSON
import org.hibernate.criterion.CriteriaSpecification

class SearchController {

    def searchTaxonLsidService
    def valuesService

    //permit search by lsid, scientificName or 'attribute|number' pairs
    def taxon() {
        def m = valuesService.findAllValues(params)

        render m as JSON
    }

    def tree() {
        def lsid = params.get("branch")
        def rank = params.get("rank")

        if ("null".equals(lsid)) lsid = null
        if ("null".equals(rank)) rank = null

        //defaults are for Kingdoms
        def eqColumn = null
        def groupName = "kidName"
        def groupId = "kid"
        def groupRank = "kingdom"
        def nextGroupId = "pid"

        //group by lsid rank
        if ((rank == null || rank.length() == 0) && lsid != null && lsid.length() > 0) {
            def r = searchTaxonLsidService.getRecord(lsid)
            if (r != null && r.rank != null) {
                rank = r.rank.getRank()
            }
        }
        if (rank != null) {
            if ("SPECIES".equalsIgnoreCase(rank)) {
                eqColumn = "sid"
                groupName = "scientificName"
                groupId = "lsid"
                groupRank = ""
                nextGroupId = ""
            } else if ("GENUS".equalsIgnoreCase(rank)) {
                eqColumn = "gid"
                groupName = "sidName"
                groupId = "sid"
                groupRank = "species"
                nextGroupId = "lsid"
            } else if ("FAMILY".equalsIgnoreCase(rank)) {
                eqColumn = "fid"
                groupName = "gidName"
                groupId = "gid"
                groupRank = "genus"
                nextGroupId = "sid"
            } else if ("ORDER".equalsIgnoreCase(rank)) {
                eqColumn = "oid"
                groupName = "fidName"
                groupId = "fid"
                groupRank = "family"
                nextGroupId = "gid"
            } else if ("CLASS".equalsIgnoreCase(rank)) {
                eqColumn = "cid"
                groupName = "oidName"
                groupId = "oid"
                groupRank = "order"
                nextGroupId = "fid"
            } else if ("PHYLUM".equalsIgnoreCase(rank)) {
                eqColumn = "pid"
                groupName = "cidName"
                groupId = "cid"
                groupRank = "class"
                nextGroupId = "oid"
            } else if ("KINGDOM".equalsIgnoreCase(rank)) {
                eqColumn = "kid"
                groupName = "pidName"
                groupId = "pid"
                groupRank = "phylum"
                nextGroupId = "cid"
            }
        }

        def nodes = Taxon.withCriteria() {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            projections {
                countDistinct("id", "count")
                groupProperty(groupName, "name")
                groupProperty(groupId, "lsid")
            }
            and {
                if (eqColumn != null) {
                    if (lsid != null) {
                        eq(eqColumn, lsid)
                    } else {
                        isNull(eqColumn)
                    }
                }
            }
        }

        //check for end node
        nodes.each() { node ->
            if (node.count == 1) {
                def taxons = Taxon.withCriteria() {
                    and {
                        if (eqColumn != null) {
                            eq(eqColumn, lsid)
                        }
                        if (node.lsid != null) {
                            eq(groupId, node.lsid)
                        } else {
                            isNull(groupId)
                        }
                    }
                }
                if (taxons != null && taxons.size() == 1) {
                    def taxon = taxons.get(0)
                    if ((rank == null && taxon.rank == null) ||
                            (getTaxonValue(taxon, nextGroupId) == null)) {
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
            if (a.attribute.characterTypeText) {
                map.put(a.attribute.label, a.text)
            } else if (a.attribute.characterTypeNumeric) {
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
}