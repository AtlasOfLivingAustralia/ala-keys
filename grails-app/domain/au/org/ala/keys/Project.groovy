package au.org.ala.keys

class Project {

    String description
    String name
    Date created = new Date()
    String createdBy
    List users
    List adminUsers

    static constraints = {
    }

    static hasMany = [
            keys: Key
    ]

    static mapping = {
        joinTable:
        [name: 'users', key: 'project_id', column: 'userId', type: "String"]
        [name: 'adminUsers', key: 'project_id', column: 'userId', type: "String"]
        keys cascade: 'all-delete-orphan'
    }
}
