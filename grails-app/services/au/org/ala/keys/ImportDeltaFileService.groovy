package au.org.ala.keys

import au.org.ala.delta.editor.slotfile.model.SlotFileRepository
import au.org.ala.delta.model.*

class ImportDeltaFileService {
    //static transactional = false

    def importStatusService
    def keyService
    def attributeService
    def taxonService

    def importDlt(Key key) {
        DeltaDataSetRepository repository = new SlotFileRepository()

        MutableDeltaDataSet dataSet = repository.findByName(keyService.getFilePath(key), null)

        for (int j = 1; j <= dataSet.getNumberOfCharacters(); j++) {
            if (j % 1 == 0) {
                importStatusService.put(key.id, ["loading: character " + j + " of " + dataSet.getNumberOfCharacters(), (j / dataSet.getNumberOfCharacters()) * 75])
            }
            au.org.ala.delta.model.Character deltaCharacter = dataSet.getCharacter(j)

            Attribute attribute = attributeService.create(deltaCharacter.getDescription())
            attribute.numeric = deltaCharacter.getCharacterType().numeric
            attribute.text = deltaCharacter.getCharacterType().text
            attribute.label = deltaCharacter.getDescription()
            attribute.notes = deltaCharacter.getNotes()
            attribute.units = deltaCharacter.getImpl().getUnits()
            attribute.key = key

            if (!attribute.save()) {
                attribute.errors.each {
                    log.error("attribute save error: " + it)
                }
            }

            if (deltaCharacter instanceof MultiStateCharacter) {
                def ac = (MultiStateCharacter) deltaCharacter
                attribute.textValues = ac.getStates()
                attribute.text = true
                attribute.numeric = false
            } else {
                attribute.text = false
                attribute.numeric = true
            }

            for (int i = 0; i < dataSet.getAllAttributesForCharacter(deltaCharacter.getCharacterId()).size(); i++) {
                au.org.ala.delta.model.Attribute deltaAttribute = dataSet.getAllAttributesForCharacter(deltaCharacter.getCharacterId()).get(i)

                if (!deltaAttribute.isUnknown()) {
                    List values = []

                    if (deltaAttribute instanceof IntegerAttribute) {
                        def aa = (IntegerAttribute) deltaAttribute
                        def value = new Value(key: key, attribute: attribute)
                        try {
                            if (aa.getNumericValue() != null) {
                                for (def k = 0; k < aa.getNumericValue().size(); k++) {
                                    if (k == 0 || value.min > aa.getNumericValue().get(k).getFullRange().getMinimumNumber()) {
                                        value.min = aa.getNumericValue().get(k).getFullRange().getMinimumNumber().doubleValue()
                                    }
                                    if (k == 0 || value.max < aa.getNumericValue().get(k).getFullRange().getMaximumNumber()) {
                                        value.max = aa.getNumericValue().get(k).getFullRange().getMaximumNumber().doubleValue()
                                    }
                                }
                            }
                        } catch (e) {
                            log.error("error getting integer numeric range")
                        }
                        values << value
                    } else if (deltaAttribute instanceof RealAttribute) {
                        def aa = (RealAttribute) deltaAttribute
                        def value = new Value(key: key, attribute: attribute)
                        try {
                            if (aa.getNumericValue() != null) {
                                for (def k = 0; k < aa.getNumericValue().size(); k++) {
                                    if (k == 0 || value.min > aa.getNumericValue().get(k).getFullRange().getMinimumNumber()) {
                                        value.min = aa.getNumericValue().get(k).getFullRange().getMinimumNumber().doubleValue()
                                    }
                                    if (k == 0 || value.max < aa.getNumericValue().get(k).getFullRange().getMaximumNumber()) {
                                        value.max = aa.getNumericValue().get(k).getFullRange().getMaximumNumber().doubleValue()
                                    }
                                }
                            }
                        } catch (e) {
                            log.error("error getting real numeric range")
                        }
                        values << value
                    } else if (deltaAttribute instanceof MultiStateAttribute) {
                        def aa = (MultiStateAttribute) deltaAttribute
                        aa.getPresentStates().each() { idx ->
                            if (attribute.textValues[idx - 1] == 'Antarctic') {
                                println(attribute.id)
                                println(attribute.label)
                                println(attribute.notes)
                            }
                            values << new Value(key: key, attribute: attribute, text: attribute.textValues[idx - 1])
                        }
                    } else if (deltaAttribute instanceof TextAttribute) {
                        def aa = (TextAttribute) deltaAttribute
                        if (aa.getText() == 'Antarctic') {
                            println(aa.id)
                            println(aa.getCharacter().label)
                            println(aa.getCharacter().notes)
                        }
                        values << new Value(key: key, attribute: attribute, text: aa.getText())
                    }

                    //taxon
                    def item = deltaAttribute.getItem()
                    def taxons = taxonService.get(item.getItemData().getDescription())

                    values.each() { value ->
                        taxons.each() { taxon ->
                            value.taxon = taxon
                        }
                    }

                    values.each() { value ->
                        if (!value.save()) {
                            value.errors.each {
                                log.error("value save error: " + it)
                            }
                        }
                    }
                }

            }
        }

        dataSet.close()
    }
}
