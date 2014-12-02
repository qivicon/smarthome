package org.eclipse.smarthome.io.sse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.items.ItemRegistryChangeListener;
import org.eclipse.smarthome.io.rest.RESTResource;
import org.eclipse.smarthome.io.rest.core.item.ItemResource;
import org.eclipse.smarthome.io.rest.core.item.beans.ItemBean;
import org.eclipse.smarthome.io.sse.util.SseUtil;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 * SSE Resource for pushing notable smarthome events to currently listening
 * clients.
 * 
 * @author ivan.iliev
 * 
 */
@Path("events")
@Singleton
public class SseResource implements RESTResource, ItemRegistryChangeListener {
    private SseBroadcaster broadcaster = new SseBroadcaster();

    private ItemRegistry itemRegistry;

    @Context
    UriInfo uriInfo;

    public void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
        this.itemRegistry.addRegistryChangeListener(this);
    }

    public void unsetItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry.removeRegistryChangeListener(this);
        this.itemRegistry = null;
    }

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getServerSentEvents() {
        final EventOutput eventOutput = new EventOutput();
        broadcaster.add(eventOutput);
        return eventOutput;
    }

    /**
     * Broadcasts an event described by the given parameters to all currently
     * listening clients.
     * 
     * @param eventMessage
     *            - event description message
     * @param eventType
     *            - event type
     * @param eventObject
     *            - bean that can be converted to a JSON object.
     */
    public void broadcastEvent(String eventMessage, String eventType, Object eventObject) {
        broadcaster.broadcast(SseUtil.buildEvent(eventType, eventMessage, eventObject));
    }

    @Override
    public void added(Item element) {
        String eventMessage = "New item was added: " + element.getName();
        String eventType = "ITEM_ADDED";

        broadcastItemEvent(eventMessage, eventType, element);
    }

    @Override
    public void removed(Item element) {
        String eventMessage = "Item was removed: " + element.getName();
        String eventType = "ITEM_REMOVED";

        broadcastItemEvent(eventMessage, eventType, element);
    }

    @Override
    public void updated(Item oldElement, Item element) {
        String eventMessage = "Item was updated: " + element.getName();
        String eventType = "ITEM_UPDATED";

        broadcastItemEvent(eventMessage, eventType, oldElement, element);
    }

    @Override
    public void allItemsChanged(Collection<String> oldItemNames) {

    }

    private void broadcastItemEvent(String eventMessage, String eventType, Item... elements) {
        Object eventObject = null;
        if (elements != null && elements.length > 0) {
            List<ItemBean> itemBeans = new ArrayList<ItemBean>();

            for (Item item : elements) {
                itemBeans.add(ItemResource.createItemBean(item, false, uriInfo.getBaseUri()
                        .toString()));
            }

            eventObject = itemBeans;
        }

        broadcastEvent(eventMessage, eventType, eventObject);
    }
}
