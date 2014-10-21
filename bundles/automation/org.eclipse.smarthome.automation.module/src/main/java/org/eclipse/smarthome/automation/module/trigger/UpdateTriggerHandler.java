package org.eclipse.smarthome.automation.module.trigger;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.automation.core.runtimemodel.ITriggerListener;
import org.eclipse.smarthome.automation.core.runtimemodel.ITriggerModuleHandler;
import org.eclipse.smarthome.core.events.AbstractEventSubscriber;
import org.eclipse.smarthome.core.types.State;

public class UpdateTriggerHandler extends AbstractEventSubscriber implements ITriggerModuleHandler {

    private Map<String, ITriggerListener> listeners = new HashMap<>();

    @Override
    public boolean addListener(Map<String, String> parameters, ITriggerListener listener) {
        String itemName = parameters.get("itemName");
        if (itemName!=null && !itemName.trim().isEmpty()){
        	listeners.put(itemName, listener);
        	return true;
        }
        return false;
    }

    @Override
    public void receiveUpdate(String itemName, State newState) {
        ITriggerListener triggerListener = listeners.get(itemName);
        if (triggerListener != null) {
            triggerListener.execute();
        }
    }

	@Override
	public boolean removeListener(Map<String, String> parameters,
			ITriggerListener listener) {
		String itemName = parameters.get("itemName");
		if (itemName!=null && !itemName.trim().isEmpty()){
			listeners.remove(itemName);
		}
		return false;
	}

}
