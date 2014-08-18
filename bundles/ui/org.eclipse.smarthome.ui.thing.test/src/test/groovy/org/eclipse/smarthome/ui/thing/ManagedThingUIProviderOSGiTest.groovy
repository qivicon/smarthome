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

import org.eclipse.smarthome.core.storage.Storage
import org.eclipse.smarthome.core.storage.StorageService
import org.eclipse.smarthome.core.thing.ThingUID
import org.eclipse.smarthome.test.OSGiTest
import org.junit.Before
import org.junit.Test

/**
 * Tests for {@link ThingUIRegistry}.
 * 
 * @author Dennis Nobel - Initital contribution
 */
class ManagedThingUIProviderOSGiTest extends OSGiTest {

    ManagedThingUIProvider managedThingUIProvider
    ThingUIRegistry thingUIRegistry

    def THING_UID = new ThingUID("b:t:i")
    def THING_UID_2 = new ThingUID("b:t:i2")
    def THING_UID_3 = new ThingUID("b:t:i3")

    @Before
    void setup() {
        registerVolatileStorageService()
        managedThingUIProvider = getService(ManagedThingUIProvider)
        thingUIRegistry = getService(ThingUIRegistry)
    }

    @Test
    void 'assert provider stores labels'() {

        managedThingUIProvider.setLabel THING_UID, "myLabel"
        def label = managedThingUIProvider.getLabel(THING_UID)

        assertThat label, is(equalTo("myLabel"))
    }

    @Test
    void 'assert registry uses label from provider'() {
        managedThingUIProvider.setLabel THING_UID_2, "myLabel3"
        def label =thingUIRegistry.getLabel(THING_UID_2)
        assertThat label, is(equalTo("myLabel3"))
    }

    @Test
    void 'assert label is updated in registry'() {
        managedThingUIProvider.setLabel THING_UID, "myLabel"
        def label =thingUIRegistry.getLabel(THING_UID)
        assertThat label, is(equalTo("myLabel"))

        managedThingUIProvider.setLabel THING_UID, "yourLabel"
        label =thingUIRegistry.getLabel(THING_UID)
        assertThat label, is(equalTo("yourLabel"))
    }

    @Test
    void 'assert provider caches labels'() {
        unregisterMocks()
        
        def numberOfStorageCalls = 0
        def storage = [
            get: {
                numberOfStorageCalls++;
                return "label"
            },
            put: {}
        ] as Storage
        registerService([
            getStorage: { a,b -> storage}
        ] as StorageService)
        
        managedThingUIProvider = getService(ManagedThingUIProvider)

        managedThingUIProvider.getLabel THING_UID_3
        managedThingUIProvider.getLabel THING_UID_3
        
        assertThat numberOfStorageCalls, is(1)
    }
}