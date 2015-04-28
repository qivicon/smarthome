package org.eclipse.smarthome.core.items.events;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.types.State;

public class ItemUpdateEvent implements Event {

    @Override
    public String getType() {
        return ItemUpdateEvent.class.getName();
    }

    @Override
    public String getTopic() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPayload() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Item getItemName() {
        // TODO
        return null;
    }

    public State getItemState() {
        // TODO
        return null;
    }

}
