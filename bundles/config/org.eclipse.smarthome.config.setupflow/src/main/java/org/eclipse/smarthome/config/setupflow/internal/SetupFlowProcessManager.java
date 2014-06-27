/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.smarthome.config.setupflow.SetupFlowContext;
import org.eclipse.smarthome.config.setupflow.SetupStepHandler;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public class SetupFlowProcessManager {

    private Logger logger = LoggerFactory.getLogger(SetupFlowProcessManager.class);

    private EventAdmin eventAdmin;
    private Map<String, SetupStepProcess> setupStepProcessByContextIdMap;


    public SetupFlowProcessManager(EventAdmin eventAdmin) {
        if (eventAdmin == null) {
            throw new IllegalArgumentException("The EventAdmin must not be null!");
        }

        this.eventAdmin = eventAdmin;
        this.setupStepProcessByContextIdMap = new HashMap<>();
    }

    public synchronized void start(SetupFlowContext context, SetupStepHandler stepHandler)
            throws IllegalArgumentException, IllegalStateException {

        if (context == null) {
            throw new IllegalArgumentException("The SetupFlowContext must not be null!");
        }

        String contextId = context.getContextId();
        String flowId = context.getFlowId();

        if (this.setupStepProcessByContextIdMap.containsKey(contextId)) {
            throw new IllegalStateException("While a step for the process flow '" + flowId
                    + "' is already runnning, no further step can be started on it!");
        }

        // can throw an IllegalArgumentException for any of the parameters
        SetupStepProcess stepProcess = new SetupStepProcess(
                contextId, stepHandler, this, this.eventAdmin);

        this.setupStepProcessByContextIdMap.put(contextId, stepProcess);

        logger.info("Starting setup flow step {} for context {}", context.getStepId(),
                context.toString());

        stepProcess.start(context);
    }

    public synchronized boolean dispose(String contextId) {
        if (contextId != null) {
            SetupStepProcess flowProcess = this.setupStepProcessByContextIdMap.remove(contextId);

            return (flowProcess != null);
        }

        return false;
    }

    public synchronized boolean abort(String contextId) throws IllegalArgumentException {
        if (contextId != null) {
            SetupStepProcess stepProcess = this.setupStepProcessByContextIdMap.get(contextId);

            if (stepProcess != null) {
                logger.info("Aborting setup flow step for context {}...", contextId);
                stepProcess.abort();

                this.setupStepProcessByContextIdMap.remove(contextId);

                return true;
            } else {
                logger.info("Could not abort setup flow step for context {},"
                        + " because no step process could be found.", contextId);
            }
        }

        return false;
    }

    public synchronized void abortAll() {
        Set<String> contextIdList = this.setupStepProcessByContextIdMap.keySet();

        for (String contextId : contextIdList) {
            abort(contextId);
        }

        this.setupStepProcessByContextIdMap.clear();
    }

}
