/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow.internal;


/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public interface SimpleServiceTrackerListener<T> {

    public enum Event {
        SERVICE_AVAILABLE,
        SERVICE_UNAVAILABLE
    }

    void serviceBindingPerformed(SimpleServiceTracker<T> source, Event event, T service)
            throws Exception;

}
