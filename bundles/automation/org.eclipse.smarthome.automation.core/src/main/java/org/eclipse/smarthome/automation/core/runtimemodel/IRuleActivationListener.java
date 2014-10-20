package org.eclipse.smarthome.automation.core.runtimemodel;

import org.eclipse.smarthome.automation.core.jsonmodel.Rule;

public interface IRuleActivationListener {

	public void activate(Rule rule);

	public void deactivate(Rule rule);
}
