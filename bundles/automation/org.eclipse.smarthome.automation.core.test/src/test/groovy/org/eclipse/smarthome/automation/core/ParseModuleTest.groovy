package org.eclipse.smarthome.automation.core;

import static org.junit.Assert.*

import org.codehaus.jackson.map.ObjectMapper
import org.eclipse.smarthome.automation.core.jsonmodel.Module
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test

class ParseModuleTest {

	@Test
	public void test() {
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
                    "label": "Recipient",
                    "required": false
                }
            ]
        }"""
		def objectMapper = new ObjectMapper();
		def Module module = objectMapper.readValue(json, Module);
		assertThat module, is(notNullValue())
		assertEquals(module.name, "SendMail")
	}

}
