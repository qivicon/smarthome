/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.events;

/**
 * The {@link TopicEventFilter} is a default Eclipse SmartHome {@link EventFilter} implementation that ensures filtering
 * of events based on an event topic.
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public class TopicEventFilter implements EventFilter {

    private final String topicRegex;
    
    public TopicEventFilter(String topicRegex) {
        this.topicRegex = topicRegex;
    }
 
    @Override
    public boolean apply(Event event) {
        return event.getTopic().matches(topicRegex);
    }

}
