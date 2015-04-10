package au.org.ala.keys

import grails.transaction.Transactional
import org.apache.commons.io.IOUtils

import java.util.zip.ZipEntry
import java.util.zip.ZipFile

@Transactional
class ZipService {
    def importStatusService
    def keyService

    def getKeys(Key key) {
        def sources = []

        ZipFile zf = new ZipFile(keyService.getFilePath(key))

        def count = 0
        zf.entries().each() { ZipEntry ze ->
            count++

            //create new key
            def k = new Key(filename: ze.getName(), status: "loading", project: key.project)
            def path = keyService.getFilePath(k)

            importStatusService.put(key.id, ["extracting file " + count + " of " + zf.size() + " from zip", 0])

            byte[] bytes = IOUtils.toByteArray(zf.getInputStream(ze))
            new FileOutputStream(path).write(bytes)

            if (!k.save()) {
                k.errors.each() {
                    log.error("failed saving datasource: " + it)
                }
            }

            sources.add(k)
        }

        return sources
    }
}
