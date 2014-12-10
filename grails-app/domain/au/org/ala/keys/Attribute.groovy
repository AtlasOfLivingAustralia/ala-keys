package au.org.ala.keys

import javax.persistence.CascadeType
import javax.persistence.ManyToOne

class Attribute {

    String label
    String notes
    String units
    boolean characterTypeNumeric
    boolean characterTypeText
    List textValues
    Date created = new Date()

    static constraints = {
    }

    static mapping = {
        notes type: 'text'

        hasMany joinTable: [name: 'textValues', key: 'attribute_id', column: 'textValue', type: "String"]

        values cascade: 'all-delete-orphan'
        textValues cascade: 'all-delete-orphan'
    }

    @ManyToOne(cascade = CascadeType.ALL)
    List values

    static hasMany = [values: Value, children: Attribute]

    static hasOne = [createdBy: DataSource, parent: Attribute]

    public def findOrCreate(label) {
        def attributeList = Attribute.findAllByLabel(label)
        if (attributeList.size() == 0) {
            Attribute attribute = new Attribute(label: label)
            attribute.save()
            return attribute
        } else {
            def attribute = attributeList.get(0)
            while (attribute.parent != null) {
                attribute = attribute.parent
            }
            return attribute
        }
    }
}
