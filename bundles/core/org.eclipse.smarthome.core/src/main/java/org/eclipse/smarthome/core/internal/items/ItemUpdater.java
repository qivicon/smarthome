/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.internal.items;

import java.util.Set;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFilter;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.eclipse.smarthome.core.events.TopicEventFilter;
import org.eclipse.smarthome.core.items.GenericItem;
import org.eclipse.smarthome.core.items.GroupItem;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemNotFoundException;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.items.events.ItemCommandEvent;
import org.eclipse.smarthome.core.items.events.ItemUpdateEvent;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

/**
 * The ItemUpdater listens on the event bus and passes any received status update
 * to the item registry.
 *
 * @author Kai Kreuzer - Initial contribution and API
 * @author Stefan Bußweiler - Migration to new ESH event concept
 */
public class ItemUpdater implements EventSubscriber {

    private final Logger logger = LoggerFactory.getLogger(ItemUpdater.class);

    protected ItemRegistry itemRegistry;

    public void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    public void unsetItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = null;
    }

    @Override
    public Set<String> getSubscribedEventTypes() {
        return ImmutableSet.of(ItemCommandEvent.TYPE, ItemUpdateEvent.TYPE);
    }

    @Override
    public EventFilter getEventFilter() {
        return new TopicEventFilter("smarthome/*");
    }

    @Override
    public void receive(Event event) {
        if (event instanceof ItemUpdateEvent) {
            ItemUpdateEvent itemUpdateEvent = (ItemUpdateEvent) event;
            receiveUpdate(itemUpdateEvent.getItemName(), itemUpdateEvent.getItemState());
        } else if (event instanceof ItemCommandEvent) {
            ItemCommandEvent itemCommandEvent = (ItemCommandEvent) event;
            receiveCommand(itemCommandEvent.getItemName(), itemCommandEvent.getItemCommand());
        }
    }

    public void receiveUpdate(String itemName, State newStatus) {
        if (itemRegistry != null) {
            try {
                GenericItem item = (GenericItem) itemRegistry.getItem(itemName);
                boolean isAccepted = false;
                if (item.getAcceptedDataTypes().contains(newStatus.getClass())) {
                    isAccepted = true;
                } else {
                    // Look for class hierarchy
                    for (Class<? extends State> state : item.getAcceptedDataTypes()) {
                        try {
                            if (!state.isEnum()
                                    && state.newInstance().getClass().isAssignableFrom(newStatus.getClass())) {
                                isAccepted = true;
                                break;
                            }
                        } catch (InstantiationException e) {
                            logger.warn("InstantiationException on {}", e.getMessage()); // Should never happen
                        } catch (IllegalAccessException e) {
                            logger.warn("IllegalAccessException on {}", e.getMessage()); // Should never happen
                        }
                    }
                }
                if (isAccepted) {
                    item.setState(newStatus);
                } else {
                    logger.debug("Received update of a not accepted type (" + newStatus.getClass().getSimpleName()
                            + ") for item " + itemName);
                }
            } catch (ItemNotFoundException e) {
                logger.debug("Received update for non-existing item: {}", e.getMessage());
            }
        }
    }

    public void receiveCommand(String itemName, Command command) {
        // if the item is a group, we have to pass the command to it as it needs to pass the command to its members
        if (itemRegistry != null) {
            try {
                Item item = itemRegistry.getItem(itemName);
                if (item instanceof GroupItem) {
                    GroupItem groupItem = (GroupItem) item;
                    groupItem.send(command);
                }
            } catch (ItemNotFoundException e) {
                logger.debug("Received command for non-existing item: {}", e.getMessage());
            }
        }
    }

}
