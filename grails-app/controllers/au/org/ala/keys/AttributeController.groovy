package au.org.ala.keys
import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

@Transactional(readOnly = true)
class AttributeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        //query
        def q = params.containsKey("q") ? params.q : null
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
        def list
        def count

        def c = Attribute.createCriteria()

        //filter
        list = c.list(params) {
            if (keyIds || projectIds || users) {
                createAlias('key', 'key', CriteriaSpecification.INNER_JOIN)
                if (keyIds) {
                    'in'("key.id", keyIds)
                }
            }
            if (projectIds || users) {
                createAlias('key.project', 'project', CriteriaSpecification.INNER_JOIN)
                if (projectIds) {
                    'in'("project.id", projectIds)
                }
            }
            if (users) {
                createAlias('project.users', 'users', CriteriaSpecification.INNER_JOIN)
                'in'("user", users)
            }
            if (valueIds || lsids || taxonIds) {
                def subqueryValue = Value.where {
                    projections {
                        distinct 'attribute.id'
                    }
                    or {
                        if (valueIds) {
                            'in'("id", valueIds)
                        }
                        if (lsids || taxonIds) {
                            def subqueryTaxon = Taxon.where {
                                projections {
                                    distinct 'id'
                                }

                                if (lsids) {
                                    'in'('lsid', lsids)
                                }
                                if (taxonIds) {
                                    'in'("id", taxonIds)
                                }
                            }
                            'in'("taxon.id", subqueryTaxon.list())
                        }
                    }
                }
                'in'("id", subqueryValue.list())
            }

            if (q) {
                or {
                    ilike("label", "%" + q + "%")
                    ilike("notes", "%" + q + "%")
                    ilike("units", "%" + q + "%")
                }
            }
            if (attributeIds) {
                'in'("id", attributeIds)
            }
        }

        count = list.totalCount

        //format output
        def map = [attributes: list, totalCount: count, params: params]
        render map as JSON
    }

    @Transactional
    def create() {
        def json = request.getJSON()

        def p = new Attribute(json)

        p.save(flush: true)
        if (p.hasErrors()) {
            render p.errors as JSON
            return
        }
        render p as JSON
    }

    def show(Long id) {
        def attributeInstance = Attribute.get(id)
        if (attributeInstance == null) {
            def map = [error: "invalid attribute id"]
            render map as JSON
        } else {
            render attributeInstance as JSON
        }
    }

    @Transactional
    def update() {
        def json = request.getJSON()

        def attributeInstance = Attribute.get(json.id)
        if (attributeInstance == null) {
            def map = [error: "invalid attribute id"]
            render map as JSON
            return
        }

        if (json.containsKey('label')) {
            attributeInstance.label = json.label
        }

        if (json.containsKey('notes')) {
            attributeInstance.notes = json.notes
        }

        if (json.containsKey('units')) {
            attributeInstance.units = json.units
        }

        if (attributeInstance.hasErrors()) {
            render attributeInstance.errors as JSON
            return
        }

        attributeInstance.save flush: true
        if (attributeInstance.hasErrors()) {
            render attributeInstance.errors as JSON
            return
        }

        render attributeInstance as JSON
    }

    @Transactional
    def delete(Attribute attributeInstance) {

        if (attributeInstance == null) {
            def map = [error: "invalid attribute id"]
            render map as JSON
            return
        }

        attributeInstance.delete flush: true

        render [:] as JSON
    }
}
