/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.upnp;

import org.osgi.service.upnp.UPnPDevice;

/**
 * Subscribe to this listener, if you want to be informed about changes of the
 * UPnP devices.
 * 
 * @author Andre Fuechsel - Initial contribution and API
 */
public interface UPnPListener {

    /**
     * Called whenever a UPnP device has been added.
     * 
     * @param device
     *            added device
     */
    void deviceAdded(UPnPDevice device);

    /**
     * Called whenever a UPnP device has been removed.
     * 
     * @param device
     *            removed device
     */
    void deviceRemoved(UPnPDevice device);

    /**
     * Called whenever a UPnP device has been updated.
     * 
     * @param device
     *            updated device
     */
    void deviceUpdated(UPnPDevice device);

}
