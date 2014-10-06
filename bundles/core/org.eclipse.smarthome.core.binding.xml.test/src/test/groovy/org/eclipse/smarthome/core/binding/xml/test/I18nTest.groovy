/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.binding.xml.test

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import org.eclipse.smarthome.config.core.ConfigDescription;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter;
import org.eclipse.smarthome.config.core.ConfigDescriptionRegistry;
import org.eclipse.smarthome.core.binding.BindingInfo
import org.eclipse.smarthome.core.binding.BindingInfoRegistry;
import org.eclipse.smarthome.test.OSGiTest
import org.eclipse.smarthome.test.SyntheticBundleInstaller
import org.junit.After
import org.junit.Before
import org.junit.Ignore;
import org.junit.Test
import org.osgi.framework.Bundle

class I18nTest extends OSGiTest {

    static final String TEST_BUNDLE_NAME = "yahooweather.bundle"

    BindingInfoRegistry bindingInfoRegistry

    @Before
    void setUp() {
        bindingInfoRegistry = getService(BindingInfoRegistry)
        assertThat bindingInfoRegistry, is(notNullValue())
    }

    @After
    void tearDown() {
        SyntheticBundleInstaller.uninstall(getBundleContext(), TEST_BUNDLE_NAME)
    }

    @Test
    void 'assert config decriptions were localized'() {
        def bundleContext = getBundleContext()
        def initialNumberOfBindingInfos = bindingInfoRegistry.getBindingInfos().size()
        
        // install test bundle
        Bundle bundle = SyntheticBundleInstaller.install(bundleContext, TEST_BUNDLE_NAME)
        assertThat bundle, is(notNullValue())
        
        def bindingInfos = bindingInfoRegistry.getBindingInfos(Locale.GERMAN)
        assertThat bindingInfos.size(), is(initialNumberOfBindingInfos + 1)
        BindingInfo bindingInfo = bindingInfos.first()

        assertThat bindingInfo, is(notNullValue())
        assertThat asString(bindingInfo), is(equalTo(
"""
binding.name = Yahoo Wetter Binding
binding.description = Das Yahoo Wetter Binding stellt verschiedene Wetterdaten wie die Temperatur, die Luftfeuchtigkeit und den Luftdruck für konfigurierbare Orte vom yahoo Wetterdienst bereit
"""
        ))

    }
    
    String asString(final BindingInfo self) {
       def name = self.getName()
       def description = self.getDescription() 
return """
binding.name = ${name}
binding.description = ${description}
"""
    }
    

}
