/**
 * 
 */
package org.eclipse.smarthome.automation.module.action;

import org.eclipse.smarthome.automation.core.module.handler.ActionHandler;
import org.eclipse.smarthome.automation.core.module.handler.ModuleContext;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemNotFoundException;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author niehues
 *
 */
public class ToggleOnOffActionHandler implements ActionHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ToggleOnOffActionHandler.class);

	private EventPublisher eventPublisher;
	private ItemRegistry itemRegistry;

	protected void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	protected void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.smarthome.automation.core.module.handler.ModuleHandler#getName
	 * ()
	 */
	@Override
	public String getName() {

		return "toggle";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.smarthome.automation.core.module.handler.ActionHandler#execute
	 * (org.eclipse.smarthome.automation.core.module.handler.ModuleContext)
	 */
	@Override
	public void execute(ModuleContext context) {
		try {
			String itemName = (String) context.getInputParameter("itemName");
			Item item = itemRegistry.getItem(itemName);
			Command command = OnOffType.ON;
			OnOffType previousState = (OnOffType) item
					.getStateAs(OnOffType.class);
			LOGGER.debug("Toggle: Items previous State: {}", previousState);
			if (previousState.equals(OnOffType.ON)) {
				command = OnOffType.OFF;
			}
			eventPublisher.postCommand(itemName, command);
		} catch (ItemNotFoundException e) {
			return;
		}
	}

}
