package org.eclipse.smarthome.io.sse;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.eclipse.smarthome.io.rest.RESTResource;
import org.eclipse.smarthome.io.sse.util.SseUtil;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 * SSE Resource for pushing events to currently listening clients.
 * 
 * @author ivan.iliev
 * 
 */
@Path("events")
@Singleton
public class SseResource implements RESTResource {

    private final Map<EventType, SseBroadcaster> broadcasterMap;

    private final ExecutorService executorService;

    @Context
    private UriInfo uriInfo;

    public SseResource() {
        HashMap<EventType, SseBroadcaster> mutableMap = new HashMap<EventType, SseBroadcaster>();

        for (EventType eventType : EventType.values()) {
            mutableMap.put(eventType, new SseBroadcaster());
        }

        this.broadcasterMap = Collections.unmodifiableMap(mutableMap);

        this.executorService = Executors.newSingleThreadExecutor();
    }

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getEvents(@QueryParam("topics") String eventFilter) {
        final EventOutput eventOutput = new EventOutput();

        subscribeOutput(eventFilter, eventOutput);

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
    public void broadcastEvent(final String objectIdentifier, final EventType eventType, final Object eventObject) {
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                broadcasterMap.get(eventType).broadcast(SseUtil.buildEvent(eventType, objectIdentifier, eventObject));
            }
        });

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
        List<EventType> eventTypesToListen = EventType.getEventTopicByFilter(eventFilter);

        for (EventType eventType : eventTypesToListen) {
            broadcasterMap.get(eventType).add(eventOutput);
        }
    }

}
