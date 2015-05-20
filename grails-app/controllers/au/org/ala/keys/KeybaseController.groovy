package au.org.ala.keys

import au.com.bytecode.opencsv.CSVWriter
import grails.converters.JSON
import groovy.sql.Sql
import org.hibernate.criterion.CriteriaSpecification

import java.text.SimpleDateFormat

class KeybaseController {
    def dataSource

    def index() {}

    def projects() {
        def projectId = params?.project as Long
        def includeItems = "true".equalsIgnoreCase(params?.items) as Boolean
        if (includeItems == null) includeItems = true
        def includeKeys = (params.containsKey('keys') ? "true".equalsIgnoreCase(params?.keys) : true) as Boolean
        if (includeKeys == null) includeKeys = true

        def projects = Project.createCriteria().list {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)

            projections {
                property 'id', 'project_id'
                property 'name', 'project_name'
                property 'geographicScope', 'geographic_scope'
                property 'imageUrl', 'imageUrl'
                property 'scopeTaxons.id', 'taxonId'
                property 'scopeTaxons.scientificName', 'taxonName'
                property 'scopeTaxons.lsid', 'lsid'
                property 'k.id', 'first_key_id'
                property 'k.name', 'first_key_name'
                property 'keyTaxons.id', 'first_key_scopeTaxons'
            }

            createAlias('scopeTaxons', 'scopeTaxons', CriteriaSpecification.LEFT_JOIN)

            createAlias('keys', 'k', CriteriaSpecification.LEFT_JOIN)
            createAlias('k.scopeTaxons', 'keyTaxons', CriteriaSpecification.LEFT_JOIN)

            //filter to get the first key
            or {
                isNull('scopeTaxons.id')
                isNull('keyTaxons.id')
                eqProperty('keyTaxons.id', 'scopeTaxons.id')
            }

            if (projectId != null) {
                eq('id', projectId)
            }
        }

        //counts
        def countTaxonMap = [:]
        Value.createCriteria().list {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)

            projections {
                countDistinct('taxon.id', 'count')
                groupProperty 'key.project.id', 'id'
            }

