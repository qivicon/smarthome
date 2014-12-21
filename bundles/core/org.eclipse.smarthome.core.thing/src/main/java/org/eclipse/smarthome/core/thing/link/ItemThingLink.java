package org.eclipse.smarthome.core.thing.link;

import org.eclipse.smarthome.core.thing.ThingUID;

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
