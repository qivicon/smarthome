/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.upnp;

import java.util.Map;

/**
 * TODO more doc on this.
 * 
 * See {@link DeviceProperties} for property keys. 
 * 
 * dlinkDCS2332LCamFilter = new UPnPDeviceFilter() {
 *  @override
 *  public boolean apply(Map<String, String> deviceProperties) {
 *      return deviceProperties.get(DeviceProperties.PROP_DEVICE_TYPE).equals("DCS-2332L")
 *          && deviceProperties.get(DeviceProperties.PROP_MODEL_NAME).equals("DCS-2332L")
 *          && deviceProperties.get(DeviceProperties.PROP_MANUFACTURER).equals("D-Link");
 *  }
 * };
 * 
 * @author Alex Tugarev
 * 
 */
public interface UPnPDeviceFilter {

    boolean apply(Map<String, String> deviceProperties);
}
