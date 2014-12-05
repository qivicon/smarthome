package org.eclipse.smarthome.io.sse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Event type for each broadcasted smarthome event using SSE.
 * 
 * @author ivan.iliev
 * 
 */
public enum EventType {

    /**
     * Item was added to the item registry.
     */
    ITEM_ADDED("smarthome", "items", "added"),
    /**
     * Item was removed from the item registry.
     */
    ITEM_REMOVED("smarthome", "items", "removed"),
    /**
     * Item was updated in the item registry.
     */
    ITEM_UPDATED("smarthome", "items", "updated"),
    /**
     * Thing was added to the thing registry.
     */
    THING_ADDED("smarthome", "things", "added"),
    /**
     * Thing was removed from the thing registry.
     */
    THING_REMOVED("smarthome", "things", "removed"),
    /**
     * Thing was updated in the thing registry.
     */
    THING_UPDATED("smarthome", "things", "updated"),
    /**
     * Thing was added to the inbox.
     */
    INBOX_THING_ADDED("smarthome", "inbox", "added"),
    /**
     * Thing was removed from the inbox.
     */
    INBOX_THING_REMOVED("smarthome", "inbox", "removed"),

    /**
     * Thing was updated in the inbox.
     */
    INBOX_THING_UPDATED("smarthome", "inbox", "updated"),

    /**
     * OSGI Update event was sent
     */
    UPDATE("smarthome", "update", ""),

    /**
     * OSGI command event was sent
     */
    COMMAND("smarthome", "command", "");

    public static final List<EventType> VALUES_LIST = Arrays.asList(EventType.values());

    /**
     * Returns a list of event types by a given filter. The filter is expected
     * to be in the format <b>namespace/event_object/event_type</b> e.g.
     * <b>smarthome/items/added</b>. Wildcard can also be used in place of each
     * component e.g. <b>smarthome/&#42;/added</b>.
     * 
     * @param filter
     * @return All events if the filter is empty, no events if the filter cannot
     *         be parsed, all events that match the given filter otherwise.
     */
    public static List<EventType> getEventTopicByFilter(String filter) {
        if (!StringUtils.isEmpty(filter)) {
            String[] filterTokens = StringUtils.split(filter.toLowerCase(), FILTER_SEPARATOR);

            String filterNamespace = "*";
            String fiterEventObject = "*";
            String filterEventType = "*";

            switch (filterTokens.length) {
            case 3:
                filterEventType = filterTokens[2];
            case 2:
                fiterEventObject = filterTokens[1];
            case 1:
                filterNamespace = filterTokens[0];
                break;
            default:
                return VALUES_LIST;
            }

            List<EventType> events = new ArrayList<EventType>();
            EventType[] values = values();

            boolean isNameSpaceWildcard = WILDCARD.equals(filterNamespace);
            boolean isEventObjectWildcard = WILDCARD.equals(fiterEventObject);
            boolean isEventTypeWildcard = WILDCARD.equals(filterEventType);

            for (EventType eventType : values) {
                boolean isInFilter = (isNameSpaceWildcard || eventType.eventNamespace.equals(filterNamespace))
                        && (isEventObjectWildcard || eventType.eventObject.equals(fiterEventObject))
                        && (isEventTypeWildcard || eventType.eventType.equals(filterEventType));

                if (isInFilter) {
                    events.add(eventType);
                }
            }

            return events;
        }

        return VALUES_LIST;
    }

    private final String eventNamespace;

    private final String eventObject;

    private final String eventType;

    private final String eventFullName;

    private static final char FILTER_SEPARATOR = '/';

    private static final String WILDCARD = "*";

    private EventType(String eventNamespace, String eventObject, String eventType) {
        this.eventNamespace = eventNamespace.toLowerCase();
        this.eventObject = eventObject.toLowerCase();
        this.eventType = eventType.toLowerCase();
        this.eventFullName = initFullName();
    }

    private String initFullName() {
        StringBuilder builder = new StringBuilder();
        builder.append(eventNamespace);
        builder.append(FILTER_SEPARATOR);
        builder.append(eventObject);
        if (!this.eventType.isEmpty()) {
            builder.append(FILTER_SEPARATOR);
            builder.append(eventType);
        }

        return builder.toString();
    }

    /**
     * Returns the full name for this event using the given identifier in the
     * following format: <b>namespace/event_type/event_object/identifier</b>
     * e.g. <b>smarthome/inbox/added/235226</b>
     * 
     * @param identifier
     * @return event type full name.
     */
    public String getFullNameWithIdentifier(String identifier) {
        return eventFullName + FILTER_SEPARATOR + identifier;
    }
}
