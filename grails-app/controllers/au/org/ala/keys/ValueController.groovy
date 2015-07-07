package au.org.ala.keys
import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

@Transactional(readOnly = true)
class ValueController {

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
        def min = q != null ? importService.parseMin(q) : null
        def mx = q != null ? importService.parseMax(q) : null
        def text = q != null ? importService.parseText(q) : null

        def list
        def count

        def c = Value.createCriteria()

        //filter
        list = c.list(params) {
            if (keyIds || projectIds || users) {
                createAlias('key', 'key', CriteriaSpecification.LEFT_JOIN)
                if (keyIds) {
                    'in'("key.id", keyIds)
                }
            }
            if (projectIds || users) {
                createAlias('key.project', 'project', CriteriaSpecification.LEFT_JOIN)
                if (projectIds) {
                    'in'("project.id", projectIds)
                }
            }
            if (users) {
                createAlias('project.users', 'users', CriteriaSpecification.LEFT_JOIN)
                'in'("user", users)
            }
            if (attributeIds) {
                createAlias('attribute', 'attribute', CriteriaSpecification.LEFT_JOIN)
                'in'("attribute.id", attributeIds)
            }
            if (lsids || taxonIds) {
                createAlias('taxon', 'taxon', CriteriaSpecification.LEFT_JOIN)
                if (taxonIds) {
                    'in'("taxon.id", taxonIds)
                }
                if (lsids) {
                    'in'("taxon.lsid", lsids)
                }
            }
            if (q) {
                if (text && !"null".equals(text)) {
                    or {
                        if ("true".equalsIgnoreCase(params.exactMatch)) {
                            eq("text", text)
                        } else {
                            ilike("text", "%" + text + "%")
                        }
                    }
                }
                println min + ":" + mx
                if (min != null && max != null) {
                    if ("true".equalsIgnoreCase(params.exactMatch)) {
                        eq("min", min)
                        eq("max", mx)
                    } else {
                        lte("min", mx)
                        gte("max", min)
                    }
                }
            }
            if (valueIds) {
                'in'("id", valueIds)
            }
        }

        count = list.totalCount

        //format output
        def map = [values: list, totalCount: count, params: params]
        render map as JSON
    }

    @Transactional
    def create() {
        def json = request.getJSON()

        def p = new Value(json)

        p.save(flush: true)
        if (p.hasErrors()) {
            render p.errors as JSON
            return
        }
        render p as JSON
    }

    def show(Long id) {
        def valueInstance = Value.get(id)
        if (valueInstance == null) {
            def map = [error: "invalid value id"]
            render map as JSON
        } else {
            render valueInstance as JSON
        }
    }

    @Transactional
    def update() {
        def json = request.getJSON()

        def valueInstance = Value.get(json.id)
        if (valueInstance == null) {
            def map = [error: "invalid value id"]
            render map as JSON
            return
        }

        if (json.containsKey('text')) {
            valueInstance.text = json.text
        }

        if (json.containsKey('min')) {
            valueInstance.min = json.min
        }

        if (json.containsKey('max')) {
            valueInstance.max = json.max
        }

        if (valueInstance.hasErrors()) {
            render valueInstance.errors as JSON
            return
        }

        valueInstance.save flush: true
        if (valueInstance.hasErrors()) {
            render valueInstance.errors as JSON
            return
        }

        render valueInstance as JSON
    }

    @Transactional
    def delete(Value valueInstance) {

        if (valueInstance == null) {
            def map = [error: "invalid value id"]
            render map as JSON
            return
        }

        valueInstance.delete flush: true

        render [:] as JSON
    }
}
