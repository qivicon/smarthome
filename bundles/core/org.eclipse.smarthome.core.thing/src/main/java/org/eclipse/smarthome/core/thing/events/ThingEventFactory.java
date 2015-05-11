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
import org.eclipse.smarthome.core.events.Topic;
import org.eclipse.smarthome.core.thing.ThingStatusInfo;
import org.eclipse.smarthome.core.thing.ThingUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;

/**
 * A {@link ThingEventFactory} is responsible for creating thing event instances, e.g. {@link ThingStatusInfoEvent}s.
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public class ThingEventFactory implements EventFactory {

    private final Set<String> supportedEventTypes = ImmutableSet.of(ThingStatusInfoEvent.TYPE);

    private static final Gson jsonConverter = new Gson();

    @Override
    public Event createEvent(String eventType, String topic, String payload) throws Exception {
        checkArguments(eventType, topic, payload);
        if (eventType.equals(ThingStatusInfoEvent.TYPE)) {
            return createThingStatusInfoEvent(eventType, topic, payload);
        } else {
            throw new IllegalArgumentException("The event type '" + eventType + "' is not supported by this factory.");
        }
    }

    private void checkArguments(String eventType, String topic, String payload) {
        Preconditions.checkArgument(eventType != null && !eventType.isEmpty(),
                "The argument 'eventType' must not be null or empty.");
        Preconditions.checkArgument(topic != null && !topic.isEmpty(),
                "The argument 'topic' must not be null or empty.");
        Preconditions.checkArgument(payload != null && !payload.isEmpty(),
                "The argument 'payload' must not be null or empty.");
    }

    private Event createThingStatusInfoEvent(String eventType, String topic, String payload) throws Exception {
        Topic topicObj = new Topic(topic);
        ThingUID thingUID = new ThingUID(topicObj.getEntityId());
        ThingStatusInfo thingStatusInfo = jsonConverter.fromJson(payload, ThingStatusInfo.class);
        return new ThingStatusInfoEvent(topic, payload, thingUID, thingStatusInfo);
    }

    @Override
    public Set<String> getSupportedEventTypes() {
        return supportedEventTypes;
    }

    /**
     * Creates a new thing status info event based on a thing UID and a thing status info object.
     * 
     * @param thingUID the thing UID
     * @param thingStatusInfo the thing status info object
     * @return the created thing status info event
     */
    public static ThingStatusInfoEvent createThingStatusInfoEvent(ThingUID thingUID, ThingStatusInfo thingStatusInfo) {
        checkArguments(thingUID, thingStatusInfo);
        Topic topicObj = new Topic("smarthome", "things", thingUID.getAsString(), "status");
        String payload = jsonConverter.toJson(thingStatusInfo);
        return new ThingStatusInfoEvent(topicObj.getAsString(), payload, thingUID, thingStatusInfo);
    }

    private static void checkArguments(ThingUID thingUID, ThingStatusInfo thingStatusInfo) {
        Preconditions.checkArgument(thingUID != null, "The argument 'thingUID' must not be null.");
        Preconditions.checkArgument(thingStatusInfo != null, "The argument 'thingStatusInfo' must not be null.");
    }

}
