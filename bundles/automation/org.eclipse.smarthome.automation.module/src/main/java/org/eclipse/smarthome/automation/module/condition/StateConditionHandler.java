package org.eclipse.smarthome.automation.module.condition;

import org.eclipse.smarthome.automation.core.module.handler.ConditionHandler;
import org.eclipse.smarthome.automation.core.module.handler.ModuleContext;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemNotFoundException;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateConditionHandler implements ConditionHandler {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(StateConditionHandler.class);

	private ItemRegistry itemRegistry;

	protected void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	@Override
	public boolean evaluate(ModuleContext context) {
		String itemName = (String) context.getInputParameter("itemName");
		try {
			Item item = itemRegistry.getItem(itemName);
			State itemState = item.getState();
			String stateString = (String) context.getInputParameter("state");
			State state = TypeParser.parseState(item.getAcceptedDataTypes(),
					stateString);
			String operator = (String) context.getInputParameter("operator");
			LOGGER.debug(
					"--> comparing current state of item {}:  {} - {} - {}",
					itemName, itemState, operator, state);
			switch (operator) {
			case "EQ":
				return itemState.equals(state);
			case "GT":
				if (itemState instanceof DecimalType
						&& state instanceof DecimalType) {
					return ((DecimalType) itemState)
							.compareTo((DecimalType) state) > 0 ? true : false;
				}
				break;
			case "LT":
				if (itemState instanceof DecimalType
						&& state instanceof DecimalType) {
					return ((DecimalType) itemState)
							.compareTo((DecimalType) state) < 0 ? true : false;
				}
				break;
			default:
				break;
			}

			return false;
		} catch (ItemNotFoundException e) {
			return false;
		}
	}

	@Override
	public String getName() {
		return "state";
	}

}
