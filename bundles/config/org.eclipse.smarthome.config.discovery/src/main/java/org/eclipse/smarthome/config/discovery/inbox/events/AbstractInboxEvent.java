/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.discovery.inbox.events;

import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.core.events.AbstractEvent;

/**
 * Abstract implementation of an inbox event.
 *
 * @author Stefan Bußweiler - Initial contribution
 */
public abstract class AbstractInboxEvent extends AbstractEvent {

    private final DiscoveryResult discoveryResult;

    /**
     * Must be called in subclass constructor to create an inbox event.
     *
     * @param topic the topic
     * @param payload the payload
     * @param source the source, can be null
     * @param discoveryResult the discovery result
     */
    protected AbstractInboxEvent(String topic, String payload, String source, DiscoveryResult discoveryResult) {
        super(topic, payload, source);
        this.discoveryResult = discoveryResult;
    }

    /**
     * Gets the discovery result.
     * 
     * @return the discoveryResult
     */
    public DiscoveryResult getDiscoveryResult() {
        return discoveryResult;
    }

}
