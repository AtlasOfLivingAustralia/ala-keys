package au.org.ala.keys
/**
 * Created by a on 7/11/14.
 */
class Key {

    String name
    String description
    Date created = new Date()
    String status
    String filename
    String geographicScope
    Map metadata

    static constraints = {
        filename nullable: true
    }

    static belongsTo = [project: Project]

    static hasMany = [attributes: Attribute, values: Value, scopeTaxons: Taxon]

    static mapping = {
        attributes cascade: 'all-delete-orphan'
        values cascade: 'all-delete-orphan'

        scopeTaxons joinTable: [name: 'key_scopetaxons', key: 'key_id', column: 'taxon_id']

        description type: 'text'
    }
}
