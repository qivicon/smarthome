package org.eclipse.smarthome.io.upnp.filter;

import java.util.Dictionary;

import org.eclipse.smarthome.io.upnp.UPnPDeviceFilter;
import org.osgi.service.upnp.UPnPDevice;

/**
 * Implements a default filter for D-Link camera DCS-942L.
 * 
 * @author Andre Fuechsel
 * 
 */
public class DLinkDCS942LCamFilter implements UPnPDeviceFilter {

    @Override
    public boolean apply(Dictionary<?, ?> upnpDeviceDescriptions) {
        return upnpDeviceDescriptions.get(UPnPDevice.MODEL_NAME).equals("DCS-942L")
                && upnpDeviceDescriptions.get(UPnPDevice.TYPE).equals("DCS-942L")
                && upnpDeviceDescriptions.get(UPnPDevice.MANUFACTURER).equals(("DLink"));
    }

}
