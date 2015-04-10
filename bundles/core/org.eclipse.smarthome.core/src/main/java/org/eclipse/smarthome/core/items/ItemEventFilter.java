package org.eclipse.smarthome.core.items;

import org.eclipse.smarthome.core.events.EventFilter;

public class ItemEventFilter implements EventFilter<ItemEvent> {

    @Override
    public boolean apply(ItemEvent event) {
        // TODO Auto-generated method stub
        return event.getTopic().contains("some topic part");
    }
    
}