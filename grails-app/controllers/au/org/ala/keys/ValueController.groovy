package au.org.ala.keys

import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class ValueController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        if (params.exactMatch == null) params.exactMatch = "false"

        //query
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

        def q = params.q
        def min = q != null ? importService.parseMin(q) : null
        def mx = q != null ? importService.parseMax(q) : null
        def text = q != null ? importService.parseText(q) : null

        def list
        def count

        def c = Value.createCriteria()

        //filter
        list = c.list(params) {
            if (keys) {
                createAlias('key', 'key', CriteriaSpecification.LEFT_JOIN)
                'in'("key.id", keys)
            }
            if (projects || users) {
                createAlias('key.project', 'project', CriteriaSpecification.LEFT_JOIN)
                if (projects) {
                    'in'("project.id", projects)
                }
            }
            if (users) {
                createAlias('project.users', 'users', CriteriaSpecification.LEFT_JOIN)
                'in'("user", users)
            }
            if (attributes) {
                createAlias('attribute', 'attribute', CriteriaSpecification.LEFT_JOIN)
                'in'("attribute.id", attributes)
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
            }
            if (values) {
                'in'("id", values)
            }
        }

        count = list.totalCount

        //format output
        if (params.containsKey("type") && "json".equalsIgnoreCase(params.type)) {
            def map = [values: list, totalCount: count, params: params]
            render map as JSON
        } else {
            respond list, model: [valueInstanceCount: count, query: q]
        }
    }

    def show(Value valueInstance) {
        respond valueInstance
    }

    def create() {
        respond new Value(params)
    }

    @Transactional
    def save(Value valueInstance) {
        if (valueInstance == null) {
            notFound()
            return
        }

        if (valueInstance.hasErrors()) {
            respond valueInstance.errors, view: 'create'
            return
        }

        valueInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'value.label', default: 'Value'), valueInstance.id])
                redirect valueInstance
            }
            '*' { respond valueInstance, [status: CREATED] }
        }
    }

    def edit(Value valueInstance) {
        respond valueInstance
    }

    @Transactional
    def update(Value valueInstance) {
        if (valueInstance == null) {
            notFound()
            return
        }

        if (valueInstance.hasErrors()) {
            respond valueInstance.errors, view: 'edit'
            return
        }

        valueInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Value.label', default: 'Value'), valueInstance.id])
                redirect valueInstance
            }
            '*' { respond valueInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Value valueInstance) {

        if (valueInstance == null) {
            notFound()
            return
        }

        valueInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Value.label', default: 'Value'), valueInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'value.label', default: 'Value'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
