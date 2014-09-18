/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow.internal;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.smarthome.config.setupflow.SetupFlow;
import org.eclipse.smarthome.config.setupflow.SetupStep;
import org.eclipse.smarthome.config.setupflow.SetupStepHandler;
import org.eclipse.smarthome.config.setupflow.SetupSteps;
import org.eclipse.smarthome.config.setupflow.StepImplementation;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TODO: Add a release method.
/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public class DeclarativeSetupFlow {

    private Logger logger = LoggerFactory.getLogger(DeclarativeSetupFlow.class);

    private Bundle bundle;
    private SetupFlow setupFlow;

    private String setupFlowURI;

    private Map<String, Class<?>> stepHandlerByStepIdClassMap;


    public DeclarativeSetupFlow(Bundle bundle, SetupFlow setupFlow)
            throws IllegalArgumentException {

        if (bundle == null) {
            throw new IllegalArgumentException("The Bundle must not be null!");
        }
        if (setupFlow == null) {
            throw new IllegalArgumentException("The SetupFlow must not be null!");
        }

        this.bundle = bundle;
        this.setupFlow = setupFlow;

        this.setupFlowURI = toSetupFlowURI(this.setupFlow.getId(), this.setupFlow.getBindingId());

        this.stepHandlerByStepIdClassMap = new HashMap<>();

        registerSetupStepHandlers(bundle, setupFlow);
    }

    public static String toSetupFlowURI(String flowId, String bindingId) {
        if ((flowId != null) && (bindingId != null)) {
            try {
                URI setupFlowId = new URI("setup-flow", flowId, "/" + bindingId, null);

                return setupFlowId.toString();
            } catch (URISyntaxException use) {
                // nothing to do
            }
        }

        return null;
    }

    public String getSetupFlowURI() {
        return this.setupFlowURI;
    }

    public SetupFlow getSetupFlow() {
        return this.setupFlow;
    }

    private void registerSetupStepHandlers(Bundle bundle, SetupFlow setupFlow)
            throws IllegalArgumentException {

        SetupSteps setupSteps = setupFlow.getSteps();

        if (setupSteps != null) {
            List<SetupStep> setupStepList =
                    setupSteps.getSearchStepsAndExternalEventStepsAndConfigurationSteps();

            if (setupStepList != null) {
                for (SetupStep setupStep : setupStepList) {
                    if (setupStep != null) {
                        registerSetupStepHandler(bundle, setupStep);
                    }
                }
            }
        }
    }

    private void registerSetupStepHandler(Bundle bundle, SetupStep setupStep)
            throws IllegalArgumentException {

        String stepId = setupStep.getId();

        StepImplementation stepImplementation = setupStep.getImplementation();

        if (stepImplementation != null) {
            String stepHandlerClazz = stepImplementation.getClazz();

            String messageFormat = String.format("The specified SetupStepHandler class '%s'"
                    + " for '%s#%s' within the module '%s'", stepHandlerClazz, this.setupFlowURI,
                    stepId, bundle.getSymbolicName()) + " %s";

            try {
                this.logger.debug(String.format(messageFormat, "is loaded."));

                Class<?> stepHandlerClass = bundle.loadClass(stepHandlerClazz);

                if (SetupStepHandler.class.isAssignableFrom(stepHandlerClass)) {
                    this.stepHandlerByStepIdClassMap.put(stepId, stepHandlerClass);
                } else {
                    String errorMessage = String.format(messageFormat,
                            "does not implement the 'SetupStepHandler' interface!");

                    this.logger.error(errorMessage);

                    throw new IllegalArgumentException(errorMessage);
                }
            } catch (ClassNotFoundException cnfe) {
                String errorMessage = String.format(messageFormat, "cannot be loaded!");

                this.logger.error(errorMessage, cnfe);

                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public SetupStepHandler createSetupStepHandler(String stepId) {
        if (stepId != null) {
            Class<?> stepHandlerClazz = this.stepHandlerByStepIdClassMap.get(stepId);

            if (stepHandlerClazz != null) {
                String messageFormat = String.format("The specified SetupStepHandler class '%s'"
                        + " for '%s#%s' within the module '%s'", stepHandlerClazz,
                        this.setupFlowURI, stepId, this.bundle.getSymbolicName()) + " %s";

                try {
                    // try to find a SetupStepHandler with the parameter BundleContext
                    Constructor<?> constructor =
                            stepHandlerClazz.getConstructor(BundleContext.class);

                    return (SetupStepHandler)
                            constructor.newInstance(this.bundle.getBundleContext());
                } catch (Exception exParam) {
                    this.logger.debug(String.format(messageFormat, "cannot be instantiated"
                            + " with the parameter-full constructor, trying to use the default"
                            + " constructor: '" + exParam.getMessage() + "'."));

                    try {
                        // else: try to find a parameterless constructor
                        return (SetupStepHandler) stepHandlerClazz.newInstance();
                    } catch (Exception exNoParam) {
                        this.logger.error(String.format(messageFormat, "cannot be instantiated with"
                                + " the default constructor: '" + exNoParam.getMessage() + "'!"));
                    }
                }
            }
        }

        return null;
    }

}
