/**
 * 
 */
package org.eclipse.smarthome.automation.core.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.smarthome.automation.core.ActionRef;
import org.eclipse.smarthome.automation.core.ConditionRef;
import org.eclipse.smarthome.automation.core.ModuleRef;
import org.eclipse.smarthome.automation.core.Rule;
import org.eclipse.smarthome.automation.core.TriggerRef;
import org.eclipse.smarthome.automation.core.module.handler.ActionHandler;
import org.eclipse.smarthome.automation.core.module.handler.ConditionHandler;
import org.eclipse.smarthome.automation.core.module.handler.ModuleContext;
import org.eclipse.smarthome.automation.core.module.handler.ModuleHandler;
import org.eclipse.smarthome.automation.core.module.handler.TriggerHandler;
import org.eclipse.smarthome.automation.core.module.handler.TriggerListener;
import org.osgi.framework.BundleContext;

/**
 * @author niehues
 *
 */
public class RuntimeRule implements TriggerListener {

	private Map<String, ModuleContext> moduleContexts = new HashMap<String, ModuleContext>();

	private Map<String, Object> ruleParameters = new HashMap<String, Object>();

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
		int index = 0;
		for (TriggerRef trigger : rule.getTriggers()) {
			ModuleHandler handler = ModuleHandlerResolver.resolve(trigger,
					this.bundleContext);
			if (handler instanceof TriggerHandler) {
				ModuleContext mc = createModuleContext(trigger, index++);
				((TriggerHandler) handler).addListener(mc, this);
			}
		}
	}

	public ModuleContext createModuleContext(ModuleRef module, int index) {
		String id = createContextId(module, index);
		Map<String, Object> replacedInputParameters = new HashMap<String, Object>(
				module.getParameters());
		for (Entry<String, Object> entry : module.getParameters().entrySet()) {
			if (entry.getValue() instanceof String
					&& ((String) entry.getValue()).startsWith("$")) {
				replacedInputParameters.put(entry.getKey(),
						ruleParameters.get((String) entry.getValue()));
			}
		}
		ModuleContext context = new ModuleContext(id, replacedInputParameters);
		moduleContexts.put(id, context);
		return context;
	}

	public String createContextId(ModuleRef module, int index) {
		String id = rule.getId() + "." + index + "." + module.getType();
		return id;
	}

	public void onTriggerHandlerAdded(TriggerHandler triggerHandler) {
		int index = 0;
		for (TriggerRef trigger : rule.getTriggers()) {
			if (triggerHandler.getName().equals(trigger.getType())) {
				ModuleContext context = createModuleContext(trigger, index++);
				triggerHandler.addListener(context, this);
			}
		}
	}

	public void onTriggerHandlerRemoved(TriggerHandler triggerHandler) {
		int index = 0;
		for (TriggerRef trigger : rule.getTriggers()) {
			if (triggerHandler.getName().equals(trigger.getType())) {
				ModuleContext context = moduleContexts.get(createContextId(
						trigger, index++));
				triggerHandler.removeListener(context, this);
			}
		}
	}

	public void disable() {
		this.rule.setEnabled(false);
		int index = 0;
		for (TriggerRef trigger : rule.getTriggers()) {
			ModuleHandler handler = ModuleHandlerResolver.resolve(trigger,
					this.bundleContext);
			if (handler instanceof TriggerHandler) {
				ModuleContext context = moduleContexts.get(createContextId(
						trigger, index++));
				((TriggerHandler) handler).removeListener(context, this);
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
	public void execute(ModuleContext context) {
		importOutputParameters("on", context);
		if (ifCondition()) {
			thenExecute();
		}

	}

	/**
	 * imports OutputParameters which come from an outputParameter
	 * 
	 * @param context
	 */
	public void importOutputParameters(String moduleType, ModuleContext context) {
		for (Entry<String, Object> entry : context.getOutputParameters()
				.entrySet()) {
			String index = context.getId().split("\\.")[1];
			ruleParameters.put(
					"${" + moduleType + "." + index + "." + entry.getKey()
							+ "}", entry.getValue());
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
		int index = 0;
		for (ConditionRef condition : conditions) {
			ModuleHandler handler = ModuleHandlerResolver.resolve(condition,
					this.bundleContext);
			if (handler instanceof ConditionHandler) {
				ModuleContext moduleContext = createModuleContext(
						condition, index++);
				if (!((ConditionHandler) handler).evaluate(moduleContext)) {
					importOutputParameters("if", moduleContext);
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
		int index = 0;
		for (ActionRef action : rule.getActions()) {
			ModuleHandler handler = ModuleHandlerResolver.resolve(action,
					this.bundleContext);
			if (handler instanceof ActionHandler) {
				ModuleContext context = createModuleContext(action,
						index++);
				((ActionHandler) handler).execute(context);
				importOutputParameters("then", context);
			}
		}
		return true;
	}

}
