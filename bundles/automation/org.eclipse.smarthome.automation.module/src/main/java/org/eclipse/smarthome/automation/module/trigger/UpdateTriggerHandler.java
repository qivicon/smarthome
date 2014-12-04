package org.eclipse.smarthome.automation.module.trigger;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.smarthome.automation.core.ThreadPoolRuleExecutor;
import org.eclipse.smarthome.automation.core.module.handler.ModuleContext;
import org.eclipse.smarthome.automation.core.module.handler.TriggerHandler;
import org.eclipse.smarthome.automation.core.module.handler.TriggerListener;
import org.eclipse.smarthome.core.events.AbstractEventSubscriber;
import org.eclipse.smarthome.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateTriggerHandler extends AbstractEventSubscriber implements
		TriggerHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateTriggerHandler.class); 
	
	private Map<ModuleContext, TriggerListener> listeners = new HashMap<>();

	@Override
	public void addListener(ModuleContext context, TriggerListener listener) {
		String itemName = (String) context.getInputParameter("itemName");
		if (itemName != null && !itemName.trim().isEmpty()) {
			listeners.put(context, listener);
			LOGGER.debug("Listener added to UpdateTrigger: {}",  (context.getId()));
		}
	}

	@Override
	public void receiveUpdate(String itemName, State newState) {
		for (Entry<ModuleContext, TriggerListener> entry : listeners.entrySet()) {
			if (itemName.equals(entry.getKey().getInputParameter("itemName"))) {
				entry.getKey().addOutputParameter("state", newState.toString());
				ThreadPoolRuleExecutor.execute(entry.getKey(), entry.getValue());
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
