package org.eclipse.smarthome.automation.core;


import static org.junit.Assert.*

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import java.lang.ProcessEnvironment.Variable;

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
			getAll: {[new SwitchItem("sample1Button"), new SwitchItem("lamp"), new SwitchItem("lamp2"), new SwitchItem("sample4Button"), new SwitchItem("presence"), new SwitchItem("lamp4")]},
			addProviderChangeListener: {},
			removeProviderChangeListener: {},
			allItemsChanged: {}] as ItemProvider
		registerService(itemProvider)
	}
	
	
	@Test
	public void testSample1() {
        
        Thread.sleep(100)
        
		def EventPublisher eventPublisher = getService(EventPublisher)
		def ItemRegistry itemRegistry = getService(ItemRegistry)
		SwitchItem swItem = itemRegistry.getItem("sample1Button")
        
        Event event = null
       
        def eventHandler = [
            handleEvent: { Event e -> event = e}
        ] as EventHandler
        
        registerService(eventHandler, ["event.topics": "smarthome/command/lamp"] as Hashtable)
        
		eventPublisher.postUpdate("sample1Button", OnOffType.ON)
        waitFor { event != null }
        assertThat event.topic, is(equalTo("smarthome/command/lamp"))
        assertThat event.getProperty("command"), is(OnOffType.ON)
		event = null;
		
		eventPublisher.postUpdate("sample1Button", OnOffType.OFF);
		waitFor{event !=null}
		assertThat event.topic, is(equalTo("smarthome/command/lamp"))
		assertThat event.getProperty("command"), is(OnOffType.OFF)
	}
	
	@Test
	public void testSample2() {
		
		def ItemRegistry itemRegistry = getService(ItemRegistry)
		Thread.sleep(100)
		
		Event event = null
	   
		def eventHandler = [
			handleEvent: { Event e -> event = e}
		] as EventHandler
		
		registerService(eventHandler, ["event.topics": "smarthome/command/lamp2"] as Hashtable)
//		eventPublisher.postUpdate("sample1Button", OnOffType.ON)
		def item = itemRegistry.getItem("lamp2") as SwitchItem
		
		// initialize Item to be OFF
		item.setState(OnOffType.OFF)
		waitFor({event!=null}, 10000) 
		assertThat event.topic, is(equalTo("smarthome/command/lamp2"))
		assertThat event.getProperty("command"), is(OnOffType.ON)
		event = null;
		// set it to on because the eventHandler doesn't do it
		item.setState(OnOffType.ON)
//		eventPublisher.postUpdate("sample1Button", OnOffType.OFF);
		waitFor({event !=null},10000)
		assertThat event.topic, is(equalTo("smarthome/command/lamp2"))
		assertThat event.getProperty("command"), is(OnOffType.OFF)
	}
	
	@Test
	public void testSample4() {
		def EventPublisher eventPublisher = getService(EventPublisher)
		def ItemRegistry itemRegistry = getService(ItemRegistry)
		Thread.sleep(100)
		
		Event event = null
	   
		def eventHandler = [
			handleEvent: { Event e -> event = e}
		] as EventHandler
		
		registerService(eventHandler, ["event.topics": "smarthome/command/lamp4"] as Hashtable)
//		eventPublisher.postUpdate("sample1Button", OnOffType.ON)
		def switch4Button = itemRegistry.getItem("sample4Button") as SwitchItem
		def presence = itemRegistry.getItem("presence") as SwitchItem
		def lamp4 = itemRegistry.getItem("lamp4") as SwitchItem
		
		//prepare initial state presence OFF
		presence.setState(OnOffType.OFF)
		lamp4.setState(OnOffType.OFF)
		
		// first scenario switch on / presence off --> nothing should happen
		eventPublisher.postUpdate("sample4Button", OnOffType.ON)
		waitFor{event!=null}
		assertNull(event)
		
		// second scenario switch OFF / presence OFF --> nothing should happen
		eventPublisher.postUpdate("sample4Button", OnOffType.OFF)
		waitFor{event!=null}
		assertNull(event)
		
		// third scenario switch ON / presence ON --> lamp should be on
		presence.setState(OnOffType.ON)
		event=null
		eventPublisher.postUpdate("sample4Button", OnOffType.ON)
		Thread.sleep(100)
		waitFor{event!=null}
		assertThat event.topic, is(equalTo("smarthome/command/lamp4"))
		assertThat event.getProperty("command"), is(OnOffType.ON)
		event=null
		
		// fourth scenario switch OFF / presence ON --> lamp should be OFF
		eventPublisher.postUpdate("sample4Button", OnOffType.OFF)
		waitFor {event!=null}
		assertThat event.topic, is(equalTo("smarthome/command/lamp4"))
		assertThat event.getProperty("command"), is(OnOffType.OFF)
		
	}
}
