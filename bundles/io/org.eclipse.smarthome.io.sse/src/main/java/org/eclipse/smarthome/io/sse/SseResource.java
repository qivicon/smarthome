package org.eclipse.smarthome.io.sse;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.eclipse.smarthome.io.rest.RESTResource;
import org.eclipse.smarthome.io.sse.beans.RootEventBean;
import org.eclipse.smarthome.io.sse.util.SseUtil;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SSE Resource for pushing notable smarthome events to currently listening
 * clients.
 * 
 * @author ivan.iliev
 * 
 */
@Path("events")
@Singleton
public class SseResource implements RESTResource {
    private static final Logger logger = LoggerFactory.getLogger(SseResource.class);

    private static final char FILTER_SEPARATOR = ',';

    private final Map<EventType, SseBroadcaster> broadcasterMap;

    @Context
    private UriInfo uriInfo;

    public SseResource() {
        HashMap<EventType, SseBroadcaster> mutableMap = new HashMap<EventType, SseBroadcaster>();

        for (EventType eventType : EventType.values()) {
            mutableMap.put(eventType, new SseBroadcaster());
        }

        this.broadcasterMap = Collections.unmodifiableMap(mutableMap);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventsInfo() {
        RootEventBean rootEventBean = new RootEventBean();

        rootEventBean.supportedEvents = EventType.values();
        rootEventBean.link = uriInfo.getBaseUriBuilder().path("/listen").toString();
        rootEventBean.description = "SSE Event stream. "
                + "Using a query parameter &filter=EVENT1,EVENT2,EVENT3 "
                + "you can filter which events you want to listen for. "
                + "All possible events are listed in the supportedEvents attribute."
                + "If no filter parameter is passed or it is empty all events will be broadcasted.";

        return Response.ok(rootEventBean).build();
    }

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    @Path("/listen")
    public EventOutput getEvents(@QueryParam("filter") String eventFilter) {
        final EventOutput eventOutput = new EventOutput();

        subscribeOutput(eventFilter, eventOutput);

        return eventOutput;
    }

    /**
     * Broadcasts an event described by the given parameters to all currently
     * listening clients.
     * 
     * @param eventMessage
     *            - event description message
     * @param eventType
     *            - event type
     * @param eventObject
     *            - bean that can be converted to a JSON object.
     */
    public void broadcastEvent(String eventMessage, EventType eventType, Object eventObject) {
        broadcasterMap.get(eventType).broadcast(
                SseUtil.buildEvent(eventType, eventMessage, eventObject));
    }

    /**
     * 
     * 
     * 
     * @param eventFilter
     * @param eventOutput
     */
    private void subscribeOutput(String eventFilter, final EventOutput eventOutput) {
        Set<EventType> eventTypesToListen = new HashSet<EventType>(EventType.VALUES_LIST);

        if (!StringUtils.isEmpty(eventFilter)) {
            eventTypesToListen = new HashSet<EventType>();

            String[] filterValues = StringUtils.split(eventFilter, FILTER_SEPARATOR);

            for (String value : filterValues) {
                try {
                    EventType eventType = EventType.valueOf(value.trim()
                            .toUpperCase(Locale.ENGLISH));

                    eventTypesToListen.add(eventType);
                } catch (IllegalArgumentException e) {
                    logger.error("Error while mapping SSE event from filter " + value, e);
                }
            }
        }

        for (EventType eventType : eventTypesToListen) {
            broadcasterMap.get(eventType).add(eventOutput);
        }
    }

}
