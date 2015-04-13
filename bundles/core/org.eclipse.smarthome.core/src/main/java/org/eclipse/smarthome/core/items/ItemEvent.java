package org.eclipse.smarthome.core.items;

import org.eclipse.smarthome.core.events.Event;

public class ItemEvent implements Event {

//    @Override
//    public String getType() {
//        // TODO Auto-generated method stub
//        return null;
//    }

    @Override
    public String getTopic() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getPayload() {
        // TODO serialize, google-gson? 
        return null;
    }

    public Item getItem() {
        // TODO
        return null;
    }
    
}