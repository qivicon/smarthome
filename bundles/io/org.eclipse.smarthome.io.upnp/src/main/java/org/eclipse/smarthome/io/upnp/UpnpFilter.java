package org.eclipse.smarthome.io.upnp;

import java.util.Map;

import org.fourthline.cling.model.meta.Device;

public class UpnpFilter {

    public static final String PROP_UDN = "UDN";
    public static final String PROP_DEVICE_TYPE = "DEVICE_TYPE";
    public static final String PROP_MODEL_NAME = "MODEL_NAME";
    public static final String PROP_MANUFACTURER = "MANUFACTURER";

    private Map<String, String> deviceProperties;

    public UpnpFilter(Map<String, String> deviceProperties) {
        this.deviceProperties = deviceProperties;
    }

    public boolean apply(Device device) {

        // TODO first only compare special properties
        String udn = deviceProperties.get(PROP_UDN);
        String deviceType = deviceProperties.get(PROP_DEVICE_TYPE);
        String modelName = deviceProperties.get(PROP_MODEL_NAME);
        String manufacturer = deviceProperties.get(PROP_MANUFACTURER);

        if (udn != null && !device.getIdentity().getUdn().equals(udn)) {
            return false;
        }

        if (deviceType != null && !device.getType().equals(deviceType)) {
            return false;
        }

        if (modelName != null && !device.getDetails().getModelDetails().getModelName().equals(modelName)) {
            return false;
        }

        if (manufacturer != null
                && !device.getDetails().getManufacturerDetails().getManufacturer().equals(manufacturer)) {
            return false;
        }

        return true;
    }

}
