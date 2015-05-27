/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.items.events;

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import org.eclipse.smarthome.core.events.Event
import org.eclipse.smarthome.core.items.events.ItemEventFactory.ItemEventPayloadBean
import org.eclipse.smarthome.core.library.types.OnOffType
import org.junit.Test

import com.google.gson.Gson

/**
 * {@link ItemEventFactoryTests} tests the {@link ItemEventFactory}.
 *
 * @author Stefan Bußweiler - Initial contribution
 */
class ItemEventFactoryTests {

    def ITEM_NAME = "ItemA"
    def SOURCE = "binding:type:id:channel"
    def ITEM_COMMAND_TYPE = ItemCommandEvent.TYPE
    def ITEM_UPDATE_TYPE = ItemStateEvent.TYPE
    def ITEM_COMMAND_TOPIC = ItemEventFactory.ITEM_COMAND_EVENT_TOPIC.replace("{itemName}", ITEM_NAME)
    def ITEM_UPDATE_TOPIC = ItemEventFactory.ITEM_STATE_EVENT_TOPIC.replace("{itemName}", ITEM_NAME)

    ItemEventFactory factory = new ItemEventFactory()

    @Test
    void 'ItemEventFactory creates Event as ItemCommandEvent (type OnOffType) correctly'() {
        OnOffType command = OnOffType.ON
        String payload = new Gson().toJson(new ItemEventPayloadBean(ITEM_NAME, command.getClass().getName(), command.toString(), SOURCE))

        Event event = factory.createEvent(ITEM_COMMAND_TYPE, ITEM_COMMAND_TOPIC, payload)

        assertThat event, is(instanceOf(ItemCommandEvent))
        ItemCommandEvent itemCommandEvent = event as ItemCommandEvent
        assertThat itemCommandEvent.getType(), is(ITEM_COMMAND_TYPE)
        assertThat itemCommandEvent.getTopic(), is(ITEM_COMMAND_TOPIC)
        assertThat itemCommandEvent.getPayload(), is(payload)
        assertThat itemCommandEvent.getItemName(), is(ITEM_NAME)
        assertThat itemCommandEvent.getSource(), is(SOURCE)
        assertThat itemCommandEvent.getItemCommand(), is(instanceOf(OnOffType))
        assertThat itemCommandEvent.getItemCommand(), is(command)
    }

    @Test
    void 'ItemEventFactory creates ItemCommandEvent (type OnOffType) correctly'() {
        OnOffType command = OnOffType.ON
        String payload = new Gson().toJson(new ItemEventPayloadBean(ITEM_NAME, command.getClass().getName(), command.toString(), SOURCE))

        ItemCommandEvent event = ItemEventFactory.createCommandEvent(ITEM_NAME, command, SOURCE)

        assertThat event.getType(), is(ITEM_COMMAND_TYPE)
        assertThat event.getTopic(), is(ITEM_COMMAND_TOPIC)
        assertThat event.getPayload(), is(payload)
        assertThat event.getItemName(), is(ITEM_NAME)
        assertThat event.getSource(), is(SOURCE)
        assertThat event.getItemCommand(), is(instanceOf(OnOffType))
        assertThat event.getItemCommand(), is(command)
    }

    @Test
    void 'ItemEventFactory creates Event as ItemUpdateEvent (type OnOffType) correctly'() {
        OnOffType state = OnOffType.ON
        String payload = new Gson().toJson(new ItemEventPayloadBean(ITEM_NAME, state.getClass().getName(), state.toString(), SOURCE))

        Event event = factory.createEvent(ITEM_UPDATE_TYPE, ITEM_UPDATE_TOPIC, payload)

        assertThat event, is(instanceOf(ItemStateEvent))
        ItemStateEvent itemUpdateEvent = event as ItemStateEvent
        assertThat itemUpdateEvent.getType(), is(ITEM_UPDATE_TYPE)
        assertThat itemUpdateEvent.getTopic(), is(ITEM_UPDATE_TOPIC)
        assertThat itemUpdateEvent.getPayload(), is(payload)
        assertThat itemUpdateEvent.getItemName(), is(ITEM_NAME)
        assertThat itemUpdateEvent.getSource(), is(SOURCE)
        assertThat itemUpdateEvent.getItemState(), is(instanceOf(OnOffType))
        assertThat itemUpdateEvent.getItemState(), is(state)
    }

    @Test
    void 'ItemEventFactory creates ItemUpdateEvent (type OnOffType) correctly'() {
        OnOffType state = OnOffType.ON
        String payload = new Gson().toJson(new ItemEventPayloadBean(ITEM_NAME, state.getClass().getName(), state.toString(), SOURCE))

        ItemStateEvent event = ItemEventFactory.createStateEvent(ITEM_NAME, state, SOURCE)

        assertThat event.getType(), is(ITEM_UPDATE_TYPE)
        assertThat event.getTopic(), is(ITEM_UPDATE_TOPIC)
        assertThat event.getPayload(), is(payload)
        assertThat event.getItemName(), is(ITEM_NAME)
        assertThat event.getSource(), is(SOURCE)
        assertThat event.getItemState(), is(instanceOf(OnOffType))
        assertThat event.getItemState(), is(state)
    }

    @Test
    public void 'ItemEventFactory throws exception for not supported event types' () {
        try {
            factory.createEvent("SOME_NOT_SUPPORTED_TYPE", ITEM_COMMAND_TOPIC, "{\"some\":\"invalidPayload")
            fail("IllegalArgumentException expected!")
        } catch(IllegalArgumentException e) {
            assertThat e.getMessage(), is("The event type 'SOME_NOT_SUPPORTED_TYPE' is not supported by this factory.")
        }
    }

    @Test
    public void 'ItemEventFactory validates arguments'() {
        def payload = "{\"some\":\"invalidPayload"
        try {
            factory.createEvent("", ITEM_COMMAND_TOPIC, payload)
            fail("IllegalArgumentException expected!")
        } catch(IllegalArgumentException e) {
            assertThat e.getMessage(), is("The argument 'eventType' must not be null or empty.")
        }
        try {
            factory.createEvent(ITEM_COMMAND_TYPE, "", payload)
            fail("IllegalArgumentException expected!")
        } catch(IllegalArgumentException e) {
            assertThat e.getMessage(), is("The argument 'topic' must not be null or empty.")
        }
        try {
            factory.createEvent(ITEM_COMMAND_TYPE, ITEM_COMMAND_TOPIC, "")
            fail("IllegalArgumentException expected!")
        } catch(IllegalArgumentException e) {
            assertThat e.getMessage(), is("The argument 'payload' must not be null or empty.")
        }
    }
}
