/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.upnp;

import java.util.Dictionary;

import org.osgi.service.upnp.UPnPDevice;

/**
 * Implement this class to filter for specific devices.
 * 
 * See {@link UPnPDevice} for property keys.
 * 
 * Example:
 * 
 * <pre>
 * public class DLinkDCS942LCamFilter implements UPnPDeviceFilter {
 * 
 *     &#064;Override
 *     public boolean apply(Dictionary&lt;?, ?&gt; upnpDeviceDescriptions) {
 *         return upnpDeviceDescriptions.get(UPnPDevice.MODEL_NAME).equals(&quot;DCS-942L&quot;)
 *                 &amp;&amp; upnpDeviceDescriptions.get(UPnPDevice.TYPE).equals(&quot;DCS-942L&quot;)
 *                 &amp;&amp; upnpDeviceDescriptions.get(UPnPDevice.MANUFACTURER).equals((&quot;DLink&quot;));
 *     }
 * 
 * }
 * </pre>
 * 
 * @author Alex Tugarev
 * @author Andre Fuechsel
 * 
 */
public interface UPnPDeviceFilter {

    /**
     * Filter is based on specific properties out of the given set of
     * {@code upnpDeviceDescriptions}.
     * 
     * @param upnpDeviceDescriptions
     *            complete set of device descriptions of the default locale as
     *            returned by {@link UPnPDevice#getDescriptions(String)}.
     * @return {@code true}, if filter matches
     */
    boolean apply(Dictionary<?, ?> upnpDeviceDescriptions);
}
