/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow.internal;

import org.eclipse.smarthome.config.setupflow.SetupFlowContext;
import org.eclipse.smarthome.config.setupflow.SetupStepHandler;
import org.eclipse.smarthome.config.setupflow.SetupStepHandlerCallback;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// TODO: Synchronization is needed.
// TODO: When abort failed, an aborted event must be sent instead of an error event.
/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public class SetupStepProcess {

    private Logger logger = LoggerFactory.getLogger(SetupStepProcess.class);

    private String contextId;

    private SetupStepHandler stepHandler;
    private SetupStepHandlerCallback stepHandlerCallback;

    private SetupFlowProcessManager flowProcessManager;
    private EventAdmin eventAdmin;

    private boolean invalid;


    protected SetupStepProcess(String contextId, SetupStepHandler stepHandler,
            SetupFlowProcessManager flowProcessManager, EventAdmin eventAdmin)
            throws IllegalArgumentException {

        if (contextId == null) {
            throw new IllegalArgumentException("The context ID must not be null!");
        }
        if (stepHandler == null) {
            throw new IllegalArgumentException("The SetupStepHandler must not be null!");
        }
        if (flowProcessManager == null) {
            throw new IllegalArgumentException("The SetupFlowProcessManager must not be null!");
        }
        if (eventAdmin == null) {
            throw new IllegalArgumentException("The EventAdmin must not be null!");
        }

        this.contextId = contextId;

        this.stepHandler = stepHandler;
        this.stepHandlerCallback = new DefaultSetupStepHandlerCallback(this);

        this.flowProcessManager = flowProcessManager;

        this.eventAdmin = eventAdmin;
    }

    private void sendEvent(Event event) {
        try {
            this.eventAdmin.sendEvent(event);
        } catch (Exception ex) {
            this.logger.error("Cannot send the event with topic '" + event.getTopic() + "'!", ex);
        }
    }

    void processStepSucceededEvent(SetupFlowContext context) {
        if (!this.invalid) {
            this.flowProcessManager.dispose(this.contextId);
            sendEvent(EventFactory.createStepSucceededEvent(context));
            this.invalid = true;
        }
    }

    void processErrorOccurredEvent(SetupFlowContext context, int errorCode, String errorMessage) {
        if (!this.invalid) {
            this.flowProcessManager.dispose(this.contextId);
            sendEvent(EventFactory.createErrorOccurredEvent(context, errorCode, errorMessage));
            this.invalid = true;
        }
    }

//    void processStepAbortedEvent(SetupFlowContext context) {
//        if (!this.invalid) {
//            this.flowProcessManager.dispose(this.contextId);
//            sendEvent(EventFactory.createStepAbortedEvent(context));
//            this.invalid = true;
//        }
//    }
//
//    void processStepStartedErrorEvent(SetupFlowContext context, int errorCode, String errorMessage) {
//        if (!this.invalid) {
//            this.flowProcessManager.dispose(this.contextId);
//            sendEvent(EventFactory.createErrorOccurredEvent(context, errorCode, errorMessage));
//            this.invalid = true;
//        }
//    }
//
//    void processStepAbortedErrorEvent(SetupFlowContext context, int errorCode, String errorMessage) {
//        if (!this.invalid) {
//            this.flowProcessManager.dispose(this.contextId);
//            sendEvent(EventFactory.createErrorOccurredEvent(context, errorCode, errorMessage));
//            this.invalid = true;
//        }
//    }

    void processErrorOccurredEvent(int errorCode, String errorMessage) {
        if (!this.invalid) {
            this.flowProcessManager.dispose(this.contextId);
            sendEvent(EventFactory.createErrorOccurredEvent(this.contextId, errorCode, errorMessage));
            this.invalid = true;
        }
    }

    public final void start(SetupFlowContext context) {
        if (!this.invalid) {
            try {
                this.stepHandler.start(context, this.stepHandlerCallback);
            } catch (Exception ex) {
                this.logger.error("Cannot start the process with ID '" + this.contextId + "'!", ex);
                processErrorOccurredEvent(EventFactory.ERROR_CODE_START_PROCESS, ex.getMessage());
            }
        }
    }

    public final void abort() {
        if (!this.invalid) {
            try {
                this.stepHandler.abort(this.contextId);
                // this.stepHandler.abort(this.contextId, this.stepHandlerCallback);
            } catch (Exception ex) {
                this.logger.error("Cannot abort the process with ID '" + this.contextId + "'!", ex);
                processErrorOccurredEvent(EventFactory.ERROR_CODE_ABORT_PROCESS, ex.getMessage());
            }
        }
    }

}
