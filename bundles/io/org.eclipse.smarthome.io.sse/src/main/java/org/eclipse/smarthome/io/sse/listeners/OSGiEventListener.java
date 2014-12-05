package org.eclipse.smarthome.io.sse.listeners;

import org.eclipse.smarthome.core.events.AbstractEventSubscriber;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.io.sse.EventType;
import org.eclipse.smarthome.io.sse.SseResource;

/**
 * Listener responsible for broadcasting internal item update/command events to
 * all clients subscribed to them.
 * 
 * @author ivan.iliev
 * 
 */
public class OSGiEventListener extends AbstractEventSubscriber {

    private SseResource sseResource;

    protected void setSseResource(SseResource sseResource) {
        this.sseResource = sseResource;
    }

    protected void unsetSseResource(SseResource sseResource) {
        this.sseResource = null;
    }

    @Override
    public void receiveCommand(String itemName, Command command) {
        sseResource.broadcastEvent(itemName, EventType.COMMAND, command.format("%s"));
    }

    @Override
    public void receiveUpdate(String itemName, State newState) {
        sseResource.broadcastEvent(itemName, EventType.UPDATE, newState.format("%s"));
    }
}
