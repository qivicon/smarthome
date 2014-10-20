package org.eclipse.smarthome.automation.core.runtimemodel;

import java.util.Map;

/**
 * Interface for Triggering module during RuleEvaluation
 * 
 * @author niehues
 *
 */
public interface ITriggerModuleHandler {

	/**
	 * adds a listener to the TriggerHandler
	 * 
	 * @return
	 */
	public boolean addListener(ITriggerListener listener);

	public void removeListener(ITriggerListener listener);

	public boolean shouldExecute(Map<String, String> parameters);
}
