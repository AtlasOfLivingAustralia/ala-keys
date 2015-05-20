package au.org.ala.keys

import grails.converters.JSON
import org.hibernate.criterion.CriteriaSpecification

import java.util.concurrent.atomic.AtomicInteger

/**
 * Player that uses params to determine current state.
 */
class PlayController {

    def importService

    def index(Integer max) {

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

        //rebuild query for return URLs
        String currentQuery = ""
        if (projects != null) currentQuery += "&projects=" + params.projects
        if (keys != null) currentQuery += "&keys=" + params.keys
        if (attributes != null) currentQuery += "&attributes=" + params.attributes
        if (values != null) currentQuery += "&values=" + params.values
        if (taxons != null) currentQuery += "&taxons=" + params.taxons
        if (lsids != null) currentQuery += "&lsids=" + params.lsids
        if (users != null) currentQuery += "&users=" + params.users
        if (currentQuery.startsWith('&')) currentQuery = currentQuery.substring(1)

        //current state
        String currentState = params.containsKey("state") ? params.state : null

        //For each currentState, get a subquery of the taxonIds contained within. These will filter out unmatched taxonIds
        def stateList = []
        def selectedAttribute = null
        def subqueries = []
        def excludeAttributes = []
        if (currentState != null) {
            def currentStateList = currentState.split("\\|")
            currentStateList.eachWithIndex { v, idx ->
                def t = v.split(":")
                if (t.length == 1) {
                    // selected attribute only occurs at the last state position
                    if (idx == currentStateList.size() - 1) selectedAttribute = t[0]
                } else {

                    excludeAttributes.add(Long.parseLong(t[0]))

                    def q = t[1]
                    def min = null
                    def mx = null
                    def sq = null
                    if (q.startsWith("r(") && q.endsWith(")")) {
                        q = q.substring(6, q.length() - 1)
                        min = importService.parseMin(q)
                        mx = importService.parseMax(q)

                        if (min == mx) {
                            stateList.add([attribute: Attribute.get(t[0]), text: min + " - " + mx])
                        } else {
                            stateList.add([attribute: Attribute.get(t[0]), text: min])
                        }
                    } else {
                        sq = Value.where {
                            projections {
                                distinct 'text'
                            }
                            eq('id', Long.parseLong(q))
                        }
                        def a = Attribute.get(t[0])
                        stateList.add([attributeId: a.id, label: a.label, text: sq.list().get(0)])
                    }

                    //fetch taxon matching the specified attribute and value/range
                    def subq = Value.where {

                        projections {
                            distinct 'taxon.id'
                        }

                        //identify taxon.ids that do not match the provided values
                        if (min != null && max != null) {
                            or {
                                gt("min", max)
                                lt("max", min)
                            }
                        } else if (sq != null) {
                            'notIn'("text", sq)
                        }

                        eq('attribute.id', Long.parseLong(t[0]))
                    }

                    //fetch lsid for the matched taxon
                    def subqt = Taxon.where {
                        projections {
                            distinct 'lsid'
                        }
                        'in'('id', subq)
                        isNotNull('lsid')
                    }

                    subqueries.add([subq, subqt])
                }
            }
        }



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
            if (taxons) {
                'in'("taxon.id", taxons)
            }
            if (lsids) {
                'in'("taxon.lsid", lsids)
            }

            //exclusion of taxon that are in the currentState Attribute:Value listing
            //but keeping taxon that are not defined in the currentState Attribute at all
            if (subqueries.size() > 0) {
                subqueries.each { l ->
                    def sq = l[0]
                    def subqt = l[1]
                    'notIn'("taxon.id", sq)
                    'notIn'("taxon.lsid", subqt)
                    'notIn'("taxon.kid", subqt)
                    'notIn'("taxon.pid", subqt)
                    'notIn'("taxon.cid", subqt)
                    'notIn'("taxon.oid", subqt)
                    'notIn'("taxon.fid", subqt)
                    'notIn'("taxon.gid", subqt)
                    'notIn'("taxon.sid", subqt)
                }
            }
            if (excludeAttributes.size() > 0) {
                excludeAttributes.each { a ->
                    notEqual('attribute.id', a)
                }

            }
            if (values) {
                'in'("id", values)
            }
        }

        count = list.totalCount

        def playerMap = makePlayerMap2(list, currentQuery, currentState, selectedAttribute)
        playerMap.put("currentState", stateList)

