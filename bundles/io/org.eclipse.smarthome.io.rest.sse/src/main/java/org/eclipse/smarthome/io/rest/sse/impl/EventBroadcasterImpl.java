/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.sse.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.io.rest.sse.EventBroadcaster;
import org.eclipse.smarthome.io.rest.sse.EventChannel;
import org.eclipse.smarthome.io.rest.sse.EventType;
import org.eclipse.smarthome.io.rest.sse.EventTypeProvider;
import org.eclipse.smarthome.io.rest.sse.beans.EventUtil;

/**
 * An implementation of the event broadcaster - tracks registered
 * {@link EventChannel}s and {@link EventTypeProvider}s. Can be used to
 * broadcast an event on all channels, obtain all currently registered
 * {@link EventType}s and filter them.
 * 
 * @author Ivan Iliev - Initial contribution and API
 * 
 */
public class EventBroadcasterImpl implements EventBroadcaster {

    private final Set<EventChannel> eventChannels;

    private final Set<EventType> eventTypes;

    private final ExecutorService executorService;

    public EventBroadcasterImpl() {
        this.eventChannels = new HashSet<EventChannel>();
        this.executorService = Executors.newSingleThreadExecutor();
        this.eventTypes = new CopyOnWriteArraySet<EventType>();
    }

    @Override
    public void broadcastEvent(final String objectIdentifier, final EventType eventType, final Object eventObject) {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                synchronized (eventChannels) {
                    for (EventChannel channel : eventChannels) {
                        channel.broadcastEvent(objectIdentifier, eventType, eventObject);
                    }
                }
            }
        });
    }

    @Override
    public Set<EventType> getEventTypes() {
        return Collections.unmodifiableSet(eventTypes);
    }

    @Override
    public Set<EventType> filterEventTypes(String filter) {
        if (!StringUtils.isEmpty(filter)) {

            // Get an array of all filters separated by a comma
            String[] subfilters = StringUtils.split(filter.toLowerCase(), EventUtil.FILTER_TOKENS_SEPARATOR);

            // Maintains sets of filter namespaces, eventObjects, eventTypes
            Set<String> filterNamespaces = new HashSet<String>();
            Set<String> filterEventObjects = new HashSet<String>();
            Set<String> filterEventTypes = new HashSet<String>();

            for (String subfilter : subfilters) {
                // split current filter into namespace, eventObject and
                // eventType
                String[] splitFilterToken = splitFilterToken(subfilter);

                filterNamespaces.add(splitFilterToken[0]);
                filterEventObjects.add(splitFilterToken[1]);
                filterEventTypes.add(splitFilterToken[2]);
            }

            Set<EventType> events = new HashSet<EventType>();

            boolean isNameSpaceWildcard = filterNamespaces.contains(EventUtil.WILDCARD);
            boolean isEventObjectWildcard = filterEventObjects.contains(EventUtil.WILDCARD);
            boolean isEventTypeWildcard = filterEventTypes.contains(EventUtil.WILDCARD);

            for (EventType eventType : eventTypes) {
                boolean isInFilter = (isNameSpaceWildcard || filterNamespaces.contains(eventType.getNamespace()))
                        && (isEventObjectWildcard || filterEventObjects.contains(eventType.getObject()))
                        && (isEventTypeWildcard || filterEventTypes.contains(eventType.getType()));

                if (isInFilter) {
                    events.add(eventType);
                }
            }

            return events;
        }

        return getEventTypes();
    }

    /**
     * Parses the given filter string into an array with size 3 where the first
     * element is the given filterNamespace, the second element is the given
     * filterEventObject and the third element is filterEventType.
     * 
     * Any component missing is considered to be a WILDCARD filter(*).
     * 
     * @param filter
     * @return
     */
    private String[] splitFilterToken(String filter) {
        String[] filterTokens = StringUtils.split(filter.toLowerCase(), EventUtil.FILTER_SEPARATOR);

        String filterNamespace = "*";
        String fiterEventObject = "*";
        String filterEventType = "*";

        switch (filterTokens.length) {
        case 3:
            filterEventType = filterTokens[2].trim();
        case 2:
            fiterEventObject = filterTokens[1].trim();
        case 1:
            filterNamespace = filterTokens[0].trim();
            break;
        default:
            break;
        }

        return new String[] { filterNamespace, fiterEventObject, filterEventType };
    }

    protected void addEventChannel(EventChannel eventChannel) {
        synchronized (eventChannels) {
            eventChannels.add(eventChannel);
        }

        eventChannel.initialize(this);
    }

    protected void removeEventChannel(EventChannel eventChannel) {
        synchronized (eventChannels) {
            eventChannels.remove(eventChannel);
        }
    }

    protected void addEventTypeProvider(EventTypeProvider eventTypeProvider) {
        synchronized (eventTypes) {
            eventTypes.addAll(eventTypeProvider.getEventTypes());
        }

    }

    protected void removeEventTypeProvider(EventTypeProvider eventTypeProvider) {
        synchronized (eventTypes) {
            eventTypes.removeAll(eventTypeProvider.getEventTypes());
        }
    }

}
