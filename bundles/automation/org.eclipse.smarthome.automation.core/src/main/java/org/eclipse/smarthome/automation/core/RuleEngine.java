package org.eclipse.smarthome.automation.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.automation.core.jsonmodel.Rule;
import org.eclipse.smarthome.automation.core.runtimemodel.IRuleActivationListener;
import org.eclipse.smarthome.automation.core.runtimemodel.RuntimeRule;

public class RuleEngine implements IRuleActivationListener {

	private Map<String, RuntimeRule> rules = new HashMap<String, RuntimeRule>();

	public void register(Rule rule) {
		if (rule.isEnabled()) {
			this.rules.put(rule.getName(), new RuntimeRule(rule));
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
