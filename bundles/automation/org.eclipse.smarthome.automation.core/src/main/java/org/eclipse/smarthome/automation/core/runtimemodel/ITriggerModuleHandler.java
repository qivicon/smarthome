package org.eclipse.smarthome.automation.core.runtimemodel;

/**
 * Interface for Triggering module during RuleEvaluation
 * 
 * @author niehues
 *
 */
public interface ITriggerModuleHandler {

	/**
	 * adds a listener to the TriggerHandler
	 * @return
	 */
	public boolean addListener(ITriggerListener listener);
}
