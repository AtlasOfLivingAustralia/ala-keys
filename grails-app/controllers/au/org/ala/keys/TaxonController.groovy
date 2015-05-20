package au.org.ala.keys
import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

@Transactional(readOnly = true)
class TaxonController {

    def searchTaxonLsidService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        if (params.exactMatch == null) params.exactMatch = "false"

        //query
        Long[] projectIds = params.containsKey("projects") ? params.projects.split(",").collect {
            Long.parseLong(it)
        } : null
        Long[] keyIds = params.containsKey("keys") ? params.keys.split(",").collect { Long.parseLong(it) } : null
        Long[] attributeIds = params.containsKey("attributes") ? params.attributes.split(",").collect {
            Long.parseLong(it)
        } : null
        Long[] valueIds = params.containsKey("values") ? params.values.split(",").collect { Long.parseLong(it) } : null
        Long[] taxonIds = params.containsKey("taxons") ? params.taxons.split(",").collect { Long.parseLong(it) } : null
        String[] lsids = params.containsKey("lsids") ? params.lsids.split(",") : null
        String[] users = params.containsKey("users") ? params.users.split(",") : null

        def q = params.q

        def list
        def count

        def c = Taxon.createCriteria()

        //filter
        list = c.list(params) {

            if (valueIds || attributeIds || keyIds || projectIds) {
                def subqueryValue = Value.where {
                    projections {
                        distinct 'taxon.id'
                    }
                    or {
                        if (valueIds) {
                            'in'("id", valueIds)
                        }
                        if (attributeIds || keyIds || projectIds) {
                            if (attributeIds) {
                                'in'("attribute.id", attributeIds)
                            }
                            if (keyIds || projectIds) {
                                if (keyIds) {
                                    'in'("key.id", keyIds)
                                }
                                if (projectIds) {
                                    def subqueryProject = Key.where {
                                        projections {
                                            distinct 'id'
                                        }
                                        'in'("project.id", projectIds)
                                    }
                                    'in'("key.id", subqueryProject.list())
                                }
                            }
                        }
                    }
                }
                def l = subqueryValue.list()
                if (l.size() > 0) {
                    'in'("id", subqueryValue.list())
                }
            }

            if (q) {
                or {
                    ilike("scientificName", "%" + q + "%")
                    ilike("lsid", "%" + q + "%")
                }
            }
            if (taxonIds) {
                'in'("id", taxonIds)
            }
        }

        count = list.totalCount

        //format output
        def map = [taxon: list, totalCount: count, params: params]
        render map as JSON
    }

    def updateLsids() {
        searchTaxonLsidService.updateLsids()

        render(contentType: "text/json") {
            [status: "started"]
        }
    }

    @Transactional(readOnly = false)
    def setLsid() {
        def json = request.getJSON()

        if (!json instanceof JSONObject) {
            def o = json
            json = new JSONArray()
            json.add(o)
        }

        json.each {
            searchTaxonLsidService.setLsid(it.scientificName, it.lsid)
        }
    }


}
