/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.automation.events;

import org.eclipse.smarthome.automation.Rule;

/**
 * An {@link RuleRemovedEvent} notifies subscribers that an item has been added.
 * Rule added events must be created with the {@link RuleEventFactory}.
 *
 * @author Benedikt Niehues - initial contribution
 *
 */
public class RuleRemovedEvent extends AbstractRuleRegistryEvent {

    public static final String TYPE = RuleRemovedEvent.class.getSimpleName();

    /**
     * constructs a new rule removed event
     *
     * @param topic
     * @param payload
     * @param source
     * @param ruleDTO
     */
    public RuleRemovedEvent(String topic, String payload, String source, Rule ruleDTO) {
        super(topic, payload, source, ruleDTO);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return "Rule '" + getRule().getUID() + "' has been removed.";
    }

}
