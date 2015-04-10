package au.org.ala.keys

import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class ProjectController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

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
        list = c.list() {
            projections {
                groupProperty 'id'
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
                }
            }
            if (projects) {
                'in'("id", projects)
            }
        }

        def ids = list.subList(0, list.size())

        //select filtered
        def c2 = Project.createCriteria()
        def list2 = c2.list(params) {
            /*resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)

            projections {
                property("id", "id")
                property("created", "created")
                property("createdBy", "createdBy")
                property("description", "description")
                property("name", "name")
            }*/

            if (ids.size() > 0) {
                'in'("id", ids)
            } else {
                eq("id", -1L)
            }
        }
        count = list2.totalCount

        //format output
        if (params.containsKey("type") && "json".equalsIgnoreCase(params.type)) {
            def map = [projects: list2, totalCount: count, params: params]
            render map as JSON
        } else {
            respond list2, model: [projectInstanceCount: count, query: q]
        }
    }

    def create() {
        respond new Project(params)
    }

    def show(Project projectInstance) {
        if (projectInstance == null) {
            redirect(action: "index")
        }

        def project = [id             : projectInstance.id,
                       alaUserId      : projectInstance.name, created: projectInstance.created,
                       description    : projectInstance.description,
                       attributesCount: projectInstance.keys == null ? 0 : projectInstance.keys.size()]

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
