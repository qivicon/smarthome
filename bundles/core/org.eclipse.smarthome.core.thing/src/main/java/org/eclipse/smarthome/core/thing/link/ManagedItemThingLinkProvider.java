package org.eclipse.smarthome.core.thing.link;

import org.eclipse.smarthome.core.common.registry.DefaultAbstractManagedProvider;

/**
 * 
 * {@link ManagedItemThingLinkProvider} is responsible for managed
 * {@link ItemThingLink}s at runtime.
 * 
 * @author Dennis Nobel - Initial contribution
 * 
 */
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
