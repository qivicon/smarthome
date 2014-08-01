/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.upnp.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.io.upnp.UpnpFilter;
import org.eclipse.smarthome.io.upnp.UpnpListener;
import org.eclipse.smarthome.io.upnp.UpnpService;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Andre Fuechsel
 * 
 */
public class UpnpServiceImpl extends DefaultRegistryListener implements UpnpService {

    private static Logger logger = LoggerFactory.getLogger(UpnpServiceImpl.class);
	
    private org.fourthline.cling.UpnpService upnpService;
    private Map<UpnpListener, UpnpFilter> upnpListeners = new HashMap<>();

    protected void activate(ComponentContext componentContext) {
        logger.debug("UPnP search has been initiated");
        upnpService = new org.fourthline.cling.UpnpServiceImpl(new EshUpnpServiceConfiguration(), this);
        upnpService.getControlPoint().search();
    }

    protected void deactivate(ComponentContext componentContext) {
        if (upnpService != null) {
            upnpService.shutdown();
        }
        upnpService = null;
        logger.debug("UPnP search has been disabled");
    }

    @Override
    public void deviceAdded(Registry registry, Device device) {
        logger.debug("ADDED: {}", device.toString());
        for (UpnpListener upnpListener : upnpListeners.keySet()) {
            if (upnpListeners.get(upnpListener).apply(device)) {
                upnpListener.deviceAdded(device);
            }
        }
    }

    @Override
    public void deviceRemoved(Registry registry, Device device) {
        logger.debug("REMOVED: {}", device.toString());
        for (UpnpListener upnpListener : upnpListeners.keySet()) {
            if (upnpListeners.get(upnpListener).apply(device)) {
                upnpListener.deviceRemoved(device);
            }
        }
    }

    @Override
    public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
        logger.debug("UPDATED: {}", device.toString());
        for (UpnpListener upnpListener : upnpListeners.keySet()) {
            if (upnpListeners.get(upnpListener).apply(device)) {
                upnpListener.deviceUpdated(device);
            }
        }
    }

    @Override
    public synchronized void addUpnpListener(UpnpFilter upnpFilter, UpnpListener upnpListener) {
        upnpListeners.put(upnpListener, upnpFilter);
    }

    @Override
    public synchronized void removeUpnpListener(UpnpListener upnpListener) {
        upnpListeners.remove(upnpListener);
    }

    @Override
    public void triggerSearch() {
        upnpService.getControlPoint().search();
    }

}
