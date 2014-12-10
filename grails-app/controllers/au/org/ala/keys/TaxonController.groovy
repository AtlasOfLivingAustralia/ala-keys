package au.org.ala.keys

import grails.transaction.Transactional

import static grails.async.Promises.onComplete
import static grails.async.Promises.task
import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class TaxonController {

    def searchTaxonLsidService
    def valuesService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Taxon.list(params), model: [taxonInstanceCount: Taxon.count()]
    }

    def show(Taxon taxonInstance) {
        def inheritedValues
        if (taxonInstance != null && taxonInstance.lsid != null) {
            def m = [scientificName: taxonInstance.lsid, inheritedOnly: true]
            inheritedValues = valuesService.findAllValuesAsList(m)
        }

        respond "show", model: [taxon: taxonInstance, inherited: inheritedValues]
    }

    def create() {
        respond new Taxon(params)
    }

    def synchronized updateLsids() {
        def importTask = task {
            try {
                searchTaxonLsidService.updateTaxonAll()
            } catch (e) {
                log.error("failed to import: " + path, e)
            }
        }

        //continue
        onComplete([importTask]) { List result ->
            log.debug("finished task: " + result.get(0))
        }

        render(contentType: "text/json") {
            [status: "started"]
        }
    }

    @Transactional
    def save(Taxon taxonInstance) {
        if (taxonInstance == null) {
            notFound()
            return
        }

        if (taxonInstance.hasErrors()) {
            respond taxonInstance.errors, view: 'create'
            return
        }

        taxonInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'taxon.label', default: 'Taxon'), taxonInstance.id])
                redirect taxonInstance
            }
            '*' { respond taxonInstance, [status: CREATED] }
        }
    }

    def edit(Taxon taxonInstance) {
        respond taxonInstance
    }

    @Transactional
    def update(Taxon taxonInstance) {
        if (taxonInstance == null) {
            notFound()
            return
        }

        if (taxonInstance.hasErrors()) {
            respond taxonInstance.errors, view: 'edit'
            return
        }

        taxonInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Taxon.label', default: 'Taxon'), taxonInstance.id])
                redirect taxonInstance
            }
            '*' { respond taxonInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Taxon taxonInstance) {

        if (taxonInstance == null) {
            notFound()
            return
        }

        taxonInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Taxon.label', default: 'Taxon'), taxonInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'taxon.label', default: 'Taxon'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    def search() {
        def lsid = params.get("lsid")
        def scientificName = params.get("scientificName")

        List taxonList = {
            if (lsid != null) {
                Taxon.findAllByLsid(lsid)
            } else {
                Taxon.findAllByScientificName(scientificName)
            }
        }

        //traverse and build
        Map attributes = [:]
        taxonList.each() { taxon ->
            attributes.putAll(getMapOfValues(taxon))
        }

        Map m = [:]
        m.put("lsid", lsid)
        m.put("attributes", attributes)

        render m
    }

    def getMapOfValues(Taxon t) {
        def map = [:]

        t.values.each() { a ->
            if (a.attribute.characterTypeText) {
                map.put(a.attribute.label, a.text)
            } else if (a.attribute.characterTypeNumeric) {
                if (a.min == a.max) {
                    map.put(a.attribute.label, (a.min + " " + a.attribute.units).trim())
                } else {
                    map.put(a.attribute.label, (a.min + "-" + a.max + " " + a.attribute.units).trim())
                }
            }
        }

        if (t.parent != null) {
            map.putAll(getMapOfValues(t.parent))
        }
    }
}
