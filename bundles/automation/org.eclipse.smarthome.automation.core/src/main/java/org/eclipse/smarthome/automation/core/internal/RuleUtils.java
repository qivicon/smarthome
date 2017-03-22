/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.automation.core.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.smarthome.automation.Action;
import org.eclipse.smarthome.automation.Condition;
import org.eclipse.smarthome.automation.Rule;
import org.eclipse.smarthome.automation.Trigger;

/**
 * An utility class creating copy of the rule
 *
 * @author Yordan Mihaylov - initial content
 * @author Victor Toni - Add support for scope prefix
 */
public class RuleUtils {
    /**
     * Constant defining separator between module UID and output name.
     */
    public static final char SCOPE_DELIMITER = ':';

    /**
     * This method creates deep copy of list of conditions
     *
     * @param conditions list of conditions
     * @return deep copy of list of conditions or empty list when the parameter is null.
     */
    public static List<Condition> getConditionsCopy(List<Condition> conditions) {
        List<Condition> res = new ArrayList<Condition>(11);
        if (conditions != null) {
            for (Condition c : conditions) {
                Condition condition = new Condition(c.getId(), c.getTypeUID(), c.getConfiguration(), c.getInputs());
                condition.setLabel(c.getLabel());
                condition.setDescription(c.getDescription());
                res.add(condition);
            }
        }
        return res;
    }

    /**
     * This method creates deep copy of list of actions
     *
     * @param actions list of actions
     * @return deep copy of list of actions or empty list when the parameter is null.
     */
    public static List<Action> getActionsCopy(List<Action> actions) {
        List<Action> res = new ArrayList<Action>();
        if (actions != null) {
            for (Action a : actions) {
                Action action = new Action(a.getId(), a.getTypeUID(), a.getConfiguration(), a.getInputs());
                action.setLabel(a.getLabel());
                action.setDescription(a.getDescription());
                res.add(action);
            }
        }
        return res;
    }

    /**
     * This method creates deep copy of list of triggers
     *
     * @param triggers list of triggers
     * @return deep copy of list of triggers or empty list when parameter is null.
     */
    public static List<Trigger> getTriggersCopy(List<Trigger> triggers) {
        List<Trigger> res = new ArrayList<Trigger>(11);
        if (triggers != null) {
            for (Trigger t : triggers) {
                Trigger trigger = new Trigger(t.getId(), t.getTypeUID(), t.getConfiguration());
                trigger.setLabel(t.getLabel());
                trigger.setDescription(t.getDescription());
                res.add(trigger);
            }
        }
        return res;
    }

    /**
     * This method creates copy of the rule
     * 
     * @param rule the rule which has to be copied
     * @return copy of the rule.
     */
    public static Rule getRuleCopy(Rule r) {
        Rule rule = new Rule(r.getUID(), getTriggersCopy(r.getTriggers()), getConditionsCopy(r.getConditions()),
                getActionsCopy(r.getActions()), r.getConfigurationDescriptions(), r.getConfiguration(),
                r.getTemplateUID(), r.getVisibility());
        rule.setName(r.getName());
        rule.setTags(r.getTags());
        rule.setDescription(r.getDescription());
        return rule;
    }

    /**
     * Gets the scope of the Rule, if any exist. This property is either set by the {@link RuleEngine} when
     * the {@link Rule} is added or by the creating party. It's an optional property.
     * <br/><br/>Implementation note
     *    <br/>The scope is part of the {@code UID} and the prefix thereof.
     *    <br/>If the UID does not contain a {@link SCOPE_DELIMITER} {@code null} will be returned.
     *    <br/>If the UID does contain a {@link SCOPE_DELIMITER} the prefix until the first occurrence will be returned.
     *    <br/>If the prefix would have a zero length {@code null} will be returned.
     *
     * @return scope of this {@link Rule}, or {@code null} if no scope or an empty scope is found
    */
    public static String getScope(Rule rule) {
        if (null != rule) {
            final String uid = rule.getUID();
            if (null != uid) {
                final int index = uid.indexOf(SCOPE_DELIMITER);

                // only when a delimiter was found and the scope is not empty
                if (0 < index) {
                    return uid.substring(0, index);
                }
            }
        }

        return null;
    }

}
