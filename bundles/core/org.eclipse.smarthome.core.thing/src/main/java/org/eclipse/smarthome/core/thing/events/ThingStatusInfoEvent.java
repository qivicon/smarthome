/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.events;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.thing.ThingStatusInfo;
import org.eclipse.smarthome.core.thing.ThingUID;

/**
 * {@link ThingStatusInfoEvent}s will be delivered through the Eclipse SmartHome event bus if the status of a thing has
 * changed. Thing status info objects can be created with the {@link ThingEventFactory}.
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public class ThingStatusInfoEvent implements Event {

    /**
     * The thing status event type.
     */
    public final static String TYPE = ThingStatusInfoEvent.class.getSimpleName();

    private final String topic;

    private final String payload;

    private final ThingUID thingUID;

    private final ThingStatusInfo thingStatusInfo;

    /**
     * Creates a new thing status event object.
     * 
     * @param topic the topic
     * @param payload the payload
     * @param thingUID the thing UID
     * @param thingStatusInfo the thing status info object
     */
    public ThingStatusInfoEvent(String topic, String payload, ThingUID thingUID, ThingStatusInfo thingStatusInfo) {
        this.topic = topic;
        this.payload = payload;
        this.thingUID = thingUID;
        this.thingStatusInfo = thingStatusInfo;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    /**
     * Gets the thing UID.
     * 
     * @return the thing UID
     */
    public ThingUID getThingUID() {
        return thingUID;
    }

    /**
     * Gets the thing 
     * @return
     */
    public ThingStatusInfo getStatusInfo() {
        return thingStatusInfo;
    }

}
