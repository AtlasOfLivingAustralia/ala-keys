package au.org.ala.keys

import grails.converters.JSON
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class AttributeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        respond Attribute.list(params), model: [attributeInstanceCount: Attribute.count()]
    }

    def dataSource(DataSource dataSource) {
        params.max = 10

        def count = Attribute.countByCreatedBy(dataSource)
        def list = Attribute.findAllByCreatedBy(dataSource, params)

        respond list, model: [attributeInstanceCount: count, dataSource: dataSource]
    }

    def taxon(String name) {
        respond list, model: [attributeInstanceCount: count]
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
        def dataSourceId = params.containsKey("dataSource") ? params.dataSource : null
        def dataSource = dataSourceId == null ? null : DataSource.get(dataSourceId)
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

                if (dataSource != null) {
                    eq("dataSource", dataSource)
                }
            }
        }

        count = list.totalCount

        def m = [list: list, count: count, query: q, dataSource: dataSource]
        render m as JSON
    }
}
