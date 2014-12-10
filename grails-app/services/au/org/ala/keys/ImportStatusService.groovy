package au.org.ala.keys


class ImportStatusService {

    def status = [:]

    def synchronized get(key) {
        return status.get(key)
    }

    def synchronized put(key, value) {
        status.put(key, value)
    }

    def remove(key) {
        status.remove(key)
    }
}
