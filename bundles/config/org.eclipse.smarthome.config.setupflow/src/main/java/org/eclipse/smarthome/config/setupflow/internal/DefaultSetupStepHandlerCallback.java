/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow.internal;

import org.eclipse.smarthome.config.setupflow.SetupFlowContext;
import org.eclipse.smarthome.config.setupflow.SetupStepHandlerCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public class DefaultSetupStepHandlerCallback implements SetupStepHandlerCallback {

    private Logger logger = LoggerFactory.getLogger(DefaultSetupStepHandlerCallback.class);

    private SetupStepProcess setupStepProcess;


    public DefaultSetupStepHandlerCallback(SetupStepProcess setupStepProcess)
            throws IllegalArgumentException {

        if (setupStepProcess == null) {
            throw new IllegalArgumentException("The SetupStepProcess must not be null!");
        }

        this.setupStepProcess = setupStepProcess;
    }

    @Override
    public void sendStepSucceededEvent(SetupFlowContext context) {
        logger.info("Setup flow step SUCCEEDED ({}).", context.toString());

        this.setupStepProcess.processStepSucceededEvent(context);
    }

    @Override
    public void sendErrorOccurredEvent(SetupFlowContext context, int errorCode, String errorMessage) {
        logger.info("Setup flow step FAILED ({}): {} - {}.",
                context.toString(), errorCode, errorMessage);

        this.setupStepProcess.processErrorOccurredEvent(context, errorCode, errorMessage);
    }

//    @Override
//    public void sendStepAbortedEvent(SetupFlowContext context) {
//        logger.info("Setup flow step ABORTED ({}).", context.toString());
//
//        this.setupStepProcess.processStepAbortedEvent(context);
//    }
//
//    @Override
//    public void sendStepStartedErrorEvent(SetupFlowContext context,
//            int errorCode, String errorMessage) {
//
//        logger.info("Setup flow step FAILED while starting ({}): {} - {}.",
//                context.toString(), errorCode, errorMessage);
//
//        this.setupStepProcess.processStepStartedErrorEvent(context, errorCode, errorMessage);
//    }
//
//    @Override
//    public void sendStepAbortedErrorEvent(SetupFlowContext context,
//            int errorCode, String errorMessage) {
//
//        logger.info("Setup flow step FAILED while aborting ({}): {} - {}.",
//                context.toString(), errorCode, errorMessage);
//
//        this.setupStepProcess.processStepAbortedErrorEvent(context, errorCode, errorMessage);
//    }

}
