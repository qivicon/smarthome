/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.discovery.inbox.events;

import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.core.events.AbstractEventFactory;
import org.eclipse.smarthome.core.events.Event;

import com.google.common.collect.Sets;

/**
 * An {@link InboxEventFactory} is responsible for creating inbox event instances.
 *
 * @author Stefan Bußweiler - Initial contribution
 */
public class InboxEventFactory extends AbstractEventFactory {

    /**
     * Constructs a new InboxEventFactory.
     */
    public InboxEventFactory() {
        super(Sets.newHashSet(InboxAddedEvent.TYPE, InboxUpdatedEvent.TYPE, InboxRemovedEvent.TYPE));
    }

    @Override
    protected Event createEventByType(String eventType, String topic, String payload, String source) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Creates an inbox added event.
     * 
     * @param result the discovery result
     * 
     * @return the created inbox added event
     */
    public static InboxAddedEvent createAddedEvent(DiscoveryResult result) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Creates an inbox removed event.
     * 
     * @param result the discovery result
     * 
     * @return the created inbox removed event
     */
    public static InboxRemovedEvent createRemovedEvent(DiscoveryResult result) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Creates an inbox updated event.
     * 
     * @param result the discovery result
     * 
     * @return the created inbox updated event
     */
    public static InboxUpdatedEvent createUpdateEvent(DiscoveryResult result) {
        // TODO Auto-generated method stub
        return null;
    }

}
