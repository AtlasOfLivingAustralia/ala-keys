package au.org.ala.keys

import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification
import org.hibernate.criterion.DetachedCriteria

import static org.springframework.http.HttpStatus.*

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

        /* def v = new DetachedCriteria(Value.class).build {
             projections {
                 distinct "attribute.id"
             }
             'in'("id", valueIds)
         }*/

        def c = Attribute.createCriteria()

        //filter
        list = c.list(params) {
            if (keyIds) {
                createAlias('key', 'key', CriteriaSpecification.INNER_JOIN)
                'in'("key.id", keyIds)
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
                def subquery = Value.where {
                    projections {
                        distinct 'attribute.id'
                    }
                    'in'("id", valueIds)
                }

                'in'("id", subquery)
            }
            if (lsids || taxonIds) {
                def subquery = Taxon.where {
                    projections {
                        value {
                            distinct 'attribute.id'
                        }
                    }

                    if (lsids) {
                        'in'('lsid', lsids)
                    }
                    if (taxonIds) {
                        'in'("id", taxonIds)
                    }
                }

                'in'("id", subquery)
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
        if (params.containsKey("type") && "json".equalsIgnoreCase(params.type)) {
            def map = [attributes: list, totalCount: count, params: params]
            render map as JSON
        } else {
            respond list, model: [attributeInstanceCount: count, query: q]
        }
    }

    def show(Attribute attributeInstance) {
        respond attributeInstance
    }

    def create() {
        respond new Attribute(params)
    }

    @Transactional
    def save(Attribute attributeInstance) {
        if (attributeInstance == null) {
            notFound()
            return
        }

        if (attributeInstance.hasErrors()) {
            respond attributeInstance.errors, view: 'create'
            return
        }

        attributeInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'attribute.label', default: 'Attribute'), attributeInstance.id])
                redirect attributeInstance
            }
            '*' { respond attributeInstance, [status: CREATED] }
        }
    }

    def edit(Attribute attributeInstance) {
        respond attributeInstance
    }

    @Transactional
    def update(Attribute attributeInstance) {
        if (attributeInstance == null) {
            notFound()
            return
        }

        if (attributeInstance.hasErrors()) {
            respond attributeInstance.errors, view: 'edit'
            return
        }

        attributeInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Attribute.label', default: 'Attribute'), attributeInstance.id])
                redirect attributeInstance
            }
            '*' { respond attributeInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Attribute attributeInstance) {

        if (attributeInstance == null) {
            notFound()
            return
        }

        attributeInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Attribute.label', default: 'Attribute'), attributeInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'attribute.label', default: 'Attribute'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    def search(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        //query
        def q = params.containsKey("q") ? params.q : null
        def keyId = params.containsKey("key") ? params.key : null
        def key = keyId == null ? null : Key.get(keyId)
        def list
        def count

        def c = Attribute.createCriteria()

        list = c.list(params) {
            projections {
                property("id")
                property("label")
                property("units")
            }

            and {
                if (q != null) {
                    ilike("label", "%" + q + "%")
                }

                if (key != null) {
                    eq("key", key)
                }
            }
        }

        count = list.totalCount

        def m = [list: list, count: count, query: q, key: key]
        render m as JSON
    }
}
