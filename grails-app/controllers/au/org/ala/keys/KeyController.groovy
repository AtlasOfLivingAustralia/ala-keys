package au.org.ala.keys

import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONArray
import org.hibernate.criterion.*

@Transactional(readOnly = true)
class KeyController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def taxonService
    def keyService

    def index() {
        params.max = Math.min(Integer.parseInt(params.max ?: "10"), 100)

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
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)

            projections {
                property 'id', 'id'
                property 'created', 'created'
                property 'description', 'description'
                property 'filename', 'filename'
                property 'geographicScope', 'geographicScope'
                property 'name', 'name'
                property 'project.id', 'projectId'
                property 'project.name', 'projectName'
                property 'scopeTaxons.id', 'taxonId'
                property 'scopeTaxons.scientificName', 'taxonScientificName'
                property 'scopeTaxons.lsid', 'taxonLsid'
                property 'status', 'status'
            }


            createAlias('scopeTaxons', 'scopeTaxons', CriteriaSpecification.LEFT_JOIN)

            if (projectIds) {
                'in'("project.id", projectIds)
            }

            createAlias('project', 'project', CriteriaSpecification.LEFT_JOIN)
            if (users) {
                createAlias('project.users', 'users', CriteriaSpecification.LEFT_JOIN)
                'in'("user", users)
            }

            if (attributeIds) {
                def subquery2 = Attribute.where {
                    projections {
                        distinct 'key.id'
                    }
                    'in'("id", attributeIds)
                }
                'in'("id", subquery2)
            }
            if (valueIds || lsids || taxonIds) {
                def subquery = Value.where {
                    projections {
                        distinct 'key.id'
                    }

                    if (valueIds) {
                        'in'("id", valueIds)
                    }

                    if (lsids || taxonIds) {
                        if (taxonIds) {
                            'in'("taxon.id", taxonIds)
                        }
                        if (lsids) {
                            taxon(CriteriaSpecification.INNER_JOIN) {
                                if (lsids) {
                                    'in'('lsid', lsids)
                                }
                            }
                        }
                    }
                }
                'in'("id", subquery)
            }

            if (q) {
                or {
                    ilike("name", "%" + q + "%")
                    ilike("description", "%" + q + "%")
                    ilike("geographicScope", "%" + q + "%")
                    ilike("filename", "%" + q + "%")
                    ilike("status", "%" + q + "%")
                }
            }
            if (keyIds) {
                'in'("id", keyIds)
            }
        }

        //TODO: fix count (it is increased for each key with > 1 scopeTaxon
        count = list.totalCount

        list = list.groupBy {
            it.id
        }.collect { k, v ->
            [id      : v[0].id, name: v[0].name, description: v[0].description,
             filename: v[0].filename, geographicScope: v[0].geographicScope, status: v[0].status,
             created : v[0].created, scopeTaxons: v.findAll { it.taxonId != null }.collect {
                [id: it.taxonId, scientificName: it.taxonScientificName, lsid: it.taxonLsid]
            }]
        }

        //format output
        def map = [keys: list, totalCount: count, params: params]
        render map as JSON
    }

    @Transactional
    def create() {
        def json = request.getJSON()
        def scopeTaxons
        if (json.containsKey('scopeTaxons')) {
            scopeTaxons = json.scopeTaxons
            json.remove('scopeTaxons')
        }
        def p = new Key(json)
        p.scopeTaxons = []
        if (scopeTaxons != null) {
            ((JSONArray) scopeTaxons).each { sn ->
                if (sn != null && sn.toString().length() > 0) {
                    def taxon = taxonService.findOrCreateWithScientificName(sn)
                    p.scopeTaxons.add(taxon)
                }
            }
        }

        p.save(flush: true)
        if (p.hasErrors()) {
            render p.errors as JSON
            return
        }
        render p as JSON
    }

    def show(Long id) {
        def keyInstance = Key.get(id)
        if (keyInstance == null) {
            def map = [error: "invalid key id"]
            render map as JSON
        } else {
            render keyInstance as JSON
        }
    }

    @Transactional
    def update() {
        def json = request.getJSON()

        def keyInstance = Key.get(json.id)
        if (keyInstance == null) {
            def map = [error: "invalid key id"]
            render map as JSON
            return
        }

        if (json.containsKey('description')) {
            keyInstance.description = json.description
        }

        if (json.containsKey('name')) {
            keyInstance.name = json.name
        }

        if (json.containsKey('geographicScope')) {
            keyInstance.geographicScope = json.geographicScope
        }

        if (json.containsKey('metadata')) {
            keyInstance.metadata = json.metadata as Map
        }

        if (json.containsKey('scopeTaxons')) {
            keyInstance.scopeTaxons = []
            ((JSONArray) json.scopeTaxons).each { sn ->
                if (sn != null && sn.toString().length() > 0) {
                    def taxon = taxonService.findOrCreateWithScientificName(sn)
                    keyInstance.scopeTaxons.add(taxon)
                }
            }
        }

        if (keyInstance.hasErrors()) {
            render keyInstance.errors as JSON
            return
        }

        keyInstance.save flush: true
        if (keyInstance.hasErrors()) {
            render keyInstance.errors as JSON
            return
        }

        render keyInstance as JSON
    }

    @Transactional
    def delete(Key keyInstance) {

        if (keyInstance == null) {
            def map = [error: "invalid key id"]
            render map as JSON
            return
        }

        keyInstance.delete flush: true

        render [:] as JSON
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
