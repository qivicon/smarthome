package org.eclipse.smarthome.automation.module.condition;

import java.util.Map;

import org.eclipse.smarthome.automation.core.runtimemodel.IConditionModuleHandler;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemNotFoundException;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.TypeParser;

public class StateConditionHandler implements IConditionModuleHandler {
    
    private ItemRegistry itemRegistry;


    protected void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }
    
    @Override
    public boolean evaluate(Map<String, String> properties) {
        String itemName = properties.get("itemName");
        try {
            Item item = itemRegistry.getItem(itemName);
            State itemState = item.getState();
            String stateString = properties.get("state");
            State state = TypeParser.parseState(item.getAcceptedDataTypes(), stateString);
            String operator = properties.get("operator");
            
            switch (operator) {
            case "EQ":
                return itemState.equals(state);
            case "GT":
                if(itemState instanceof DecimalType && state instanceof DecimalType) {
                    return ((DecimalType) itemState).compareTo((DecimalType) state) > 0 ? true : false;
                }
                break;
            case "LT":
                if(itemState instanceof DecimalType && state instanceof DecimalType) {
                    return ((DecimalType) itemState).compareTo((DecimalType) state) < 0 ? true : false;
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

}
