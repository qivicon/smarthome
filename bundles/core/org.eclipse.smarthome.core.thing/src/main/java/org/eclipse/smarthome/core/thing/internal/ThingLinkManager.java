package org.eclipse.smarthome.core.thing.internal;

import java.util.List;
import java.util.Set;

import org.eclipse.smarthome.core.common.registry.RegistryChangeListener;
import org.eclipse.smarthome.core.items.GroupItem;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemNotFoundException;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.link.ItemChannelLink;
import org.eclipse.smarthome.core.thing.link.ItemChannelLinkRegistry;
import org.eclipse.smarthome.core.thing.link.ItemThingLink;
import org.eclipse.smarthome.core.thing.link.ItemThingLinkRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThingLinkManager {

    private ItemChannelLinkRegistry itemChannelLinkRegistry;

    private final RegistryChangeListener<ItemChannelLink> itemChannelLinkRegistryChangeListener = new RegistryChangeListener<ItemChannelLink>() {

        @Override
        public void added(ItemChannelLink itemChannelLink) {
            ChannelUID channelUID = itemChannelLink.getUID();
            Thing thing = thingRegistry.get(channelUID.getThingUID());
            if (thing != null) {
                Channel channel = thing.getChannel(channelUID.getId());
                if (channel != null) {
                    ThingLinkManager.this.addLinkedItemToChannel(channel, itemChannelLink.getItemName());
                }
            }
        }

        @Override
        public void removed(ItemChannelLink itemChannelLink) {
            ChannelUID channelUID = itemChannelLink.getUID();
            Thing thing = thingRegistry.get(channelUID.getThingUID());
            if (thing != null) {
                Channel channel = thing.getChannel(channelUID.getId());
                if (channel != null) {
                    ThingLinkManager.this.removeLinkedItemFromChannel(channel, itemChannelLink.getItemName());
                }
            }
        }

        @Override
        public void updated(ItemChannelLink oldElement, ItemChannelLink element) {
            this.removed(oldElement);
            this.added(element);
        }

    };
    private ItemRegistry itemRegistry;
    private ItemThingLinkRegistry itemThingLinkRegistry;
    private final RegistryChangeListener<ItemThingLink> itemThingLinkRegistryChangeListener = new RegistryChangeListener<ItemThingLink>() {

        @Override
        public void added(ItemThingLink itemThingLink) {
            Thing thing = thingRegistry.get(itemThingLink.getUID());
            if (thing != null) {
                addLinkedItemToThing((ThingImpl) thing, itemThingLink.getItemName());
            }
        }

        @Override
        public void removed(ItemThingLink itemThingLink) {
            Thing thing = thingRegistry.get(itemThingLink.getUID());
            if (thing != null) {
                removeLinkedItemFromThing((ThingImpl) thing);
            }
        }

        @Override
        public void updated(ItemThingLink olditemThingLink, ItemThingLink itemThingLink) {
            added(itemThingLink);
        }

    };

    private Logger logger = LoggerFactory.getLogger(ThingLinkManager.class);
    private ThingRegistry thingRegistry;

    public ThingLinkManager(ItemRegistry itemRegistry, ThingRegistry thingRegistry,
            ItemChannelLinkRegistry itemChannelLinkRegistry, ItemThingLinkRegistry itemThingLinkRegistry) {
        this.itemRegistry = itemRegistry;
        this.thingRegistry = thingRegistry;
        this.itemChannelLinkRegistry = itemChannelLinkRegistry;
        this.itemThingLinkRegistry = itemThingLinkRegistry;
    }

    public void startListening() {
        itemChannelLinkRegistry.addRegistryChangeListener(itemChannelLinkRegistryChangeListener);
        itemThingLinkRegistry.addRegistryChangeListener(itemThingLinkRegistryChangeListener);
    }

    public void stopListening() {
        itemChannelLinkRegistry.removeRegistryChangeListener(itemChannelLinkRegistryChangeListener);
        itemThingLinkRegistry.removeRegistryChangeListener(itemThingLinkRegistryChangeListener);
    }

    public void thingAdded(Thing thing) {
        String itemName = itemThingLinkRegistry.getFirstLinkedItem(thing.getUID());
        if (itemName != null) {
            addLinkedItemToThing((ThingImpl) thing, itemName);
        }
        List<Channel> channels = thing.getChannels();
        for (Channel channel : channels) {
            Set<String> linkedItems = itemChannelLinkRegistry.getLinkedItems(channel.getUID());
            for(String linkedItem: linkedItems) {
                addLinkedItemToChannel(channel, linkedItem);
            }
        }
    }

    public void thingRemoved(Thing thing) {
        removeLinkedItemFromThing((ThingImpl) thing);
        List<Channel> channels = thing.getChannels();
        for (Channel channel : channels) {
            Set<String> linkedItems = itemChannelLinkRegistry.getLinkedItems(channel.getUID());
            for(String linkedItem: linkedItems) {
                removeLinkedItemFromChannel(channel, linkedItem);
            }
        }
    }

    public void thingUpdated(Thing thing) {
        // TODO: better implement a diff!
        thingRemoved(thing);
        thingAdded(thing);
    }

    private void addLinkedItemToChannel(Channel channel, String itemName) {
        try {
            Item item = itemRegistry.getItem(itemName);
            logger.debug("Adding linked item '{}' to channel '{}'.", item.getName(), channel.getUID());
            channel.addLinkedItem(item);
        } catch (ItemNotFoundException ignored) {
        }
    }

    private void addLinkedItemToThing(ThingImpl thing, String itemName) {
        try {
            Item item = itemRegistry.getItem(itemName);
            if (item instanceof GroupItem) {
                logger.debug("Assigning linked group item '{}' to thing '{}'.", item.getName(), thing.getUID());
                thing.setLinkedItem((GroupItem) item);
            }
        } catch (ItemNotFoundException ignored) {
        }
    }

    private void removeLinkedItemFromChannel(Channel channel, String itemName) {
        try {
            Item item = itemRegistry.getItem(itemName);
            logger.debug("Removing linked item '{}' from channel '{}'.", item.getName(), channel.getUID());
            channel.removeLinkedItem(item);
        } catch (ItemNotFoundException ignored) {
        }
    }

    private void removeLinkedItemFromThing(ThingImpl thing) {
        logger.debug("Removing linked group item from thing '{}'.", thing.getUID());
        thing.setLinkedItem(null);
    }
}
