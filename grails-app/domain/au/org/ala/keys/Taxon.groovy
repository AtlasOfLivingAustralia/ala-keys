package au.org.ala.keys

class Taxon {
    def searchTaxonLsidService

    String scientificName
    String rank
    String lsid
    Date created = new Date()
    Integer leftt
    Integer rightt

    String kid, pid, cid, oid, fid, gid, sid
    String kidName, pidName, cidName, oidName, fidName, gidName, sidName

    static constraints = {
    }

    static hasMany = [values: Value]

    static mapping = {
        values cascade: 'all-delete-orphan'
    }


    public static synchronized def findOrCreate(scientificName) {
        def taxonList = Taxon.findAllByScientificName(scientificName)
        if (taxonList.size() == 0) {
            Taxon.withTransaction {
                Taxon taxon = new Taxon(scientificName: scientificName)
                if (!taxon.save()) {
                    taxon.errors.each() {
                        println it
                    }
                }
                taxonList.add(taxon)
            }
        }
        return taxonList.get(0)
    }

    public static synchronized def findOrCreateWithLsid(lsid, scientificName) {
        def taxonList = Taxon.findAllByLsid(lsid)
        if (taxonList.size() == 0) {
            return findOrCreate(scientificName)
        }
        return taxonList.get(0)
    }
}
