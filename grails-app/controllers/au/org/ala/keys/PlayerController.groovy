package au.org.ala.keys
import grails.converters.JSON
import groovy.sql.Sql
import org.hibernate.criterion.CriteriaSpecification
/**
 * Transforms key into couplet form for a player.
 *
 * For dichotomous keys. These are keys that are created from a single text file import.
 *
 */
class PlayerController {

    //set to 0 for use with keybase data only
    def MAX_DEPTH = 0

    def importService

    def dataSource

    def index(Integer max) {
        if (params.exactMatch == null) params.exactMatch = "false"

        //query
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

        //key_id mandatory
        if (params.containsKey("key_id")) {
            keys = [Long.parseLong(params.key_id)]
        } else {
            return null
        }

        def q = params.q

        def list
        def count

        def c = Value.createCriteria()

        //filter
        list = c.list(params) {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)

            createAlias('key', 'key', CriteriaSpecification.LEFT_JOIN)
            createAlias('key.scopeTaxons', 'scopeTaxons', CriteriaSpecification.LEFT_JOIN)
            createAlias('key.project', 'project', CriteriaSpecification.LEFT_JOIN)
            createAlias('attribute', 'attribute', CriteriaSpecification.LEFT_JOIN)
            createAlias('taxon', 'taxon', CriteriaSpecification.LEFT_JOIN)

            projections {
                property("key.id", "key.id")
                property("key.name", "key.name")
                property("key.description", "key.description")
                property("scopeTaxons.rank", "key.scopeTaxons.rank")
                property("scopeTaxons.scientificName", "key.scopeTaxons.scientificName")
                property("project.id", "project.id")
                property("project.name", "project.name")
                property("key.geographicScope", "key.geographicScope")
                property("project.imageUrl", "project.imageUrl")
                property("id", "id")
                property("min", "min")
                property("max", "max")
                property("text", "text")
                property("attribute.id", "attribute.id")
                property("attribute.label", "attribute.label")
                property("attribute.units", "attribute.units")
                property("taxon.id", "taxon.id")
                property("taxon.lsid", "taxon.lsid")
                property("taxon.rank", "taxon.rank")
                property("taxon.scientificName", "taxon.scientificName")
            }

            if (keys) {

                'in'("key.id", keys)
            }
            if (projects || users) {
                if (projects) {
                    'in'("project.id", projects)
                }
            }
            if (users) {
                createAlias('project.users', 'users', CriteriaSpecification.LEFT_JOIN)
                'in'("user", users)
            }
            if (attributes) {

                'in'("attribute.id", attributes)
            }
            if (lsids || taxons) {

                if (taxons) {
                    'in'("taxon.id", taxons)
                }
                if (lsids) {
                    'in'("taxon.lsid", lsids)
                }
            }
            if (values) {
                'in'("id", values)
            }

            order('id')
        }

