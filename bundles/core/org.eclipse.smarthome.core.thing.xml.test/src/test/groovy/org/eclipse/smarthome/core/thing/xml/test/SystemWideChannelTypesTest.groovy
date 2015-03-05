/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.xml.test

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import org.eclipse.smarthome.core.thing.binding.ThingTypeProvider
import org.eclipse.smarthome.core.thing.type.BridgeType
import org.eclipse.smarthome.core.thing.type.ChannelDefinition
import org.eclipse.smarthome.core.thing.type.SystemChannelTypeProvider;
import org.eclipse.smarthome.core.thing.type.ThingType
import org.eclipse.smarthome.test.OSGiTest
import org.eclipse.smarthome.test.SyntheticBundleInstaller
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.osgi.framework.Bundle

class SystemWideChannelTypesTest extends OSGiTest {

    static final String SYSTEM_CHANNELS_BUNDLE_NAME = "SystemChannels.bundle"
    
    static final String SYSTEM_CHANNELS_USER_BUNDLE_NAME = "SystemChannelsUser.bundle"

    ThingTypeProvider thingTypeProvider
    
    SystemChannelTypeProvider systemChannelTypeProvider

    @Before
    void setUp() {
        thingTypeProvider = getService(ThingTypeProvider)
        systemChannelTypeProvider = getService(SystemChannelTypeProvider)
        assertThat thingTypeProvider, is(notNullValue())
        assertThat systemChannelTypeProvider, is(notNullValue())
    }

    @After
    void tearDown() {
        SyntheticBundleInstaller.uninstall(getBundleContext(), SYSTEM_CHANNELS_BUNDLE_NAME)
        SyntheticBundleInstaller.uninstall(getBundleContext(), SYSTEM_CHANNELS_USER_BUNDLE_NAME)
    }
   
    @Test
    void 'assert that System Channels are loaded and unloaded'() {
        def bundleContext = getBundleContext()
        def initialNumberOfThingTypes = thingTypeProvider.getThingTypes(null).size()
        
        assertThat systemChannelTypeProvider.getSystemChannelTypes().isEmpty(), is(true)

        // install test bundle
        Bundle bundle = SyntheticBundleInstaller.install(bundleContext, SYSTEM_CHANNELS_BUNDLE_NAME)
        assertThat bundle, is(notNullValue())

        def thingTypes = thingTypeProvider.getThingTypes(null)
        assertThat thingTypes.size(), is(initialNumberOfThingTypes + 1)
        
        assertThat systemChannelTypeProvider.getSystemChannelTypes().size(), is(1)

        // uninstall test bundle
        bundle.uninstall();
        assertThat bundle.state, is(Bundle.UNINSTALLED)

        thingTypes = thingTypeProvider.getThingTypes(null)
        assertThat thingTypes.size(), is(initialNumberOfThingTypes)
        
        assertThat systemChannelTypeProvider.getSystemChannelTypes().isEmpty(), is(true)
        
    }
    
    @Test
    void 'assert that System Channels are used by other binding'() {
        def bundleContext = getBundleContext()
        def initialNumberOfThingTypes = thingTypeProvider.getThingTypes(null).size()
        
        assertThat systemChannelTypeProvider.getSystemChannelTypes().isEmpty(), is(true)

        // install test bundle
        Bundle sysBundle = SyntheticBundleInstaller.install(bundleContext, SYSTEM_CHANNELS_BUNDLE_NAME)
        assertThat sysBundle, is(notNullValue())
        
        Bundle sysUserBundle = SyntheticBundleInstaller.install(bundleContext, SYSTEM_CHANNELS_USER_BUNDLE_NAME)
        assertThat sysUserBundle, is(notNullValue())

        def thingTypes = thingTypeProvider.getThingTypes(null)
        assertThat thingTypes.size(), is(initialNumberOfThingTypes + 2)
        
        assertThat systemChannelTypeProvider.getSystemChannelTypes().size(), is(1)
       
    }

}
