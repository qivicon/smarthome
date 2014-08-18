/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.ui.thing

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import org.eclipse.smarthome.core.thing.ThingUID
import org.eclipse.smarthome.test.OSGiTest
import org.junit.Before
import org.junit.Test

/**
 * Tests for {@link ThingUIRegistry}.
 * 
 * @author Dennis Nobel - Initital contribution
 */
class ThingUIRegistryOSGiTest extends OSGiTest {

    ThingUIRegistry thingUIRegistry
    def THING_UID = new ThingUID("b:t:i")
    def THING_UID_2 = new ThingUID("b:t:i2")
    
    @Before
    void setup() {
        thingUIRegistry = getService(ThingUIRegistry)
    }
    
    @Test
    void 'assert ThingUIRegistry tracks ThingUIProviders'() {
        
        def label = thingUIRegistry.getLabel(THING_UID)
        assertThat label, is(null)
        
        registerService ([ getLabel: { "myLabel" }] as ThingUIProvider)
        
        label = thingUIRegistry.getLabel(THING_UID)
        
        assertThat label, is(equalTo("myLabel"))
    }
    
}
