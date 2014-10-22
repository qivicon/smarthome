package org.eclipse.smarthome.automation.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.smarthome.automation.core.internal.RuntimeRule;
import org.eclipse.smarthome.automation.core.module.handler.TriggerHandler;
import org.eclipse.smarthome.core.common.registry.AbstractRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleEngine extends AbstractRegistry<Rule> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleEngine.class);

    private Map<String, RuntimeRule> rules = new HashMap<String, RuntimeRule>();
    private List<TriggerHandler> triggerHandlers = new CopyOnWriteArrayList<TriggerHandler>();
    private BundleContext bundleContext;

    protected void activate(ComponentContext context) {
        this.bundleContext = context.getBundleContext();

    }

    protected void deactivate(ComponentContext context) {
        this.bundleContext = null;
    }

    protected void addTriggerHandler(TriggerHandler handler) {
        for (RuntimeRule rRule : rules.values()) {
            rRule.onTriggerHandlerAdded(handler);
        }
        triggerHandlers.add(handler);
    }

    protected void removeTriggerHandler(TriggerHandler handler) {
        for (RuntimeRule rRule : rules.values()) {
            rRule.onTriggerHandlerRemoved(handler);
        }
        triggerHandlers.remove(handler);
    }

    @Override
    protected void onAddElement(Rule rule) throws IllegalArgumentException {
        RuntimeRule rRule = rules.get(rule.getId());
        if (rRule == null) {
            this.rules.put(rule.getId(), new RuntimeRule(rule, getBundleContext()));
            enable(rule);
        } else {
            LOGGER.warn("Rule {} with Id: {} was already registered.", rule.getName(), rule.getId());
        }
    }

    private BundleContext getBundleContext() {
        /**
         * TODO: consider other mechanism to get bundle context
         */
        if(this.bundleContext == null) {
            this.bundleContext = FrameworkUtil.getBundle(RuleEngine.class).getBundleContext();
        }
        
        return this.bundleContext;
    }

    @Override
    protected void onRemoveElement(Rule rule) {
        disable(rule);
        this.rules.remove(rule.getId());
    }

    /* Rule Activation Listener */
    public void enable(Rule rule) {
        RuntimeRule rRule = rules.get(rule.getId());
        if (rRule != null) {
            rRule.enable();
            LOGGER.debug("Rule {} with Id: {} was activated", rule.getName(), rule.getId());
        }
    }

    public void disable(Rule rule) {

        RuntimeRule rRule = rules.get(rule.getId());
        if (rRule != null) {
            rRule.disable();
            LOGGER.debug("Rule {} with Id: {} was deactivated", rule.getName(), rule.getId());
        }

    }

}
