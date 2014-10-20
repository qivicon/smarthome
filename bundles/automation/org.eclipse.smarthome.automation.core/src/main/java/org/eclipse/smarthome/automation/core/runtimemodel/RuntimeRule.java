/**
 * 
 */
package org.eclipse.smarthome.automation.core.runtimemodel;

import org.eclipse.smarthome.automation.core.ModuleHandlerResolver;
import org.eclipse.smarthome.automation.core.jsonmodel.ModuleRef;
import org.eclipse.smarthome.automation.core.jsonmodel.Rule;

/**
 * @author niehues
 *
 */
public class RuntimeRule implements ITriggerListener {

	private Rule rule;

	public RuntimeRule(Rule ruleDescription) {
		this.rule = ruleDescription;
		for (ModuleRef trigger : ruleDescription.getOn()) {
			IModuleHandler handler = ModuleHandlerResolver.resolve(trigger);
			if (handler instanceof ITriggerModuleHandler) {
				((ITriggerModuleHandler) handler).addListener(trigger.getParameters(), this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.smarthome.automation.core.runtimemodel.ITriggerListener#execute
	 * ()
	 */
	@Override
	public void execute() {
		if (ifCondition()) {
			thenExecute();
		}

	}

	/**
	 * checks if the Conditions are fulfilled returns true if it is fulfilled.
	 * 
	 * @return
	 */
	private boolean ifCondition() {
		// at a first step conditions are logically AND combined
		for (ModuleRef condition : rule.get_if()) {
			IModuleHandler handler = ModuleHandlerResolver.resolve(condition);
			if (handler instanceof IConditionModuleHandler) {
				if (!((IConditionModuleHandler) handler).evaluate(condition
						.getParameters())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * executes the actionsModules
	 * 
	 * @return
	 */
	private boolean thenExecute() {
		for (ModuleRef action : rule.get_then()) {
			IModuleHandler handler = ModuleHandlerResolver.resolve(action);
			if (handler instanceof IActionModuleHandler) {
				((IActionModuleHandler) handler)
						.execute(action.getParameters());
			}
		}
		return true;
	}

}
