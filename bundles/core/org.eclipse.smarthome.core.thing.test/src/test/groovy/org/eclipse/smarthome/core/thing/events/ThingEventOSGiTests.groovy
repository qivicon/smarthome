package org.eclipse.smarthome.core.thing.events

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import java.util.Set;

import org.eclipse.smarthome.core.events.AbstractTypedEventSubscriber;
import org.eclipse.smarthome.core.events.Event
import org.eclipse.smarthome.core.events.EventFactory
import org.eclipse.smarthome.core.events.EventFilter;
import org.eclipse.smarthome.core.events.EventPublisher
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.eclipse.smarthome.core.events.TopicEventFilter;
import org.eclipse.smarthome.core.thing.Thing
import org.eclipse.smarthome.core.thing.ThingStatus
import org.eclipse.smarthome.core.thing.ThingStatusDetail
import org.eclipse.smarthome.core.thing.ThingStatusInfo;
import org.eclipse.smarthome.core.thing.ThingTypeUID
import org.eclipse.smarthome.core.thing.ThingUID
import org.eclipse.smarthome.core.thing.binding.ThingHandlerCallback
import org.eclipse.smarthome.core.thing.binding.builder.ThingBuilder
import org.eclipse.smarthome.core.thing.binding.builder.ThingStatusInfoBuilder
import org.eclipse.smarthome.core.thing.events.ThingEventFactory
import org.eclipse.smarthome.core.thing.events.ThingStatusInfoEvent
import org.eclipse.smarthome.test.OSGiTest
import org.junit.Before
import org.junit.Test
import org.osgi.service.event.EventConstants
import org.osgi.service.event.EventHandler

import com.google.gson.Gson;


class ThingEventOSGiTests extends OSGiTest {

    def THING_TYPE_UID = new ThingTypeUID("binding:type")
    
    def THING_UID = new ThingUID(THING_TYPE_UID, "id")
    
    Thing THING = ThingBuilder.create(THING_UID).build()

    String THING_STATUS_INFO_EVENT_TOPIC = "smarthome/things/{thingUID}/status";
    
    EventPublisher eventPublisher;
    
    EventFactory eventFactory;
    
    
    @Before
    void setUp() {
        eventPublisher = getService(EventPublisher)
        eventFactory = getService(EventFactory)
    }
    
    
    @Test
    void 'receive thing status info event via event subscriber'() {
        ThingStatusInfoEvent receivedEvent
        boolean receiveTypedEventCalled = false

        EventSubscriber thingStatusEventSubscriber = new AbstractTypedEventSubscriber<ThingStatusInfoEvent>(ThingStatusInfoEvent.TYPE) {
    
            @Override
            protected void receiveTypedEvent(ThingStatusInfoEvent event) {
                receivedEvent = event
                receiveTypedEventCalled = true
            }
        };
        registerService(thingStatusEventSubscriber, EventSubscriber.class.getName());
    
        def statusInfo = ThingStatusInfoBuilder.create(ThingStatus.ONLINE, ThingStatusDetail.NONE).build()
        Event event = ThingEventFactory.createThingStatusInfoEvent(THING_UID, statusInfo)
        eventPublisher.postEvent(event)
                
        waitForAssert {assertThat receiveTypedEventCalled, is(true)}
        
        assertThat receivedEvent.getTopic(), is(THING_STATUS_INFO_EVENT_TOPIC.replace("{thingUID}",THING_UID.getAsString()))
        assertThat receivedEvent.getType(), is(ThingStatusInfoEvent.TYPE)
        assertThat receivedEvent.getPayload(), is(new Gson().toJson(statusInfo))
        assertThat receivedEvent.getThingUID(), is(THING_UID)
        assertThat receivedEvent.getStatusInfo(), is(statusInfo)
    }
    
    
    @Test
    void 'receive thing status info event via OSGi event handler'() {
        org.osgi.service.event.Event receivedEvent
        boolean handleEventCalled = false

        EventHandler eventHandler = new EventHandler() {

            @Override
            public void handleEvent(org.osgi.service.event.Event event) {
                receivedEvent = event
                handleEventCalled = true
            }
        };
        Dictionary<String, String> properties = new Hashtable<String, String>();
        properties.put(EventConstants.EVENT_TOPIC, "smarthome")
        registerService(eventHandler, EventHandler.class.getName(), properties);

        def statusInfo = ThingStatusInfoBuilder.create(ThingStatus.ONLINE, ThingStatusDetail.NONE).build()
        Event event = ThingEventFactory.createThingStatusInfoEvent(THING_UID, statusInfo)
        eventPublisher.postEvent(event)

        waitForAssert {assertThat handleEventCalled, is(true)}
        
        //assertThat receivedEvent.getTopic(), is(THING_STATUS_INFO_EVENT_TOPIC.replace("{thingUID}",THING_UID.getAsString()))
        assertThat receivedEvent.getProperty("type"), is(ThingStatusInfoEvent.TYPE)
        assertThat receivedEvent.getProperty("payload"), is(new Gson().toJson(statusInfo))
    }

}
