/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.sse.test;

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import org.eclipse.smarthome.io.rest.sse.EventType
import org.eclipse.smarthome.io.rest.sse.impl.CoreEventType
import org.eclipse.smarthome.io.rest.sse.impl.CoreEventTypeProvider
import org.eclipse.smarthome.io.rest.sse.impl.EventBroadcasterImpl
import org.eclipse.smarthome.io.rest.sse.impl.SseResource
import org.junit.Before
import org.junit.Test

class EventBroadcasterTest {

    private static final String OBJECT_ID = "test"

    private static final EventType EVENT_TYPE = CoreEventType.THING_ADDED;

    def eventBroadcaster;

    def eventTypeProvider;

    def initializeCalled=false;

    def _objectIdentifier = null, _eventType = null, _eventObject = null

    def waitLock = new Object()

    def eventChannel = [
        initialize: { def broadcaster-> initializeCalled = true },
        broadcastEvent: { def objectIdentifier, def eventType, def eventObject ->
            _objectIdentifier = objectIdentifier
            _eventType = eventType
            _eventObject = eventObject

            synchronized(waitLock) {
                waitLock.notifyAll();
            }
        }
    ] as SseResource;

    @Before
    public void setUp() {
        eventTypeProvider = new CoreEventTypeProvider();
        eventBroadcaster = new EventBroadcasterImpl();
    }


    @Test
    public void eventTypeProviderAddRemoveTest() {

        assertThat(eventBroadcaster.getEventTypes().isEmpty(), is(true))

        eventBroadcaster.addEventTypeProvider(eventTypeProvider)

        assertThat(eventBroadcaster.getEventTypes().containsAll(eventTypeProvider.getEventTypes()), is(true))

        eventBroadcaster.removeEventTypeProvider(eventTypeProvider)

        assertThat(eventBroadcaster.getEventTypes().isEmpty(), is(true))
    }

    @Test
    public void eventChannelEmptyTest() {

        eventBroadcaster.broadcastEvent(OBJECT_ID, EVENT_TYPE, OBJECT_ID);

        assertThat(_objectIdentifier, is(null))
        assertThat(_eventType, is(null))
        assertThat(_eventObject, is(null))
    }


    @Test
    public void eventChannelAddRemoveTest() {

        eventBroadcaster.addEventChannel(eventChannel)

        assertThat(initializeCalled, is(true))

        eventBroadcaster.broadcastEvent(OBJECT_ID, EVENT_TYPE, OBJECT_ID);

        // since broadcastEvent spawns a new thread we wait until the broadcastEvent method of our channel has finished
        synchronized(waitLock) {
            waitLock.wait();
            assertThat(_objectIdentifier, is(OBJECT_ID))
            assertThat(_eventType, is(EVENT_TYPE))
            assertThat(_eventObject, is(OBJECT_ID))
        }

        eventBroadcaster.removeEventChannel(eventChannel)
    }
}
