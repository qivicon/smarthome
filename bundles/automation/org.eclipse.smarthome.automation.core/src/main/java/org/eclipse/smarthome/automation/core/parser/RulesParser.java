/**
 * 
 */
package org.eclipse.smarthome.automation.core.parser;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.smarthome.automation.core.Rule;
import org.eclipse.smarthome.automation.core.module.Module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author niehues
 *
 */
public class RulesParser {
    private Gson gson;

	public RulesParser() {
	    final GsonBuilder gsonBuilder = new GsonBuilder();
	    this.gson = gsonBuilder.create();
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
		    return gson.fromJson(jsonString, Module.class);
		} catch (Exception e) {
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
		    return gson.fromJson(jsonString, Rule.class);
		} catch (Exception e) {
			throw new RuleParserException(e);
		}
	}
	
	/**
     * parses a json formatted inputStream
     * 
     * @param inputStream
     * @return
     * @throws RuleParserException
     */
    public Rule parseRule(InputStream inputStream) throws RuleParserException {
        try {
            return gson.fromJson(new InputStreamReader(inputStream), Rule.class);
        } catch (Exception e) {
            throw new RuleParserException(e);
        }
    }
}
