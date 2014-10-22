package org.eclipse.smarthome.automation.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.smarthome.automation.core.internal.RuntimeRule;
import org.eclipse.smarthome.automation.core.module.handler.TriggerHandler;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleEngine {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RuleEngine.class);

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
		triggerHandlers.add(handler);
		for (RuntimeRule rRule : rules.values()) {
			for (TriggerRef triggerRef : rRule.getRule().getTriggers()) {
				//add RuntimeRule as Listener to triggerRef
			}
		}
	}

	protected void removeTriggerHandler(TriggerHandler handler) {
		triggerHandlers.remove(handler);
	}

	/**
	 * Registers a rule to the RuleEngine
	 * 
	 * @param rule
	 */
	public void add(Rule rule) {
		RuntimeRule rRule = rules.get(rule.getId());
		if (rRule == null && rule.isEnabled()) {
			this.rules.put(rule.getId(), new RuntimeRule(rule, bundleContext));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Rule {} registered", rule.getName());
			}
		} else {
			LOGGER.warn("Rule {} with Id: {} was already registered.",
					rule.getName(), rule.getId());
		}
	}

	/* Rule Activation Listener */
	public void activate(Rule rule) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Rule {} with Id: {} was activated", rule.getName(),
					rule.getId());
		}
		// TODO Auto-generated method stub

	}

	public void deactivate(Rule rule) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Rule {} with Id: {} was deactivated", rule.getName(),
					rule.getId());
		}
		// TODO Auto-generated method stub

	}

}
