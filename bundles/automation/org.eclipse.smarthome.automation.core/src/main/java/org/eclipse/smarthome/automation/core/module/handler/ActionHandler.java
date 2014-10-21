package org.eclipse.smarthome.automation.core.module.handler;

import java.util.Map;

/**
 * Interface for Modules which perform actions during RuleEvaluation
 * @author niehues
 *
 */
public interface ActionHandler extends ModuleHandler{

	
	/**
	 * Performs an action during RuleEvaluation
	 * @param parameters
	 */
	public void execute(Map<String, Object> parameters);
}
