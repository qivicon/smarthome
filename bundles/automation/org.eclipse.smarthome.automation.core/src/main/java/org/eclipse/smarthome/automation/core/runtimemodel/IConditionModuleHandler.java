package org.eclipse.smarthome.automation.core.runtimemodel;

import java.util.Map;

/**
 * Interface for a condition which can be evaluated during RuleEvaluation
 * 
 * @author niehues
 *
 */
public interface IConditionModuleHandler {
	
	/**
	 * evaluate the condition regarding the properties
	 * 
	 * @param properties
	 * @return
	 */
	public boolean evaluate(Map<String, String> properties);
	

}
