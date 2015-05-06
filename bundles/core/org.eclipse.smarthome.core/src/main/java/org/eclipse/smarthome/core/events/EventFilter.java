/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.events;

/**
 * An {@link EventFilter} can be provided by an {@link EventSubscriber} in order
 * to receive specific {@link Event}s by an {@link EventPublisher} if the filter applies.
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public interface EventFilter {

    /**
     * Apply the filter on an event.
     * 
     * @param event the event
     * @return true if the filter criterion applies
     */
    boolean apply(Event event);
}
