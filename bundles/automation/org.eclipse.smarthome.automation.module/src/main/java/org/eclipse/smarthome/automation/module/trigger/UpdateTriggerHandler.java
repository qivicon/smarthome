package org.eclipse.smarthome.automation.module.trigger;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.smarthome.automation.core.module.handler.ModuleContext;
import org.eclipse.smarthome.automation.core.module.handler.TriggerHandler;
import org.eclipse.smarthome.automation.core.module.handler.TriggerListener;
import org.eclipse.smarthome.core.events.AbstractEventSubscriber;
import org.eclipse.smarthome.core.types.State;

public class UpdateTriggerHandler extends AbstractEventSubscriber implements
		TriggerHandler {

	private Map<ModuleContext, TriggerListener> listeners = new HashMap<>();

	@Override
	public void addListener(ModuleContext context, TriggerListener listener) {
		String itemName = (String) context.getInputParameter("itemName");
		if (itemName != null && !itemName.trim().isEmpty()) {
			listeners.put(context, listener);
		}
	}

	@Override
	public void receiveUpdate(String itemName, State newState) {
		for (Entry<ModuleContext, TriggerListener> entry : listeners.entrySet()) {
			if (itemName.equals(entry.getKey().getInputParameter("itemName"))) {
				entry.getKey().addOutputParameter("state", newState.toString());
				entry.getValue().execute(entry.getKey());
			}
		}
	}

	@Override
	public void removeListener(ModuleContext context, TriggerListener listener) {
		listeners.remove(context);
	}

	@Override
	public String getName() {
		return "update";
	}

}
