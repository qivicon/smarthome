/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.items.events;

import org.eclipse.smarthome.core.events.AbstractEvent;
import org.eclipse.smarthome.core.items.Item;

/**
 * Abstract implementation of an item registry event.
 *
 * @author Stefan Bußweiler - Initial contribution
 */
public abstract class AbstractItemRegistryEvent extends AbstractEvent {

    private final Item item;

    /**
     * Must be called in subclass constructor to create a new item registry event.
     *
     * @param topic the topic
     * @param payload the payload
     * @param source the source
     */
    public AbstractItemRegistryEvent(String topic, String payload, String source, Item item) {
        super(topic, payload, source);
        this.item = item;
    }

    /**
     * Gets the item.
     * 
     * @return the item
     */
    public Item getItem() {
        return item;
    }

}
