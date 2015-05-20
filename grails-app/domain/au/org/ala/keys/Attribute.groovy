package au.org.ala.keys

import javax.persistence.CascadeType
import javax.persistence.ManyToOne

class Attribute {

    String label
    String notes
    String units
    boolean numeric
    boolean text
    List textValues
    Date created = new Date()

    static constraints = {
    }

    static mapping = {
        notes type: 'text'
        values cascade: 'all-delete-orphan'

        deleted nullable: true
        deletedBy nullable: true

        hasMany joinTable: [name: 'textValues', key: 'attribute_id', column: 'textValue', type: "String"]

        textValues cascade: 'all-delete-orphan'
    }

    @ManyToOne(cascade = CascadeType.ALL)
    List values

    static belongsTo = [key: Key]

    static hasMany = [values: Value, children: Attribute]

    static hasOne = [parent: Attribute]

    static mappedBy = [parent: 'children', children: 'parent']
}
