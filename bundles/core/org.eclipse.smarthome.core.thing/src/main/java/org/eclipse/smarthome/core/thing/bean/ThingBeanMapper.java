/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.bean;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.smarthome.core.items.GroupItem;
import org.eclipse.smarthome.core.items.bean.GroupItemBean;
import org.eclipse.smarthome.core.items.bean.ItemBeanMapper;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.Thing;

/**
 * The {@link ThingBeanMapper} is an utility class to map things into thing beans.
 */
public class ThingBeanMapper {
    
    /**
     * Maps thing into thing bean object.
     * 
     * @param thing the thing
     * @return the thing bean object
     */
    public static ThingBean mapThingToBean(Thing thing) {
        return mapThingToBean(thing, null);
    }

    /**
     * Maps thing into thing bean object.
     * 
     * @param thing the thing
     * @param uri the uri
     * @return the thing bean object
     */
    public static ThingBean mapThingToBean(Thing thing, URI uri) {
        List<ChannelBean> channelBeans = new ArrayList<>();
        for (Channel channel : thing.getChannels()) {
            ChannelBean channelBean = ChannelBeanMapper.mapChannelToBean(channel);
            channelBeans.add(channelBean);
        }

        String thingUID = thing.getUID().toString();
        String bridgeUID = thing.getBridgeUID() != null ? thing.getBridgeUID().toString() : null;

        GroupItem groupItem = thing.getLinkedItem();
        GroupItemBean groupItemBean = groupItem != null ? (GroupItemBean) ItemBeanMapper.mapItemToBean(groupItem, true, uri)
                : null;

        return new ThingBean(thingUID, bridgeUID, thing.getStatusInfo(), channelBeans, thing.getConfiguration(),
                thing.getProperties(), groupItemBean);
    }
    
}
