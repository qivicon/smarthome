package org.eclipse.smarthome.core.thing;

import org.eclipse.smarthome.core.events.EventFilter;

public class StatusInfoEventFilter implements EventFilter<StatusInfoEvent> {

    @Override
    public boolean apply(StatusInfoEvent event) {
        // TODO Auto-generated method stub
        return event.getTopic().contains("some topic part");
    }

}
