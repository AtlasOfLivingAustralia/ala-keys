package au.org.ala.keys

import au.org.ala.names.search.ALANameSearcher
import au.org.ala.names.search.HomonymException
import au.org.ala.names.search.MisappliedException
import grails.transaction.Transactional

@Transactional
class SearchTaxonLsidService {

    ALANameSearcher searcher = new ALANameSearcher("/data/lucene/namematching")

    def importStatusService

    def updateTaxon() {
        log.debug("begin updateTaxon")

        try {
            def total = 0
            Map qry = [lsid: null]

            Taxon.findAllWhere(qry).each() { taxon ->
                total++
            }
            def count = 0
            Taxon.findAllWhere(qry).each() { taxon ->

                count++

                //filter
                def name = filterName(taxon.scientificName)

                log.debug('searching: ' + taxon.scientificName + ', filtered:' + name)

                try {
                    applyRecord(taxon)

                    if (!taxon.save()) {
                        taxon.errors.each {
                            log.error("Error saving taxon", it)
                        }
                    }
                } catch (e) {
                    log.error("Error searching scientific name: " + name, e)
                }
            }
        } catch (e) {
            log.error("error searching LSIDs", e)
        }
    }

    def updateTaxonAll() {
        log.debug("begin updateTaxonAll")

        try {
            def total = Taxon.count()

            def count = 0
            Taxon.findAll().each() { taxon ->
                count++

                updateTaxon(taxon)
            }
        } catch (e) {
            log.error("error searching LSIDs", e)
        }
    }

    def updateTaxon(taxon) {
        //filter
        def name = filterName(taxon.scientificName)

        log.debug('searching: ' + taxon.scientificName + ', filtered:' + name)

        try {
            applyRecord(taxon)

            if (!taxon.save()) {
                taxon.errors.each {
                    log.error("Error saving taxon", it)
                }
            }
        } catch (e) {
            log.error("Error searching scientific name: " + name, e)
        }
    }

    private def filterName(String name) {
        if (name.contains('\\i')) {
            def split = name.split('\\\\i')
            if (split.length > 1) {
                return split[1].trim()
            }
        } else {
            return name
        }
    }

    def getLsid(String name) {
        def lsid = null
        try {
            lsid = searcher.searchForLSID(name, true)
        } catch (MisappliedException e) {
            lsid = null //e.matchedResult.acceptedLsid
        } catch (HomonymException e) {
            lsid = null //searcher.searchForLSID(name, true, true)
        } catch (e) {
        }

        return lsid
    }

    def getRecord(String name) {
        def r

        try {
            r = searcher.searchForRecord(name)
            if (r == null) {
                r = searcher.searchForRecordByLsid(name)
            }
        } catch (e) {
            log.error(e.getMessage() + ", scientificName: " + name)
        }

        return r
    }

    private def applyRecord(taxon) {

        def r = getRecord(filterName(taxon.scientificName))

        if (r != null) {
            if (r.rank == null && r.acceptedLsid != null) {
                r = getRecord(r.acceptedLsid)
            }

            try {
                taxon.leftt = r.left.toInteger()
                taxon.rightt = r.right.toInteger()
            } catch (e) {}

            if (r.rankClass.kid != null) {
                taxon.kid = r.rankClass.kid
                taxon.kidName = r.rankClass.kingdom
            } else {
                taxon.kid = null
                taxon.kidName = null
            }
            if (r.rankClass.pid != null) {
                taxon.pid = r.rankClass.pid
                taxon.pidName = r.rankClass.phylum
            } else {
                taxon.pid = null
                taxon.pidName = null
            }
            if (r.rankClass.cid != null) {
                taxon.cid = r.rankClass.cid
                taxon.cidName = r.rankClass.klass
            } else {
                taxon.cid = null
                taxon.cidName = null
            }
            if (r.rankClass.oid != null) {
                taxon.oid = r.rankClass.oid
                taxon.oidName = r.rankClass.order
            } else {
                taxon.oid = null
                taxon.oidName = null
            }
            if (r.rankClass.fid != null) {
                taxon.fid = r.rankClass.fid
                taxon.fidName = r.rankClass.family
            } else {
                taxon.fid = null
                taxon.fidName = null
            }
            if (r.rankClass.gid != null) {
                taxon.gid = r.rankClass.gid
                taxon.gidName = r.rankClass.genus
            } else {
                taxon.gid = null
                taxon.gidName = null
            }
            if (r.rankClass.sid != null) {
                taxon.sid = r.rankClass.sid
                taxon.sidName = r.rankClass.species
            } else {
                taxon.sid = null
                taxon.sidName = null
            }

            taxon.rank = r.rank
            taxon.lsid = r.lsid
        }
    }

}
