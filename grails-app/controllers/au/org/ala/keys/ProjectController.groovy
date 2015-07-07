package au.org.ala.keys
import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.hibernate.criterion.CriteriaSpecification

@Transactional(readOnly = true)
class ProjectController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def taxonService

    def index() {
        def max = params?.max as Integer
        if (max == null) max = 10
        def offset = params?.offset as Integer
        if (offset == null) offset = 0

        //query
        def q = params.containsKey("q") ? params.q : null
        Long[] projects = params.containsKey("projects") ? params.projects.split(",").collect {
            Long.parseLong(it)
        } : null
        Long[] keys = params.containsKey("keys") ? params.keys.split(",").collect { Long.parseLong(it) } : null
        Long[] attributes = params.containsKey("attributes") ? params.attributes.split(",").collect {
            Long.parseLong(it)
        } : null
        Long[] values = params.containsKey("values") ? params.values.split(",").collect { Long.parseLong(it) } : null
        Long[] taxons = params.containsKey("taxons") ? params.taxons.split(",").collect { Long.parseLong(it) } : null
        String[] lsids = params.containsKey("lsids") ? params.lsids.split(",") : null
        String[] users = params.containsKey("users") ? params.users.split(",") : null
        def list
        def count

        def c = Project.createCriteria()

        //filter
        list = c.list {
            projections {
                distinct 'id'
            }

            if (keys || attributes || values || lsids) {
                createAlias('keys', 'key', CriteriaSpecification.LEFT_JOIN)
                if (keys) {
                    'in'("key.id", keys)
                }
            }
            if (attributes) {
                createAlias('key.attributes', 'attribute', CriteriaSpecification.LEFT_JOIN)
                'in'("attribute.id", attributes)
            }
            if (values || lsids) {
                createAlias('key.values', 'value', CriteriaSpecification.LEFT_JOIN)
                if (values) {
                    'in'("value.id", values)
                }
            }
            if (lsids || taxons) {
                createAlias('taxon', 'taxon', CriteriaSpecification.LEFT_JOIN)
                if (taxons) {
                    'in'("taxon.id", taxons)
                }
                if (lsids) {
                    'in'("taxon.lsid", lsids)
                }
            }
            if (users) {
                createAlias('users.userId', 'user', CriteriaSpecification.LEFT_JOIN)
                'in'("user", users)
            }
            if (q) {
                or {
                    ilike("description", "%" + q + "%")
                    ilike("name", "%" + q + "%")
                    ilike("geographicScope", "%" + q + "%")
                }
            }
            if (projects) {
                'in'("id", projects)
            }
        }

        def ids = list.subList(offset, offset + Math.min(list.size() - offset, max))

        //select filtered
        def c2 = Project.createCriteria()
        def list2 = c2.list() {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)

            createAlias('scopeTaxons', 'scopeTaxons', CriteriaSpecification.LEFT_JOIN)

            projections {
                property 'id', 'id'
                property 'name', 'name'
                property 'description', 'description'
                property 'imageUrl', 'imageUrl'
                property 'geographicScope', 'geographicScope'
                property 'created', 'created'

                property 'scopeTaxons.id', 'taxonId'
                property 'scopeTaxons.scientificName', 'taxonScientificName'
                property 'scopeTaxons.lsid', 'taxonLsid'
            }

            if (ids.size() > 0) {
                'in'("id", ids)
            } else {
                eq("id", -1L)
            }
        }.groupBy {
            it.id
        }.collect { k, v ->
            [id      : v[0].id, name: v[0].name, description: v[0].description,
             imageUrl: v[0].imageUrl, geographicScope: v[0].geographicScope,
             created : v[0].created, scopeTaxons: v.findAll { it.taxonId != null }.collect {
                [id: it.taxonId, scientificName: it.taxonScientificName, lsid: it.taxonLsid]
            }]
        }

        //format output
        def map = [projects: list2, totalCount: ids.size(), params: params]
        render map as JSON
    }


    @Transactional
    def create() {
        def jsonArray = request.getJSON()

        //allow an array of projects in the request
        if (jsonArray instanceof JSONObject) {
            def newJsonArray = new JSONArray()
            newJsonArray.add(jsonArray)
            jsonArray = newJsonArray
        }

        def list = []
        for (int i = 0; i < jsonArray.size(); i++) {
            def json = jsonArray.getAt(i)

            def scopeTaxons
            if (json.containsKey('scopeTaxons')) {
                scopeTaxons = json.scopeTaxons
                json.remove('scopeTaxons')
            }

            def p = new Project(json)
            p.scopeTaxons = []
            if (scopeTaxons != null) {
                ((JSONArray) scopeTaxons).each { sn ->
                    if (sn != null && sn.toString().length() > 0) {
                        def taxon = taxonService.findOrCreateWithScientificName(sn)
                        p.scopeTaxons.add(taxon)
                    }
                }
            }
            p.save(flush: true)
            if (p.hasErrors()) {
                render p.errors as JSON
                return
            }
            list.add(p)
        }

        render list as JSON
    }

    def show(Long id) {
        def projectInstance = Project.get(id)
        if (projectInstance == null) {
            def map = [error: "invalid project id"]
            render map as JSON
        } else {
            render projectInstance as JSON
        }
    }

    @Transactional
    def update() {
        def json = request.getJSON()

        def projectInstance = Project.get(json.id)
        if (projectInstance == null) {
            def map = [error: "invalid project id"]
            render map as JSON
            return
        }

        if (json.containsKey('name')) {
            projectInstance.name = json.name
        }

        if (json.containsKey('description')) {
            projectInstance.description = json.description
        }

        if (json.containsKey('imageUrl')) {
            projectInstance.imageUrl = json.imageUrl
        }

        if (json.containsKey('geographicScope')) {
            projectInstance.geographicScope = json.geographicScope
        }

        if (json.containsKey('scopeTaxons')) {
            projectInstance.scopeTaxons = []
            ((JSONArray) json.scopeTaxons).each { sn ->
                if (sn != null && sn.toString().length() > 0) {
                    def taxon = taxonService.findOrCreateWithScientificName(sn)
                    projectInstance.scopeTaxons.add(taxon)
                }
            }
        }

        if (projectInstance.hasErrors()) {
            render projectInstance.errors as JSON
            return
        }

        projectInstance.save flush: true

        render projectInstance as JSON
    }

    @Transactional
    def delete(Project projectInstance) {

        if (projectInstance == null) {
            def map = [error: "invalid project id"]
            render map as JSON
            return
        }

        projectInstance.delete flush: true

        render [:] as JSON
    }

}
