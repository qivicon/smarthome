/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.sse;

import java.util.Collection;

/**
 * Provider for {@link EventType}s that the {@link EventBroadcaster} will take
 * into account.
 * 
 * @author Ivan Iliev - Initial contribution and API
 * 
 */
public interface EventTypeProvider {

    /**
     * Should provide a collection of EventTypes in order for them to be
     * registered with the {@link EventBroadcaster}
     * 
     * @return
     */
    public Collection<EventType> getEventTypes();

}
