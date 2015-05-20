/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.items.events;

import java.util.Set;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFilter;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.eclipse.smarthome.core.events.TopicEventFilter;

import com.google.common.collect.ImmutableSet;

/**
 * The {@link AbstractItemEventSubscriber} defines an abstract implementation of the {@link EventSubscriber} interface
 * for receiving {@link ItemUpdateEvent}s and {@link ItemCommandEvent}s from the Eclipse SmartHome event bus. </p>
 * 
 * A subclass can implement the methods {@link #receiveUpdate(ItemUpdateEvent)} and
 * {@link #receiveCommand(ItemCommandEvent)} in order to receive and handle such item events.
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public abstract class AbstractItemEventSubscriber implements EventSubscriber {

    @Override
    public Set<String> getSubscribedEventTypes() {
        return ImmutableSet.of(ItemUpdateEvent.TYPE, ItemCommandEvent.TYPE);
    }

    @Override
    public EventFilter getEventFilter() {
        return new TopicEventFilter("smarthome/items/.*");
    }

    @Override
    public void receive(Event event) {
        if (event instanceof ItemUpdateEvent) {
            receiveUpdate((ItemUpdateEvent) event);
        } else if (event instanceof ItemCommandEvent) {
            receiveCommand((ItemCommandEvent) event);
        }
    }

    /**
     * Callback method for receiving item command events from the Eclipse SmartHome event bus.
     * 
     * @param commandEvent the item command event
     */
    protected void receiveCommand(ItemCommandEvent commandEvent) {
        // Default implementation: do nothing.
        // Can be implemented by subclass in order to handle item commands.
    }

    /**
     * Callback method for receiving item update events from the Eclipse SmartHome event bus.
     * 
     * @param updateEvent the item update event
     */
    protected void receiveUpdate(ItemUpdateEvent updateEvent) {
        // Default implementation: do nothing.
        // Can be implemented by subclass in order to handle item updates.
    }

}
