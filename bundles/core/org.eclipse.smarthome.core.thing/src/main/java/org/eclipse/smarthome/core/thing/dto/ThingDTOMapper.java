/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.dto;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.smarthome.core.items.GroupItem;
import org.eclipse.smarthome.core.items.dto.GroupItemDTO;
import org.eclipse.smarthome.core.items.dto.ItemDTOMapper;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.Thing;

/**
 * The {@link ThingDTOMapper} is an utility class to map things into thing beans.
 */
public class ThingDTOMapper {
    
    /**
     * Maps thing into thing bean object.
     * 
     * @param thing the thing
     * @return the thing bean object
     */
    public static ThingDTO mapThingToBean(Thing thing) {
        return mapThingToBean(thing, null);
    }

    /**
     * Maps thing into thing bean object.
     * 
     * @param thing the thing
     * @param uri the uri
     * @return the thing bean object
     */
    public static ThingDTO mapThingToBean(Thing thing, URI uri) {
        List<ChannelDTO> channelBeans = new ArrayList<>();
        for (Channel channel : thing.getChannels()) {
            ChannelDTO channelBean = ChannelDTOMapper.mapChannelToBean(channel);
            channelBeans.add(channelBean);
        }

        String thingUID = thing.getUID().toString();
        String bridgeUID = thing.getBridgeUID() != null ? thing.getBridgeUID().toString() : null;

        GroupItem groupItem = thing.getLinkedItem();
        GroupItemDTO groupItemBean = groupItem != null ? (GroupItemDTO) ItemDTOMapper.mapItemToBean(groupItem, true, uri)
                : null;

        return new ThingDTO(thingUID, bridgeUID, thing.getStatusInfo(), channelBeans, thing.getConfiguration(),
                thing.getProperties(), groupItemBean);
    }
    
}
