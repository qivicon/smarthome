/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.events;

public class TypedEventFilter implements EventFilter {

    private final String eventType; 
    
    public TypedEventFilter(String eventType) {
        this.eventType = eventType;
    }
    
    @Override
    public boolean apply(Event event) {
        return event.getType().equals(eventType);
    }
    
}
