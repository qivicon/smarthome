/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.sse.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.eclipse.smarthome.io.rest.sse.EventBroadcaster;
import org.eclipse.smarthome.io.rest.sse.EventChannel;
import org.eclipse.smarthome.io.rest.sse.EventType;
import org.eclipse.smarthome.io.rest.sse.internal.util.SseUtil;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 * SSE Resource for pushing events to currently listening clients.
 * 
 * @author Ivan Iliev - Initial Contribution and API
 * 
 */
@Path("events")
@Singleton
public class SseResource implements EventChannel {

    private final Map<EventType, SseBroadcaster> broadcasterMap;

    @Context
    private HttpServletResponse response;

    private EventBroadcaster eventBroadcaster;

    public SseResource() {
        this.broadcasterMap = new ConcurrentHashMap<EventType, SseBroadcaster>();
    }

    /**
     * Subscribes the connecting client to the stream of events filtered by the
     * given eventFilter.
     * 
     * @param eventFilter
     * @return {@link EventOutput} object associated with the incoming
     *         connection.
     * @throws IOException
     * @throws InterruptedException
     */
    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public Object getEvents(@QueryParam("topics") String eventFilter) throws IOException, InterruptedException {

        // if this channel has not yet been initialized return service
        // unavailable
        if (eventBroadcaster == null) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }

        final EventOutput eventOutput = new EventOutput();

        subscribeOutput(eventFilter, eventOutput);

        if (!SseUtil.SERVLET3_SUPPORT) {
            // if we don't have sevlet 3.0 async support, we want to make sure
            // that the response is not compressed and buffered so that the
            // client receives server sent events at the moment of sending them
            response.addHeader(HttpHeaders.CONTENT_ENCODING, "identity");

            // Response headers are written now, since the thread will be
            // blocked later on.
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(SseFeature.SERVER_SENT_EVENTS);
            response.flushBuffer();

            // enable blocking for this thread
            SseUtil.enableBlockingSse();
        }

        return eventOutput;
    }

    /**
     * Broadcasts an event described by the given parameters to all currently
     * listening clients.
     * 
     * @param objectIdentifier
     *            - identifier of the event object
     * @param eventType
     *            - event type
     * @param eventObject
     *            - bean that can be converted to a JSON object.
     */
    @Override
    public void broadcastEvent(final String objectIdentifier, final EventType eventType, final Object eventObject) {
        SseBroadcaster sseBroadcaster = broadcasterMap.get(eventType);

        if (sseBroadcaster != null) {
            sseBroadcaster.broadcast(SseUtil.buildEvent(eventType, objectIdentifier, eventObject));
        }
    }

    /**
     * 
     * Subscribes the given eventOutput to all EventTypes matching the given
     * filter.
     * 
     * @param eventFilter
     * @param eventOutput
     */
    private void subscribeOutput(String eventFilter, final EventOutput eventOutput) {
        Set<EventType> eventTypesToListen = eventBroadcaster.filterEventTypes(eventFilter);

        for (EventType eventType : eventTypesToListen) {
            SseBroadcaster sseBroadcaster = broadcasterMap.get(eventType);
            // if we do not already have a broadcaster for this type - create it
            if (sseBroadcaster == null) {
                sseBroadcaster = new SseBroadcaster();
                broadcasterMap.put(eventType, sseBroadcaster);
            }

            sseBroadcaster.add(eventOutput);
        }
    }

    @Override
    public void initialize(EventBroadcaster broadcaster) {
        this.eventBroadcaster = broadcaster;
    }

}
