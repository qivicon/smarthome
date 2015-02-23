package org.eclipse.smarthome.io.rest.sse.beans;

import org.eclipse.smarthome.io.rest.sse.EventType;

public class EventUtil {

    public static final char FILTER_SEPARATOR = '/';

    public static final char FILTER_TOKENS_SEPARATOR = ',';

    public static final String WILDCARD = "*";

    /**
     * 
     * Returns the topic for the given event using the given identifier in the
     * following format: <b>namespace/event_type/event_object/identifier</b>
     * e.g. <b>smarthome/inbox/added/235226</b>
     * 
     * @param eventType
     * @param objectIdentifier
     * @return
     */
    public static String getTopic(EventType eventType, String objectIdentifier) {
        StringBuilder builder = new StringBuilder();

        builder.append(eventType.getNamespace());
        builder.append(FILTER_SEPARATOR);
        builder.append(eventType.getObject());

        String type = eventType.getType();

        if (type != null && !type.isEmpty()) {
            builder.append(FILTER_SEPARATOR);
            builder.append(type);
        }

        if (objectIdentifier != null && !objectIdentifier.isEmpty()) {
            builder.append(FILTER_SEPARATOR);
            builder.append(objectIdentifier);
        }

        return builder.toString();
    }
}
