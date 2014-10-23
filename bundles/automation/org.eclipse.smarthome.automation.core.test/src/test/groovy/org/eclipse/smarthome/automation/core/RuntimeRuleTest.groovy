package org.eclipse.smarthome.automation.core;


import static org.junit.Assert.*

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import org.eclipse.smarthome.core.events.EventPublisher
import org.eclipse.smarthome.core.items.ItemProvider
import org.eclipse.smarthome.core.items.ItemRegistry
import org.eclipse.smarthome.core.library.items.NumberItem
import org.eclipse.smarthome.core.library.items.SwitchItem
import org.eclipse.smarthome.core.library.types.OnOffType
import org.eclipse.smarthome.test.OSGiTest
import org.junit.Before
import org.junit.Test
import org.osgi.service.event.Event
import org.osgi.service.event.EventHandler

class RuntimeRuleTest extends OSGiTest{

	@Before
	void before() {
		def itemProvider = [
			getAll: {[new SwitchItem("switch"), new NumberItem("number")]},
			addProviderChangeListener: {},
			removeProviderChangeListener: {},
			allItemsChanged: {}] as ItemProvider
		registerService(itemProvider)
	}
	
	
	@Test
	public void test() {
        
        Thread.sleep(100)
        
		def EventPublisher eventPublisher = getService(EventPublisher)
		def ItemRegistry itemRegistry = getService(ItemRegistry)
		SwitchItem swItem = itemRegistry.getItem("switch")
        
        Event event = null
       
        def eventHandler = [
            handleEvent: { Event e -> event = e}
        ] as EventHandler
        
        registerService(eventHandler, ["event.topics": "smarthome/command/number"] as Hashtable)
        
		eventPublisher.postUpdate("switch", OnOffType.ON)
        waitFor { event != null }
        Thread.sleep(60000)
        assertThat event.topic, is(equalTo("smarthome/command/number"))
        assertThat event.getProperty("command").intValue(), is(23)
	}
	
	
	
}
