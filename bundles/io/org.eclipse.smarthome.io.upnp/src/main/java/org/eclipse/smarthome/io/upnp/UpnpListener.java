package org.eclipse.smarthome.io.upnp;

import org.cybergarage.upnp.Device;

public interface UpnpListener {

    void deviceAdded(Device device);

    void deviceRemoved(Device device);

    void deviceRefreshed(Device device);

}
