package org.eclipse.smarthome.automation.core;

import static org.junit.Assert.*

import org.eclipse.smarthome.automation.core.parser.RulesParser;
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
						  "id":"xyz",
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
						        "command": 23
						      }
						    }
						  ]
						}
		 """

		def Rule rule = getRuleParser().parseRule(json)
		assertNotNull rule
		assertEquals(rule.name, "Heat up in the Morning")
		assertEquals(rule.enabled, true)
		assertEquals(rule.id, "xyz")
		assertNotNull(rule.triggers)
		assertNotNull(rule.actions)
		assertNotNull(rule.triggers)
		assertFalse(rule.triggers.empty)
		assertFalse(rule.actions.empty)
		assertFalse(rule.triggers.empty)
		assertEquals(rule.conditions.size(), 1)
		assertEquals(rule.actions.size(),1)
		assertEquals(rule.triggers.size(), 1)
		
		def ModuleRef timer = rule.triggers.get(0)
		assertEquals(timer.type,"Timer")
		assertNotNull(timer.parameters)
		assertFalse(timer.parameters.isEmpty())
		assertEquals(timer.parameters.get("time"),"30 6 * * *")
		
		def ModuleRef compare = rule.conditions.get(0)
		assertEquals(compare.type, "Compare")
		assertNotNull(compare.parameters)
		assertEquals(compare.parameters.size(),3)
		assertEquals(compare.parameters.get("operator"),"<")
		assertEquals(compare.parameters.get("left"),"TemperatureItem")
		assertEquals(compare.parameters.get("right"),"18")

		def ModuleRef sendCommand = rule.actions.get(0)
		assertEquals(sendCommand.type,"SendCommand")
		assertNotNull(sendCommand.parameters)
		assertEquals(sendCommand.parameters.size(),2)
		assertEquals(sendCommand.parameters.get("itemName"),"TemperatureActuator")
		assertEquals(sendCommand.parameters.get("command") as double, 23.0, 0.0)
	}
}
