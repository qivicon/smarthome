/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.sse;

import java.util.Set;

/**
 * Service used for broadcasting events across all registered event channels.
 * 
 * @author Ivan Iliev - Initial contribution and API
 * 
 */
public interface EventBroadcaster {

    /**
     * Broadcasts the given event on all {@link EventChannel}s.
     * 
     * @param objectIdentifier
     * @param eventType
     * @param eventObject
     */
    public void broadcastEvent(final String objectIdentifier, final EventType eventType, final Object eventObject);

    /**
     * Return an unmodifiable set of all event types currently known to this
     * broadcaster.
     * 
     * @return
     */
    public Set<EventType> getEventTypes();

    /**
     * Returns a set of event types by a given filter. The filter is expected to
     * be in the format <b>namespace/event_object/event_type</b> e.g.
     * <b>smarthome/items/added</b>. Wildcard can also be used in place of each
     * component e.g. <b>smarthome/&#42;/added</b>.
     * 
     * @param filter
     * @return
     */
    public Set<EventType> filterEventTypes(String filter);

}
