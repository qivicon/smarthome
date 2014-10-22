/**
 * 
 */
package org.eclipse.smarthome.automation.core.module.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author niehues
 *
 */
public class RuleContext {

	
	private Map<String, Object> parmeterMap;
	
	public RuleContext(){
		this.parmeterMap = new HashMap<String, Object>();
	}
	
	public void addParameter(String key, Object value){
		parmeterMap.put(key, value);
	}
	
	
}
