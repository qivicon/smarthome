/**
 * 
 */
package org.eclipse.smarthome.automation.core;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author niehues
 *
 */
public class RulesParser {
	ObjectMapper objectMapper;
	
	public RulesParser(){
		this.objectMapper= new ObjectMapper();
	}
	
	
	public Object parseJsonString(String jsonString){
		
		return null;
	}
}
