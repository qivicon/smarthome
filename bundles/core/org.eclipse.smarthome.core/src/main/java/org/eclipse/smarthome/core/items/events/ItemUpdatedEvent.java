/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.items.events;

import org.eclipse.smarthome.core.items.Item;

/**
 * An {@link ItemUpdatedEvent} notifies subscribers that an item has been updated.
 *
 * @author Stefan Bußweiler - Initial contribution
 */
public class ItemUpdatedEvent extends AbstractItemRegistryEvent {

    private final Item oldItem;
    
    /**
     * The item updated event type.
     */
    public final static String TYPE = ItemUpdatedEvent.class.getSimpleName();

    /**
     * Constructs a new item updated event object.
     *
     * @param topic the topic
     * @param payload the payload
     * @param item the item
     * @param oldItem the old item
     */
    protected ItemUpdatedEvent(String topic, String payload, Item item, Item oldItem) {
        super(topic, payload, null, item);
        this.oldItem = oldItem;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    /**
     * Gets the old item.
     * 
     * @return the oldItem
     */
    public Item getOldItem() {
        return oldItem;
    }

    @Override
    public String toString() {
        return "Item '" + getItem().getName() + "' has been updated.";
    }

}
