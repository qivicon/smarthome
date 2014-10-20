package org.eclipse.smarthome.automation.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.automation.core.jsonmodel.Rule;
import org.eclipse.smarthome.automation.core.runtimemodel.IRuleActivationListener;
import org.eclipse.smarthome.automation.core.runtimemodel.RuntimeRule;
import org.osgi.framework.BundleContext;

public class RuleEngine implements IRuleActivationListener {

    private Map<String, RuntimeRule> rules = new HashMap<String, RuntimeRule>();
    private BundleContext bundleContext;

    public RuleEngine(BundleContext context) {
        this.bundleContext = context;
    }

    public void register(Rule rule) {
        if (rule.isEnabled()) {
            this.rules.put(rule.getName(), new RuntimeRule(rule, bundleContext));
        }
    }

    /* Rule Activation Listener */
    @Override
    public void activate(Rule rule) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deactivate(Rule rule) {
        // TODO Auto-generated method stub

    }

}
