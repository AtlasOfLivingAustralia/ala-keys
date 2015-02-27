package au.org.ala.keys

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class ProjectController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        //query
        def q = params.containsKey("q") ? params.q : null
        def list
        def count
        if (q != null) {
            def c = Project.createCriteria()

            list = c.list(params) {
                or {
                    ilike("description", "%" + q + "%")
                    ilike("name", "%" + q + "%")
                }
            }

            count = list.totalCount
        } else {
            list = Project.list(params)
            count = Project.count()
        }

        respond list, model: [projectInstanceCount: count, query: q]
    }

    def create() {
        respond new Project(params)
    }

    def show(Project projectInstance) {
        if (projectInstance == null) {
            redirect(action: "index")
        }

        //This is to stop the paginate using params.offset/max to calculate current step and use the offset/max attributes instead
        params.offset = null
        params.max = null

        def project = [id             : projectInstance.id,
                       alaUserId      : projectInstance.name, created: projectInstance.created,
                       description    : projectInstance.description,
                       attributesCount: projectInstance.dataSources == null ? 0 : projectInstance.dataSources.size()]

        [project: project]
    }

    @Transactional
    def save(Project projectInstance) {
        if (projectInstance == null) {
            notFound()
            return
        }

        if (projectInstance.hasErrors()) {
            respond projectInstance.errors, view: 'create'
            return
        }

        projectInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'project.label', default: 'Project'), projectInstance.id])
                redirect projectInstance
            }
            '*' { respond projectInstance, [status: CREATED] }
        }
    }

    def edit(Project projectInstance) {
        respond projectInstance
    }

    @Transactional
    def update(Project projectInstance) {
        if (projectInstance == null) {
            notFound()
            return
        }

        if (projectInstance.hasErrors()) {
            respond projectInstance.errors, view: 'edit'
            return
        }

        projectInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Project.label', default: 'Project'), projectInstance.id])
                redirect projectInstance
            }
            '*' { respond projectInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Project projectInstance) {

        if (projectInstance == null) {
            notFound()
            return
        }

        projectInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Project.label', default: 'Project'), projectInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

}
