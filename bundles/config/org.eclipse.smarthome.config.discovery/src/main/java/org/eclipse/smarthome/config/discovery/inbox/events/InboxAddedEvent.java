/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.discovery.inbox.events;

import org.eclipse.smarthome.config.discovery.DiscoveryResult;

/**
 * An {@link InboxAddedEvent} notifies subscribers that a discovery result has been added to the inbox.
 *
 * @author Stefan Bußweiler - Initial contribution
 */
public class InboxAddedEvent extends AbstractInboxEvent {

    /**
     * The inbox added event type.
     */
    public final static String TYPE = InboxAddedEvent.class.getSimpleName();

    /**
     * Constructs a new inbox added event object.
     *
     * @param topic the topic
     * @param payload the payload
     * @param source the source
     * @param discoveryResult the discovery result
     */
    protected InboxAddedEvent(String topic, String payload, String source, DiscoveryResult discoveryResult) {
        super(topic, payload, source, discoveryResult);
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
