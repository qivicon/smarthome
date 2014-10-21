package org.eclipse.smarthome.automation.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.automation.core.internal.RuntimeRule;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleEngine {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuleEngine.class);
	
    private Map<String, RuntimeRule> rules = new HashMap<String, RuntimeRule>();
    private BundleContext bundleContext;

    public RuleEngine(BundleContext context) {
        this.bundleContext = context;
    }

    /**
     * Registers a rule to the RuleEngine
     * @param rule
     */
    public void register(Rule rule) {
        if (rule.isEnabled()) {
            this.rules.put(rule.getName(), new RuntimeRule(rule, bundleContext));
            if (LOGGER.isDebugEnabled()){
            	LOGGER.debug("Rule {} registered", rule.getName());
            }
        }
    }

    /* Rule Activation Listener */
    public void activate(Rule rule) {
    	if (LOGGER.isDebugEnabled()){
    		LOGGER.debug("Rule {} was activated", rule.getName());
    	}
        // TODO Auto-generated method stub

    }

    public void deactivate(Rule rule) {
    	if (LOGGER.isDebugEnabled()){
    		LOGGER.debug("Rule {} was deactivated", rule.getName());
    	}
        // TODO Auto-generated method stub

    }

}
