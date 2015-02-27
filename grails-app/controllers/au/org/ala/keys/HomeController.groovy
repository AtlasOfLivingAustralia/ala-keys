package au.org.ala.keys

class HomeController {

    def index() {
        def c = DataSource.createCriteria()
        def results = c {
            projections {
                groupProperty("created")
                rowCount()
            }
            order("created")
        }

        [results: results, projectCount: Project.count(), dataSourceCount: DataSource.count(), taxonCount: Taxon.count(),
         attributeCount: Attribute.count(), valueCount: Value.count()]
    }
}
