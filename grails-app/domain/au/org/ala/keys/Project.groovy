package au.org.ala.keys

class Project {

    String description
    String name
    Date created = new Date()
    List users

    static constraints = {
    }

    static hasMany = [
            dataSources: DataSource
    ]

    static mapping = {
        joinTable:
        [name: 'users', key: 'project_id', column: 'userId', type: "String"]
        dataSources cascade: 'all-delete-orphan'
    }
}
