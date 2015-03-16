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

import org.eclipse.smarthome.io.rest.sse.EventBroadcaster
import org.eclipse.smarthome.io.rest.sse.EventType
import org.eclipse.smarthome.io.rest.sse.EventTypeProvider
import org.eclipse.smarthome.io.rest.sse.impl.CoreEventType
import org.eclipse.smarthome.io.rest.sse.impl.CoreEventTypeProvider
import org.eclipse.smarthome.io.rest.sse.impl.EventBroadcasterImpl
import org.eclipse.smarthome.io.rest.sse.beans.EventUtil
import org.junit.Before
import org.junit.Test

class EventTypeFilterTest {

    private static final String THINGS_OBJECT = "things";

    private static final String ITEMS_OBJECT = "items";

    private static final String ADDED_EVENT = "added";

    private static final String SH_NAMESPACE = "smarthome";
    
    private EventBroadcaster broadcaster;
    
    private EventTypeProvider eventTypeProvider;
    
    @Before
    public void setUp() {
        eventTypeProvider = new CoreEventTypeProvider();
        
        def eventBroadcaster = new EventBroadcasterImpl()
        eventBroadcaster.addEventTypeProvider(eventTypeProvider);
        
        broadcaster = eventBroadcaster;
    }
    
    
    @Test
    public void wildcardTest() {
        testFilter("*/*/*", broadcaster.getEventTypes())
        testFilter("*/*", broadcaster.getEventTypes())
        testFilter("*", broadcaster.getEventTypes())
        testFilter("", broadcaster.getEventTypes())
    }

    @Test
    public void namespaceTest() {
        testFilter(SH_NAMESPACE, getAllEventsByNamespace(SH_NAMESPACE))
        testFilter(SH_NAMESPACE+"/", getAllEventsByNamespace(SH_NAMESPACE))
        testFilter(SH_NAMESPACE+"/*", getAllEventsByNamespace(SH_NAMESPACE))
        testFilter(SH_NAMESPACE+"/*/*", getAllEventsByNamespace(SH_NAMESPACE))
    }

    @Test
    public void invalidTest() {
        //if filter tokens are more or less than the expected number we return all events
        testFilter("///////", broadcaster.getEventTypes())
        testFilter("asd/fghj/sdh/sdhd/dfj/dfj/dj", broadcaster.getEventTypes())
        testFilter("*/*/"+ADDED_EVENT+"/*", broadcaster.getEventTypes())

        //if filter can be parsed to 1 argument "@@@@" should return no values
        testFilter("//////////////@@@@", [])
    }

    @Test
    public void eventObjectTest() {
        def thingEvents = getAllEventsByEventObject(THINGS_OBJECT);
        testFilter("*/"+THINGS_OBJECT+"/*", thingEvents)
        testFilter("*/"+THINGS_OBJECT+"/", thingEvents)
        testFilter("*/"+THINGS_OBJECT, thingEvents)

        def smarthomeThingEvents = getAllEventsByNamespace(SH_NAMESPACE).findAll { THINGS_OBJECT.equals(it.object) }

        testFilter(SH_NAMESPACE+"/"+THINGS_OBJECT, smarthomeThingEvents)
        testFilter(SH_NAMESPACE+"/"+THINGS_OBJECT+"/", smarthomeThingEvents)
        testFilter(SH_NAMESPACE+"/"+THINGS_OBJECT+"/*", smarthomeThingEvents)
    }

    @Test
    public void eventTypeTest() {
        def addedEvents = getAllEventsByEventType(ADDED_EVENT);

        testFilter("*/*/"+ADDED_EVENT, addedEvents)
        testFilter("*/*/"+ADDED_EVENT+"/", addedEvents)

        def smarthomeAddedEvents = addedEvents.findAll() { SH_NAMESPACE.equals(it.namespace) }

        testFilter(SH_NAMESPACE+"/*/"+ADDED_EVENT, smarthomeAddedEvents)
        testFilter(SH_NAMESPACE+"/*/"+ADDED_EVENT+"/", smarthomeAddedEvents)

        def smarthomeThingAddedEvents = smarthomeAddedEvents.findAll() { THINGS_OBJECT.equals(it.object) }

        testFilter(SH_NAMESPACE+"/" + THINGS_OBJECT + "/"+ADDED_EVENT, smarthomeThingAddedEvents)
        testFilter(SH_NAMESPACE+"/" + THINGS_OBJECT + "/"+ADDED_EVENT+"/", smarthomeThingAddedEvents)
    }


    @Test
    public void fullNameTest() {
        assertThat((getFullName(CoreEventType.THING_ADDED) + "/test").equals(EventUtil.getTopic(CoreEventType.THING_ADDED, "test")), is(true));
    }

    @Test
    public void multipleFilterTest() {
        def itemAndThingsEvents = getAllEventsByEventObject(THINGS_OBJECT)
        itemAndThingsEvents.addAll(getAllEventsByEventObject(ITEMS_OBJECT))

        testFilter("*/"+THINGS_OBJECT+",*/"+ITEMS_OBJECT, itemAndThingsEvents)
        testFilter("*/"+THINGS_OBJECT+",*/"+ITEMS_OBJECT+",*", broadcaster.getEventTypes())
    }

    private void testFilter(filter,expected)  {
        def setExpected = expected as Set
        def filtered = broadcaster.filterEventTypes(filter) as Set

        assertThat(filtered.equals(setExpected), is(true))
    }

    private List getAllEventsByNamespace(namespace) {
        (broadcaster.getEventTypes() as List).findAll { namespace.equals(it.namespace) }
    }

    private List getAllEventsByEventType(eventType) {
        (broadcaster.getEventTypes() as List).findAll { eventType.equals(it.type) }
    }

    private List getAllEventsByEventObject(eventObject) {
        (broadcaster.getEventTypes() as List).findAll { eventObject.equals(it.object) }
    }

    private String getFullName(EventType event) {
        StringBuilder builder = new StringBuilder();
        builder.append(event.namespace);
        builder.append(EventUtil.FILTER_SEPARATOR);
        builder.append(event.object);
        if (!event.type.isEmpty()) {
            builder.append(EventUtil.FILTER_SEPARATOR);
            builder.append(event.type);
        }

        return builder.toString();
    }
}
