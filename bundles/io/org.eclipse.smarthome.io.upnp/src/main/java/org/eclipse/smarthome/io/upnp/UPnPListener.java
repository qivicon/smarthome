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
 * Subscribe to this listener, if you want to be informed about changes in the
 * UPnP devices.
 * 
 * @author Andre Fuechsel
 */
public interface UPnPListener {

    void deviceAdded(UPnPDevice device);

    void deviceRemoved(UPnPDevice device);

    void deviceUpdated(UPnPDevice device);

}
