package au.org.ala.keys

class Value {

    String text
    Double min
    Double max
    Date created = new Date()

    static constraints = {
        min nullable: true
        max nullable: true
        text nullable: true
        attribute nullable: false
        taxon nullable: false
    }

    static mapping = {
        text type: 'text'
        attribute cascade: 'save-update'
        taxon cascade: 'save-update'
    }

    static belongsTo = [attribute: Attribute]

    static hasOne = [createdBy: DataSource, taxon: Taxon]

    static def getForLsid(lsid) {
        def values = []

        if (lsid != null) {
            def v = Taxon.findByLsid(lsid)
            if (v != null && v.values != null) {
                values.addAll(v.values)
            }
        }

        return values
    }

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
