package au.org.ala.keys.listener

import au.org.ala.keys.AuditService
import org.grails.datastore.mapping.core.Datastore
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEventListener
import org.grails.datastore.mapping.engine.event.EventType
import org.springframework.context.ApplicationEvent

/**
 * GORM event listener to trigger ElasticSearch updates when domain classes change
 *
 * see http://grails.org/doc/latest/guide/single.html#eventsAutoTimestamping
 *
 * @author "Nick dos Remedios <Nick.dosRemedios@csiro.au>"
 */
class AuditListener extends AbstractPersistenceEventListener {

    def auditService

    public AuditListener(final Datastore datastore, AuditService auditService) {
        super(datastore)
        this.auditService = auditService
    }

    @Override
    protected void onPersistenceEvent(final AbstractPersistenceEvent event) {

        switch (event.eventType) {
            case EventType.PostInsert:
            case EventType.PostUpdate:
            case EventType.PostDelete:
                auditService.logGormEvent(event)
                break
            case EventType.PreUpdate:
                auditService.logGormEvent(event)
                break
        }
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return true
    }
}