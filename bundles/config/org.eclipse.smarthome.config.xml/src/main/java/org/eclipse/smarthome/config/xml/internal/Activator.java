/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.xml.internal;

import java.util.List;

import org.eclipse.smarthome.config.core.ConfigDescription;
import org.eclipse.smarthome.config.core.ConfigDescriptionProvider;
import org.eclipse.smarthome.config.core.i18n.ConfigDescriptionI18nProvider;
import org.eclipse.smarthome.config.xml.XmlConfigDescriptionProvider;
import org.eclipse.smarthome.config.xml.osgi.XmlDocumentBundleTracker;
import org.eclipse.smarthome.config.xml.osgi.XmlDocumentProviderFactory;
import org.eclipse.smarthome.config.xml.util.XmlDocumentReader;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;


/**
 * The {@link Activator} class is responsible to activate this module.
 * <p>
 * This module tracks any XML documents of other bundles in the {@link #XML_DIRECTORY}
 * folder and tries to extract config description information from them. Any
 * {@link ConfigDescription} objects are registered at the {@link XmlConfigDescriptionProvider}
 * which itself is registered as service at the <i>OSGi</i> service registry and unregistered
 * again, if the providing bundle or this bundle is stopped.
 * 
 * @author Michael Grammling - Initial Contribution
 */
public class Activator implements BundleActivator {

    private static final String XML_DIRECTORY = "/ESH-INF/config/";

    private ServiceRegistration<?> configDescriptionProviderReg;

    private XmlDocumentBundleTracker<List<ConfigDescription>> configDescriptionTracker;
    
    private XmlConfigDescriptionProvider configDescriptionProvider;
    
    private ServiceTracker configDescriptionI18nProviderServiceTracker;

    private class ConfigDescriptionI18nProviderServiceTracker extends ServiceTracker {

        public ConfigDescriptionI18nProviderServiceTracker(BundleContext context) {
            super(context, ConfigDescriptionI18nProvider.class.getName(), null);
        }

        @Override
        public Object addingService(ServiceReference reference) {
        	ConfigDescriptionI18nProvider service = (ConfigDescriptionI18nProvider) this.context.getService(reference);
            configDescriptionProvider.setConfigDescriptionI18nProvider(service);
            return service;
        }

        @Override
        public void removedService(ServiceReference reference, Object service) {
        	configDescriptionProvider.unsetConfigDescriptionI18nProvider((ConfigDescriptionI18nProvider) this.context.getService(reference));
        }

    };

    @Override
    public void start(BundleContext context) throws Exception {
        this.configDescriptionProvider = new XmlConfigDescriptionProvider();

        this.configDescriptionI18nProviderServiceTracker = new ConfigDescriptionI18nProviderServiceTracker(context);
        this.configDescriptionI18nProviderServiceTracker.open();
        
        this.configDescriptionProviderReg = context.registerService(
                ConfigDescriptionProvider.class.getName(), configDescriptionProvider, null);

        XmlDocumentReader<List<ConfigDescription>> configDescriptionReader =
                new ConfigDescriptionReader();

        XmlDocumentProviderFactory<List<ConfigDescription>> configDescriptionProviderFactory =
                new ConfigDescriptionXmlProviderFactory(configDescriptionProvider);

        this.configDescriptionTracker = new XmlDocumentBundleTracker<>(
                context, XML_DIRECTORY, configDescriptionReader, configDescriptionProviderFactory);

        this.configDescriptionTracker.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        this.configDescriptionTracker.close();

        this.configDescriptionProviderReg.unregister();
        this.configDescriptionProviderReg = null;
        
        this.configDescriptionI18nProviderServiceTracker.close();
        this.configDescriptionProvider = null;
    }

}
