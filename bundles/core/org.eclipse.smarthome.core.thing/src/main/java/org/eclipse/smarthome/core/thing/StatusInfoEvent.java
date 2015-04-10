package org.eclipse.smarthome.core.thing;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.types.EventType;

public class StatusInfoEvent implements Event {

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
    
    public StatusInfo getStatusInfo() {
        // TODO Auto-generated method stub
        return null;
    }

}
