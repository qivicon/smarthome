package org.eclipse.smarthome.io.sse.listeners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.inbox.Inbox;
import org.eclipse.smarthome.config.discovery.inbox.InboxListener;
import org.eclipse.smarthome.io.rest.core.discovery.beans.DiscoveryResultBean;
import org.eclipse.smarthome.io.rest.core.util.BeanMapper;
import org.eclipse.smarthome.io.sse.EventType;
import org.eclipse.smarthome.io.sse.SseResource;

/**
 * Listener responsible for broadcasting inbox events to all clients subscribed
 * to them.
 * 
 * @author ivan.iliev
 * 
 */
public class InboxEventListener implements InboxListener {

    private Inbox inbox;

    private SseResource sseResource;

    protected void setInbox(Inbox inbox) {
        this.inbox = inbox;
        this.inbox.addInboxListener(this);
    }

    protected void unsetInbox(Inbox inbox) {
        this.inbox.removeInboxListener(this);
        this.inbox = null;
    }

    protected void setSseResource(SseResource sseResource) {
        this.sseResource = sseResource;
    }

    protected void unsetSseResource(SseResource sseResource) {
        this.sseResource = null;
    }

    @Override
    public void thingAdded(Inbox source, DiscoveryResult result) {
        broadcastInboxEvent(result.getThingUID().getId(), EventType.INBOX_THING_ADDED, result);

    }

    @Override
    public void thingUpdated(Inbox source, DiscoveryResult result) {
        broadcastInboxEvent(result.getThingUID().getId(), EventType.INBOX_THING_UPDATED, result);
    }

    @Override
    public void thingRemoved(Inbox source, DiscoveryResult result) {
        broadcastInboxEvent(result.getThingUID().getId(), EventType.INBOX_THING_REMOVED, result);
    }

    private void broadcastInboxEvent(String resultIdentifier, EventType eventType, DiscoveryResult... elements) {
        Object eventObject = null;
        if (elements != null && elements.length > 0) {
            List<DiscoveryResultBean> discoveryBeans = new ArrayList<DiscoveryResultBean>();

            for (DiscoveryResult discoveryResult : elements) {
                discoveryBeans.add(BeanMapper.mapDiscoveryResultToBean(discoveryResult));
            }

            eventObject = discoveryBeans;
        }

        sseResource.broadcastEvent(resultIdentifier, eventType, eventObject);
    }

}
