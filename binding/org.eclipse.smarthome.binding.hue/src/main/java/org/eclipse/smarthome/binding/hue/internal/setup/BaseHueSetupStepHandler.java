/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.binding.hue.internal.setup;

import java.io.IOException;
import java.net.InetAddress;

import nl.q42.jue.HueBridge;
import nl.q42.jue.exceptions.ApiException;

import org.eclipse.smarthome.config.setupflow.BaseSetupStepHandler;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.osgi.framework.BundleContext;

/**
 * The {@link BaseHueSetupStepHandler} provides some basic features of hue step
 * handlers. This class might become obsolete after
 * {@link ThingRegistry#createThingForConfiguration(String, java.util.Map)} and
 * the hue bulb pairing has been implemented.
 * 
 * @author Oliver Libutzki - Initial contribution
 * 
 */
public abstract class BaseHueSetupStepHandler extends BaseSetupStepHandler {

    public BaseHueSetupStepHandler(BundleContext bundleContext) {
        super(bundleContext);
    }

    /*
     * The method checks if a given ip address is reachable.
     * 
     * @param ipAddress the ip address to be checked
     * 
     * @return true if the ip address is reachable
     */
    protected boolean isReachable(String ipAddress) {
        // check if the ip address is reachable
        boolean ipAddressIsReachable = false;
        try {
            ipAddressIsReachable = InetAddress.getByName(ipAddress).isReachable(10000);
        } catch (IOException e) {
            // ignore
        }
        return ipAddressIsReachable;
    }

    /**
     * The method tries to connect to a hue bridge at the given ip address by
     * using the given username.
     * 
     * @param ipAddress
     *            the ip address of the hue bridge
     * @param username
     *            the username which is used to connect to the hue bridge
     * @return true if the connection could be established successfully
     */
    protected boolean tryToConnect(String ipAddress, String username) {
        boolean connectSuccessful = false;
        try {
            new HueBridge(ipAddress, username);
            connectSuccessful = true;
        } catch (IOException | ApiException e) {

        }
        return connectSuccessful;
    }

}
