/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.upnp;

import org.fourthline.cling.model.meta.Device;

/**
 * TODO please rename to UPnPListener
 * 
 *
 */
public interface UPnPListener {

    // TODO should we abstract o.f.cling.m.m.Device here and introduce our Device bean? See UPnPDevice in this package
    // It would be nice to hide these types from o.e.smarthome.io.upnp users!

    void deviceAdded(Device device);

    void deviceRemoved(Device device);

    void deviceUpdated(Device device);

}
