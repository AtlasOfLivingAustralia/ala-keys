package au.org.ala.keys

class HomeController {

    def index() {
        def c = Key.createCriteria()
        def results = c {
            projections {
                groupProperty("created")
                rowCount()
            }
            order("created")
        }

        [results: results, projectCount: Project.count(), keyCount: Key.count(), taxonCount: Taxon.count(),
         attributeCount: Attribute.count(), valueCount: Value.count()]
    }
}
