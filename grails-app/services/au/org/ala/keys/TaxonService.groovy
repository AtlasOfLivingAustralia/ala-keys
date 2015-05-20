package au.org.ala.keys
/**
 * Created by a on 30/03/15.
 */
class TaxonService {
    def searchTaxonLsidService

    /**
     * Return a taxon. Create a new taxon if necessary.
     *
     * @param scientificName
     * @return
     */
    def findOrCreateWithScientificName(scientificName) {
        if (scientificName == null || scientificName.toString().length() == 0) {
            return null
        }
        def taxonList = Taxon.findAllByScientificName(scientificName)
        if (taxonList.size() == 0) {
            Taxon.withTransaction {
                Taxon taxon = new Taxon(scientificName: scientificName)

                searchTaxonLsidService.updateTaxon(taxon)

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

    /**
     * Return a taxon. Create a new taxon if necessary.
     *
     * @param scientificName
     * @return
     */
    def get(lsid_or_scientificName) {
        def taxonList = Taxon.findAllByLsid(lsid_or_scientificName)
        if (taxonList.size() == 0) {
            return findOrCreateWithScientificName(lsid_or_scientificName)
        }
        return taxonList.get(0)
    }
}
