package org.eclipse.smarthome.core.items;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.types.EventType;

public class ItemEvent implements Event {

    @Override
    public EventType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTopic() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String asString() {
        // TODO serialize, google-gson? 
        return null;
    }

    public Item getItem() {
        // TODO
        return null;
    }
    
}