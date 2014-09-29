/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.binding.xml.internal;

import org.eclipse.smarthome.config.core.ConfigDescription;
import org.eclipse.smarthome.config.core.ConfigDescriptionProvider;
import org.eclipse.smarthome.config.core.i18n.ConfigDescriptionI18nProvider;
import org.eclipse.smarthome.config.xml.XmlConfigDescriptionProvider;
import org.eclipse.smarthome.config.xml.osgi.XmlDocumentBundleTracker;
import org.eclipse.smarthome.config.xml.osgi.XmlDocumentProviderFactory;
import org.eclipse.smarthome.config.xml.util.XmlDocumentReader;
import org.eclipse.smarthome.core.binding.BindingInfo;
import org.eclipse.smarthome.core.binding.BindingInfoProvider;
import org.eclipse.smarthome.core.i18n.BindingInfoI18nProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;


/**
 * The {@link Activator} class is responsible to activate this module.
 * <p>
 * This module tracks any XML documents of other bundles in the {@link #XML_DIRECTORY}
 * folder and tries to extract binding information from them. Any {@link BindingInfo} objects
 * are registered at the {@link XmlBindingInfoProvider} which itself is registered as service
 * at the <i>OSGi</i> service registry and unregistered again, if the providing bundle or this
 * bundle is stopped.
 * <p>
 * If the binding information contains a {@code config-description} section, the according
 * {@link ConfigDescription} object is added to the {@link XmlConfigDescriptionProvider}
 * and removed again, if the providing bundle or this bundle is stopped.<br>
 * The {@link XmlConfigDescriptionProvider} is registered itself as <i>OSGi</i> service
 * at the service registry.
 * 
 * @author Michael Grammling - Initial Contribution
 */
public class Activator implements BundleActivator {

    private static final String XML_DIRECTORY = "/ESH-INF/binding/";

    private ServiceRegistration<?> bindingInfoProviderReg;
    private ServiceRegistration<?> configDescriptionProviderReg;

    private ConfigDescriptionI18nProviderServiceTracker configDescriptionI18nProviderServiceTracker;
    private BindingInfoI18nProviderServiceTracker bindinginfoI18nProviderServiceTracker;

    private XmlDocumentBundleTracker<BindingInfoXmlResult> bindingInfoTracker;


    private class ConfigDescriptionI18nProviderServiceTracker extends ServiceTracker {

        private XmlConfigDescriptionProvider configDescriptionProvider;

        public ConfigDescriptionI18nProviderServiceTracker(
                BundleContext context, XmlConfigDescriptionProvider configDescriptionProvider) {

            super(context, ConfigDescriptionI18nProvider.class.getName(), null);

            this.configDescriptionProvider = configDescriptionProvider;
        }

        @Override
        public Object addingService(ServiceReference reference) {
            ConfigDescriptionI18nProvider service =
                    (ConfigDescriptionI18nProvider) this.context.getService(reference);

            this.configDescriptionProvider.setConfigDescriptionI18nProvider(service);
            return service;
        }

        @Override
        public void removedService(ServiceReference reference, Object service) {
            this.configDescriptionProvider.unsetConfigDescriptionI18nProvider(
                    (ConfigDescriptionI18nProvider) this.context.getService(reference));
        }

    };


    private class BindingInfoI18nProviderServiceTracker extends ServiceTracker {

        private XmlBindingInfoProvider bindingInfoProvider;

        public BindingInfoI18nProviderServiceTracker(
                BundleContext context, BindingInfoProvider bindingInfoProvider) {

            super(context, BindingInfoI18nProvider.class.getName(), null);
        }

        @Override
        public Object addingService(ServiceReference reference) {
            BindingInfoI18nProvider service =
                    (BindingInfoI18nProvider) this.context.getService(reference);

            this.bindingInfoProvider.setBindingInfoI18nProvider(service);
            return service;
        }

        @Override
        public void removedService(ServiceReference reference, Object service) {
            this.bindingInfoProvider.setBindingInfoI18nProvider(null);
        }

    };


    @Override
    public void start(BundleContext context) throws Exception {
        XmlBindingInfoProvider bindingInfoProvider = new XmlBindingInfoProvider();
        this.bindinginfoI18nProviderServiceTracker =
                new BindingInfoI18nProviderServiceTracker(context, bindingInfoProvider);
        this.bindinginfoI18nProviderServiceTracker.open();

        XmlConfigDescriptionProvider configDescriptionProvider = new XmlConfigDescriptionProvider();
        this.configDescriptionI18nProviderServiceTracker =
                new ConfigDescriptionI18nProviderServiceTracker(context, configDescriptionProvider);
        this.configDescriptionI18nProviderServiceTracker.open();

        XmlDocumentReader<BindingInfoXmlResult> bindingInfoReader = new BindingInfoReader();

        XmlDocumentProviderFactory<BindingInfoXmlResult> bindingInfoProviderFactory =
                new BindingInfoXmlProviderFactory(bindingInfoProvider, configDescriptionProvider);

        this.bindingInfoTracker = new XmlDocumentBundleTracker<>(
                context, XML_DIRECTORY, bindingInfoReader, bindingInfoProviderFactory);

        this.bindingInfoTracker.open();

        this.configDescriptionProviderReg = context.registerService(
                ConfigDescriptionProvider.class.getName(), configDescriptionProvider, null);

        this.bindingInfoProviderReg = context.registerService(
                BindingInfoProvider.class.getName(), bindingInfoProvider, null);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        this.bindingInfoProviderReg.unregister();
        this.bindingInfoProviderReg = null;

        this.configDescriptionProviderReg.unregister();
        this.configDescriptionProviderReg = null;

        this.bindingInfoTracker.close();

        this.configDescriptionI18nProviderServiceTracker.close();
        this.bindinginfoI18nProviderServiceTracker.close();
    }

}
