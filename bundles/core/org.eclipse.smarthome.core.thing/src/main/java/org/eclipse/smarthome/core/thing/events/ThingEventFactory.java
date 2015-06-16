/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.events;

import java.util.List;

import org.eclipse.smarthome.core.events.AbstractEventFactory;
import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatusInfo;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.bean.ThingBean;
import org.eclipse.smarthome.core.thing.bean.ThingBeanMapper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
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
        super(Sets.newHashSet(ThingStatusInfoEvent.TYPE, ThingAddedEvent.TYPE, ThingRemovedEvent.TYPE,
                ThingUpdatedEvent.TYPE));
    }

    @Override
    protected Event createEventByType(String eventType, String topic, String payload, String source) throws Exception {
        Event event = null;
        if (eventType.equals(ThingStatusInfoEvent.TYPE)) {
            event = createStatusInfoEvent(topic, payload);
        } else if (eventType.equals(ThingAddedEvent.TYPE)) {
            event = createAddedEvent(topic, payload);
        } else if (eventType.equals(ThingRemovedEvent.TYPE)) {
            event = createRemovedEvent(topic, payload);
        } else if (eventType.equals(ThingUpdatedEvent.TYPE)) {
            event = createUpdatedEvent(topic, payload);
        }
        return event;
    }

    private Event createStatusInfoEvent(String topic, String payload) throws Exception {
        // TODO: thingUID determination will be replaced in QSDK-2530
        String[] segments = topic.split("/");
        ThingUID thingUID = new ThingUID(segments[2]);
        ThingStatusInfo thingStatusInfo = deserializePayload(payload, ThingStatusInfo.class);
        return new ThingStatusInfoEvent(topic, payload, thingUID, thingStatusInfo);
    }

    private Event createAddedEvent(String topic, String payload) throws Exception {
        ThingBean thingBean = deserializePayload(payload, ThingBean.class);
        return new ThingAddedEvent(topic, payload, thingBean);
    }

    private Event createRemovedEvent(String topic, String payload) throws Exception {
        ThingBean thingBean = deserializePayload(payload, ThingBean.class);
        return new ThingRemovedEvent(topic, payload, thingBean);
    }

    private Event createUpdatedEvent(String topic, String payload) throws Exception {
        ThingBean[] thingBean = deserializePayload(payload, ThingBean[].class);
        if (thingBean.length != 2)
            throw new IllegalArgumentException("ThingUpdateEvent creation failed, caused by invalid payload: "
                    + payload);
        return new ThingUpdatedEvent(topic, payload, thingBean[0], thingBean[1]);
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
    public static ThingAddedEvent createAddedEvent(Thing thing) {
        // TODO: param checks
        String topic = buildTopic(THING_ADDED_EVENT_TOPIC, thing.getUID().getAsString());
        ThingBean thingDTO = mapThingToBean(thing);
        String payload = serializePayload(thingDTO);
        return new ThingAddedEvent(topic, payload, thingDTO);
    }

    /**
     * Creates a thing removed event.
     * 
     * @param thing the thing
     * 
     * @return the created thing removed event
     */
    public static ThingRemovedEvent createRemovedEvent(Thing thing) {
        // TODO: param checks
        String topic = buildTopic(THING_REMOVED_EVENT_TOPIC, thing.getUID().getAsString());
        ThingBean thingDTO = mapThingToBean(thing);
        String payload = serializePayload(thingDTO);
        return new ThingRemovedEvent(topic, payload, thingDTO);
    }

    /**
     * Creates a thing updated event.
     * 
     * @param thing the thing
     * @param oldThing the old thing
     * @return the created item updated event
     */
    public static ThingUpdatedEvent createUpdateEvent(Thing thing, Thing oldThing) {
        // TODO : param checks
        String topic = buildTopic(THING_UPDATED_EVENT_TOPIC, thing.getUID().getAsString());
        ThingBean thingDTO = mapThingToBean(thing);
        ThingBean oldThingDTO = mapThingToBean(oldThing);
        List<ThingBean> beans = Lists.newLinkedList();
        beans.add(thingDTO);
        beans.add(oldThingDTO);
        String payload = serializePayload(beans);
        return new ThingUpdatedEvent(topic, payload, thingDTO, oldThingDTO);
    }

    private static String buildTopic(String topic, String thingUID) {
        return topic.replace("{thingUID}", thingUID);
    }

    private static ThingBean mapThingToBean(Thing thing) {
        return ThingBeanMapper.mapThingToBean(thing);
    }

}
