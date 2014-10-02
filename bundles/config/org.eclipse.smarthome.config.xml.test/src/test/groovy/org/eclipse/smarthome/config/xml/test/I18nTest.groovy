/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.xml.test

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import org.eclipse.smarthome.config.core.ConfigDescription;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter;
import org.eclipse.smarthome.config.core.ConfigDescriptionRegistry;
import org.eclipse.smarthome.test.OSGiTest
import org.eclipse.smarthome.test.SyntheticBundleInstaller
import org.junit.After
import org.junit.Before
import org.junit.Ignore;
import org.junit.Test
import org.osgi.framework.Bundle

class I18nTest extends OSGiTest {

    static final String TEST_BUNDLE_NAME = "yahooweather.bundle"

    ConfigDescriptionRegistry configDescriptionRegistry

    @Before
    void setUp() {
        configDescriptionRegistry = getService(ConfigDescriptionRegistry)
        assertThat configDescriptionRegistry, is(notNullValue())
    }

    @After
    void tearDown() {
        SyntheticBundleInstaller.uninstall(getBundleContext(), TEST_BUNDLE_NAME)
    }

    @Test
    void 'assert config decriptions were localized'() {
        def bundleContext = getBundleContext()
        def initialNumberOfConfigDescriptions = configDescriptionRegistry.getConfigDescriptions().size()
        
        // install test bundle
        Bundle bundle = SyntheticBundleInstaller.install(bundleContext, TEST_BUNDLE_NAME)
        assertThat bundle, is(notNullValue())
        
        def configDescriptions = configDescriptionRegistry.getConfigDescriptions(Locale.GERMAN)
        assertThat configDescriptions.size(), is(initialNumberOfConfigDescriptions + 1)
        
        def config = configDescriptions.find { it.uri.equals(new URI("thing-type://yahooweather:weather")) } as ConfigDescription
        

        assertThat config, is(notNullValue())
        assertThat asString(config), is(equalTo(
"""
thing-type.config.yahooweather.weather.location.label = Ort
thing-type.config.yahooweather.weather.location.description = Ort der Wetterinformation.
thing-type.config.yahooweather.weather.unit.label = Einheit
thing-type.config.yahooweather.weather.unit.description = Spezifiziert die Einheit der Daten. Valide Werte sind 'us' und 'metric'
thing-type.config.yahooweather.weather.refresh.label = Aktualisierungsintervall
thing-type.config.yahooweather.weather.refresh.description = Spezifiziert das Aktualisierungsintervall in Sekunden
"""
        ))

    }
    
    String asString(final ConfigDescription self) {
       def location = self.getParameters().find { it.getName().equals("location") } as ConfigDescriptionParameter
       def location_label = location.getLabel()
       def location_description = location.getDescription() 
       def unit = self.getParameters().find { it.getName().equals("unit") } as ConfigDescriptionParameter
       def unit_label = unit.getLabel()
       def unit_description = unit.getDescription() 
       def refresh = self.getParameters().find { it.getName().equals("refresh") } as ConfigDescriptionParameter
       def refresh_label = refresh.getLabel()
       def refresh_description = refresh.getDescription() 
return """
thing-type.config.yahooweather.weather.location.label = ${location_label}
thing-type.config.yahooweather.weather.location.description = ${location_description}
thing-type.config.yahooweather.weather.unit.label = ${unit_label}
thing-type.config.yahooweather.weather.unit.description = ${unit_description}
thing-type.config.yahooweather.weather.refresh.label = ${refresh_label}
thing-type.config.yahooweather.weather.refresh.description = ${refresh_description}
"""
    }
    

}
