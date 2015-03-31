/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.sse.impl;

import org.eclipse.smarthome.io.rest.sse.EventType;

/**
 * Event type for each broadcasted smarthome event using SSE.
 * 
 * @author Ivan Iliev - Initial Contribution and API
 * 
 */
public enum CoreEventType implements EventType {

    /**
     * Item was added to the item registry.
     */
    ITEM_ADDED("smarthome", "items", "added"),
    /**
     * Item was removed from the item registry.
     */
    ITEM_REMOVED("smarthome", "items", "removed"),
    /**
     * Item was updated in the item registry.
     */
    ITEM_UPDATED("smarthome", "items", "updated"),
    /**
     * Thing was added to the thing registry.
     */
    THING_ADDED("smarthome", "things", "added"),
    /**
     * Thing was removed from the thing registry.
     */
    THING_REMOVED("smarthome", "things", "removed"),
    /**
     * Thing was updated in the thing registry.
     */
    THING_UPDATED("smarthome", "things", "updated"),
    /**
     * Thing was added to the inbox.
     */
    INBOX_THING_ADDED("smarthome", "inbox", "added"),
    /**
     * Thing was removed from the inbox.
     */
    INBOX_THING_REMOVED("smarthome", "inbox", "removed"),

    /**
     * Thing was updated in the inbox.
     */
    INBOX_THING_UPDATED("smarthome", "inbox", "updated"),

    /**
     * OSGI Update event was sent
     */
    UPDATE("smarthome", "update", ""),

    /**
     * OSGI command event was sent
     */
    COMMAND("smarthome", "command", "");

    private final String eventNamespace;

    private final String eventObject;

    private final String eventType;

    private CoreEventType(String eventNamespace, String eventObject, String eventType) {
        this.eventNamespace = eventNamespace.toLowerCase();
        this.eventObject = eventObject.toLowerCase();
        this.eventType = eventType.toLowerCase();
    }

    @Override
    public String getNamespace() {
        return eventNamespace;
    }

    @Override
    public String getObject() {
        return eventObject;
    }

    @Override
    public String getType() {
        return eventType;
    }

}
