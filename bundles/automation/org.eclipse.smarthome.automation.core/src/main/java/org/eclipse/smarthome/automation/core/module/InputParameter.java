package org.eclipse.smarthome.automation.core.module;

/**
 * This is the class for modules input parameters.
 * @author niehues
 *
 */
public class InputParameter extends Parameter {

	private String context;
	private boolean required;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
