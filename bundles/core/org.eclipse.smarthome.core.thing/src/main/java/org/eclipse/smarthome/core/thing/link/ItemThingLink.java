package org.eclipse.smarthome.core.thing.link;

import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.thing.ThingUID;

/**
 * {@link ItemThingLink} defines a link between an {@link Item} and a
 * {@link Think}.
 * 
 * @author Dennis Nobel - Initial contribution
 */
public class ItemThingLink extends AbstractLink {

    private final ThingUID thingUID;

    ItemThingLink() {
        super();
        this.thingUID = null;
    }

    public ItemThingLink(String itemName, ThingUID thingUID) {
        super(itemName);
        this.thingUID = thingUID;
    }
    
    @Override
    public ThingUID getUID() {
        return thingUID;
    }
    
}
