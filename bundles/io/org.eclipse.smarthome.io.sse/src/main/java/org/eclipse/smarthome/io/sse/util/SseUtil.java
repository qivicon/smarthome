package org.eclipse.smarthome.io.sse.util;

import javax.ws.rs.core.MediaType;

import org.eclipse.smarthome.io.sse.EventType;
import org.eclipse.smarthome.io.sse.beans.EventBean;
import org.glassfish.jersey.media.sse.OutboundEvent;

public class SseUtil {
    public static OutboundEvent buildEvent(EventType eventType, String objectIdentifier, Object eventObject) {

        EventBean eventBean = new EventBean();
        eventBean.eventType = eventType.getFullNameWithIdentifier(objectIdentifier);
        eventBean.eventObject = eventObject;

        OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        OutboundEvent event = eventBuilder.name("message").mediaType(MediaType.APPLICATION_JSON_TYPE).data(eventBean)
                .build();

        return event;
    }
}
