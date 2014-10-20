package org.eclipse.smarthome.automation.core;

import static org.junit.Assert.*

import org.codehaus.jackson.map.ObjectMapper
import org.eclipse.smarthome.automation.core.jsonmodel.ModuleRef
import org.eclipse.smarthome.automation.core.jsonmodel.Rule
import org.junit.Test

class ParseRuleTest {

	private RulesParser getRuleParser(){
		return new RulesParser();
	}
	
	@Test
	public void test() {
		def json = """{
						  "name" : "Heat up in the Morning",
						  "enabled": true,
						  "on": [
						    {
						      "type": "Timer",
						      "parameters": {
						          "time": "30 6 * * *"
						      }
						    }
						  ],
						  "if": [
						    {
						      "type": "Compare",
						      "parameters": {
						        "operator": "<",
						        "left": "TemperatureItem",
						        "right": "18"
						      }
						    }
						  ],
						  "then": [
						    {
						      "type": "SendCommand",
						      "parameters": {
						        "itemName": "TemperatureActuator",
						        "command": "23"
						      }
						    }
						  ]
						}
		 """

		def Rule rule = getRuleParser().parseRule(json)
		assertNotNull rule
		assertEquals(rule.name, "Heat up in the Morning")
		assertEquals(rule.enabled, true)
		assertNotNull(rule._if)
		assertNotNull(rule._then)
		assertNotNull(rule.on)
		assertFalse(rule._if.empty)
		assertFalse(rule._then.empty)
		assertFalse(rule.on.empty)
		assertEquals(rule._if.size(), 1)
		assertEquals(rule._then.size(),1)
		assertEquals(rule.on.size(), 1)
		
		def ModuleRef timer = rule.on.get(0)
		assertEquals(timer.type,"Timer")
		assertNotNull(timer.parameters)
		assertFalse(timer.parameters.isEmpty())
		assertEquals(timer.parameters.get("time"),"30 6 * * *")
		
		def ModuleRef compare = rule._if.get(0)
		assertEquals(compare.type, "Compare")
		assertNotNull(compare.parameters)
		assertEquals(compare.parameters.size(),3)
		assertEquals(compare.parameters.get("operator"),"<")
		assertEquals(compare.parameters.get("left"),"TemperatureItem")
		assertEquals(compare.parameters.get("right"),"18")

		def ModuleRef sendCommand = rule._then.get(0)
		assertEquals(sendCommand.type,"SendCommand")
		assertNotNull(sendCommand.parameters)
		assertEquals(sendCommand.parameters.size(),2)
		assertEquals(sendCommand.parameters.get("itemName"),"TemperatureActuator")
		assertEquals(sendCommand.parameters.get("command"),"23")
	}
}
