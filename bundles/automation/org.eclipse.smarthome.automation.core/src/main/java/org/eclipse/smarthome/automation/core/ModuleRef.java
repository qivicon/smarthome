package org.eclipse.smarthome.automation.core;

import java.util.Collections;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * This class represents a reference to a module within the json representation
 * 
 * @author niehues
 *
 */
public abstract class ModuleRef {

	private String type;
	@JsonDeserialize(keyAs = String.class, contentAs = Object.class)
	private Map<String, Object> parameters;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

	public void addParameter(String s, Object o) {
		this.parameters.put(s, o);
	}

	// public void setParameters(Map<String, Object> parameters) {
	// this.parameters = parameters;
	// }

}
