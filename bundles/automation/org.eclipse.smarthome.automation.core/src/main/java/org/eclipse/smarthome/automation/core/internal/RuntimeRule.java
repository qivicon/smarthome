/**
 * 
 */
package org.eclipse.smarthome.automation.core.internal;

import java.util.List;

import org.eclipse.smarthome.automation.core.ActionRef;
import org.eclipse.smarthome.automation.core.ConditionRef;
import org.eclipse.smarthome.automation.core.Rule;
import org.eclipse.smarthome.automation.core.TriggerRef;
import org.eclipse.smarthome.automation.core.module.handler.ActionHandler;
import org.eclipse.smarthome.automation.core.module.handler.ConditionHandler;
import org.eclipse.smarthome.automation.core.module.handler.ModuleHandler;
import org.eclipse.smarthome.automation.core.module.handler.TriggerHandler;
import org.eclipse.smarthome.automation.core.module.handler.TriggerListener;
import org.osgi.framework.BundleContext;

/**
 * @author niehues
 *
 */
public class RuntimeRule implements TriggerListener {

	private Rule rule;

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	private BundleContext bundleContext;

	public RuntimeRule(Rule ruleDescription, BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		this.rule = ruleDescription;
	}

	public void enable() {
		this.rule.setEnabled(true);
		for (TriggerRef trigger : rule.getTriggers()) {
			ModuleHandler handler = ModuleHandlerResolver.resolve(trigger,
					this.bundleContext);
			if (handler instanceof TriggerHandler) {
				((TriggerHandler) handler).addListener(trigger.getParameters(),
						this);
			}
		}
	}

	public void onTriggerHandlerAdded(TriggerHandler triggerHandler) {
		for (TriggerRef trigger : rule.getTriggers()) {
			if (triggerHandler.getName().equals(trigger.getType())) {
				triggerHandler.addListener(trigger.getParameters(), this);
			}
		}
	}

	public void onTriggerHandlerRemoved(TriggerHandler triggerHandler) {
		for (TriggerRef trigger : rule.getTriggers()) {
			if (triggerHandler.getName().equals(trigger.getType())) {
				triggerHandler.removeListener(trigger.getParameters(), this);
			}
		}
	}

	public void disable() {
		this.rule.setEnabled(false);
		for (TriggerRef trigger : rule.getTriggers()) {
			ModuleHandler handler = ModuleHandlerResolver.resolve(trigger,
					this.bundleContext);
			if (handler instanceof TriggerHandler) {
				((TriggerHandler) handler).removeListener(
						trigger.getParameters(), this);
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
		List<ConditionRef> conditions = rule.getConditions();
		if (conditions == null) {
			return true;
		}
		for (ConditionRef condition : conditions) {
			ModuleHandler handler = ModuleHandlerResolver.resolve(condition,
					this.bundleContext);
			if (handler instanceof ConditionHandler) {
				if (!((ConditionHandler) handler).evaluate(condition
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
		for (ActionRef action : rule.getActions()) {
			ModuleHandler handler = ModuleHandlerResolver.resolve(action,
					this.bundleContext);
			if (handler instanceof ActionHandler) {
				((ActionHandler) handler).execute(action.getParameters());
			}
		}
		return true;
	}

}
