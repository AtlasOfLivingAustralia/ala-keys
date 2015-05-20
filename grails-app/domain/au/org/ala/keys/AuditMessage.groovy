package au.org.ala.keys

import au.org.ala.keys.listener.AuditEventType

class AuditMessage {

    Long id
    Date date
    String userId
    AuditEventType eventType
    String entityType
    String entityId
    Map entity

    static constraints = {
        entityId nullable: true
    }

    static mapping = {
        version false
    }
}