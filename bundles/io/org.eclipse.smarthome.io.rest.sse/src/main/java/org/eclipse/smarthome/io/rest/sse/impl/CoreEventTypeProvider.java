/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.sse.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.smarthome.io.rest.sse.EventType;
import org.eclipse.smarthome.io.rest.sse.EventTypeProvider;

/**
 * Provider implementation for Core event types - {@link CoreEventType}
 * 
 * @author Ivan Iliev - Initial contribution and API
 * 
 */
public class CoreEventTypeProvider implements EventTypeProvider {

    @Override
    public Collection<EventType> getEventTypes() {
        return new ArrayList<EventType>(Arrays.asList(CoreEventType.values()));
    }

}
