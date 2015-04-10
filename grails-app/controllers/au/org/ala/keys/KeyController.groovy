package au.org.ala.keys

import grails.converters.JSON
import grails.transaction.Transactional
import org.grails.datastore.mapping.query.Restrictions
import org.hibernate.Criteria
import org.hibernate.criterion.CriteriaSpecification

import javax.persistence.criteria.JoinType

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class KeyController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def keyService

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

        def c = Key.createCriteria()

        //filter
        list = c.list(params) {

            if (projectIds) {
                'in'("project.id", projectIds)
            }
            if (users) {
                createAlias('project', 'project', CriteriaSpecification.LEFT_JOIN)
                createAlias('project.users', 'users', CriteriaSpecification.LEFT_JOIN)
                'in'("user", users)
            }
            if (attributeIds) {
                attributes(CriteriaSpecification.INNER_JOIN) {
                    groupProperty('key')
                    'in'("id", attributeIds)
                }
            }
            if (valueIds || lsids || taxonIds) {
                values(CriteriaSpecification.INNER_JOIN) {
                    groupProperty('key')

                    if (valueIds) {
                        'in'("id", valueIds)
                    }

                    if (lsids || taxonIds) {
                        taxon(CriteriaSpecification.INNER_JOIN) {
                            if (lsids) {
                                'in'('lsid', lsids)
                            }
                            if (taxonIds) {
                                'in'("id", taxonIds)
                            }
                        }
                    }
                }
            }

            if (q) {
                or {
                    ilike("filename", "%" + q + "%")
                    ilike("status", "%" + q + "%")
                }
            }
            if (keyIds) {
                'in'("id", keyIds)
            }
        }

        count = list.totalCount

        //format output
        if (params.containsKey("type") && "json".equalsIgnoreCase(params.type)) {
            def map = [keys: list, totalCount: count, params: params]
            render map as JSON
        } else {
            respond list, model: [k: list, keyInstanceCount: count, query: q]
        }
    }

    def show(Key keyInstance) {
        if (keyInstance == null) {
            redirect(action: "index")
        }

        //This is to stop the paginate using params.offset/max to calculate current step and use the offset/max attributes instead
        params.offset = null
        params.max = null

        def key = [id             : keyInstance.id,
                   created        : keyInstance.created,
                   description    : keyInstance.description, filename: keyInstance.filename,
                   status         : keyInstance.status,
                   valuesCount    : keyInstance.values == null ? 0 : keyInstance.values.size(),
                   attributesCount: keyInstance.attributes == null ? 0 : keyInstance.attributes.size(),
                   projectName    : keyInstance.project.name,
                   projectId      : keyInstance.project.id,]

        [key: key]
    }

    def create() {
        redirect([controller: "uploadItem", action: "create"])
    }

    @Transactional
    def save(Key keyInstance) {
        if (keyInstance == null) {
            notFound()
            return
        }

        if (keyInstance.hasErrors()) {
            respond keyInstance.errors, view: 'create'
            return
        }

        keyInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'key.label', default: 'Key'), keyInstance.id])
                redirect keyInstance
            }
            '*' { respond keyInstance, [status: CREATED] }
        }
    }

    def edit(Key keyInstance) {
        respond keyInstance
    }

    @Transactional
    def update(Key keyInstance) {
        if (keyInstance == null) {
            notFound()
            return
        }

        if (keyInstance.hasErrors()) {
            respond keyInstance.errors, view: 'edit'
            return
        }

        keyInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'key.label', default: 'Key'), keyInstance.id])
                redirect keyInstance
            }
            '*' { respond keyInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Key keyInstance) {

        if (keyInstance == null) {
            notFound()
            return
        }

        keyInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'key.label', default: 'Key'), keyInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'key.label', default: 'Key'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }


    def download(long id) {
        def key = Key.get(id)
        if (key == null) {
            flash.message = "File not found."
            redirect(action: 'list')
        } else {
            response.setContentType("APPLICATION/OCTET-STREAM")
            response.setHeader("Content-Disposition", "Attachment;Filename=\"${key.filename}\"")

            def file = new File(keyService.getFilePath(key))
            def fileInputStream = new FileInputStream(file)
            def outputStream = response.getOutputStream()

            byte[] buffer = new byte[4096];
            int len;
            while ((len = fileInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            outputStream.flush()
            outputStream.close()
            fileInputStream.close()
        }
    }

}
