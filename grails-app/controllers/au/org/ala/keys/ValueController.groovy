package au.org.ala.keys

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class ValueController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Value.list(params), model: [valueInstanceCount: Value.count()]
    }

    def dataSource(DataSource dataSource) {
        params.max = 10

        def count = Value.countByCreatedBy(dataSource)
        def list = Value.findAllByCreatedBy(dataSource, params)

        respond list, model: [valueInstanceCount: count, dataSource: dataSource]
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
