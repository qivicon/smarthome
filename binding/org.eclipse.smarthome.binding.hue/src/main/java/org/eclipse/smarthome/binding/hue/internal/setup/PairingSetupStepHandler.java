/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.binding.hue.internal.setup;

import java.io.IOException;
import java.util.Map;

import nl.q42.jue.HueBridge;
import nl.q42.jue.exceptions.ApiException;
import nl.q42.jue.exceptions.LinkButtonException;

import static org.eclipse.smarthome.binding.hue.HueBindingConstants.*;
import org.eclipse.smarthome.config.setupflow.SetupFlowContext;
import org.eclipse.smarthome.config.setupflow.SetupStepHandlerCallback;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PairingSetupStepHandler} pairs the Hue bridge with the binding.
 * 
 * @author Oliver Libutzki
 */
public class PairingSetupStepHandler extends BaseHueSetupStepHandler {

    private final static Logger logger = LoggerFactory.getLogger(PairingSetupStepHandler.class);

    public PairingSetupStepHandler(BundleContext bundleContext) {
        super(bundleContext);
    }

    @Override
    protected void startInternal(final SetupFlowContext setupFlowContext,
            final SetupStepHandlerCallback callback) {
        final Map<String, Object> properties = setupFlowContext.getProperties();
        final String bridgeIpAddress = (String) properties.get(HOST);
        setupFlowContext.setNextStepId(null);

        Task pairingTask = new AbstractTask("HueBridgePairing") {
            @Override
            public void executeTask(TaskExecutionState taskExecutionState) {
                String bridgeUserName = (String) properties.get(USER_NAME);
                long startTime = System.currentTimeMillis();
                HueBridge bridge = new HueBridge(bridgeIpAddress);
                final String deviceType = "Eclipse SmartHome";
                while ((startTime + 30000) > System.currentTimeMillis()) {
                    String linkedUserName = bridgeUserName;
                    try {
                        if (linkedUserName != null) {
                            bridge.link(linkedUserName, deviceType);
                        } else {
                            linkedUserName = bridge.link(deviceType);
                        }
                        logger.debug("The Hue bridge is linked successfully on {}.",
                                bridgeIpAddress);

                        // if no exception is thrown everything is fine, the
                        // binding is linked to the bridge
                        if (taskExecutionState.isAborted()) {
                            logger.debug("The task execution has been aborted.");
                            return;
                        }
                        properties.put(HOST, bridgeIpAddress);
                        properties.put(USER_NAME, linkedUserName);
                        callback.sendStepSucceededEvent(setupFlowContext);
                        return;

                    } catch (IOException e) {
                        if (taskExecutionState.isAborted()) {
                            logger.debug("The task execution has been aborted.");
                            return;
                        }
                        logger.error("The bridge is not reachable on {}.", bridgeIpAddress);
                        HueErrorCode bridgeNotFoundErrorCode = HueErrorCode.BRIDGE_NOT_RESPONDING;
                        callback.sendErrorOccurredEvent(setupFlowContext,
                                bridgeNotFoundErrorCode.getCode(),
                                bridgeNotFoundErrorCode.getMessage());
                        return;

                    } catch (LinkButtonException e) {

                        if (taskExecutionState.isAborted()) {
                            logger.debug("The task execution has been aborted.");
                            return;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException interruptedException) {
                            // do nothing
                        }

                    } catch (ApiException e) {
                        // this exception is unexpected
                        logger.error("The bridge is not reachable on {}.", bridgeIpAddress);
                        if (taskExecutionState.isAborted()) {
                            logger.debug("The task execution has been aborted.");
                            return;
                        }
                        HueErrorCode unexpectedErrorCode = HueErrorCode.UNEXPECTED_ERROR;
                        callback.sendErrorOccurredEvent(setupFlowContext,
                                unexpectedErrorCode.getCode(),
                                unexpectedErrorCode.getMessage());
                        return;
                    }
                }
                // the link button has not been pressed
                logger.debug("The link button has not been pressed.");
                HueErrorCode pushlinkButtonNotPressedErrorCode = HueErrorCode.PUSHLINK_BUTTON_NOT_PRESSED;
                callback.sendErrorOccurredEvent(setupFlowContext,
                        pushlinkButtonNotPressedErrorCode.getCode(), pushlinkButtonNotPressedErrorCode.getMessage());
            }
        };
        logger.debug("Trying to link to the hue bridge. IP: {}", bridgeIpAddress);
        runTask(pairingTask, setupFlowContext);
    }




}
