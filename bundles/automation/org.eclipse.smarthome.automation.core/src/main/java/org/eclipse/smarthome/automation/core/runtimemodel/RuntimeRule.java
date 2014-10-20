/**
 * 
 */
package org.eclipse.smarthome.automation.core.runtimemodel;

import java.util.List;

import org.eclipse.smarthome.automation.core.ModuleHandlerResolver;
import org.eclipse.smarthome.automation.core.jsonmodel.ModuleRef;
import org.eclipse.smarthome.automation.core.jsonmodel.Rule;
import org.osgi.framework.BundleContext;

/**
 * @author niehues
 *
 */
public class RuntimeRule implements ITriggerListener {

	private Rule rule;
    private BundleContext bundleContext;

	public RuntimeRule(Rule ruleDescription, BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	    this.rule = ruleDescription;
		for (ModuleRef trigger : ruleDescription.getOn()) {
			IModuleHandler handler = ModuleHandlerResolver.resolve(ITriggerModuleHandler.class, trigger, this.bundleContext);
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
		List<ModuleRef> conditions = rule.get_if();
		if(conditions == null) {
		    return true;
		}
        for (ModuleRef condition : conditions) {
			IModuleHandler handler = ModuleHandlerResolver.resolve(IConditionModuleHandler.class, condition, this.bundleContext);
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
			IModuleHandler handler = ModuleHandlerResolver.resolve(IActionModuleHandler.class, action, this.bundleContext);
			if (handler instanceof IActionModuleHandler) {
				((IActionModuleHandler) handler)
						.execute(action.getParameters());
			}
		}
		return true;
	}

}
