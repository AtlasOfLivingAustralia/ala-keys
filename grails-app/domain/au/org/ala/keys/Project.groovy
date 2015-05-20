package au.org.ala.keys

class Project {

    String description
    String name
    Date created = new Date()
    String imageUrl
    String geographicScope

    static constraints = {
        name nullable: false
        imageUrl nullable: true
        description nullable: true
    }

    static hasMany = [
            keys       : Key,
            scopeTaxons: Taxon
    ]

    static mapping = {

        joinTable:
        [name: 'users', key: 'project_id', column: 'userId', type: "String"]
        joinTable:
        [name: 'adminUsers', key: 'project_id', column: 'userId', type: "String"]

        scopeTaxons joinTable: [name: 'project_scopetaxons', key: 'project_id', column: 'taxon_id']

        keys cascade: 'all-delete-orphan'

        description type: 'text'
    }
}
