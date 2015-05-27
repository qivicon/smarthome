/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.sse.internal.listeners;

import org.eclipse.smarthome.core.items.events.AbstractItemEventSubscriber;
import org.eclipse.smarthome.core.items.events.ItemCommandEvent;
import org.eclipse.smarthome.core.items.events.ItemStateEvent;
import org.eclipse.smarthome.io.rest.sse.EventType;
import org.eclipse.smarthome.io.rest.sse.SseResource;

/**
 * Listener responsible for broadcasting internal item update/command events to
 * all clients subscribed to them.
 *
 * @author Ivan Iliev - Initial Contribution and API
 * @author Stefan Bußweiler - Migration to new ESH event concept
 *
 */
public class OSGiEventListener extends AbstractItemEventSubscriber {

    private SseResource sseResource;

    protected void setSseResource(SseResource sseResource) {
        this.sseResource = sseResource;
    }

    protected void unsetSseResource(SseResource sseResource) {
        this.sseResource = null;
    }

    @Override
    protected void receiveCommand(ItemCommandEvent commandEvent) {
        sseResource.broadcastEvent(commandEvent.getItemName(), EventType.COMMAND, commandEvent.getItemCommand().toString());
    }

    @Override
    protected void receiveUpdate(ItemStateEvent updateEvent) {
        sseResource.broadcastEvent(updateEvent.getItemName(), EventType.UPDATE, updateEvent.getItemState().toString());
    }
}
