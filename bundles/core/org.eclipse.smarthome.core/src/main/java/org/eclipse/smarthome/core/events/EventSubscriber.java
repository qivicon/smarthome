/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.events;

import java.util.Set;

/**
 * The {@link EventSubscriber} defines the callback interface for receiving events from
 * the Eclipse SmartHome event bus.
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public interface EventSubscriber {

    public static String ALL_EVENT_TYPES = "ALL";

    /**
     * Gets the event types to which the event subscriber is subscribed to.
     * 
     * @return subscribed event types
     */
    Set<String> getSubscribedEventTypes();

    /**
     * Gets an {@link EventFilter} in order to receive specific events if the filter applies.
     * 
     * @return the event filter
     */
    EventFilter getEventFilter();

    /**
     * Callback method for receiving {@link Event}s from the Eclipse SmartHome event bus.
     * 
     * @param event the received event
     */
    void receive(Event event);
}
