/**
 * 
 */
package org.eclipse.smarthome.automation.core.jsonmodel;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * This class represents the Rule which can be specified in json-Format
 * 
 * @author niehues
 *
 */
public class Rule {

	private String name;
	private boolean enabled;
	private List<ModuleRef> on;
	private List<ModuleRef> _if;
	private List<ModuleRef> _then;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<ModuleRef> getOn() {
		return on;
	}

	public void setOn(List<ModuleRef> on) {
		this.on = on;
	}

	public List<ModuleRef> get_if() {
		return _if;
	}

	@JsonProperty("if")
	public void set_if(List<ModuleRef> _if) {
		this._if = _if;
	}

	public List<ModuleRef> get_then() {
		return _then;
	}

	@JsonProperty("then")
	public void set_then(List<ModuleRef> _then) {
		this._then = _then;
	}

}
