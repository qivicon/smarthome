package org.eclipse.smarthome.automation.core;


import static org.junit.Assert.*

import org.eclipse.smarthome.core.events.EventPublisher
import org.eclipse.smarthome.core.items.Item
import org.eclipse.smarthome.core.items.ItemProvider
import org.eclipse.smarthome.core.items.ItemRegistry
import org.eclipse.smarthome.core.library.items.NumberItem
import org.eclipse.smarthome.core.library.items.SwitchItem
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType
import org.eclipse.smarthome.test.OSGiTest
import org.junit.Before
import org.junit.Test

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
		def EventPublisher eventPublisher = getService(EventPublisher)
		def ItemRegistry itemRegistry = getService(ItemRegistry)
		SwitchItem swItem = itemRegistry.getItem("switch")
		Thread.sleep(2000)
		eventPublisher.postUpdate("switch", OnOffType.ON)
		Thread.sleep(1100)
		Item number = itemRegistry.getItem("number")
		assertEquals(number.getState().intValue(),23)
		
	}
	
	
	
}
