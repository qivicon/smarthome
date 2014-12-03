package org.eclipse.smarthome.io.sse.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.items.ItemRegistryChangeListener;
import org.eclipse.smarthome.io.rest.core.item.ItemResource;
import org.eclipse.smarthome.io.rest.core.item.beans.ItemBean;
import org.eclipse.smarthome.io.sse.EventType;
import org.eclipse.smarthome.io.sse.SseResource;

public class ItemRegistryEventListener implements ItemRegistryChangeListener {

    private ItemRegistry itemRegistry;

    private SseResource sseResource;

    protected void setSseResource(SseResource sseResource) {
        this.sseResource = sseResource;
    }

    protected void unsetSseResource(SseResource sseResource) {
        this.sseResource = null;
    }

    protected void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
        this.itemRegistry.addRegistryChangeListener(this);
    }

    protected void unsetItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry.removeRegistryChangeListener(this);
        this.itemRegistry = null;
    }

    @Override
    public void added(Item element) {
        String eventMessage = "New item was added to the item registry: " + element.getName();

        broadcastItemEvent(eventMessage, EventType.ITEM_ADDED, element);
    }

    @Override
    public void removed(Item element) {
        String eventMessage = "Item was removed from the item registry : " + element.getName();

        broadcastItemEvent(eventMessage, EventType.ITEM_REMOVED, element);
    }

    @Override
    public void updated(Item oldElement, Item element) {
        String eventMessage = "Item was updated in the item registry: " + element.getName();

        broadcastItemEvent(eventMessage, EventType.ITEM_UPDATED, oldElement, element);
    }

    @Override
    public void allItemsChanged(Collection<String> oldItemNames) {

    }

    private void broadcastItemEvent(String eventMessage, EventType eventType, Item... elements) {
        Object eventObject = null;
        if (elements != null && elements.length > 0) {
            List<ItemBean> itemBeans = new ArrayList<ItemBean>();

            for (Item item : elements) {
                itemBeans.add(ItemResource.createItemBean(item, false, "/"));
            }

            eventObject = itemBeans;
        }

        sseResource.broadcastEvent(eventMessage, eventType, eventObject);
    }

}
