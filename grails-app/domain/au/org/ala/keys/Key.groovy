package au.org.ala.keys

import org.apache.commons.io.FileUtils

/**
 * Created by a on 7/11/14.
 */
class Key {

    String description
    Date created = new Date()
    String createdBy
    Date deleted
    String deletedBy
    String status
    String filename

    static constraints = {
        createdBy nullable: true
        deleted nullable: true
        deletedBy nullable: true
        filename nullable: true
    }

    static belongsTo = [project: Project]

    static hasMany = [attributes: Attribute, values: Value]

    static mapping = {
        attributes cascade: 'all-delete-orphan'
        values cascade: 'all-delete-orphan'
    }
}
