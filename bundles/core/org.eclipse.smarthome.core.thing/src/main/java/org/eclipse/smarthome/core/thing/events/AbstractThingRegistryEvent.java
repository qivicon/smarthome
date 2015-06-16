/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.events;

import org.eclipse.smarthome.core.events.AbstractEvent;
import org.eclipse.smarthome.core.thing.bean.ThingBean;

/**
 * Abstract implementation of a thing registry event.
 *
 * @author Stefan Bußweiler - Initial contribution
 */
public abstract class AbstractThingRegistryEvent extends AbstractEvent {

    private final ThingBean thing;

    /**
     * Must be called in subclass constructor to create a new thing registry event.
     *
     * @param topic the topic
     * @param payload the payload
     * @param source the source, can be null
     * @param thing the thing
     */
    protected AbstractThingRegistryEvent(String topic, String payload, String source, ThingBean thing) {
        super(topic, payload, source);
        this.thing = thing;
    }

    /**
     * Gets the thing.
     * 
     * @return the thing
     */
    public ThingBean getThing() {
        return thing;
    }

}
