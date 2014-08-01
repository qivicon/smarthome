package org.eclipse.smarthome.io.upnp;

import org.fourthline.cling.model.meta.Device;

public interface UpnpListener {

    void deviceAdded(Device device);

    void deviceRemoved(Device device);

    void deviceUpdated(Device device);

}
