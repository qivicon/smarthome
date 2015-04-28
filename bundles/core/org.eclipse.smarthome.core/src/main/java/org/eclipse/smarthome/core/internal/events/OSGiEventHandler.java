/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.internal.events;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFactory;
import org.eclipse.smarthome.core.events.EventFilter;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;

import com.google.common.collect.Sets;

/**
 * The {@link OSGiEventHandler} provides an OSGi based default implementation for the Eclipse SmartHome (ESH) event bus.
 * 
 * The OSGiEventHandler tracks {@link OldEventSubscriber}s and {@link EventFactory}s, receives OSGi events (by
 * implementing the OSGi {@link EventHandler} interface) and dispatches the received OSGi events as ESH {@link Event}s
 * to the {@link OldEventSubscriber}s.
 * 
 * Based on the event type of the received OSGi event the corresponding EventFactory is determined in order to create an
 * event instance. The OSGiEventHandler dispatches the created ESH event to the EventSubscribers if the provided
 * EventFilter applies.
 * 
 * The {@link OSGiEventHandler} also serves as {@link EventPublisher} by implementing the EventPublisher interface.
 * Events are send via the OSGi Event Admin mechanism.
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public class OSGiEventHandler implements EventHandler, EventPublisher {

    private EventAdmin osgiEventAdmin;

    private Map<String, EventFactory> typedEventFactoryCache = new ConcurrentHashMap<String, EventFactory>();

    private Map<String, Set<EventSubscriber>> typedEventSubscriberCache = new ConcurrentHashMap<String, Set<EventSubscriber>>();

    protected void setEventAdmin(EventAdmin eventAdmin) {
        this.osgiEventAdmin = eventAdmin;
    }

    protected void unsetEventAdmin(EventAdmin eventAdmin) {
        this.osgiEventAdmin = null;
    }

    protected void addEventFactory(EventFactory eventFactory) {
        Set<String> supportedEventTypes = eventFactory.getSupportedEventTypes();

        for (String supportedEventType : supportedEventTypes) {
            if (!typedEventFactoryCache.containsKey(supportedEventType)) {
                typedEventFactoryCache.put(supportedEventType, eventFactory);
            }
        }
    }

    protected void removeEventFactory(EventFactory eventFactory) {
        Set<String> supportedEventTypes = eventFactory.getSupportedEventTypes();

        for (String supportedEventType : supportedEventTypes) {
            if (typedEventFactoryCache.containsKey(supportedEventType)) {
                typedEventFactoryCache.remove(supportedEventType);
            }
        }
    }

    protected void addEventSubscriber(EventSubscriber eventSubscriber) {
        Set<String> supportedEventTypes = eventSubscriber.getSubscribedEventTypes();

        for (String supportedEventType : supportedEventTypes) {
            if (typedEventSubscriberCache.containsKey(supportedEventType)) {
                typedEventSubscriberCache.get(supportedEventType).add(eventSubscriber);
            } else {
                typedEventSubscriberCache.put(supportedEventType, Sets.newHashSet(eventSubscriber));
            }
        }
    }

    protected void removeEventSubscriber(EventSubscriber eventSubscriber) {
        Set<String> supportedEventTypes = eventSubscriber.getSubscribedEventTypes();

        for (String supportedEventType : supportedEventTypes) {
            Set<EventSubscriber> cachedEventSubscribers = typedEventSubscriberCache.get(supportedEventType);
            cachedEventSubscribers.remove(eventSubscriber);
            if (cachedEventSubscribers.isEmpty()) {
                typedEventSubscriberCache.remove(supportedEventType);
            }
        }
    }

    @Override
    public void handleEvent(org.osgi.service.event.Event osgiEvent) {
        String eventType = osgiEvent.getProperty("type").toString();
        String payload = osgiEvent.getProperty("payload").toString();
        String topic = decodeTopic(osgiEvent.getTopic());

        Set<EventSubscriber> eventSubscribers = typedEventSubscriberCache.get(eventType);
        EventFactory eventFactory = typedEventFactoryCache.get(eventType);

        if (eventSubscribers != null && !eventSubscribers.isEmpty() && eventFactory != null) {
            Event eshEvent = eventFactory.createEvent(eventType, topic, payload);

            for (EventSubscriber eventSubscriber : eventSubscribers) {
                EventFilter filter = eventSubscriber.getEventFilter();
                if (filter == null) {
                    eventSubscriber.receive(eshEvent);
                } else if (filter.apply(eshEvent)) {
                    eventSubscriber.receive(eshEvent);
                }
            }
        }
    }

    @Override
    public void postEvent(final Event event) throws IllegalArgumentException, IllegalStateException {
        EventAdmin eventAdmin = this.osgiEventAdmin;
        assertValidArgument(event);
        assertValidState(eventAdmin);
        postAsOSGiEvent(eventAdmin, event);
    }

    private void postAsOSGiEvent(final EventAdmin eventAdmin, final Event event) throws IllegalStateException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                @Override
                public Void run() throws Exception {
                    Dictionary<String, Object> properties = new Hashtable<String, Object>(2);
                    properties.put("type", event.getType());
                    properties.put("payload", event.getPayload());
                    eventAdmin.postEvent(new org.osgi.service.event.Event(encodeTopic(event.getTopic()), properties));
                    return null;
                }
            });
        } catch (PrivilegedActionException pae) {
            throw new IllegalStateException("Cannot post the command.", pae.getException());
        }
    }

    private void assertValidArgument(Event event) throws IllegalArgumentException {
        if (event == null) {
            throw new IllegalArgumentException("Argument 'event' must not be null.");
        }
        // TODO: further check to ensure 'valid' event argument
    }

    private void assertValidState(EventAdmin eventAdmin) throws IllegalStateException {
        if (eventAdmin == null) {
            throw new IllegalStateException("The event bus module is not available!");
        }
        // TODO: further checks to ensure 'valid' event bus module
    }

    private String encodeTopic(String topic) {
        // TODO: find appropriate encoding character
        return topic.replaceAll(":", "-");
    }

    private String decodeTopic(String topic) {
        // TODO: find appropriate encoding character
        return topic.replaceAll("-", ":");
    }

}
