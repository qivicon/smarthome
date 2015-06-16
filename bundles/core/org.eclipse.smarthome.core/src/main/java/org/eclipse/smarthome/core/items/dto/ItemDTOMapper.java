/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.items.dto;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.smarthome.core.items.GenericItem;
import org.eclipse.smarthome.core.items.GroupItem;
import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemFactory;

import com.google.common.base.Preconditions;

/**
 * The {@link ItemDTOMapper} is an utility class to map items into item beans.
 */
public class ItemDTOMapper {

    /**
     * Maps item bean into item object.
     *
     * @param bean the bean
     * @param itemFactories the item factories in order to create the items
     * @return the item object
     */
    public static Item mapBeanToItem(ItemDTO bean, Set<ItemFactory> itemFactories) {
        Preconditions.checkArgument(bean != null, "The argument 'bean' must no be null.");
        Preconditions.checkArgument(itemFactories != null, "The argument 'itemFactories' must no be null.");

        GenericItem newItem = null;
        if (bean.type != null) {
            if (bean.type.equals("GroupItem")) {
                newItem = new GroupItem(bean.name);
            } else {
                String itemType = bean.type.substring(0, bean.type.length() - 4);
                for (ItemFactory itemFactory : itemFactories) {
                    newItem = itemFactory.createItem(itemType, bean.name);
                    if (newItem != null) {
                        break;
                    }
                }
            }
            if (newItem != null) {
                newItem.setLabel(bean.label);
                newItem.setCategory(bean.category);
                newItem.addGroupNames(bean.groupNames);
                newItem.addTags(bean.tags);
            }
        }
        return newItem;
    }

    /**
     * Maps item into item bean object.
     * 
     * @param item the item
     * @param drillDown the drill down
     * @param uri the uri
     * @return item bean object
     */
    public static ItemDTO mapItemToBean(Item item, boolean drillDown, URI uri) {
        ItemDTO bean = item instanceof GroupItem ? new GroupItemDTO() : new ItemDTO();
        fillProperties(bean, item, drillDown, uri);
        return bean;
    }
    
    private static void fillProperties(ItemDTO bean, Item item, boolean drillDown, URI uri) {
        if (item instanceof GroupItem && drillDown) {
            GroupItem groupItem = (GroupItem) item;
            Collection<ItemDTO> members = new LinkedHashSet<ItemDTO>();
            for (Item member : groupItem.getMembers()) {
                members.add(mapItemToBean(member, drillDown, uri));
            }
            ((GroupItemDTO) bean).members = members.toArray(new ItemDTO[members.size()]);
        }
        bean.name = item.getName();
        bean.state = item.getState().toString();
        // bean.state = considerTransformation(item.getState().toString(), item.getStateDescription());
        bean.type = item.getClass().getSimpleName();
        if (uri != null) {
            String fullPath = uri.toASCIIString() + "/items/" + bean.name;
            bean.link = URI.create(fullPath).toASCIIString();
        }
        bean.label = item.getLabel();
        bean.tags = item.getTags();
        bean.category = item.getCategory();
        bean.stateDescription = item.getStateDescription();
        // bean.stateDescription = considerTransformation(item.getStateDescription());
        bean.groupNames = item.getGroupNames();
    }

    // private static StateDescription considerTransformation(StateDescription desc) {
    // if (desc == null || desc.getPattern() == null) {
    // return desc;
    // } else {
    // return TransformationHelper.isTransform(desc.getPattern()) ? new StateDescription(desc.getMinimum(),
    // desc.getMaximum(), desc.getStep(), "", desc.isReadOnly(), desc.getOptions()) : desc;
    // }
    // }
    //
    // private static String considerTransformation(String state, StateDescription stateDescription) {
    // if (stateDescription != null && stateDescription.getPattern() != null) {
    // return TransformationHelper.transform(RESTCoreActivator.getBundleContext(), stateDescription.getPattern(),
    // state.toString());
    // } else {
    // return state;
    // }
    // }
}
