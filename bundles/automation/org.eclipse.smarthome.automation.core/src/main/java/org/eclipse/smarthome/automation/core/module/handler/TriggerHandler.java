package org.eclipse.smarthome.automation.core.module.handler;

import java.util.Map;


/**
 * Interface for Triggering module during RuleEvaluation
 * 
 * @author niehues
 *
 */
public interface TriggerHandler extends ModuleHandler {

	/**
	 * adds a listener to the TriggerHandler
	 * @return
	 */
	public boolean addListener(Map<String, Object> parameters,  TriggerListener listener);
	
	public boolean removeListener(Map<String, Object> parameters, TriggerListener listener);
}
