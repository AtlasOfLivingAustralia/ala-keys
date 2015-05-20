package au.org.ala.keys

import grails.converters.JSON
import groovy.sql.Sql
import org.hibernate.criterion.CriteriaSpecification

import java.util.concurrent.atomic.AtomicInteger

/**
 * Transforms key into couplet form for a player
 *
 * For dichotomous keys.
 * *
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
        }

        if (list.size() == 0) {
            def map = [error: 'key not found']
            render map as JSON
        } else {
            //metadata
            final Sql sql = new Sql(dataSource)
            def query = "select * from key_metadata where metadata = :key_id"
            def metadataMap = [:]
            sql.rows(query, key_id: keys[0]).each { metadataMap.put(it.metadata_idx, it.metadata_elt) }

            def playerMap = makePlayerMap(list, metadataMap)

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
    def makePlayerMap(objects, metadata) {
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
//        source.put("author", null)
//        source.put("publication_year", null)
//        source.put("title", null)
//        source.put("in_author", null)
//        source.put("in_title", null)
//        source.put("edition", null)
//        source.put("journal", null)
//        source.put("series", null)
//        source.put("volume", null)
//        source.put("part", null)
//        source.put("publisher", null)
//        source.put("place_of_publication", null)
//        source.put("page", null)
//        source.put("is_modified", null)
//        source.put("url", null)
        map.put("source", source)

        //taxon
        def items = [:]
        for (Map mo : objects) {
            if (items.get(mo.get("taxon.id")) == null) {
                def taxon = [:]
                taxon.put("item_id", String.valueOf(mo.get("taxon.id")))
                taxon.put("item_name", mo.get("taxon.scientificName"))
                taxon.put("url", "http://bie.ala.org.au/species/" + mo.get("taxon.lsid"))
                taxon.put("to_key", null)
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
        AtomicInteger ids = new AtomicInteger(1)

        def orderedQuestions = orderValues(values, ids.intValue(), ids, null, 1)

        orderedQuestions
    }

    def orderValues(objects, parentId, position, nextAttributeLabel, depth) {
        List<Map> output = new ArrayList<Map>()


        def attributes = [:]
        Set attributesUniqueNames = new HashSet<String>()
        for (Map p : objects) {
            if (nextAttributeLabel == null || nextAttributeLabel.equals(p.get("attribute.label"))) {
                def key = p.get("attribute.id")
                attributesUniqueNames.add(p.get("attribute.label"))
                Set set = null
                if (attributes.containsKey(key)) {
                    set = attributes.get(key)
                } else {
                    set = new HashSet()
                }
                set.add(p.get("taxon.scientificName"))
                attributes.put(key, set)
            }
        }

        def maxSize = 0
        def attributeId = -1
        //(old) get the next best attribute by taxon coverage
//        attributes.each { k, v ->
//            if (v.size() > maxSize || (v.size() == maxSize && k < attributeId)) {
//                attributeId = k
//                maxSize = v.size()
//            }
//        }
        //(keybase) get the next best attribute by largest taxon count to identify the next ordered couplet
        attributes.each { k, v ->
            if (attributeId == -1 || maxSize < v.size()) {
                attributeId = k
                maxSize = v.size()
            }
        }

        Set taxonSet = attributes.get(attributeId)

        //split attribute by value description
        //include value description "Not defined" for uncovered taxa
        Map values = [:]
        for (Map p : objects) {
            def key = p.get("attribute.id")
            def value = null
            if (key == attributeId) {
                def o = p
                value = "description".equals(o.get("attribute.label")) ? "" : o.get("attribute.label") + ": "
                value +=
                        (o.get("text") != null ? o.get("text") :
                                (o.get("min") == o.get("max") ? o.get("min") : o.get("min") + " - " + o.get("max"))) +
                        " " + (o.get("attribute.units") != null ? o.get("attribute.units") : "")
            } else if (!taxonSet.contains(p.get("taxon.scientificName"))) {
                value = "Not defined"
            }
            if (value != null) {
                def list = values.get(value)
                if (list == null) list = new ArrayList<Map>()
                list.add(p)
                values.put(value, list)
            }
        }

        if (values.size() == 1) {
            //convert to list of q's for taxon
            values.each { k, v ->
                v.each { v1 ->
                    def next = [:]
                    next.parent_id = String.valueOf(parentId)
                    next.lead_id = String.valueOf(position.incrementAndGet())
                    next.lead_text = v1.get("taxon.scientificName")
                    //next.item = "Taxon: " + v1.get("taxon.id")
                    next.item = String.valueOf(v1.get("taxon.id"))
                    output.add(next)
                }
            }
        } else if (values.size() > 3 && attributesUniqueNames.size() > 1 && depth < MAX_DEPTH) {
            //convert to list of q's for next attribute
            attributesUniqueNames.each { v ->
                def next = [:]
                next.parent_id = String.valueOf(parentId)
                next.lead_id = String.valueOf(position.incrementAndGet())
                next.lead_text = "Question: " + v
                next.item = null

                output.add(next)

                //repeat using subset of objects that have the same unique attribute name
                def list = new ArrayList<Map>()
                objects.each { obj ->
                    if (nextAttributeLabel == null || !nextAttributeLabel.equals(obj.get("attribute.label"))) {
                        list.add(obj)
                    }
                }
                output.addAll(orderValues(list, next.lead_id, position, v, depth + 1))
            }
        } else {
            //drill into each value
            values.each { k, v ->
                //write this one
                def next = [:]
                next.parent_id = String.valueOf(parentId)
                next.lead_id = String.valueOf(position.incrementAndGet())
                def o = v.get(0)
                next.lead_text = k

                //set taxon value if within the remaining v's if # taxon = 1
                Set vs = new HashSet<String>()
                Set tx = new HashSet<String>()
                v.each { a ->
                    vs.add(a.get("id"))
                    tx.add(a.get("taxon.id"))
                }
                if (tx.size() == 1) {
                    next.item = String.valueOf(tx.first())

                    output.add(next)
                } else {
                    next.item = null

                    output.add(next)

                    //repeat using subset of objects that intersect v.taxon (tx)
                    def list = new ArrayList<Map>()
                    objects.each { obj ->
                        if (tx.contains(obj.get("taxon.id")) && obj.get("attribute.id") != attributeId) {
                            list.add(obj)
                        }
                    }
                    output.addAll(orderValues(list, next.lead_id, position, null, depth + 1))
                }
            }
        }

        output
    }
}