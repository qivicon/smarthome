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
import org.eclipse.smarthome.core.events.TopicEventFilter;
import org.eclipse.smarthome.core.items.events.ItemCommandEvent;
import org.eclipse.smarthome.core.items.events.ItemUpdateEvent;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.io.rest.sse.EventType;
import org.eclipse.smarthome.io.rest.sse.SseResource;

import com.google.common.collect.ImmutableSet;

/**
 * Listener responsible for broadcasting internal item update/command events to
 * all clients subscribed to them.
 *
 * @author Ivan Iliev - Initial Contribution and API
 * @author Stefan Bußweiler - Migration to new ESH event concept
 *
 */
public class OSGiEventListener implements EventSubscriber {

    private SseResource sseResource;

    protected void setSseResource(SseResource sseResource) {
        this.sseResource = sseResource;
    }

    protected void unsetSseResource(SseResource sseResource) {
        this.sseResource = null;
    }

    @Override
    public Set<String> getSubscribedEventTypes() {
        return ImmutableSet.of(ItemCommandEvent.TYPE, ItemUpdateEvent.TYPE);
    }

    @Override
    public EventFilter getEventFilter() {
        return new TopicEventFilter("smarthome/.*");
    }

    @Override
    public void receive(Event event) {
        if (event instanceof ItemUpdateEvent) {
            ItemUpdateEvent itemUpdateEvent = (ItemUpdateEvent) event;
            receiveUpdate(itemUpdateEvent.getItemName(), itemUpdateEvent.getItemState());
        } else if (event instanceof ItemCommandEvent) {
            ItemCommandEvent itemCommandEvent = (ItemCommandEvent) event;
            receiveCommand(itemCommandEvent.getItemName(), itemCommandEvent.getItemCommand());
        }
    }

    private void receiveCommand(String itemName, Command command) {
        sseResource.broadcastEvent(itemName, EventType.COMMAND, command.toString());
    }

    private void receiveUpdate(String itemName, State newState) {
        sseResource.broadcastEvent(itemName, EventType.UPDATE, newState.toString());
    }
}
