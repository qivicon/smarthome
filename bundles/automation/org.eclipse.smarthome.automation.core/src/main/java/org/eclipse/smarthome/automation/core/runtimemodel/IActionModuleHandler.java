package org.eclipse.smarthome.automation.core.runtimemodel;

import java.util.Map;

/**
 * Interface for Modules which perform actions during RuleEvaluation
 * @author niehues
 *
 */
public interface IActionModuleHandler extends IModuleHandler{

	
	/**
	 * Performs an action during RuleEvaluation
	 * @param parameters
	 */
	public void execute(Map<String, String> parameters);
}
