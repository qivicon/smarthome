package org.eclipse.smarthome.automation.core.runtimemodel;

import java.util.Map;


/**
 * Interface for Triggering module during RuleEvaluation
 * 
 * @author niehues
 *
 */
public interface ITriggerModuleHandler extends IModuleHandler {

	/**
	 * adds a listener to the TriggerHandler
	 * @return
	 */
	public boolean addListener(Map<String, String> parameters,  ITriggerListener listener);
	
	public boolean removeListener(Map<String, String> parameters, ITriggerListener listener);
}
