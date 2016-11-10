/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.binding.hue.internal.discovery;

import static org.eclipse.smarthome.binding.hue.HueBindingConstants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * The {@link HueBridgeNupnpDiscovery} is responsible for discovering new
 * hue bridges. It uses the 'NUPnP service provided by Philips'.
 *
 * @author Awelkiyar Wehabrebi - Initial contribution
 *
 */
public class HueBridgeNupnpDiscovery extends AbstractDiscoveryService {

    private static final String MODEL_NAME_PHILIPS_HUE = "<modelName>Philips hue";

    private static final String BRIDGE_INDICATOR = "fffe";

    private final Logger logger = LoggerFactory.getLogger(HueBridgeNupnpDiscovery.class);

    private static final String URL = "https://www.meethue.com/api/nupnp";

    private static final String LABEL_PATTERN = "Philips hue (IP)";

    private static final int REQUEST_TIMEOUT = 5000;

    private static final int DISCOVERY_TIMEOUT = 10;

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Collections.singleton(THING_TYPE_BRIDGE);

    private static final int BRIDGE_SCAN_INTERVAL = 60;

    private ScheduledFuture<?> bridgeScanningJob;

    public HueBridgeNupnpDiscovery() {
        super(SUPPORTED_THING_TYPES, DISCOVERY_TIMEOUT);
    }

    @Override
    protected void startBackgroundDiscovery() {
        if (bridgeScanningJob == null) {
            this.bridgeScanningJob = scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    discoverHueBridges();
                }
            }, 0, BRIDGE_SCAN_INTERVAL, TimeUnit.SECONDS);
        }
    }

    @Override
    protected void stopBackgroundDiscovery() {
        if (bridgeScanningJob != null) {
            bridgeScanningJob.cancel(true);
            bridgeScanningJob = null;
        }
    }

    @Override
    protected void startScan() {
        discoverHueBridges();
    }

    /**
     * Discover available Hue Bridges and then add them in the discovery inbox
     */
    private void discoverHueBridges() {
        for (BridgeJsonParameters bridge : getBridgeList()) {
            if (isReachableAndValidHueBridge(bridge)) {
                String ip = bridge.getInternalIpAddress();
                String id = bridge.getId().substring(0, 6) + bridge.getId().substring(10);
                ThingUID uid = new ThingUID(THING_TYPE_BRIDGE, id);
                DiscoveryResult result = DiscoveryResultBuilder.create(uid).withProperties(buildProperties(ip, id))
                        .withLabel(LABEL_PATTERN.replace("IP", ip)).withRepresentationProperty(SERIAL_NUMBER).build();
                thingDiscovered(result);
            }
        }
    }

    /**
     * Builds the bridge properties.
     * 
     * @param ip the ip of the bridge
     * @param id the id of the bridge
     * @return the bridge properties
     */
    private Map<String, Object> buildProperties(String ip, String id) {
        Map<String, Object> properties = new HashMap<>(2);
        properties.put(HOST, ip);
        properties.put(SERIAL_NUMBER, id);
        return properties;
    }

    /**
     * Checks if the Bridge is a reachable Hue Bridge with a valid id.
     *
     * @param bridge the {@link BridgeJsonParameters}s
     * @return true if Bridge is a reachable Hue Bridge with a id containing BRIDGE_INDICATOR longer then 10
     */
    private boolean isReachableAndValidHueBridge(BridgeJsonParameters bridge) {
        String ip = bridge.getInternalIpAddress();
        String id = bridge.getId();
        String description;
        if (ip == null) {
            logger.debug("Bridge not discovered: ip is null");
            return false;
        }
        if (id == null) {
            logger.debug("Bridge not discovered: id is null");
            return false;
        }
        if (id.length() < 10) {
            logger.debug("Bridge not discovered: id {} is shorter then 10.", id);
            return false;
        }
        if (id.substring(6, 10).equals(BRIDGE_INDICATOR)) {
            logger.debug("Bridge not discovered: id {} does not contain bridge indicator {}.", id, BRIDGE_INDICATOR);
            return false;
        }
        try {
            String url = "http://" + ip + "/description.xml";
            description = HttpUtil.executeUrl("GET", url, REQUEST_TIMEOUT);
        } catch (IOException e) {
            logger.debug("Bridge not discovered: Failure accessing description file for ip: {}.", ip);
            return false;
        }
        if (!description.contains(MODEL_NAME_PHILIPS_HUE)) {
            logger.debug("Bridge not discovered: Description does not containing the model name: {}.", description);
            return false;
        }
        return true;
    }

    /**
     * Use the Philips Hue NUPnP service to find Hue Bridges in local Network.
     *
     * @return a list of available Hue Bridges
     */
    private List<BridgeJsonParameters> getBridgeList() {
        try {
            Gson gson = new Gson();
            String json = HttpUtil.executeUrl("GET", URL, REQUEST_TIMEOUT);
            return gson.fromJson(json, new TypeToken<List<BridgeJsonParameters>>() {
            }.getType());
        } catch (IOException e) {
            logger.debug("Philips Hue NUPnP service not reachable. Can't discover bridges", e);
        }
        return new ArrayList<BridgeJsonParameters>();
    }

}
