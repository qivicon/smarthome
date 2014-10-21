/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.smarthome.config.setupflow.SetupFlow;
import org.eclipse.smarthome.config.setupflow.SetupFlows;
import org.eclipse.smarthome.config.setupflow.SetupStepHandler;
import org.eclipse.smarthome.config.setupflow.ThingTypes;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public class SetupFlowProvider {

    private Logger logger = LoggerFactory.getLogger(SetupFlowProvider.class);

    private Bundle bundle;

    private Map<String, DeclarativeSetupFlow> setupFlowByFlowURIMap;


    public SetupFlowProvider(Bundle bundle) throws IllegalArgumentException {
        if (bundle == null) {
            throw new IllegalArgumentException("The Bundle must not be null!");
        }

        this.bundle = bundle;

        this.setupFlowByFlowURIMap = new HashMap<>();
    }

    public final synchronized void addSetupFlows(SetupFlows setupFlows) {
        if ((setupFlows != null) && (setupFlows.getSetupFlows() != null)) {
            for (SetupFlow setupFlow : setupFlows.getSetupFlows()) {
                addSetupFlow(setupFlow);
            }
        }
    }

    public final synchronized void addSetupFlow(SetupFlow setupFlow) {
        if (setupFlow != null) {
            try {
                DeclarativeSetupFlow declarativeSetupFlow =
                        new DeclarativeSetupFlow(this.bundle, setupFlow);

                String setupFlowURI = declarativeSetupFlow.getSetupFlowURI();

                this.setupFlowByFlowURIMap.put(setupFlowURI, declarativeSetupFlow);
            } catch (Exception ex) {
                this.logger.error("The setup flow cannot be resolved!", ex);
            }
        }
    }

    public final boolean containsSetupFlow(String flowId, String bindingId) {
        if ((flowId != null) && (bindingId != null)) {
            String setupFlowURI = DeclarativeSetupFlow.toSetupFlowURI(flowId, bindingId);

            return this.setupFlowByFlowURIMap.containsKey(setupFlowURI);
        }

        return false;
    }

    public final synchronized Map<String, List<String>> getSupportedThingTypes() {
        Map<String, List<String>> supportedThingTypesByBindingIdMap = new HashMap<>();

        Collection<DeclarativeSetupFlow> setupFlows = this.setupFlowByFlowURIMap.values();

        for (DeclarativeSetupFlow declarativeSetupFlow : setupFlows) {
            SetupFlow setupFlow = declarativeSetupFlow.getSetupFlow();
            ThingTypes thingTypes = setupFlow.getThingTypes();
            String bindingId = setupFlow.getBindingId();

            List<String> availableThingTypes = supportedThingTypesByBindingIdMap.get(bindingId);

            if (availableThingTypes == null) {
                supportedThingTypesByBindingIdMap.put(bindingId, thingTypes.getThingTypes());
            } else {
                availableThingTypes.addAll(thingTypes.getThingTypes());
            }
        }

        return supportedThingTypesByBindingIdMap;
    }

    public final synchronized SetupFlow getSetupFlow(ThingTypeUID thingTypeUID) {
        if (thingTypeUID != null) {
            Collection<DeclarativeSetupFlow> setupFlows = this.setupFlowByFlowURIMap.values();

            for (DeclarativeSetupFlow declarativeSetupFlow : setupFlows) {
                SetupFlow setupFlow = declarativeSetupFlow.getSetupFlow();
                ThingTypes thingTypes = setupFlow.getThingTypes();

                if (thingTypes != null) {
                    List<String> types = thingTypes.getThingTypes();

                    for (String type : types) {
                        if (thingTypeUID.toString().equals(type)) {
                            return setupFlow;
                        }
                    }
                }
            }
        }

        return null;
    }

    public final synchronized SetupStepHandler createSetupStepHandler(
            String flowId, String bindingId, String stepId) {

        String setupFlowURI = DeclarativeSetupFlow.toSetupFlowURI(flowId, bindingId);

        if ((setupFlowURI != null) && (stepId != null)) {
            DeclarativeSetupFlow setupFlow = setupFlowByFlowURIMap.get(setupFlowURI);

            if (setupFlow != null) {
                return setupFlow.createSetupStepHandler(stepId);   // can return null
            }
        }

        return null;
    }

}
