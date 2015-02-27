package au.org.ala.keys

import grails.transaction.Transactional
import org.apache.commons.io.IOUtils

import java.util.zip.ZipEntry
import java.util.zip.ZipFile

@Transactional
class ZipService {
    def importStatusService

    def getDataSources(DataSource dataSource) {
        def sources = []

        ZipFile zf = new ZipFile(dataSource.getFilePath())

        def count = 0
        zf.entries().each() { ZipEntry ze ->
            count++

            //create new datasource
            def ds = new DataSource(filename: ze.getName(), status: "loading", project: dataSource.project)
            def path = ds.getFilePath()

            importStatusService.put(dataSource.id, ["extracting file " + count + " of " + zf.size() + " from zip", 0])

            byte[] bytes = IOUtils.toByteArray(zf.getInputStream(ze))
            new FileOutputStream(path).write(bytes)

            if (!ds.save()) {
                ds.errors.each() {
                    log.error("failed saving datasource: " + it)
                }
            }

            sources.add(ds)
        }

        return sources
    }
}
