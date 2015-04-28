package org.eclipse.smarthome.core.items.events;

import java.util.Set;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFactory;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;

public class ItemEventFactory implements EventFactory {

    @Override
    public Event createEvent(String eventType, String topic, String payload) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getSupportedEventTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    public static ItemCommandEvent createCommandEvent(String itemName, Command command) {
        // TODO
        return null;
    }

    public static ItemUpdateEvent createUpdateEvent(String itemName, State state) {
        // TODO
        return null;
    }

}
