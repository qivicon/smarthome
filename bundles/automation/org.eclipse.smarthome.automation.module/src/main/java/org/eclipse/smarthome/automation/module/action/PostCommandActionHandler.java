package org.eclipse.smarthome.automation.module.action;

import java.util.Map;

import org.eclipse.smarthome.automation.core.module.handler.ActionHandler;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemNotFoundException;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.TypeParser;

public class PostCommandActionHandler implements ActionHandler {

	private EventPublisher eventPublisher;
	private ItemRegistry itemRegistry;

	protected void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	protected void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	@Override
	public void execute(Map<String, Object> parameters) {
		try {
			String itemName = (String) parameters.get("itemName");
			Item item = itemRegistry.getItem(itemName);
			String command = (String) parameters.get("command");
			Command commandObj = TypeParser.parseCommand(
					item.getAcceptedCommandTypes(), command);
			eventPublisher.postCommand(itemName, commandObj);
		} catch (ItemNotFoundException e) {
			return;
		}
	}

	@Override
	public String getName() {
		return "post";
	}

}
