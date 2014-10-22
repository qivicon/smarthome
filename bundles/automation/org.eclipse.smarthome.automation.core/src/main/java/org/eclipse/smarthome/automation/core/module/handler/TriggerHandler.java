package org.eclipse.smarthome.automation.core.module.handler;


/**
 * Interface for Triggering module during RuleEvaluation
 * 
 * @author niehues
 *
 */
public interface TriggerHandler extends ModuleHandler {

	/**
	 * adds a listener to the TriggerHandler
	 * 
	 * @return
	 */
	public void addListener(ModuleContext context, TriggerListener listener);

	public void removeListener(ModuleContext context,
			TriggerListener listener);
}
