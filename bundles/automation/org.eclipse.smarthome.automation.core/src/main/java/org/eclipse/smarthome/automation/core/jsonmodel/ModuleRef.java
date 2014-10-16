package org.eclipse.smarthome.automation.core.jsonmodel;

import java.util.Map;

/**
 * This class represents a reference to a module within the json representation
 * 
 * @author niehues
 *
 */
public class ModuleRef {

	private String type;
	private Map<String, String> parameters;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

}
