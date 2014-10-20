package org.eclipse.smarthome.automation.core.runtimemodel;

public interface IRuntimeRule extends ITriggerListener {

	boolean ifCondition();

	boolean thenExecute();
}
