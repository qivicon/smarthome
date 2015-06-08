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
 * An {@link ItemAddedEvent} notifies subscribers that an item has been added.
 *
 * @author Stefan Bußweiler - Initial contribution
 */
public class ItemAddedEvent extends AbstractItemRegistryEvent {

    /**
     * The item added event type.
     */
    public final static String TYPE = ItemAddedEvent.class.getSimpleName();

    protected ItemAddedEvent(String topic, String payload, Item item) {
        super(topic, payload, null, item);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "Item '" + getItem().getName() + "' has been added.";
    }

}
