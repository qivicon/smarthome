/**
 * 
 */
package org.eclipse.smarthome.automation.module.trigger;

import org.eclipse.smarthome.automation.core.module.handler.ModuleContext;
import org.eclipse.smarthome.automation.core.module.handler.TriggerListener;

/**
 * Pojo intended for holding rules with their context as value in a map.
 * 
 * @author niehues
 *
 */
public class ModuleContextRuleHolder {

	private ModuleContext context;
	private TriggerListener rule;

	public ModuleContextRuleHolder(ModuleContext context, TriggerListener rule) {
		this.setContext(context);
		this.setRule(rule);
	}

	public ModuleContext getContext() {
		return context;
	}

	public void setContext(ModuleContext context) {
		this.context = context;
	}

	public TriggerListener getRule() {
		return rule;
	}

	public void setRule(TriggerListener rule) {
		this.rule = rule;
	}

}
