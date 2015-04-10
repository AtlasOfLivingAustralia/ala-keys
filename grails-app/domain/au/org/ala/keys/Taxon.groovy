package au.org.ala.keys

class Taxon {

    String scientificName
    String rank
    String lsid
    Date created = new Date()

    String kid, pid, cid, oid, fid, gid, sid
    String kidName, pidName, cidName, oidName, fidName, gidName, sidName

    static constraints = {
    }

    static hasMany = [values: Value]

    static mapping = {
        values cascade: 'all-delete-orphan'
    }
}
