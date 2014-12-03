package org.eclipse.smarthome.io.sse.listeners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.ThingRegistryChangeListener;
import org.eclipse.smarthome.core.thing.link.ItemChannelLinkRegistry;
import org.eclipse.smarthome.io.rest.core.thing.ThingResource;
import org.eclipse.smarthome.io.rest.core.thing.beans.ThingBean;
import org.eclipse.smarthome.io.sse.EventType;
import org.eclipse.smarthome.io.sse.SseResource;

public class ThingRegistryEventListener implements ThingRegistryChangeListener {

    private ThingRegistry thingRegistry;

    private ItemChannelLinkRegistry itemChannelLinkRegistry;

    private SseResource sseResource;

    protected void setItemChannelLinkRegistry(ItemChannelLinkRegistry itemChannelLinkRegistry) {
        this.itemChannelLinkRegistry = itemChannelLinkRegistry;
    }

    protected void unsetItemChannelLinkRegistry(ItemChannelLinkRegistry itemChannelLinkRegistry) {
        this.itemChannelLinkRegistry = null;
    }

    protected void setSseResource(SseResource sseResource) {
        this.sseResource = sseResource;
    }

    protected void unsetSseResource(SseResource sseResource) {
        this.sseResource = null;
    }

    protected void setThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = thingRegistry;
        this.thingRegistry.addRegistryChangeListener(this);
    }

    protected void unsetThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry.removeRegistryChangeListener(this);
        this.thingRegistry = null;
    }

    @Override
    public void added(Thing element) {
        String eventMessage = "New thing was added to the thing registry: " + element.getUID();

        broadcastThingEvent(eventMessage, EventType.THING_ADDED, element);

    }

    @Override
    public void removed(Thing element) {
        String eventMessage = "Thing was removed from the thing registry: " + element.getUID();

        broadcastThingEvent(eventMessage, EventType.THING_REMOVED, element);
    }

    @Override
    public void updated(Thing oldElement, Thing element) {
        String eventMessage = "Thing was updated in the thing registry: " + element.getUID();

        broadcastThingEvent(eventMessage, EventType.THING_UPDATED, element);
    }

    private void broadcastThingEvent(String eventMessage, EventType eventType, Thing... elements) {
        Object eventObject = null;
        if (elements != null && elements.length > 0) {
            List<ThingBean> thingBeans = new ArrayList<ThingBean>();

            for (Thing thing : elements) {
                thingBeans.add(ThingResource.convertToThingBean(thing, itemChannelLinkRegistry));
            }

            eventObject = thingBeans;
        }

        sseResource.broadcastEvent(eventMessage, eventType, eventObject);
    }

}
