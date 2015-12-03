/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.internal;

import org.eclipse.smarthome.config.core.ConfigDescriptionRegistry;
import org.eclipse.smarthome.core.common.osgi.ServiceBinder;
import org.eclipse.smarthome.core.common.osgi.ServiceBinder.Bind;
import org.eclipse.smarthome.core.common.osgi.ServiceBinder.Unbind;
import org.eclipse.smarthome.core.thing.type.ChannelTypeRegistry;
import org.eclipse.smarthome.core.thing.type.ThingTypeRegistry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public static class ChannelTypeRegistryBinder {
        @Bind
        @Unbind
        public void bindChannelTypeRegistry(ChannelTypeRegistry channelTypeRegistry) {
            Activator.channelTypeRegistry = channelTypeRegistry;
        }
    }

    public static class ThingTypeRegistryBinder {
        @Bind
        @Unbind
        public void bindThingTypeRegistry(ThingTypeRegistry thingTypeRegistry) {
            Activator.thingTypeRegistry = thingTypeRegistry;
        }
    }

    public static class ConfigDescriptionRegistryBinder {
        @Bind
        @Unbind
        public void bindConfigDescriptionRegistry(ConfigDescriptionRegistry configDescriptionRegistry) {
            Activator.configDescriptionRegistry = configDescriptionRegistry;
        }
    }

    private static ChannelTypeRegistry channelTypeRegistry;
    private static BundleContext context;
    private static ThingTypeRegistry thingTypeRegistry;
    private static ConfigDescriptionRegistry configDescriptionRegistry;

    /**
     * Returns the {@link ChannelTypeRegistry}
     *
     * @return {@link ChannelTypeRegistry} or null
     */
    public static ChannelTypeRegistry getChannelTypeRegistry() {
        return Activator.channelTypeRegistry;
    }

    /**
     * Returns the bundle context of this bundle
     *
     * @return the bundle context
     */
    public static BundleContext getContext() {
        return Activator.context;
    }

    /**
     * Returns the {@link ThingTypeRegistry}
     *
     * @return {@link ThingTypeRegistry} or null
     */
    public static ThingTypeRegistry getThingTypeRegistry() {
        return Activator.thingTypeRegistry;
    }

    /**
     * Returns the {@link ConfigDescriptionRegistry}.
     *
     * @return {@link ConfigDescriptionRegistry} or null
     */
    public static ConfigDescriptionRegistry getConfigDescriptionRegistry() {
        return Activator.configDescriptionRegistry;
    }

    private ServiceBinder channelTypeServiceBinder;

    private ServiceBinder thingTypeServiceBinder;

    private ServiceBinder configDescriptionServiceBinder;

    @Override
    public void start(BundleContext context) throws Exception {
        Activator.context = context;
        thingTypeServiceBinder = new ServiceBinder(context, new ThingTypeRegistryBinder());
        thingTypeServiceBinder.open();
        channelTypeServiceBinder = new ServiceBinder(context, new ChannelTypeRegistryBinder());
        channelTypeServiceBinder.open();
        configDescriptionServiceBinder = new ServiceBinder(context, new ConfigDescriptionRegistryBinder());
        configDescriptionServiceBinder.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        channelTypeServiceBinder.close();
        thingTypeServiceBinder.close();
        configDescriptionServiceBinder.close();
        channelTypeServiceBinder = null;
        thingTypeServiceBinder = null;
        configDescriptionServiceBinder = null;
        Activator.context = null;
    }
}
