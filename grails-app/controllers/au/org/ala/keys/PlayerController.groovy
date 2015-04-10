package au.org.ala.keys

import grails.converters.JSON
import org.hibernate.criterion.CriteriaSpecification

import java.util.concurrent.atomic.AtomicInteger

class PlayerController {

    def MAX_DEPTH = 8

    def importService

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

        def q = params.q

        def list
        def count

        def c = Value.createCriteria()

        //filter
        list = c.list(params) {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)

            createAlias('key', 'key', CriteriaSpecification.LEFT_JOIN)
            createAlias('key.project', 'project', CriteriaSpecification.LEFT_JOIN)
            createAlias('attribute', 'attribute', CriteriaSpecification.LEFT_JOIN)
            createAlias('taxon', 'taxon', CriteriaSpecification.LEFT_JOIN)

            projections {
                property("key.id", "key.id")
                property("key.description", "key.description")
                property("project.id", "project.id")
                property("project.name", "project.name")
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

        count = list.totalCount

        def playerMap = makePlayerMap(list)

        //format output
        if (params.containsKey("type") && "json".equalsIgnoreCase(params.type)) {
            render playerMap as JSON
        } else if (params.containsKey("type") && "json2".equalsIgnoreCase(params.type)) {
            render list as JSON
        } else {
            respond list, model: [valueInstanceCount: count, query: q]
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
    def makePlayerMap(objects) {
        def map = [:]

        //key
        map.put("key_id", 123)
        map.put("key_name", 123)
        map.put("UID", 123)
        map.put("description", 123)
        map.put("rank", 123)
        map.put("taxonomic_scope", 123)
        map.put("geographic_scope", 123)
        map.put("notes", 123)
        map.put("owner", 123)

        //project
        def project = [:]
        project.put("project_id", 123)
        project.put("project_name", 123)
        project.put("project_icon", 123)
        map.put("project", project)

        //source
        def source = [:]
        source.put("author", 123)
        source.put("publication_year", 123)
        source.put("title", 123)
        source.put("in_author", 123)
        source.put("in_title", 123)
        source.put("edition", 123)
        source.put("journal", 123)
        source.put("series", 123)
        source.put("volume", 123)
        source.put("part", 123)
        source.put("publisher", 123)
        source.put("place_of_publication", 123)
        source.put("page", 123)
        source.put("is_modified", 123)
        source.put("url", 123)
        map.put("source", source)

        //taxon
        def items = [:]
        for (Map o : objects) {
            println o.get("taxon.id")
            if (items.get(o.get("taxon.id")) == null) {
                def taxon = [:]
                taxon.put("item_id", o.get("taxon.id"))
                taxon.put("item_name", o.get("taxon.scientificName"))
                taxon.put("url", "http://bie.ala.org.au/species/" + o.get("taxon.lsid"))
                taxon.put("to_key", null)
                taxon.put("link_to_item_id", null)
                taxon.put("link_to_item_name", null)
                taxon.put("link_to_url", null)
                taxon.put("link_to_key", null)
                taxon.put("rank", o.get("taxon.rank"))
                items.put(o.get("taxon.id"), taxon)
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

        //get the next best attribute by taxon coverage
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
        attributes.each { k, v ->
            if (v.size() > maxSize || (v.size() == maxSize && k < attributeId)) {
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
                value = o.get("attribute.label") + ": " +
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
                    next.parent_id = parentId
                    next.lead_id = position.incrementAndGet()
                    next.lead_text = v1.get("taxon.scientificName")
                    next.item = "Taxon: " + v1.get("taxon.id")
                    output.add(next)
                }
            }
        } else if (values.size() > 3 && attributesUniqueNames.size() > 1 && depth < MAX_DEPTH) {
            //convert to list of q's for next attribute
            attributesUniqueNames.each { v ->
                def next = [:]
                next.parent_id = parentId
                next.lead_id = position.incrementAndGet()
                next.lead_text = "Question: " + v
                next.item = null

                output.add(next)

                //repeat using subset of objects that have the same unique attribute name
                def list = new ArrayList<Map>()
                println("nextAttributeLabel: " + nextAttributeLabel)
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
                next.parent_id = parentId
                next.lead_id = position.incrementAndGet()
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
                    next.item = tx.first()

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