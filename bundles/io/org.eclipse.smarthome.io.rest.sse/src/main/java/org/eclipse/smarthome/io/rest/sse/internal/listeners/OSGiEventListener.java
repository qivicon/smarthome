/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.sse.internal.listeners;

import org.eclipse.smarthome.core.events.AbstractEventSubscriber;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.io.rest.sse.EventBroadcaster;
import org.eclipse.smarthome.io.rest.sse.impl.CoreEventType;

/**
 * Listener responsible for broadcasting internal item update/command events to
 * all clients subscribed to them.
 * 
 * @author Ivan Iliev - Initial Contribution and API
 * 
 */
public class OSGiEventListener extends AbstractEventSubscriber {

    private EventBroadcaster eventBroadcaster;

    protected void setBroadcaster(EventBroadcaster eventBroadcaster) {
        this.eventBroadcaster = eventBroadcaster;
    }

    protected void unsetBroadcaster(EventBroadcaster eventBroadcaster) {
        this.eventBroadcaster = null;
    }

    @Override
    public void receiveCommand(String itemName, Command command) {
        eventBroadcaster.broadcastEvent(itemName, CoreEventType.COMMAND, command.toString());
    }

    @Override
    public void receiveUpdate(String itemName, State newState) {
        eventBroadcaster.broadcastEvent(itemName, CoreEventType.UPDATE, newState.toString());
    }
}
