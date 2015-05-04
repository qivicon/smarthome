package org.eclipse.smarthome.core.internal.events;

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import org.junit.Test

import org.eclipse.smarthome.core.events.Event


class OSGiEventHandlerTest {

    @Test
    public void 'OSGiEventHandler validates events before posted'() {
        OSGiEventHandler eventHandler = new OSGiEventHandler();

        try {
            eventHandler.postEvent(null)
        } catch(IllegalArgumentException e) {
            assertThat e.getMessage(), is("Argument 'event' must not be null.")
        }

        Event event = [ getType: {}, getPayload: {"{a: 'A', b: 'B'}"}, getTopic: {"smarthome/some/topic"} ] as Event
        try {
            eventHandler.postEvent(event)
        } catch(IllegalArgumentException e) {
            assertThat e.getMessage(), is("The type of the 'event' argument must not be null or empty.")
        }

        event = [ getType: {"SomeType"}, getPayload: {}, getTopic: {"smarthome/some/topic"} ] as Event
        try {
            eventHandler.postEvent(event)
        } catch(IllegalArgumentException e) {
            assertThat e.getMessage(), is("The payload of the 'event' argument must not be null or empty.")
        }

        event = [ getType: {"SomeType"}, getPayload: {"{a: 'A', b: 'B'}"}, getTopic: {} ] as Event
        try {
            eventHandler.postEvent(event)
        } catch(IllegalArgumentException e) {
            assertThat e.getMessage(), is("The topic of the 'event' argument must not be null or empty.")
        }

        event = [ getType: {"SomeType"}, getPayload: {"{a: 'A', b: 'B'}"}, getTopic: {"smarthome/some/topic"} ] as Event
        try {
            eventHandler.postEvent(event)
        } catch(IllegalStateException e) {
            assertThat e.getMessage(), is("The event bus module is not available!")
        }
    }
}
