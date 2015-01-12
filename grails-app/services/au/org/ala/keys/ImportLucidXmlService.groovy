package au.org.ala.keys

import org.lucidcentral._2007.ds.*

import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException

class ImportLucidXmlService {

    //static transactional = false

    def importStatusService

    def importXml(DataSource dataSource) {

        try {
            def file = new File(dataSource.getFilePath())
            Dataset2 dataset = JAXBContext.newInstance(Dataset2.class).createUnmarshaller().unmarshal(file)

            if (dataset == null) {
                return false
            }

            def entities = [:]
            def features = [:]
            def states = [:]
            def stateFeatures = [:]

            //create taxon
            dataset.getItems().entities.entity.eachWithIndex() { entity, i ->
                if (i % 10 == 0) {
                    importStatusService.put(dataSource.id,
                            [("loading: taxon " + i + " of " + dataset.getItems().entities.entity.size()), (i / dataset.getItems().entities.entity.size() * 25)])
                }
                def scientificName = entity.representation.label.get(0).value

                def taxons = Taxon.findOrCreate(scientificName)

                taxons.each() { taxon ->
                    entities.put(entity.id, taxon)
                }
            }

            //create attributes
            dataset.getItems().features.multistateFeatureOrNumericFeature.eachWithIndex() { feature, i ->
                if (i % 5 == 0) {
                    importStatusService.put(dataSource.id,
                            ["loading: feature " + i + " of " + dataset.getItems().features.multistateFeatureOrNumericFeature.size()
                             , 25 + i / dataset.getItems().features.multistateFeatureOrNumericFeature.size() * 25])
                }
                def label = feature.representation.label.get(0).value
                Attribute attribute = Attribute.createNewOrChild(label)

                if (feature instanceof MultistateFeature) {
                    attribute.isText = true
                    attribute.isNumeric = false
                    attribute.textValues = []
                    feature.states.state.each() { state ->
                        attribute.textValues.add(state.representation.label.get(0).value)
                        states.put(state.id, state.representation.label.get(0).value)
                        stateFeatures.put(state.id, feature.id)
                    }
                } else if (feature instanceof NumericFeature) {
                    attribute.isText = false
                    attribute.isNumeric = true
                    def unitPrefix = ""
                    if (feature.unitPrefix?.label?.size() > 0) {
                        feature.unitPrefix.label.get(0).value
                    }
                    def measurementUnit = ""
                    if (feature.measurementUnit?.label?.size() > 0) {
                        feature.measurementUnit.label.get(0).value
                    }

                    try {
                        attribute.units = (unitPrefix.toString() + " " + measurementUnit.toString()).trim()
                    } catch (Exception e) {
                        e.printStackTrace()
                    }
                }

                attribute.save()

                features.put(feature.id, attribute)
            }

            //create joins
            dataset.descriptions.description.eachWithIndex() { description, i ->
                if (i % 5 == 0) {
                    importStatusService.put(dataSource.id,
                            ["loading: description " + i + " of " + dataset.descriptions.description.size(),
                             i / dataset.descriptions.description.size() * 25 + 50])
                }

                Taxon taxon = null
                //deal with xml variations
                if (description.ref == null || entities.get(description.ref) == null) {
                    taxon = entities.get(description.entity.ref)
                } else {
                    taxon = entities.get(description.ref)
                }

                description.data.multistateDataOrNumericData.each() { data ->
                    Attribute attribute = null
                    //deal with xml variations
                    if (data.ref == null || features.get(data.ref) == null) {
                        attribute = features.get(data.feature.ref)
                    } else {
                        println data.ref
                        attribute = features.get(data.ref)
                    }

                    if (attribute == null) {
                        println features
                    }

                    if (data instanceof MultistateData) {
                        data.stateData.each() { stateData ->
                            stateData.state.each() { state ->
                                Value value = new Value(createdBy: dataSource, taxon: taxon, attribute: attribute, text: states.get(state.ref))

                                if (!value.save()) {
                                    value.errors.each {
                                        log.error("error saving value: " + it)
                                    }
                                }
                            }
                        }
                    } else if (data instanceof NumericData) {
                        double min = null
                        double max = null
                        data.value.each() { value ->
                            if (value instanceof NumericDataValue.Measure) {
                                if ((value.type == NumericMeasureTypeEnum.NMIN || value.type == NumericMeasureTypeEnum.OMIN)
                                        && (min == null || min > value.measure.value)) {
                                    min = value.measure.value
                                }
                                if ((value.type == NumericMeasureTypeEnum.NMAX || value.type == NumericMeasureTypeEnum.OMAX)
                                        && (max == null || max < value.measure.value)) {
                                    max = value.measure.value
                                }
                            }
                        }
                        Value value = new Value(createdBy: dataSource, taxon: taxon, attribute: attribute, min: min, max: max)

                        if (!value.save()) {
                            value.errors.each {
                                log.error("error saving value: " + it)
                            }
                        }

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
}
