/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.events;

import org.eclipse.smarthome.core.events.AbstractEventFactory;
import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatusInfo;
import org.eclipse.smarthome.core.thing.ThingUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

/**
 * A {@link ThingEventFactory} is responsible for creating thing event instances, e.g. {@link ThingStatusInfoEvent}s.
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public class ThingEventFactory extends AbstractEventFactory {

    private static final String THING_STATUS_INFO_EVENT_TOPIC = "smarthome/things/{thingUID}/status";

    private static final String THING_ADDED_EVENT_TOPIC = "smarthome/things/{thingUID}/added";

    private static final String THING_REMOVED_EVENT_TOPIC = "smarthome/things/{thingUID}/removed";

    private static final String THING_UPDATED_EVENT_TOPIC = "smarthome/things/{thingUID}/updated";

    /**
     * Constructs a new ThingEventFactory.
     */
    public ThingEventFactory() {
        super(Sets.newHashSet(ThingStatusInfoEvent.TYPE));
    }

    @Override
    protected Event createEventByType(String eventType, String topic, String payload, String source) throws Exception {
        boolean isThingStatusEvent = eventType.equals(ThingStatusInfoEvent.TYPE);
        return isThingStatusEvent ? createStatusInfoEvent(eventType, topic, payload) : null;
    }

    private Event createStatusInfoEvent(String eventType, String topic, String payload) throws Exception {
        // TODO: thingUID determination will be replaced in QSDK-2530
        String[] segments = topic.split("/");
        ThingUID thingUID = new ThingUID(segments[2]);
        ThingStatusInfo thingStatusInfo = deserializePayload(payload, ThingStatusInfo.class);
        return new ThingStatusInfoEvent(topic, payload, thingUID, thingStatusInfo);
    }

    /**
     * Creates a new thing status info event based on a thing UID and a thing status info object.
     * 
     * @param thingUID the thing UID
     * @param thingStatusInfo the thing status info object
     * 
     * @return the created thing status info event
     */
    public static ThingStatusInfoEvent createStatusInfoEvent(ThingUID thingUID, ThingStatusInfo thingStatusInfo) {
        checkArguments(thingUID, thingStatusInfo);
        String topic = THING_STATUS_INFO_EVENT_TOPIC.replace("{thingUID}", thingUID.getAsString());
        String payload = serializePayload(thingStatusInfo);
        return new ThingStatusInfoEvent(topic, payload, thingUID, thingStatusInfo);
    }

    private static void checkArguments(ThingUID thingUID, ThingStatusInfo thingStatusInfo) {
        Preconditions.checkArgument(thingUID != null, "The argument 'thingUID' must not be null.");
        Preconditions.checkArgument(thingStatusInfo != null, "The argument 'thingStatusInfo' must not be null.");
    }

    /**
     * Creates a thing added event.
     * 
     * @param thing the thing
     * 
     * @return the created thing added event
     */
    public static Event createAddedEvent(Thing thing) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Creates a thing removed event.
     * 
     * @param thing the thing
     * 
     * @return the created thing removed event
     */
    public static Event createRemovedEvent(Thing thing) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Creates a thing updated event.
     * 
     * @param thing the thing
     * @param oldThing the old thing
     * @return the created item updated event
     */
    public static Event createUpdateEvent(Thing thing, Thing oldThing) {
        // TODO Auto-generated method stub
        return null;
    }

}
