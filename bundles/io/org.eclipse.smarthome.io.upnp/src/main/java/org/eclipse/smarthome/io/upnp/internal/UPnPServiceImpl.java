/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.upnp.internal;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.io.upnp.UPnPDeviceFilter;
import org.eclipse.smarthome.io.upnp.UPnPListener;
import org.eclipse.smarthome.io.upnp.UPnPService;
import org.eclipse.smarthome.io.upnp.device.UPnPDeviceImpl;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.upnp.UPnPDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Andre Fuechsel
 * 
 */
@SuppressWarnings("rawtypes")
public class UPnPServiceImpl extends DefaultRegistryListener implements UPnPService {

    private static Logger logger = LoggerFactory.getLogger(UPnPServiceImpl.class);
	
    private org.fourthline.cling.UpnpService upnpService;
    private Map<UPnPListener, UPnPDeviceFilter> upnpListeners = new HashMap<>();

    protected void activate(ComponentContext componentContext) {
        logger.debug("UPnP search has been initiated");
        upnpService = new org.fourthline.cling.UpnpServiceImpl(new EshUPnPServiceConfiguration(), this);
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
        UPnPDevice upnpDevice = new UPnPDeviceImpl(device);
        Dictionary deviceDescritions = upnpDevice.getDescriptions(null);
        for (UPnPListener upnpListener : upnpListeners.keySet()) {
            if (upnpListeners.get(upnpListener).apply(deviceDescritions)) {
                upnpListener.deviceAdded(upnpDevice);
            }
        }
    }

    @Override
    public void deviceRemoved(Registry registry, Device device) {
        logger.debug("REMOVED: {}", device.toString());
        UPnPDevice upnpDevice = new UPnPDeviceImpl(device);
        Dictionary deviceDescritions = upnpDevice.getDescriptions(null);
        for (UPnPListener upnpListener : upnpListeners.keySet()) {
            if (upnpListeners.get(upnpListener).apply(deviceDescritions)) {
                upnpListener.deviceRemoved(new UPnPDeviceImpl(device));
            }
        }
    }

    @Override
    public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
        logger.debug("UPDATED: {}", device.toString());
        UPnPDevice upnpDevice = new UPnPDeviceImpl(device);
        Dictionary deviceDescritions = upnpDevice.getDescriptions(null);
        for (UPnPListener upnpListener : upnpListeners.keySet()) {
            if (upnpListeners.get(upnpListener).apply(deviceDescritions)) {
                upnpListener.deviceUpdated(new UPnPDeviceImpl(device));
            }
        }
    }

    @Override
    public synchronized void addUpnpListener(UPnPDeviceFilter upnpFilter, UPnPListener upnpListener) {
        upnpListeners.put(upnpListener, upnpFilter);
    }

    @Override
    public synchronized void removeUpnpListener(UPnPListener upnpListener) {
        upnpListeners.remove(upnpListener);
    }

    @Override
    public void triggerSearch() {
        upnpService.getControlPoint().search();
    }

}
