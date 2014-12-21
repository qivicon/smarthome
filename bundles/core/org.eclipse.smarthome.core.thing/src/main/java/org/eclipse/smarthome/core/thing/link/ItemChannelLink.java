/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.link;

import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.thing.ChannelUID;

/**
 * {@link ItemChannelLink} defines a link between an {@link Item} and a
 * {@link Channel}.
 * 
 * @author Dennis Nobel - Initial contribution, Added getIDFor method
 */
public class ItemChannelLink extends AbstractLink {

    private final ChannelUID channelUID;

    ItemChannelLink() {
        super();
        this.channelUID = null;
    }
    
    public ItemChannelLink(String itemName, ChannelUID channelUID) {
        super(itemName);
        this.channelUID = channelUID;
    }

    @Override
    public ChannelUID getUID() {
        return this.channelUID;
    }

}