        if (list.size() == 0) {
            def map = [error: 'key not found']
            render map as JSON
        } else {
            //taxon to key link, within this project only
            final Sql sql = new Sql(dataSource)
            def query = "select key_id as key_id, scientific_name from key_scopetaxons inner join taxon on taxon_id = taxon.id " +
                    "                         inner join key on key_scopetaxons.key_id = key.id where project_id = :project" +
                    "                         union" +
                    "                         select value.key_id as id, " +
                    "                         (case when count(distinct(pid)) > 1 then max(kid_name)" +
                    "                         when count(distinct(cid)) > 1 then max(pid_name)" +
                    "                         when count(distinct(oid)) > 1 then max(cid_name)" +
                    "                         when count(distinct(fid)) > 1 then max(oid_name)" +
                    "                         when count(distinct(gid)) > 1 then max(fid_name)" +
                    "                         when count(distinct(sid)) > 1 then max(gid_name)" +
                    "                         else max(sid_name) end) as name" +
                    "                         from value inner join taxon on value.taxon_id = taxon.id" +
                    "                         inner join key on key.id = value.key_id" +
                    "                         left join key_scopetaxons on value.key_id = key_scopetaxons.key_id" +
                    "                         where key_scopetaxons.key_id is null" +
                    "                         and project_id = :project" +
                    "                         group by value.key_id"

            def taxonKeyLink = [:]
            sql.rows(query, project: list[0].getAt('project.id')).each {
                taxonKeyLink.put(it.scientific_name, it.key_id)
            }

            //metadata
            def query2 = "select * from key_metadata where metadata = :key_id"
            def metadataMap = [:]
            sql.rows(query2, key_id: keys[0]).each { metadataMap.put(it.metadata_idx, it.metadata_elt) }

            def playerMap = makePlayerMap(list, metadataMap, taxonKeyLink)

            //format output
            render playerMap as JSON
        }
    }

    /**
     * Make a map like:
     *
     key_id: "2413",
     key_name: "Banksia",
     UID: "002120",
     description: null,
     rank: null,
     taxonomic_scope: "Banksia",
     geographic_scope: "Victoria",
     notes: null,
     owner: "Niels Klazenga",
     project: {project_id: "10",
     project_name: "Flora of Victoria",
     project_icon: "http://keybase.rbg.vic.gov.au/images/projecticons/epacris.png"},
     source: {author: "Jeanes, J.A.",
     publication_year: "1996",
     title: "Proteaceae",
     in_author: "Walsh, N.G.; Entwisle, T.J. (eds)",
     in_title: "Flora of Victoria. Vol. 3. Dicotyledons Winteraceae to Myrataceae",
     edition: null,
     journal: null,
     series: null,
     volume: null,
     part: null,
     publisher: "Inkata Press",
     place_of_publication: "Melbourne",
     page: null,
     is_modified: false,
     url: null},
     items: [{item_id: "1000",
     item_name: "Banksia canei",
     url: "http://data.rbg.vic.gov.au/vicflora/flora/taxon/e116fef0-5b38-4efc-98ea-a9f9ac3a82c4",
     to_key: null,
     link_to_item_id: null,
     link_to_item_name: null,
     link_to_url: null,
     link_to_key: null}],
     first_step: {root_node_id: "235842"},
     leads: [{parent_id: "235842",
     lead_id: "235843",
     lead_text: "Style straightening or remaining weakly curved at anthesis, yellowish; perianth creamy-yellow to brownish; cones short-cylindric, ovoid or globose; leaves not as above",
     item: null}* @param objects
     * @return
     */
    def makePlayerMap(objects, metadata, taxonKeyLink) {
        def map = [:]

        def o = objects.get(0)

        //key
        map.put("key_id", o.get("key.id"))
        map.put("key_name", o.get("key.name"))
        map.put("UID", null)
        map.put("description", o.get("key.description"))
        map.put("rank", o.get("key.scopeTaxons.rank"))
        map.put("taxonomic_scope", o.get("key.scopeTaxons.scientificName"))
        map.put("geographic_scope", o.get("key.geographicScope"))
        map.put("notes", "")
        map.put("owner", "")

        //project
        def project = [:]
        project.put("project_id", o.get("project.id"))
        project.put("project_name", o.get("project.name"))
        project.put("project_icon", o.get("project.imageUrl"))
        map.put("project", project)

        //source
        def source = metadata
        map.put("source", source)

        //taxon
        def items = [:]
        for (Map mo : objects) {
            if (items.get(mo.get("taxon.id")) == null) {
                def taxon = [:]
                def sn = mo.get("taxon.scientificName")
                taxon.put("item_id", String.valueOf(mo.get("taxon.id")))
                taxon.put("item_name", sn)
                taxon.put("url", "http://bie.ala.org.au/species/" + mo.get("taxon.lsid"))
                taxon.put("to_key", taxonKeyLink.containsKey(sn) ? String.valueOf(taxonKeyLink.get(sn)) : null)
                taxon.put("link_to_item_id", null)
                taxon.put("link_to_item_name", null)
                taxon.put("link_to_url", null)
                taxon.put("link_to_key", null)
                taxon.put("rank", mo.get("taxon.rank"))
                items.put(mo.get("taxon.id"), taxon)
            }
        }
        map.put("items", items.values())

        //first step
        def first_step = [:]
        first_step.put("root_node_id", 1)
        map.put("first_step", first_step)

        //questions
        map.put("leads", orderQuestions(objects))

        map
    }

    def orderQuestions(values) {
        //for keybase data

        def currentId = 1

        def writtenAttributes = [:]
        def previousTaxon = null
        def orderedQuestions = []
        def parentIds = [:]

        def parentId = 1

        def firstAttributeId = values.get(0).get("attribute.id")

        for (Map o : values) {
            def attributeId = o.get("attribute.id")
            def label = "description".equals(o.get("attribute.label")) ? "" : o.get("attribute.label") + ": "
            label +=
                    (o.get("text") != null ? o.get("text") :
                            (o.get("min") == o.get("max") ? o.get("min") : o.get("min") + " - " + o.get("max"))) +
                            " " + (o.get("attribute.units") != null ? o.get("attribute.units") : "")
            def tid = o.get("taxon.id")

            def key = attributeId + ';' + label

            // update taxonId of previous record
            if (orderedQuestions.size() > 0 && orderedQuestions.last().item == null &&
                    (parentIds.containsKey(attributeId) || tid != previousTaxon)) {
                orderedQuestions.last().item = String.valueOf(previousTaxon)
            }

            // write this lead if it not already written
            if (!writtenAttributes.containsKey(key)) {
                //lead id
                currentId = currentId + 1

                writtenAttributes.put(key, currentId)

                if (parentIds.containsKey(attributeId)) {
                    parentId = parentIds.get(attributeId)
                } else {
                    parentIds.put(attributeId, parentId)
                }

                def next = [:]
                next.parent_id = String.valueOf(parentId)
                next.lead_id = String.valueOf(currentId)
                next.lead_text = label

                //next.item is updated in the next loop 
                next.item = null

                orderedQuestions.add(next)

                //makes parentId available for next loop
                //when there is rollback to a previous attribute the parentId stored in parentIds is used
                parentId = currentId
            }

            previousTaxon = tid
        }

        // update taxonId of previous record
        orderedQuestions.last().item = String.valueOf(previousTaxon)

        orderedQuestions
    }
}