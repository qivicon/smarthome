package org.eclipse.smarthome.core.thing.link;

import org.eclipse.smarthome.core.common.registry.DefaultAbstractManagedProvider;

public class ManagedItemThingLinkProvider extends DefaultAbstractManagedProvider<ItemThingLink, String> implements
        ItemThingLinkProvider {

    @Override
    protected String getKey(ItemThingLink element) {
        return element.getID();
    }

    @Override
    protected String getStorageName() {
        return ItemThingLink.class.getName();
    }

    @Override
    protected String keyToString(String key) {
        return key;
    }

}
