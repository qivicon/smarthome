/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.events;

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import org.eclipse.smarthome.core.events.Event
import org.eclipse.smarthome.core.thing.ThingStatus
import org.eclipse.smarthome.core.thing.ThingStatusDetail
import org.eclipse.smarthome.core.thing.ThingUID
import org.eclipse.smarthome.core.thing.binding.builder.ThingStatusInfoBuilder
import org.junit.Test

import com.google.gson.Gson

/**
 * {@link ThingEventFactoryTests} tests the {@link ThingEventFactory}.
 *
 * @author Stefan Bußweiler - Initial contribution
 */
class ThingEventFactoryTests {
    def THING_STATUS_INFO = ThingStatusInfoBuilder
            .create(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR)
            .withDescription("Some description").build();
    def THING_UID = new ThingUID("binding:type:id")
    def TYPE = ThingStatusInfoEvent.TYPE
    def TOPIC = ThingEventFactory.THING_STATUS_INFO_EVENT_TOPIC.replace("{thingUID}", THING_UID.getAsString())
    def PAYLOAD = new Gson().toJson(THING_STATUS_INFO)

    ThingEventFactory factory = new ThingEventFactory()

    @Test
    void 'ThingEventFactory creates Event as ThingStatusInfoEvent correctly'() {
        Event event = factory.createEvent(TYPE, TOPIC, PAYLOAD, "")

        assertThat event, is(instanceOf(ThingStatusInfoEvent))
        ThingStatusInfoEvent statusEvent = event as ThingStatusInfoEvent
        assertThat statusEvent.getType(), is(TYPE)
        assertThat statusEvent.getTopic(), is(TOPIC)
        assertThat statusEvent.getPayload(), is(PAYLOAD)
        assertThat statusEvent.getStatusInfo(), is(THING_STATUS_INFO)
        assertThat statusEvent.getThingUID(), is(THING_UID)
    }

    @Test
    void 'ThingEventFactory creates ThingStatusInfoEvent correctly'() {
        ThingStatusInfoEvent event = ThingEventFactory.createStatusInfoEvent(THING_UID, THING_STATUS_INFO)

        assertThat event.getType(), is(TYPE)
        assertThat event.getTopic(), is(TOPIC)
        assertThat event.getPayload(), is(PAYLOAD)
        assertThat event.getStatusInfo(), is(THING_STATUS_INFO)
        assertThat event.getThingUID(), is(THING_UID)
    }

    @Test
    public void 'ThingEventFactory throws exception for not supported event types' () {
        try {
            factory.createEvent("SOME_NOT_SUPPORTED_TYPE", TOPIC, PAYLOAD, "")
            fail("IllegalArgumentException expected!")
        } catch(IllegalArgumentException e) {
            assertThat e.getMessage(), is("The event type 'SOME_NOT_SUPPORTED_TYPE' is not supported by this factory.")
        }
    }

    @Test
    public void 'ThingEventFactory validates arguments'() {
        try {
            factory.createEvent("", TOPIC, PAYLOAD, null)
            fail("IllegalArgumentException expected!")
        } catch(IllegalArgumentException e) {
            assertThat e.getMessage(), is("The argument 'eventType' must not be null or empty.")
        }
        try {
            factory.createEvent(TYPE, "", PAYLOAD, null)
            fail("IllegalArgumentException expected!")
        } catch(IllegalArgumentException e) {
            assertThat e.getMessage(), is("The argument 'topic' must not be null or empty.")
        }
        try {
            factory.createEvent(TYPE, TOPIC, "", null)
            fail("IllegalArgumentException expected!")
        } catch(IllegalArgumentException e) {
            assertThat e.getMessage(), is("The argument 'payload' must not be null or empty.")
        }
    }
}
