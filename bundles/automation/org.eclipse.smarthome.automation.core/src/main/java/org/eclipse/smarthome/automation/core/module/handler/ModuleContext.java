/**
 * 
 */
package org.eclipse.smarthome.automation.core.module.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author niehues
 *
 */
public class ModuleContext {

	private final Map<String, Object> inputParameters;
	private Map<String, Object> outputParameters;
	private String id;

	public ModuleContext(String id, final Map<String, Object> inputParameters) {
		this.inputParameters = inputParameters;
		this.id = id;
		outputParameters = new HashMap<String, Object>();
	}

	public void addInputParameter(String key, Object value) {
		inputParameters.put(key, value);
	}

	public void addOutputParameter(String key, Object value) {
		outputParameters.put(key, value);
	}

	public Object getInputParameter(String key) {
		return inputParameters.get(key);
	}

	public Map<String, Object> getOutputParameters() {
		return Collections.unmodifiableMap(outputParameters);
	}

	public String getId() {
		return id;
	}
}
