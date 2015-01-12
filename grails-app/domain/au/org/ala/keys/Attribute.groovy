package au.org.ala.keys

import javax.persistence.CascadeType
import javax.persistence.ManyToOne

class Attribute {

    String label
    String notes
    String units
    boolean isNumeric
    boolean isText
    List textValues
    Date created = new Date()

    boolean primaryy = false

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

    static hasOne = [parent: Attribute]

    public static def createNewOrChild(label) {
        def attributeList = Attribute.findAllByLabel(label)
        if (attributeList.size() == 0) {
            //Attribute.withTransaction {
                Attribute attribute = new Attribute(label: label, primaryy: true)
                //attribute.save()
                return attribute
           // }
        } else {
            def parent = null
            attributeList.each() { a ->
                if (a.parent) {
                    parent = a.parent;
                }
            }

           // Attribute.withTransaction {
                Attribute attribute = new Attribute(label: label, primaryy: parent == null, parent: parent)
            //    attribute.save()
                return attribute
            //}
        }
    }
}
