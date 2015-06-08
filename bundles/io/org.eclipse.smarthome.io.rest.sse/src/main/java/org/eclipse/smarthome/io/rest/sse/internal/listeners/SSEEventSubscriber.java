/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.sse.internal.listeners;

import java.util.Set;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFilter;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.eclipse.smarthome.core.items.events.ItemAddedEvent;
import org.eclipse.smarthome.core.items.events.ItemRemovedEvent;
import org.eclipse.smarthome.core.items.events.ItemUpdatedEvent;
import org.eclipse.smarthome.io.rest.sse.EventType;
import org.eclipse.smarthome.io.rest.sse.SseResource;

import com.google.common.collect.ImmutableSet;

/**
 * The {@link SSEEventSubscriber} is responsible for broadcasting Eclipse SmartHome events
 * to currently listening SSE clients.
 *
 * @author Stefan Bußweiler - Initial contribution
 */
public class SSEEventSubscriber implements EventSubscriber {

    private SseResource sseResource;

    private final Set<String> subscribedEventTypes = ImmutableSet.of(ItemAddedEvent.TYPE, ItemRemovedEvent.TYPE,
            ItemUpdatedEvent.TYPE);

    protected void setSseResource(SseResource sseResource) {
        this.sseResource = sseResource;
    }

    protected void unsetSseResource(SseResource sseResource) {
        this.sseResource = null;
    }

    @Override
    public Set<String> getSubscribedEventTypes() {
        return subscribedEventTypes;
    }

    @Override
    public EventFilter getEventFilter() {
        return null;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof ItemAddedEvent) {
            sseResource.broadcastEvent(EventType.ITEM_ADDED, event);
        } else if (event instanceof ItemRemovedEvent) {
            sseResource.broadcastEvent(EventType.ITEM_REMOVED, event);
        } else if (event instanceof ItemUpdatedEvent) {
            sseResource.broadcastEvent(EventType.ITEM_UPDATED, event);
        }
    }

}
