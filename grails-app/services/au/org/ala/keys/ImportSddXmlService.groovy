package au.org.ala.keys

import org.tdwg.rs.ubif._2006.Dataset
import org.tdwg.rs.ubif._2006.Datasets
import org.tdwg.rs.ubif._2006.LongStringL

import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException

class ImportSddXmlService {
    //static transactional = false

    def importStatusService

    def importXml(DataSource dataSource) {

        try {
            def file = new File(dataSource.getFilePath())
            Datasets datasets = JAXBContext.newInstance(Datasets.class).createUnmarshaller().unmarshal(file)

            if (datasets == null) {
                return false;
            }

            def taxonNames = [:]
            def identificationKeys = [:]

            //create taxon
            datasets.dataset.each() { dataset ->
                ((Dataset) dataset).taxonNames.taxonName.each() { taxon ->
                    taxon.representation.representationGroup.each() { taxonItem ->
                        def taxonList = taxonNames.get(taxon.id)
                        if (taxonList == null) {
                            taxonList = []
                        }
                        taxonList.addAll(Taxon.findOrCreate(taxonItem.value))
                        taxonNames.put(taxon.id, taxonList)
                    }
                }
                ((Dataset) dataset).identificationKeys.identificationKey.each() { idkeys ->
                    idkeys.leads.lead.each() { lead ->
                        def statement = null
                        lead.statementOrMediaObject.each() { obj ->
                            if (obj instanceof LongStringL) {
                                statement = obj
                            } else {
                                println obj
                            }
                        }

                        def m = [:]
                        if (lead.parent != null) {
                            m.put("parent", lead.parent.ref)
                        }
                        m.put("statement", statement.value)
                        if (lead.taxonName != null) {
                            m.put("taxon", lead.taxonName.ref)
                        }

                        identificationKeys.put(lead.id, m)
                    }
                }
            }

            //Construct one Attribute with multiple Values
            /*Attribute attribute = new Attribute(createdBy: dataSource, label: 'description')
            def i = 0.0
            identificationKeys.each() { key, identificationKey ->
                if (identificationKey.containsKey("taxon")) {
                    i ++
                    importStatusService.put(dataSource.id,
                            [("loading: for taxon " + i + " of " + taxonNames.size()), (i / taxonNames.size() * 75) ])

                    taxonNames.get(identificationKey.get("taxon")).each() { taxon ->
                        makeValue(dataSource, identificationKeys, taxon, attribute, identificationKey)
                    }
                }
            }
            if (!attribute.save()) {
                attribute.errors.each {
                    log.error("error saving attribute: " + it)
                }
            }*/

            //Construct one Attribute for each shared parent
            def attributeParents = [:]
            Attribute attribute = Attribute.createNewOrChild('description')
            Attribute firstAttribute = attribute
            if (!attribute.save()) {
                attribute.errors.each {
                    log.error("error saving attribute: " + it)
                }
            }
            attributeParents.put("", attribute)
            identificationKeys.each() { key, identificationKey ->
                if (identificationKey.containsKey("parent")) {
                    def parent = identificationKey.get("parent")
                    if (!attributeParents.containsKey(parent)) {
                        attribute = Attribute.createNewOrChild('description')
                        if (attribute.parent == null) {
                            attribute.parent = firstAttribute
                            attribute.primaryy = false
                        }
                        if (!attribute.save()) {
                            attribute.errors.each {
                                log.error("error saving attribute: " + it)
                            }
                        }

                        attributeParents.put(parent, attribute)
                    }
                }
            }
            def i = 0.0
            identificationKeys.each() { key, identificationKey ->
                i = i + 1
                if (identificationKey.containsKey("taxon")) {
                    importStatusService.put(dataSource.id,
                            [("loading: for taxon " + i + " of " + identificationKeys.size()), (i / identificationKeys.size() * 75)])

                    taxonNames.get(identificationKey.get("taxon")).each() { taxon ->
                        makeValueManyAttributes(dataSource, identificationKeys, taxon, attributeParents, identificationKey)
                    }
                }
            }

            return true
        } catch (JAXBException e) {
            //e.printStackTrace()
            //log.error("failed to import Lucid Dataset XML from: " + dataSource.getFilePath())
        }

        return false
    }

    def makeValue(dataSource, identificationKeys, taxon, attribute, identificationKey) {
        Value value = new Value(createdBy: dataSource, taxon: taxon, attribute: attribute, text: identificationKey.get("statement"))
        if (!value.save()) {
            value.errors.each {
                log.error("error saving value: " + it)
            }
        }

        if (identificationKey.containsKey("parent")) {
            makeValue(dataSource, identificationKeys, taxon, attribute, identificationKeys.get(identificationKey.get("parent")))
        }
    }

    def makeValueManyAttributes(dataSource, identificationKeys, taxon, attributeParents, identificationKey) {
        def parent = identificationKey.get("parent")
        if (parent == null) {
            parent = ""
        }
        def attribute = attributeParents.get(parent)
        Value value = new Value(createdBy: dataSource, taxon: taxon, attribute: attribute, text: identificationKey.get("statement"))
        if (!value.save()) {
            value.errors.each {
                log.error("error saving value: " + it)
            }
        }


        if (identificationKey.containsKey("parent")) {
            makeValueManyAttributes(dataSource, identificationKeys, taxon, attributeParents, identificationKeys.get(identificationKey.get("parent")))
        }
    }
}
