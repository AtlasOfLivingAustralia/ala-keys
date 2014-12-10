package au.org.ala.keys

import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class DataSourceController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        //query
        def q = params.containsKey("q") ? params.q : null
        def list
        def count
        if (q != null) {
            def c = DataSource.createCriteria()

            list = c.list(params) {
                or {
                    ilike("filename", "%" + q + "%")
                    ilike("alaUserId", "%" + q + "%")
                    ilike("status", "%" + q + "%")
                }
            }

            count = list.totalCount
        } else {
            list = DataSource.list(params)
            count = DataSource.count()
        }

        respond list, model: [dataSourceInstanceCount: count, query: q]
    }

    def show(DataSource dataSourceInstance) {
        if (dataSourceInstance == null) {
            redirect(action: "index")
        }

        //This is to stop the paginate using params.offset/max to calculate current step and use the offset/max attributes instead
        params.offset = null
        params.max = null

        def dataSource = [id             : dataSourceInstance.id,
                          alaUserId      : dataSourceInstance.alaUserId, created: dataSourceInstance.created,
                          description    : dataSourceInstance.description, filename: dataSourceInstance.filename,
                          status         : dataSourceInstance.status,
                          valuesCount    : dataSourceInstance.values == null ? 0 : dataSourceInstance.values.size(),
                          attributesCount: dataSourceInstance.attributes == null ? 0 : dataSourceInstance.attributes.size()]

        [dataSource: dataSource]
    }

    def create() {
        redirect([controller: "uploadItem", action: "create"])
    }

    @Transactional
    def save(DataSource dataSourceInstance) {
        if (dataSourceInstance == null) {
            notFound()
            return
        }

        if (dataSourceInstance.hasErrors()) {
            respond dataSourceInstance.errors, view: 'create'
            return
        }

        dataSourceInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'dataSource.label', default: 'DataSource'), dataSourceInstance.id])
                redirect dataSourceInstance
            }
            '*' { respond dataSourceInstance, [status: CREATED] }
        }
    }

    def edit(DataSource dataSourceInstance) {
        respond dataSourceInstance
    }

    @Transactional
    def update(DataSource dataSourceInstance) {
        if (dataSourceInstance == null) {
            notFound()
            return
        }

        if (dataSourceInstance.hasErrors()) {
            respond dataSourceInstance.errors, view: 'edit'
            return
        }

        dataSourceInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'DataSource.label', default: 'DataSource'), dataSourceInstance.id])
                redirect dataSourceInstance
            }
            '*' { respond dataSourceInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(DataSource dataSourceInstance) {

        if (dataSourceInstance == null) {
            notFound()
            return
        }

        dataSourceInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'DataSource.label', default: 'DataSource'), dataSourceInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'dataSource.label', default: 'DataSource'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }


    def download(long id) {
        def dataSource = DataSource.get(id)
        if (dataSource == null) {
            flash.message = "File not found."
            redirect(action: 'list')
        } else {
            response.setContentType("APPLICATION/OCTET-STREAM")
            response.setHeader("Content-Disposition", "Attachment;Filename=\"${dataSource.filename}\"")

            def file = new File(dataSource.getFilePath())
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
