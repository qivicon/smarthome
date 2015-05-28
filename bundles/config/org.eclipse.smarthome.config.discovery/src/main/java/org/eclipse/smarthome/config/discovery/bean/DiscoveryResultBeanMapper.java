/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.discovery.bean;

import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.core.thing.ThingUID;

/**
 * The {@link DiscoveryResultBeanMapper} is an utility class to map discovery results into discovery result beans.
 */
public class DiscoveryResultBeanMapper {
    
    /**
     * Maps discovery result into discovery result bean object.
     * 
     * @param discoveryResult the discovery result
     * @return the discovery result bean object
     */
    public static DiscoveryResultBean mapDiscoveryResultToBean(DiscoveryResult discoveryResult) {
        ThingUID thingUID = discoveryResult.getThingUID();
        ThingUID bridgeUID = discoveryResult.getBridgeUID();

        return new DiscoveryResultBean(thingUID.toString(), bridgeUID != null ? bridgeUID.toString() : null,
                discoveryResult.getLabel(), discoveryResult.getFlag(), discoveryResult.getProperties(),
                discoveryResult.getRepresentationProperty());
    }
}
