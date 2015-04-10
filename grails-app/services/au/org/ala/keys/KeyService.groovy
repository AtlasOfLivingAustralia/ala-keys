package au.org.ala.keys

import org.apache.commons.io.FileUtils

/**
 * Created by a on 30/03/15.
 */
class KeyService {
    def getFilePath(Key key) {
        if (key.id == null) {
            key.save()
        }

        def dir = new Key().domainClass.grailsApplication.config.uploadFolder + "/" + key.id + "/"
        def file = new File(dir)
        if (!file.exists()) {
            FileUtils.forceMkdir(file)
        }

        return dir + key.filename
    }
}
