package au.org.ala.keys

import org.apache.commons.io.FileUtils

/**
 * Created by a on 7/11/14.
 */
class DataSource {

    String description
    String filename
    String alaUserId
    Date created = new Date()
    String status

    static constraints = {
    }

    static hasMany = [attributes: Attribute, values: Value]

    static mapping = {
        attributes cascade: 'all-delete-orphan'
        values cascade: 'all-delete-orphan'
    }

    def getFilePath() {
        if (id == null) {
            save()
        }

        def dir = new DataSource().domainClass.grailsApplication.config.uploadFolder + "/" + id + "/"
        def file = new File(dir)
        if (!file.exists()) {
            FileUtils.forceMkdir(file)
        }

        return dir + filename
    }
}
