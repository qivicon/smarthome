/**
 * 
 */
package org.eclipse.smarthome.automation.core;

import java.util.ArrayList;
import java.util.Collections;
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
	@JsonProperty("on")
	private List<TriggerRef> triggers = new ArrayList<TriggerRef>();
	@JsonProperty("if")
	private List<ConditionRef> conditions = new ArrayList<ConditionRef>();
	@JsonProperty("then")
	private List<ActionRef> actions = new ArrayList<ActionRef>();
	
	
	//TODO REMOVE METHODS....

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

	public List<TriggerRef> getTriggers() {
		return Collections.unmodifiableList(triggers);
	}

	public void addTrigger(TriggerRef trigger) {
		triggers.add(trigger);
	}

	public List<ConditionRef> getConditions() {
		return Collections.unmodifiableList(conditions);
	}

	public void addCondition(ConditionRef condition) {
		conditions.add(condition);
	}

	public List<ActionRef> getActions() {
		return Collections.unmodifiableList(actions);
	}

	public void addAction(ActionRef action) {
		actions.add(action);
	}

}
