/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.items.bean;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.eclipse.smarthome.core.items.GroupItem;
import org.eclipse.smarthome.core.items.Item;

/**
 * The {@link ItemBeanMapper} is an utility class to map items into item beans.
 */
public class ItemBeanMapper {
    
    /**
     * Maps item into item bean object.
     * 
     * @param item the item
     * @param drillDown the drill down
     * @param uri the uri
     * @return item bean object
     */
    public static ItemBean mapItemToBean(Item item, boolean drillDown, URI uri) {
        ItemBean bean = item instanceof GroupItem ? new GroupItemBean() : new ItemBean();
        fillProperties(bean, item, drillDown, uri);
        return bean;
    }
    
    private static void fillProperties(ItemBean bean, Item item, boolean drillDown, URI uri) {
        if (item instanceof GroupItem && drillDown) {
            GroupItem groupItem = (GroupItem) item;
            Collection<ItemBean> members = new LinkedHashSet<ItemBean>();
            for (Item member : groupItem.getMembers()) {
                members.add(mapItemToBean(member, drillDown, uri));
            }
            ((GroupItemBean) bean).members = members.toArray(new ItemBean[members.size()]);
        }
        bean.name = item.getName();
        bean.state = item.getState().toString();
        bean.type = item.getClass().getSimpleName();
        if (uri != null) {
            String fullPath = uri.toASCIIString() + "/items/" + bean.name;
            bean.link = URI.create(fullPath).toASCIIString();
        }
        bean.label = item.getLabel();
        bean.tags = item.getTags();
        bean.category = item.getCategory();
        bean.stateDescription = item.getStateDescription();
        bean.groupNames = item.getGroupNames();
    }
}
