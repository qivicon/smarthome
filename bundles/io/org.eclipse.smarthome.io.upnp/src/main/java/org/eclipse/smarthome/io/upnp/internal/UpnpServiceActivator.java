/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.upnp.internal;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator
 */
public final class UpnpServiceActivator extends DefaultRegistryListener implements BundleActivator {

    private static Logger logger = LoggerFactory.getLogger(UpnpServiceActivator.class);

    private UpnpService upnpService;

    /**
     * Called whenever the OSGi framework starts our bundle
     */
    public void start(BundleContext context) throws Exception {
        logger.debug("Upnp service has been started.");

        upnpService = new org.fourthline.cling.UpnpServiceImpl(new EshUpnpServiceConfiguration(), this);
        upnpService.getControlPoint().search();
    }

    /**
     * Called whenever the OSGi framework stops our bundle
     */
    public void stop(BundleContext context) throws Exception {
        logger.debug("Upnp service has been stopped.");

        if (upnpService != null) {
            upnpService.shutdown();
        }
        upnpService = null;
    }

    @Override
    public void deviceAdded(Registry registry, Device device) {
        logger.debug("ADDED: {}", device.toString());
        dumpRegistry(registry);
    }

    @Override
    public void deviceRemoved(Registry registry, Device device) {
        logger.debug("REMOVED: {}", device.toString());
        dumpRegistry(registry);
    }

    private void dumpRegistry(Registry registry) {
        for (Device device : registry.getDevices()) {
            logger.debug(device.toString());
        }
    }

}
