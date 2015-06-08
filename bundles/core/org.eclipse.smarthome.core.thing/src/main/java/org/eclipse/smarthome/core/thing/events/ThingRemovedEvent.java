/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.events;

import org.eclipse.smarthome.core.thing.Thing;

/**
 * A {@link ThingRemovedEvent} notifies subscribers that a thing has been removed.
 *
 * @author Stefan Bußweiler - Initial contribution
 */
public class ThingRemovedEvent extends AbstractThingRegistryEvent {

    /**
     * The thing removed event type.
     */
    public final String TYPE = ThingRemovedEvent.class.getSimpleName();

    /**
     * Constructs a new thing removed event object.
     *
     * @param topic the topic
     * @param payload the payload
     * @param thing the thing
     */
    public ThingRemovedEvent(String topic, String payload, Thing thing) {
        super(topic, payload, null, thing);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "Thing '" + getThing().getUID() + "' has been removed.";
    }

}
