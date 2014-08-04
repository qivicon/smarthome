/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.upnp;

/**
 * The {@link UPnPService} provides interfaces to subscribe to UPnP
 * events for specific devices. It also provides a method to trigger a search
 * for specific devices.
 * 
 * @author Andre Fuechsel - Initial contribution and API
 */
public interface UPnPService {
	
    /**
     * Add a listener for UPnP events. This listener will only be triggered, if
     * the device found matches the provided filter.
     * 
     * @param upnpFilter
     *            implementation of {@link UPnPDeviceFilter} to filter for
     *            specific devices
     * @param upnpListener
     *            listener to be called
     */
    void addUpnpListener(UPnPDeviceFilter upnpFilter, UPnPListener upnpListener);

    /**
     * Remove the given listener.
     * 
     * @param upnpListener
     *            listener to be removed
     */
    void removeUpnpListener(UPnPListener upnpListener);
    
    void triggerSearch();
    
}
