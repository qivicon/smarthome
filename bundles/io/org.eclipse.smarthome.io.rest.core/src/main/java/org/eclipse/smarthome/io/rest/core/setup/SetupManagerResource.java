/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.core.setup;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.core.items.GroupItem;
import org.eclipse.smarthome.core.thing.SetupManager;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.link.ItemChannelLinkRegistry;
import org.eclipse.smarthome.io.rest.RESTResource;
import org.eclipse.smarthome.io.rest.core.thing.beans.ThingBean;
import org.eclipse.smarthome.io.rest.core.util.BeanMapper;

/**
 * This class acts as a REST resource for the setup manager.
 * 
 * @author Dennis Nobel - Initial contribution
 */
@Path("setup")
public class SetupManagerResource implements RESTResource {

    private ItemChannelLinkRegistry itemChannelLinkRegistry;
    private SetupManager setupManager;
    @Context 
    private UriInfo uriInfo;
    
    @POST
    @Path("things")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addThing(ThingBean thingBean) throws IOException {

        ThingUID thingUIDObject = new ThingUID(thingBean.UID);
        ThingUID bridgeUID = null;

        if (thingBean.bridgeUID != null) {
            bridgeUID = new ThingUID(thingBean.bridgeUID);
        }

        Configuration configuration = getConfiguration(thingBean);

        setupManager.addThing(thingUIDObject, configuration, bridgeUID, thingBean.item.label);

        return Response.ok().build();
    }

    @GET
    @Path("things")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getThings() {
        List<ThingBean> thingBeans = new ArrayList<>();
        Map<Thing, GroupItem> things = setupManager.getThings();
        for (Entry<Thing, GroupItem> entry : things.entrySet()) {
            ThingBean thingItemBean = BeanMapper.mapThingToBean(entry.getKey(), itemChannelLinkRegistry,
                    entry.getValue(), uriInfo.getBaseUri().toASCIIString());
            thingBeans.add(thingItemBean);
        }
        return Response.ok(thingBeans).build();
    }
    
    @DELETE
    @Path("/things/{thingUID}")
    public Response remove(@PathParam("thingUID") String thingUID) {
        setupManager.removeThing(new ThingUID(thingUID));
        return Response.ok().build();
    }
    
    @PUT
    @Path("/labels/{itemName}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response setLabel(@PathParam("itemName") String itemName, String label) {
        setupManager.setLabel(itemName, label);
        return Response.ok().build();
    }
    
    protected void setItemChannelLinkRegistry(ItemChannelLinkRegistry itemChannelLinkRegistry) {
        this.itemChannelLinkRegistry = itemChannelLinkRegistry;
    }
 
    protected void setSetupManager(SetupManager setupManager) {
        this.setupManager = setupManager;
    }

    protected void unsetItemChannelLinkRegistry(ItemChannelLinkRegistry itemChannelLinkRegistry) {
        this.itemChannelLinkRegistry = itemChannelLinkRegistry;
    }
    
    protected void unsetSetupManager(SetupManager setupManager) {
        this.setupManager = null;
    }
    
    private Configuration getConfiguration(ThingBean thingBean) {
        Configuration configuration = new Configuration();
        
        for (Entry<String, Object> parameter : thingBean.configuration.entrySet()) {
            String name = parameter.getKey();
            Object value = parameter.getValue();
            configuration.put(name, value instanceof Double ? new BigDecimal((Double) value) : value);
        }
        
        return configuration;
    }

}
