package au.org.ala.keys

import grails.converters.JSON

/**
 * Created by a on 7/11/14.
 */
class UploadItemController {

    def importStatusService
    def importService
    def authorisationService
    def dataSource

    def create() {
    }

    /**
     * upload a file for import, or json list of taxon/attribute/value
     *
     *{*      apiKey: "123",
     *      alaUserId: "123",
     *      projectId: "1",
     *      data: [{*              lsid: "urn:lsid:123",
     *              attributeId: 123,
     *              attributeLabel: "text",
     *              attributeUnits: "mm",
     *              value: "something"
     *}]
     *}*
     * - Must have attributeId OR attributeLabel.
     * - attributeUnits is optional
     *
     * @return
     */
    def upload() {
        def file
        try {
            file = request.getFile('file')
        } catch (e) {
        }
        if (file == null || file.empty) {
            try {
                def json = request.JSON
                if (json != null && json.size() > 0) {
                    if (!authorisationService.isValidUser()
                            && !authorisationService.validate(json.get("apiKey"), json.get("alaUserId"))) {
                        throw new RuntimeException("Invalid authorisation.")
                    }

                    def values = importService.importJson(json)
                    if (values.size() > 0) {
                        render values as JSON
                    } else {
                        //render(status: 404, text: '{ "error": "No records imported." }', contentType: "application/json")
                        def m = [error: "No records imported."]
                        response.status = 404
                        render m as JSON
                    }
                }
            } catch (e) {
            }
            flash.message = "File cannot be empty"

            render flash as JSON
        } else {
            if (!authorisationService.isValidUser(params.projectId)
                    && !authorisationService.validate(params.apiKey, params.alaUserId)) {
                throw new RuntimeException("Invalid authorisation.")
            }

            def tmp = File.createTempFile("upload", file.originalFilename)
            file.transferTo(tmp)

            def existingKey = params.containsKey('keyId') ? Key.get(Long.parseLong(params.keyId)) : null

            println "debug: projectId=" + params.projectId
            def project = Project.get(Long.parseLong(params.projectId))
            if (project == null) {
                log.error("failed to find project")
            }

            def key = importService.importFile(project, existingKey, tmp, file.originalFilename)

            render key as JSON
        }
    }

    def status(long id) {
        def info = importStatusService.get(id)
        def stat = ""
        def percent = 0

        if (info == null) {
            stat = Key.get(id).status
            if (stat == 'loaded') {
                percent = 100
            } else {
                percent = 0
            }
        } else {
            stat = info[0]
            percent = info[1]
        }

        render(contentType: "text/json") {
            [status: stat, percentage: percent]
        }
    }

}
