/**
 * 
 */
package org.eclipse.smarthome.automation.core.module;

/**
 * @author niehues
 * 
 *         This is an abstract class for module parameters. Module parameters
 *         can be input and output parameters.
 *
 */
public abstract class Parameter {

	private String name;
	private String type;
	private String label;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
