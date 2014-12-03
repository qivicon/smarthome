package org.eclipse.smarthome.io.sse;

import java.util.Arrays;
import java.util.List;

public enum EventType {
    ITEM_ADDED, ITEM_REMOVED, ITEM_UPDATED, THING_ADDED, THING_REMOVED, THING_UPDATED;

    public static final List<EventType> VALUES_LIST = Arrays.asList(EventType.values());
}
