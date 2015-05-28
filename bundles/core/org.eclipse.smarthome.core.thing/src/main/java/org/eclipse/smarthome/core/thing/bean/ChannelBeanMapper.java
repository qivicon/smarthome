/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.bean;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.thing.Channel;

/**
 * The {@link ChannelBeanMapper} is an utility class to map channels into channel beans.
 */
public class ChannelBeanMapper {
    
    /**
     * Maps channel into channel bean object.
     * 
     * @param channel the channel
     * @return the channel bean object
     */
    public static ChannelBean mapChannelToBean(Channel channel) {
        List<String> linkedItemNames = new ArrayList<>();
        for (Item item : channel.getLinkedItems()) {
            linkedItemNames.add(item.getName());
        }
        return new ChannelBean(channel.getUID().getId(), channel.getAcceptedItemType().toString(), linkedItemNames);
    }
}
