/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow.internal;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.smarthome.config.core.ConfigDescription;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter;
import org.eclipse.smarthome.config.core.ConfigDescriptionRegistry;
import org.eclipse.smarthome.config.setupflow.ConfigurationStep;
import org.eclipse.smarthome.config.setupflow.Properties;
import org.eclipse.smarthome.config.setupflow.Property;
import org.eclipse.smarthome.config.setupflow.SetupFlow;
import org.eclipse.smarthome.config.setupflow.SetupFlowContext;
import org.eclipse.smarthome.config.setupflow.SetupFlowManager;
import org.eclipse.smarthome.config.setupflow.SetupStep;
import org.eclipse.smarthome.config.setupflow.SetupStepHandler;
import org.eclipse.smarthome.config.setupflow.SetupSteps;
import org.eclipse.smarthome.config.setupflow.StepImplementation;
import org.eclipse.smarthome.config.setupflow.ThingTypes;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public class SetupFlowManagerImpl implements SetupFlowManager {

    private Logger logger = LoggerFactory.getLogger(SetupFlowManagerImpl.class);

    private SetupFlowProcessManager flowProcessManager;

    private ConfigDescriptionRegistryTracker configDescriptionRegistryTracker;

    private List<SetupFlowProvider> setupFlowProviders;

    private boolean invalid;

    public SetupFlowManagerImpl(BundleContext bundleContext,
            SetupFlowProcessManager flowProcessManager)
            throws IllegalArgumentException {

        this.flowProcessManager = flowProcessManager;

        this.setupFlowProviders = new ArrayList<>();

        this.configDescriptionRegistryTracker = new ConfigDescriptionRegistryTracker(bundleContext);
        
        configDescriptionRegistryTracker.open();
    }

    public final synchronized void release() {
        if (!this.invalid) {
            this.logger.debug("Release service...");

            this.setupFlowProviders.clear();
            this.flowProcessManager.abortAll();

            this.invalid = true;
        }

        configDescriptionRegistryTracker.close();
    }

    private void assertServiceValid() throws IllegalStateException {
        if (this.invalid) {
            throw new IllegalStateException("The service is no longer available!");
        }
    }

    final synchronized boolean addSetupFlowProvider(SetupFlowProvider flowProvider)
            throws IllegalStateException {

        assertServiceValid();

        if (flowProvider != null) {
            if (!this.setupFlowProviders.contains(flowProvider)) {
                this.logger.debug("Adding SetupFlowProvider '{}'...", flowProvider);

                this.setupFlowProviders.add(flowProvider);

                this.logger.debug("SetupFlowProvider '{}' has been added.", flowProvider);

                return true;
            }
        }

        return false;
    }

    final synchronized boolean removeSetupFlowProvider(SetupFlowProvider flowProvider)
            throws IllegalStateException {

        assertServiceValid();

        if (flowProvider != null) {
            this.logger.debug("Removing SetupFlowProvider '{}'...", flowProvider);

            boolean removed = this.setupFlowProviders.remove(flowProvider);

            if (removed) {
                this.logger.debug("SetupFlowProvider '{}' has been removed.", flowProvider);

                return true;
            }
        }

        return false;
    }

    @Override
    public final synchronized Map<String, List<String>> getSupportedThingTypeIds()
            throws IllegalStateException {

        assertServiceValid();

        Map<String, List<String>> supportedThingTypesByBindingIdMap = new HashMap<>();

        for (SetupFlowProvider setupFlowProvider : this.setupFlowProviders) {
            supportedThingTypesByBindingIdMap.putAll(setupFlowProvider.getSupportedThingTypes());
        }

        return supportedThingTypesByBindingIdMap;
    }

    @Override
    public final synchronized SetupFlow getSetupFlow(ThingTypeUID thingTypeUID)
            throws IllegalStateException {

        assertServiceValid();

        SetupFlow setupFlow = null;
        for (Iterator<SetupFlowProvider> setupFlowProviderIterator = this.setupFlowProviders
                .iterator(); setupFlow == null && setupFlowProviderIterator.hasNext();) {

            SetupFlowProvider setupFlowProvider = setupFlowProviderIterator.next();
            setupFlow = setupFlowProvider.getSetupFlow(thingTypeUID);
        }
        if (setupFlow == null) {
            setupFlow = buildGenericSetupFlow(thingTypeUID);
        }

        return setupFlow;
    }

    private SetupFlow buildGenericSetupFlow(ThingTypeUID thingTypeUID) {
        // try to build the setup flow by using the config description
        // In this first implementation we obtain the ConfigDecription by
        // passing the thingType
        ConfigDescriptionRegistry configDescriptionRegistry = configDescriptionRegistryTracker
                .getService();

        Iterable<ConfigDescriptionParameter> requiredParameters = null;
        if (configDescriptionRegistry != null) {
            ConfigDescription configDescription = configDescriptionRegistry
                    .getConfigDescription(URI.create(thingTypeUID.toString()));

            if (configDescription != null) {
                requiredParameters = Iterables.filter(configDescription.getParameters(),
                        new Predicate<ConfigDescriptionParameter>() {
                            @Override
                            public boolean apply(ConfigDescriptionParameter parameter) {
                                return parameter.isRequired();
                            }
                        });
            }
        }

        return createGenericSetupFlow(thingTypeUID, requiredParameters);
    }

    private SetupFlow createGenericSetupFlow(ThingTypeUID thingTypeUID,
            Iterable<ConfigDescriptionParameter> requiredParameters) {

        String bindingId = thingTypeUID.getBindingId();
        String thingTypeId = thingTypeUID.getId();
        SetupFlow setupFlow = new SetupFlow();
        ThingTypes thingTypes = new ThingTypes();
        setupFlow.setThingTypes(thingTypes);
        thingTypes.getThingTypes().add(thingTypeId);
        String flowId = getFlowId(thingTypeUID);
        setupFlow.setId(flowId);
        setupFlow.setBindingId(bindingId);
        SetupSteps setupSteps = new SetupSteps();
        setupFlow.setSteps(setupSteps);
        List<SetupStep> setupStepList = setupSteps
                .getSearchStepsAndExternalEventStepsAndConfigurationSteps();
        if (requiredParameters != null && requiredParameters.iterator().hasNext()) {
            ConfigurationStep genericConfigurationStep = createGenericConfigurationStep(requiredParameters);
            setupStepList.add(genericConfigurationStep);
        }
        return setupFlow;

    }
    
    private String getFlowId(ThingTypeUID thingTypeUID) {
        return thingTypeUID.getId().replaceAll("[^a-zA-Z0-9_]", "-") + "-genericflow";
    }

    private ConfigurationStep createGenericConfigurationStep(
            Iterable<ConfigDescriptionParameter> requiredParameters) {
        ConfigurationStep genericConfigurationStep = new ConfigurationStep();

        genericConfigurationStep.setId("generic-config");
        StepImplementation stepImplementation = new StepImplementation();
        genericConfigurationStep.setImplementation(stepImplementation);
        stepImplementation.setClazz(GenericConfigurationStepHandler.class.getName());
        Properties genericConfigurationStepProperties = new Properties();
        genericConfigurationStep.setProperties(genericConfigurationStepProperties);
        List<Property> propertyList = genericConfigurationStepProperties.getProperties();
        for (ConfigDescriptionParameter parameter : requiredParameters) {
            Property configProperty = new Property();
            propertyList.add(configProperty);
            configProperty.setName(parameter.getName());
        }
        return genericConfigurationStep;
    }

    @Override
    public final SetupFlowContext createSetupFlowContext(String flowId, ThingTypeUID thingTypeUID,
            ThingUID thingUID, ThingUID bridgeUID) throws IllegalArgumentException {

        String nextStepId = null;
        Map<String, Object> properties = new HashMap<>();

        // can throw an IllegalArgumentException
        SetupFlowContext setupFlowContext = new SetupFlowContext(null, flowId,
                thingTypeUID.toString(), thingUID != null ? thingUID.toString() : null,
                bridgeUID != null ? bridgeUID.toString() : null, properties);

        setupFlowContext.setNextStepId(nextStepId);

        return setupFlowContext;
    }

    // TODO: param check
    @Override
    public final synchronized void startSetupStep(SetupFlowContext context)
            throws IllegalArgumentException, IllegalStateException {

        assertServiceValid();

        String flowId = context.getFlowId();
        String bindingId = context.getBindingId();
        String stepId = context.getStepId();

        for (SetupFlowProvider setupFlowProvider : this.setupFlowProviders) {
            if (setupFlowProvider.containsSetupFlow(flowId, bindingId)) {
                // can be null
                SetupStepHandler stepHandler = setupFlowProvider.createSetupStepHandler(flowId,
                        bindingId, stepId);

                // can throw an IllegalArgumentException or an
                // IllegalStateException
                this.flowProcessManager.start(context, stepHandler);
            }
        }
    }

    @Override
    public final synchronized boolean abortSetupStep(SetupFlowContext context)
            throws IllegalStateException {

        assertServiceValid();

        return this.flowProcessManager.abort(context.getContextId());
    }

}
