package org.eclipse.smarthome.automation.core;

import static org.junit.Assert.*

import org.eclipse.smarthome.automation.core.module.Module;
import org.eclipse.smarthome.automation.core.parser.RulesParser;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test

/**
 * Test class for parsing json strings containing module definitions.
 * @author niehues
 *
 */
class ParseModuleTest {
	
	private RulesParser getRuleParser(){
		return new RulesParser();
	}
	
	@Test
	public void testMailModule() {
		String json = """{
            "name": "SendMail",
            "moduleType": "action",
            "label": "Send an email",
            "shortcut": "@",
            "description": "Allows to send a Mail",
            "inputParameters": [
                {
                    "name": "recipient",
                    "type": "text",
                    "label": "Recipient",
                    "context": "email",
                    "required": true
                },
                {
                    "name": "subject",
                    "type": "text",
                    "label": "Recipient",
                    "required": true
                },
                {
                    "name": "body",
                    "type": "text",
                    "label": "Body",
                    "required": false
                }
            ]
        }"""
		def Module module = getRuleParser().parseModule(json);
		assertThat module, is(notNullValue())
		assertEquals(module.name, "SendMail")
		assertEquals(module.moduleType,"action")
		assertEquals(module.description,"Allows to send a Mail")
		assertEquals(module.label,"Send an email")
		assertEquals(module.shortcut, "@")
		def inputParameters = module.inputParameters
		assertNotNull(inputParameters)
		assertNull(module.outputParameters)
		assertFalse inputParameters.empty
		for (inputParam in inputParameters) {
			switch (inputParam.name){
				case "recipient":
					assertEquals(inputParam.context, "email")
					assertEquals(inputParam.label,"Recipient")
					assertEquals(inputParam.required, true)
					assertEquals(inputParam.type, "text")
					break
				case "subject":
					assertEquals(inputParam.type, "text")
					assertEquals(inputParam.label, "Recipient")
					assertEquals(inputParam.required, true)
					break
				case "body":
					assertEquals(inputParam.type, "text")
					assertEquals(inputParam.label, "Body")
					assertEquals(inputParam.required, false)
					break
				default: fail("inputParameter not expected: " + inputParam.name)
			}
		}
	}

	@Test
	void testTimerModule() {
		def json = """{
			"name": "Timer",
			"moduleType": "trigger",
			"label": "Timer",
			"shortcut": "cron",
			"inputParameters": [
				{
					"name": "timer",
					"type": "text",
					"label": "Time",
					"context": "cron",
					"required": true
				}
			],
			"outputParameters": [
				{
					"name": "time",
					"type": "datetime",
					"label": "Time of the trigger"
				}
			]
		}"""
		def Module module = getRuleParser().parseModule(json);
		assertThat module, is(notNullValue())
		assertEquals(module.name, "Timer")
		assertEquals(module.moduleType, "trigger")
		assertNull(module.description)
		assertEquals(module.label,"Timer")
		assertEquals(module.shortcut, "cron")
		def inputParameters = module.inputParameters
		assertNotNull(inputParameters)
		assertFalse inputParameters.empty
		for (inputParam in module.inputParameters) {
			switch(inputParam.name){
				case "timer":
					assertEquals(inputParam.type, "text")
					assertEquals(inputParam.label, "Time")
					assertEquals(inputParam.context, "cron")
					assertEquals(inputParam.required, true)
					break
				default:
					fail("inputParameter " + inputParam.name + " is not expected")
			}
		}
		for (outParam in module.outputParameters) {
			switch(outParam.name){
				case "time":
					assertEquals(outParam.label,"Time of the trigger")
					assertEquals(outParam.type, "datetime")

					break
				default:
					fail("outputParameter " + outputParam.name + " is not expected")
			}
		}
	}
}