            createAlias('key', 'key')
        }.each {
            countTaxonMap.put(it.id, it.count)
        }

        def countKeyMap = [:]
        Value.createCriteria().list {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)

            projections {
                countDistinct('key.id', 'count')
                groupProperty 'key.project.id', 'id'
            }

            createAlias('key', 'key')
        }.each {
            countKeyMap.put(it.id, it.count)
        }

        //project grouping
        def groupedProjects = [:]
        projects.each { k ->
            def p
            if (!groupedProjects.containsKey(k.project_id)) {
                p = [project_id      : k.project_id, project_name: k.project_name,
                     taxonomic_scope : [],
                     geographic_scope: k.geographic_scope,
                     project_icon    : k.imageUrl,
                     first_key       : [id: null, name: null],
                     number_of_items : (countTaxonMap.containsKey(k.project_id) ? countTaxonMap.get(k.project_id) : 0),
                     number_of_keys  : (countKeyMap.containsKey(k.project_id) ? countKeyMap.get(k.project_id) : 0)]
                groupedProjects.put(k.project_id, p)
            } else {
                p = groupedProjects.get(k.project_id)
            }
            if (k.first_key_scopeTaxons == k.taxonId || k.taxonId == null) {
                //want the min(key.id) as the first key
                if (p.first_key.id == null || p.first_key.id < k.first_key_id) {
                    p.first_key = [id: k.first_key_id, name: k.first_key_name]
                }
            }
            if (k.taxonId != null) {
                //only want 1 match
                groupedProjects.get(k.project_id).taxonomic_scope = [id: k.taxonId, name: k.taxonName, lsid: k.lsid]
            }
        }

        if (projectId != null && includeItems) {
            groupedProjects.each { i, p ->
                def taxons = Key.createCriteria().list {
                    resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)

                    projections {
                        groupProperty 'taxon.id', 'id'
                        groupProperty 'taxon.scientificName', 'name'
                        groupProperty 'taxon.lsid', 'lsid'
                    }

                    createAlias('values', 'values', CriteriaSpecification.INNER_JOIN)
                    createAlias('values.taxon', 'taxon', CriteriaSpecification.INNER_JOIN)

                    eq('project.id', projectId)
                }
                p.putAt('items', taxons)
            }
        }

        if (projectId != null && includeKeys) {
            groupedProjects.each { i, p ->
                def keys = Key.createCriteria().list {
                    resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)

                    projections {
                        property 'id', 'id'
                        property 'id', 'parent_id'
                        property 'name', 'name'
                        property 'scopeTaxons.id', 'taxonId'
                        property 'scopeTaxons.scientificName', 'taxonName'
                        property 'scopeTaxons.lsid', 'lsid'
                    }

                    createAlias('scopeTaxons', 'scopeTaxons', CriteriaSpecification.LEFT_JOIN)

                    eq('project.id', projectId)
                }

                //get key parents
                final Sql sql = new Sql(dataSource)
                def query = "select distinct key_scope.key_id as key_id, parent_key_taxon.key_id as parent_key_id from" +
                        " (" +
                        " select key_id as key_id, scientific_name from key_scopetaxons inner join taxon on taxon_id = taxon.id " +
                        " inner join key on key_scopetaxons.key_id = key.id where project_id = :project" +
                        " union" +
                        " select value.key_id as id, " +
                        " (case when count(distinct(pid)) > 1 then max(kid_name)" +
                        " when count(distinct(cid)) > 1 then max(pid_name)" +
                        " when count(distinct(oid)) > 1 then max(cid_name)" +
                        " when count(distinct(fid)) > 1 then max(oid_name)" +
                        " when count(distinct(gid)) > 1 then max(fid_name)" +
                        " when count(distinct(sid)) > 1 then max(gid_name)" +
                        " else max(sid_name) end) as name" +
                        " from value inner join taxon on value.taxon_id = taxon.id" +
                        " inner join key on key.id = value.key_id" +
                        " left join key_scopetaxons on value.key_id = key_scopetaxons.key_id" +
                        " where key_scopetaxons.key_id is null" +
                        " and project_id = :project" +
                        " group by value.key_id" +
                        " ) key_scope" +
                        " inner join" +
                        " (" +
                        " select key_id, scientific_name" +
                        " from value inner join taxon on value.taxon_id = taxon.id" +
                        " inner join key on key.id = value.key_id" +
                        " where project_id = :project" +
                        " ) parent_key_taxon" +
                        " on key_scope.scientific_name = parent_key_taxon.scientific_name"

                def parentsMap = [:]
                sql.rows(query, project: projectId).each { parentsMap.put(it.key_id, it.parent_key_id) }

                //keys grouping
                def groupedKeys = [:]
                keys.each { k ->
                    if (!groupedKeys.containsKey(k.id)) {
                        groupedKeys.put(k.id, [id: k.id, parent_id: parentsMap?.get(k.id), name: k.name, taxonomic_scope: []])
                    }
                    if (k.taxonId != null) {
                        groupedKeys.get(k.id).taxonomic_scope.add([id: k.taxonId, name: k.taxonName, lsid: k.lsid])
                    }
                }

                p.putAt('keys', groupedKeys.values())
            }
        }

        render groupedProjects.values().sort { it.number_of_keys * -1 } as JSON
    }

    def items() {
        def pageSize = params?.pageSize as Long
        if (pageSize == null) pageSize = 100
        def page = params?.page as Long
        if (page == null) page = 0

        def limits = [max: pageSize, offset: page * pageSize]

        def keyId = params?.key as Long
        def key
        if (keyId != null) {
            key = Key.get(keyId)
        }

        def projectId = params?.project as Long
        def project
        if (projectId != null) {
            project = Project.get(projectId)
        } else if (key != null) {
            project = Project.get(key.project.id)
        }

        def taxons = Value.createCriteria().list(limits) {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)

            projections {
                groupProperty 'taxon.id', 'ItemsID'
                groupProperty 'taxon.scientificName', 'ItemName'
                groupProperty 'taxon.lsid', 'ItemLSID'
                groupProperty 'k.id', 'KeysID'
                groupProperty 'k.description', 'KeyName'
                groupProperty 'scopeTaxons.scientificName', 'TaxonomicScope'
                groupProperty 'project.id', "ProjectsID"
                groupProperty 'project.name', "ProjectName"
            }

            createAlias('taxon', 'taxon', CriteriaSpecification.LEFT_JOIN)
            createAlias('key', 'k', CriteriaSpecification.LEFT_JOIN)
            createAlias('k.scopeTaxons', 'scopeTaxons', CriteriaSpecification.LEFT_JOIN)
            createAlias('k.project', 'project', CriteriaSpecification.LEFT_JOIN)

            if (keyId != null) {
                eq('k.id', keyId)
            } else if (projectId != null) {
                eq('project.id', projectId)
            }
        }

        def map = [ProjectsId         : project.id, ProjectName: project.name,
                   TimestampDownloaded: new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),
                   numberOfItems      : taxons.size(),
                   Items              : taxons]

        if (params.containsKey("format") && "csv".equalsIgnoreCase(params.format)) {
            StringWriter sw = new StringWriter()
            CSVWriter csv = new CSVWriter(sw)

            String[] header = ["ItemsID", "ItemName", "ItemLSID", "KeysID", "KeyName", "TaxonomicScope", "ProjectsID", "ProjectName"]
            csv.writeNext(header)

            String[] line = new String[header.length]

            taxons.each { t ->
                for (int i = 0; i < header.length; i++) {
                    line[i] = t.getAt(header[i])
                }
                csv.writeNext(line)
            }
            csv.flush()

            response.setHeader "Content-disposition", "attachment; filename=test.csv"
            response.contentType = 'text/csv'
            response.outputStream << sw.getBuffer().toString()
            response.outputStream.flush()
        } else {
            render map as JSON
        }
    }

    def keys() {
        def pageSize = params?.pageSize as Long
        if (pageSize == null) pageSize = 100
        def page = params?.page as Long
        if (page == null) page = 0

        def limits = [max: pageSize, offset: page * pageSize]


        def tscope = params?.tscope as String
        def key
        if (tscope != null) {
            def keylist = Key.createCriteria().list {
                createAlias('scopeTaxons', 'scopeTaxons', CriteriaSpecification.LEFT_JOIN)

                eq('scopeTaxons.scientificName', tscope)
            }
            if (keylist.size() > 0) {
                key = keylist.get(0)
            }
        }

        def projectId = params?.project as Long
        def project
        if (projectId != null) {
            project = Project.get(projectId)
        } else if (key != null) {
            project = Project.get(key.project.id)
        }

        def keys = Key.createCriteria().list(limits) {
            resultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)

            projections {
                groupProperty 'id', 'KeysID'
                groupProperty 'description', 'KeyName'
                groupProperty 'scopeTaxons.scientificName', 'TaxonomicScope'
                groupProperty 'project.id', "ProjectsID"
                groupProperty 'project.name', "ProjectName"
            }

            createAlias('scopeTaxons', 'scopeTaxons', CriteriaSpecification.LEFT_JOIN)
            createAlias('project', 'project', CriteriaSpecification.LEFT_JOIN)

            if (key != null) {
                eq('id', key.id)
            } else if (projectId != null) {
                eq('project.id', projectId)
            }
        }

        def map = [ProjectsId         : project.id, ProjectName: project.name,
                   TimestampDownloaded: new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()),
                   numberOfKeys       : keys.totalCount,
                   Items              : keys]

        if (key != null && key.scopeTaxons != null && key.scopeTaxons.size() > 0) {
            map.putAll([ItemsId : key.scopeTaxons[0].id, ItemName: key.scopeTaxons[0].scientificName,
                        ItemLSID: key.scopeTaxons[0].lsid])
        }

        if (params.containsKey("format") && "csv".equalsIgnoreCase(params.format)) {
            StringWriter sw = new StringWriter()
            CSVWriter csv = new CSVWriter(sw)

            String[] header = ["ProjectsID", "ProjectName", "KeysID", "KeyName", "TaxonomicScope"]
            csv.writeNext(header)

            String[] line = new String[header.length]

            keys.each { t ->
                for (int i = 0; i < header.length; i++) {
                    line[i] = t.getAt(header[i])
                }
                csv.writeNext(line)
            }
            csv.flush()

            response.setHeader "Content-disposition", "attachment; filename=test.csv"
            response.contentType = 'text/csv'
            response.outputStream << sw.getBuffer().toString()
            response.outputStream.flush()
        } else {
            render map as JSON
        }
    }
}
