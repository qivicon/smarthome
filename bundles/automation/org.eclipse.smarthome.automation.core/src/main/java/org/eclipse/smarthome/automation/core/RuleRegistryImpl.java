/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.automation.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.smarthome.automation.Rule;
import org.eclipse.smarthome.automation.RuleRegistry;
import org.eclipse.smarthome.automation.RuleStatus;
import org.eclipse.smarthome.automation.events.RuleEventFactory;
import org.eclipse.smarthome.core.common.registry.AbstractRegistry;
import org.eclipse.smarthome.core.common.registry.Provider;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.eclipse.smarthome.core.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yordan Mihaylov - Initial Contribution
 * @author Ana Dimova - Persistence implementation
 * @author Kai Kreuzer - refactored (managed) provider and registry implementation
 * @author Benedikt Niehues - added events for rules
 */
public class RuleRegistryImpl extends AbstractRegistry<Rule, String>implements RuleRegistry {

    private RuleEngine ruleEngine;
    private Set<String> disabledRuledSet = new HashSet(0);
    private Logger logger;
    private Storage<Boolean> disabledRulesStorage;
    
    private static final String SOURCE = RuleRegistryImpl.class.getSimpleName();

    public RuleRegistryImpl(RuleEngine ruleManager) {
        logger = LoggerFactory.getLogger(getClass());
        this.ruleEngine = ruleManager;

    }

    @Override
    protected void addProvider(Provider<Rule> provider) {
        Collection<Rule> rules = provider.getAll();
        for (Iterator<Rule> it = rules.iterator(); it.hasNext();) {
            Rule rule = it.next();
            try {
                addIntoRuleEngine(rule);
            } catch (Exception e) {
                logger.error("Can't add rule: " + rule.getUID() + " into rule engine.", e);
            }

        }
        super.addProvider(provider);
    }

    @Override
    public synchronized void add(Rule element) {
        super.add(element);
        addIntoRuleEngine(element);
        postEvent(RuleEventFactory.createRuleAddedEvent(element, SOURCE));
    }

    private String addIntoRuleEngine(Rule element) {
        String rUID = ruleEngine.addRule(element);
        if (disabledRuledSet.contains(rUID)) {
            ruleEngine.setRuleEnabled(rUID, false);
        }

        ruleEngine.setRule(rUID);
        return rUID;
    }

    @Override
    public synchronized Rule remove(String key) {
        Rule rule = get(key);
        ruleEngine.removeRule(key);
        setEnabled(key, false);
        postEvent(RuleEventFactory.createRuleRemovedEvent(rule, SOURCE));
        return super.remove(key);
    }

    @Override
    public synchronized Rule update(Rule element) {
        Rule old = get(element.getUID());
        if (element != null) {
            String rUID = element.getUID();
            if (disabledRuledSet.contains(rUID)) {
                ruleEngine.setRuleEnabled(rUID, false);
            }
            ruleEngine.updateRule(element);// update memory map
            element = super.update(element);// update storage with new rule and return old rule
            postEvent(RuleEventFactory.createRuleUpdatedEvent(element, old, SOURCE));
        }
        return element;
    }

    @Override
    public Rule get(String key) {
        return ruleEngine.getRule(key);
    }

    @Override
    public Collection<Rule> getByTag(String tag) {
        return ruleEngine.getRulesByTag(tag);
    }

    @Override
    public Collection<Rule> getByTags(Set<String> tags) {
        return ruleEngine.getRulesByTags(tags);
    }

    @Override
    public synchronized void setEnabled(String uid, boolean isEnabled) {
        if (isEnabled) {
            if (disabledRuledSet.remove(uid)) {
                if (disabledRulesStorage != null) {
                    disabledRulesStorage.remove(uid);
                }
            }
        } else {
            if (!disabledRuledSet.contains(uid)) {
                disabledRuledSet.add(uid);
                if (disabledRulesStorage != null) {
                    disabledRulesStorage.put(uid, Boolean.TRUE);
                }
            }
        }

        ruleEngine.setRuleEnabled(uid, isEnabled);
    }

    @Override
    public RuleStatus getStatus(String ruleUID) {
        return ruleEngine.getRuleStatus(ruleUID);
    }

    protected synchronized void dispose() {
        for (Iterator<String> it = disabledRuledSet.iterator(); it.hasNext();) {
            String rUID = it.next();
            if (!ruleEngine.hasRule(rUID)) {
                it.remove();
                if (disabledRulesStorage != null) {
                    disabledRulesStorage.remove(rUID);
                }
            }
        }
        ruleEngine.dispose();
    }

    private synchronized Set<String> loadDisabledRuleMap() {
        Set<String> result = disabledRuledSet == null ? new HashSet<String>() : disabledRuledSet;
        if (disabledRulesStorage != null) {
            for (Iterator<String> it = disabledRulesStorage.getKeys().iterator(); it.hasNext();) {
                String key = it.next();
                result.add(key);
            }
        }
        return result;
    }

    protected void setDisabledRuleStorage(Storage disabledRulesStorage) {
        this.disabledRulesStorage = disabledRulesStorage;
        disabledRuledSet = loadDisabledRuleMap();
        // TODO disabled active rules
    }
    
    public void setEventPublisher(EventPublisher eventPublisher) {
        super.setEventPublisher(eventPublisher);
    }

    public void unsetEventPublisher(EventPublisher eventPublisher) {
        super.unsetEventPublisher(eventPublisher);
    }

}
