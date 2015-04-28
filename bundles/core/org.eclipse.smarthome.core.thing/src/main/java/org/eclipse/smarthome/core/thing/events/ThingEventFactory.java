/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.events;

import java.util.Set;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFactory;
import org.eclipse.smarthome.core.thing.ThingStatusInfo;
import org.eclipse.smarthome.core.thing.ThingUID;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;

public class ThingEventFactory implements EventFactory {

    private static final String THING_STATUS_INFO_EVENT_TOPIC = "smarthome/things/{thingUID}/status";

    private final Set<String> supportedEventTypes = ImmutableSet.of(ThingStatusInfoEvent.TYPE);

    @Override
    public Event createEvent(String eventType, String topic, String payload) {
        Event event = null;
        if (supportedEventTypes.contains(eventType)) {
            if (eventType.equals(ThingStatusInfoEvent.TYPE)) {
                ThingStatusInfo thingStatusInfo = new Gson().fromJson(payload, ThingStatusInfo.class);
                ThingUID thingUID = new ThingUID(getTopicElement(topic, 2));
                event = new ThingStatusInfoEvent(topic, payload, thingUID, thingStatusInfo);
            }
        }
        return event;
    }

    @Override
    public Set<String> getSupportedEventTypes() {
        return supportedEventTypes;
    }

    public static ThingStatusInfoEvent createThingStatusInfoEvent(ThingUID thingUID, ThingStatusInfo thingStatusInfo) {
        String topic = buildThingStatusTopic(thingUID);
        String payload = new Gson().toJson(thingStatusInfo);
        return new ThingStatusInfoEvent(topic, payload, thingUID, thingStatusInfo);
    }

    private String getTopicElement(String topic, int position) {
        String segments[] = topic.split("/");
        return (segments.length > position) ? segments[position] : null;
    }

    private static String buildThingStatusTopic(ThingUID thingUID) {
        return THING_STATUS_INFO_EVENT_TOPIC.replace("{thingUID}", thingUID.getAsString());
    }

}