        //format output
        render playerMap as JSON
    }

    /**
     * Make a map like (4 panel):
     *
     * Choose attribute for drill down{taxonIncluded: [ { scientificName:"", taxonId:1 } ],
     taxonExcluded: [ { scientificName:"", taxonId:1 } ],
     currentState: [ { attributeLabel: "label", value: "value" } ]
     questions: [{label: "attribute label",
     url: "http://localhost:8080/ala-keys/player2?" + query + "&state=a1,"}]}OR

     Choose attribute value for drill down{taxonIncluded: [ { scientificName:"", taxonId:1 } ],
     taxonExcluded: [ { scientificName:"", taxonId:1 } ],
     currentState: [ { attributeLabel: "label", value: "value" } ]
     questions: [{label: "attribute label + value",
     url: "http://localhost:8080/ala-keys/player2?" + query + "&state=attributeId:" + questionValue}]}* @param objects
     * @return
     */
    def makePlayerMap2(objects, currentQuery, currentState, selectedAttribute) {
        def map = [:]

        //taxon
        def items = [:]
        for (Map o : objects) {
            if (items.get(o.get("taxon.id")) == null) {
                def taxon = [:]
                taxon.put("item_id", o.get("taxon.id"))
                taxon.put("item_name", o.get("taxon.scientificName"))
                taxon.put("url", "http://bie.ala.org.au/species/" + o.get("taxon.lsid"))
                taxon.put("rank", o.get("taxon.rank"))
                items.put(o.get("taxon.id"), taxon)
            }
        }
        map.put("taxonRemaining", items.values())

        //questions
        map.put("questions", orderQuestions(objects, currentQuery, currentState, selectedAttribute))

        map
    }

    def orderQuestions(values, currentQuery, currentState, selectedAttribute) {
        AtomicInteger ids = new AtomicInteger(1)

        def orderedQuestions = orderValues(values, ids.intValue(), ids, null, 1, currentQuery, currentState, selectedAttribute)

        orderedQuestions
    }

    def orderValues(objects, parentId, position, nextAttributeLabel, depth, currentQuery, currentState, selectedAttribute) {
        List<Map> output = new ArrayList<Map>()

        def otherQuestions = []

        //get the next best attribute by taxon coverage
        def attributes = [:]
        def attributesAnyId = [:]
        for (Map p : objects) {
            if (nextAttributeLabel == null || nextAttributeLabel.equals(p.get("attribute.label"))) {
                def key = p.get("attribute.id")
                attributesAnyId.put(p.get("attribute.label"), key)
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
                v.eachWithIndex { v1, idx ->
                    def next = [:]
                    next.lead_text = v1.get("taxon.scientificName")
                    next.item = "Taxon: " + v1.get("taxon.id")

                    //next.url = getURL(v1, idx, currentState, currentQuery, null)

                    //output.add(next)
                }
            }
        } else if (values.size() > 3 && attributesAnyId.size() > 1 && depth < MAX_DEPTH) {
            //convert to list of q's for next attribute
            attributesAnyId.each { v, vv ->

//                if (selectedAttribute == null) {
//                    def next = [:]
//                    next.lead_text = "Question: " + v
//                    next.item = null
//
//                    next.url = getURL(null, 1, currentState, currentQuery, vv)
//
//                    output.add(next)
//
//                } else if (vv == Long.parseLong(selectedAttribute)){
//                    //repeat using subset of objects that have the same unique attribute name
//                    def list = new ArrayList<Map>()
//                    objects.each { obj ->
//                        if (nextAttributeLabel == null || !nextAttributeLabel.equals(obj.get("attribute.label"))) {
//                            list.add(obj)
//                        }
//                    }
//                    output.addAll(orderValues(list, null, position, v, depth + 1, currentQuery, currentState, selectedAttribute))
//                    
//                }
                if (selectedAttribute == null || vv != Long.parseLong(selectedAttribute)) {
                    def next = [:]
                    next.lead_text = "Question: " + v
                    next.item = null

                    next.url = getURL(null, 1, currentState, currentQuery, vv)

                    otherQuestions.add(next)

                } else if (vv == Long.parseLong(selectedAttribute)) {
                    //repeat using subset of objects that have the same unique attribute name
                    def list = new ArrayList<Map>()
                    objects.each { obj ->
                        if (nextAttributeLabel == null || !nextAttributeLabel.equals(obj.get("attribute.label"))) {
                            list.add(obj)
                        }
                    }
                    output.addAll(orderValues(list, null, position, v, depth + 1, currentQuery, currentState, selectedAttribute))

                }
            }
        } else {
            //drill into each value
            values.eachWithIndex { k, v, idx ->
                //write this one
                def next = [:]
                def o = v.get(0)
                next.lead_text = k

                next.url = getURL(o, idx, currentState, currentQuery, null)

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
                    //output.addAll(orderValues(list, next.lead_id, position, null, depth + 1))
                }
            }
        }

        output.add(otherQuestions)

        output
    }

    def getURL(obj, idx, currentState, currentQuery, attributeLabel) {

        //value is the numeric range OR a value.id with the text
        def value = obj != null ? obj.get("text") : null
        def attributeId = attributeLabel
        if (value == null && obj != null) {
            if (obj.get("max") != obj.get("min")) {
                value = "r(" + obj.get("min") + " - " + obj.get("max") + ")"
            } else {
                value = "r(" + obj.get("min") + ")"
            }
        } else if (obj != null) {
            value = obj.get("id")
        } else {
            value = ""
        }
        if (obj != null) {
            attributeId = obj.get("attribute.id")
        }

        String cs = (currentState == null ? "" : currentState + "|")
        String url = "http://localhost:8080/ala-keys/player2/?" + currentQuery + '&type=json&state=' +
                cs +
                attributeId + ":" + value

        url
    }
}