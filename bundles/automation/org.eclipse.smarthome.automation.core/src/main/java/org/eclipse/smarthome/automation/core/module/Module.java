/**
 * 
 */
package org.eclipse.smarthome.automation.core.module;

import java.util.List;

/**
 * This class represents the module which can be defined in json format. It is
 * part of the object model for the rule engine.
 * 
 * @author niehues
 *
 */
public class Module {
	private String name;
	private String moduleType;
	private String label;
	private String shortcut;
	private String description;
	private List<InputParameter> inputParameters;
	//--> better use ConfigDescription as InputParameter Type
	private List<OutputParameter> outputParameters;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getShortcut() {
		return shortcut;
	}

	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<InputParameter> getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(List<InputParameter> inputParameters) {
		this.inputParameters = inputParameters;
	}

	public List<OutputParameter> getOutputParameters() {
		return outputParameters;
	}

	public void setOutputParameters(List<OutputParameter> outputParameters) {
		this.outputParameters = outputParameters;
	}

}
