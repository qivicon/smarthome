package org.eclipse.smarthome.core.thing.internal;

import java.util.Locale;
import java.util.Set;

import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.link.ItemChannelLinkRegistry;
import org.eclipse.smarthome.core.thing.type.ChannelType;
import org.eclipse.smarthome.core.thing.type.ThingTypeRegistry;
import org.eclipse.smarthome.core.types.StateDescription;
import org.eclipse.smarthome.core.types.StateDescriptionProvider;

public class ChannelStateDescriptionProvider implements StateDescriptionProvider {

    private ItemChannelLinkRegistry itemChannelLinkRegistry;
    private ThingTypeRegistry thingTypeRegistry;

    @Override
    public StateDescription getStateDescription(String itemName, Locale locale) {
        Set<ChannelUID> boundChannels = itemChannelLinkRegistry.getBoundChannels(itemName);
        if (!boundChannels.isEmpty()) {
            ChannelUID channelUID = boundChannels.iterator().next();
            ChannelType channelType = thingTypeRegistry.getChannelType(channelUID, locale);
            return channelType != null ? channelType.getState() : null;
        } else { 
            return null;
        }
    }

    protected void setThingTypeRegistry(ThingTypeRegistry thingTypeRegistry) {
        this.thingTypeRegistry = thingTypeRegistry;
    }

    protected void unsetThingTypeRegistry(ThingTypeRegistry thingTypeRegistry) {
        this.thingTypeRegistry = null;
    }

    protected void setItemChannelLinkRegistry(ItemChannelLinkRegistry itemChannelLinkRegistry) {
        this.itemChannelLinkRegistry = itemChannelLinkRegistry;
    }

    protected void unsetItemChannelLinkRegistry(ItemChannelLinkRegistry itemChannelLinkRegistry) {
        this.itemChannelLinkRegistry = null;
    }

}
