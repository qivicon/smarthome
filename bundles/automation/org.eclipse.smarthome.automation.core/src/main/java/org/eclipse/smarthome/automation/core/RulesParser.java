/**
 * 
 */
package org.eclipse.smarthome.automation.core;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.smarthome.automation.core.jsonmodel.Module;
import org.eclipse.smarthome.automation.core.jsonmodel.Rule;

/**
 * @author niehues
 *
 */
public class RulesParser {
	ObjectMapper objectMapper;

	public RulesParser() {
		this.objectMapper = new ObjectMapper();
	}

	/**
	 * parses a json formatted module string
	 * 
	 * @param jsonString
	 * @return
	 * @throws RuleParserException
	 */
	public Module parseModule(String jsonString) throws RuleParserException {
		try {
			return objectMapper.readValue(jsonString, Module.class);
		} catch (IOException e) {
			throw new RuleParserException(e);
		}
	}

	/**
	 * parses a json formatted rule-string
	 * 
	 * @param jsonString
	 * @return
	 * @throws RuleParserException
	 */
	public Rule parseRule(String jsonString) throws RuleParserException {
		try {
			return objectMapper.readValue(jsonString, Rule.class);
		} catch (IOException e) {
			throw new RuleParserException(e);
		}
	}
}
