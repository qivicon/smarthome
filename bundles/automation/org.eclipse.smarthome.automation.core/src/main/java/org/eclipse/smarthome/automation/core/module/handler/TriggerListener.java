package org.eclipse.smarthome.automation.core.module.handler;

/**
 * Listener interface for Triggers
 * @author niehues
 *
 */
public interface TriggerListener {

	/**
	 * executes the Listener
	 */
	void execute(ModuleContext context);
}
