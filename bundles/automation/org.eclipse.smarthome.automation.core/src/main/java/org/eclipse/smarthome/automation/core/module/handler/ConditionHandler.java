package org.eclipse.smarthome.automation.core.module.handler;

import java.util.Map;

/**
 * Interface for a condition which can be evaluated during RuleEvaluation
 * 
 * @author niehues
 *
 */
public interface ConditionHandler extends ModuleHandler {
	
	/**
	 * evaluate the condition regarding the properties
	 * 
	 * @param properties
	 * @return
	 */
	public boolean evaluate(Map<String, Object> properties);
	

}
