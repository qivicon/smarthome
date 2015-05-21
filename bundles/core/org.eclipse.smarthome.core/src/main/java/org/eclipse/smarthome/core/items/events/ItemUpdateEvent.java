/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.items.events;

import org.eclipse.smarthome.core.events.AbstractEvent;
import org.eclipse.smarthome.core.types.State;

/**
 * {@link ItemUpdateEvent}s can be used to deliver item status updates through the Eclipse SmartHome event bus.
 * Update events must be created with the {@link ItemEventFactory}.
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public class ItemUpdateEvent extends AbstractEvent {

    /**
     * The item update event type.
     */
    public final static String TYPE = ItemUpdateEvent.class.getSimpleName();

    private final String itemName;

    private final State itemState;
    
    private final String source;

    /**
     * Constructs a new item update event.
     * 
     * @param topic the topic
     * @param payload the payload
     * @param itemName the item name
     * @param itemState the item state
     * @param source the source
     */
    public ItemUpdateEvent(String topic, String payload, String itemName, State itemState, String source) {
        super(topic, payload);
        this.itemName = itemName;
        this.itemState = itemState;
        this.source = source;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    /**
     * Gets the item name.
     * 
     * @return the item name
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Gets the item state.
     * 
     * @return the item state
     */
    public State getItemState() {
        return itemState;
    }
    
    /**
     * Gets the source of the item command.
     * 
     * @return the source
     */
    public String getSource() {
        return source;
    }

}
