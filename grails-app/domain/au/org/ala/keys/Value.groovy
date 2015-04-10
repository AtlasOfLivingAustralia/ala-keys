package au.org.ala.keys

class Value {

    String text
    Double min
    Double max
    Date created = new Date()
    String createdBy
    Date deleted
    String deletedBy

    static constraints = {
        min nullable: true
        max nullable: true
        text nullable: true
        attribute nullable: false
        taxon nullable: false
        deleted nullable: true
        deletedBy nullable: true
    }

    static mapping = {
        text type: 'text'
        attribute cascade: 'save-update'
        taxon cascade: 'save-update'
    }

    static belongsTo = [attribute: Attribute]

    static hasOne = [key: Key, taxon: Taxon]

    public def asText(params) {
        def noLabel = params?.noLabel ? params.noLabel : false
        def noUnits = params?.noUnits ? params.noLabel : false
        def labelWrapper = noLabel ? ["", ""] : (params?.labelWrapper ? params.labelWrapper : ["", ""])
        def unitWrapper = params?.unitWrapper ? params.unitWrapper : [" (", ")"]
        def valueWrapper = params?.valueWrapper ? params.valueWrapper : [(noLabel ? "" : ": "), ""]
        def unitsLast = params?.unitsLast ? params.unitsLast : false

        def labelFormatted = noLabel ? "" : (labelWrapper[0] + attribute.label + labelWrapper[1])
        def unitFormatted = noUnits ? "" : (attribute.units?.length() > 0 ? unitWrapper[0] + attribute.units + unitWrapper[1] : "")
        def valueFormatted = valueWrapper[0] + (min == null ? text : (min == max ? min : min + " - " + max)) + valueWrapper[1]

        if (unitsLast) {
            return labelFormatted + valueFormatted + unitFormatted
        } else {
            return labelFormatted + unitFormatted + valueFormatted
        }
    }

    public def asTextTaxon(params) {
        def taxonWrapper = params?.taxonWrapper ? params.taxonWrapper : ["[", "] "]
        return taxonWrapper[0] + taxon.scientificName + taxonWrapper[1] + asText(params)
    }
}
