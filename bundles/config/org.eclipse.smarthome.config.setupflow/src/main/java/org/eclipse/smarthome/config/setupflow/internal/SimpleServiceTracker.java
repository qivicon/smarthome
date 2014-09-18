/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow.internal;

import org.eclipse.smarthome.config.setupflow.internal.SimpleServiceTrackerListener.Event;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// TODO: What to do if an exception raises within the listener?
/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public class SimpleServiceTracker<T> extends ServiceTracker {

    private Logger logger = LoggerFactory.getLogger(SimpleServiceTracker.class);

    private T service;
    private SimpleServiceTrackerListener<T> listener;


    public SimpleServiceTracker(BundleContext bundleContext, Class<T> clazz,
            SimpleServiceTrackerListener<T> listener) throws IllegalArgumentException {

        super(bundleContext, clazz.getName(), null);

        if (bundleContext == null) {
            throw new IllegalArgumentException("The BundleContext must not be null!");
        }
        if (listener == null) {
            throw new IllegalArgumentException("The SimpleServiceTrackerListener must not be null!");
        }

        this.listener = listener;
    }

    @Override
    public synchronized Object addingService(ServiceReference reference) {
        if (this.service == null) {
            this.service = (T) super.context.getService(reference);

            if (this.service != null) {
                try {
                    this.logger.debug("Binding service...");
                    this.listener.serviceBindingPerformed(this, Event.SERVICE_AVAILABLE, this.service);
                    this.logger.debug("Service bound.");

                    return this.service;
                } catch (Exception ex) {
                    this.logger.error("Could not bind service!", ex);
                }
            }
        }

        return null;
    }

    @Override
    public synchronized void removedService(ServiceReference reference, Object service) {
        try {
            this.logger.debug("Unbinding service...");
            this.listener.serviceBindingPerformed(this, Event.SERVICE_UNAVAILABLE, this.service);
            this.logger.debug("Service unbound.");
        } catch (Exception ex) {
            this.logger.error("Could not unbind service!", ex);
        }

        this.service = null;

        super.removedService(reference, service);
    }

}
