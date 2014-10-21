package org.eclipse.smarthome.automation.module.trigger;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.automation.core.module.handler.TriggerListener;
import org.eclipse.smarthome.automation.core.module.handler.TriggerHandler;
import org.eclipse.smarthome.core.events.AbstractEventSubscriber;
import org.eclipse.smarthome.core.types.State;

public class UpdateTriggerHandler extends AbstractEventSubscriber implements TriggerHandler {

    private Map<String, TriggerListener> listeners = new HashMap<>();

    @Override
    public boolean addListener(Map<String, Object> parameters, TriggerListener listener) {
        String itemName =(String) parameters.get("itemName");
        if (itemName!=null && !itemName.trim().isEmpty()){
        	listeners.put(itemName, listener);
        	return true;
        }
        return false;
    }

    @Override
    public void receiveUpdate(String itemName, State newState) {
        TriggerListener triggerListener = listeners.get(itemName);
        if (triggerListener != null) {
            triggerListener.execute();
        }
    }

	@Override
	public boolean removeListener(Map<String, Object> parameters,
			TriggerListener listener) {
		String itemName = (String)parameters.get("itemName");
		if (itemName!=null && !itemName.trim().isEmpty()){
			listeners.remove(itemName);
		}
		return false;
	}

}
