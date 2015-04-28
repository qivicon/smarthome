/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.events;

/**
 * SmartHome {@link Event} objects are delivered through the Eclipse SmartHome event bus to {@link EventSubscriber}
 * implementations which subscribe to the topic of the event.
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public interface Event {

    /**
     * Gets the event type.
     * 
     * @return the event type
     */
    String getType();

    /**
     * Gets the topic of an event.
     * 
     * @return the event topic
     */
    String getTopic();

    /**
     * Gets the event as an serialized string.
     * 
     * @return the serialized event
     */
    String getPayload();

}
