/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.sse;

/**
 * Represents an event channel and is responsible for actually publishing events
 * to where they need to go - either over HTTP connection(SSE), OSGI EventBus,
 * writing to the database, etc. These services are tracked by
 * {@link EventBroadcaster} and whenever an event is broadcasted the
 * {@link #broadcastEvent(String, EventType, Object)} method is called for every
 * EventChannel.
 * 
 * @author Ivan Iliev - Initial contribution and API
 * 
 */
public interface EventChannel {

    /**
     * Write the given event into a desired event output stream.
     * 
     * @param objectIdentifier
     * @param eventType
     * @param eventObject
     */
    public void broadcastEvent(final String objectIdentifier, final EventType eventType, final Object eventObject);

    /**
     * Called whenever an EventChannel is picked up by the
     * {@link EventBroadcaster}, useful for initializations
     * 
     * @param broadcaster
     */
    public void initialize(EventBroadcaster broadcaster);

}
