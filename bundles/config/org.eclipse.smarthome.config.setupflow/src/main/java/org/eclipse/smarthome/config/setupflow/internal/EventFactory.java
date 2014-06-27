/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.smarthome.config.setupflow.SetupFlowContext;
import org.osgi.service.event.Event;


/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public class EventFactory {

    public static final String TOPIC_STEP_SUCCEEDED = "smarthome/setup/step/success";
    // public static final String TOPIC_STEP_ABORTED = "smarthome/setup/step/abort";
    public static final String TOPIC_STEP_ERROR = "smarthome/setup/step/error";

    public static final String PROP_SETUP_FLOW_CONTEXT_ID = "contextId";
    public static final String PROP_SETUP_FLOW_CONTEXT = "setupFlowContext";
    public static final String PROP_ERROR_CODE = "errorCode";
    public static final String PROP_ERROR_MESSAGE = "errorMessage";

    public static final int ERROR_CODE_START_PROCESS = -1001;
    public static final int ERROR_CODE_ABORT_PROCESS = -1002;
    public static final int ERROR_CODE_CONFIGURATION_DESCRIPTION_MISSING = -1003;
    public static final int ERROR_CODE_REQUIRED_PARAMETER_MISSING = -1004;


    private EventFactory() {
        // nothing to do
    }

    private static Dictionary<String, Object> createEventProperties(SetupFlowContext context) {
        Dictionary<String, Object> properties = new Hashtable<>();

        if (context != null) {
            properties.put(PROP_SETUP_FLOW_CONTEXT, context);
        }

        return properties;
    }

    public static Event createStepSucceededEvent(SetupFlowContext context) {
        Dictionary<String, Object> properties = createEventProperties(context);

        Event event = new Event(TOPIC_STEP_SUCCEEDED, properties);

        return event;
    }

//    public static Event createStepAbortedEvent(SetupFlowContext context) {
//        Dictionary<String, Object> properties = createEventProperties(context);
//
//        Event event = new Event(TOPIC_STEP_ABORTED, properties);
//
//        return event;
//    }

    public static Event createErrorOccurredEvent(
            String contextId, int errorCode, String errorMessage) {

        Dictionary<String, Object> properties = new Hashtable<>();

        properties.put(PROP_SETUP_FLOW_CONTEXT_ID, contextId);
        properties.put(PROP_ERROR_CODE, errorCode);
        properties.put(PROP_ERROR_MESSAGE, (errorMessage != null) ? errorMessage : "");

        Event event = new Event(TOPIC_STEP_ERROR, properties);

        return event;
    }

    public static Event createErrorOccurredEvent(
            SetupFlowContext context, int errorCode, String errorMessage) {

        Dictionary<String, Object> properties = createEventProperties(context);

        properties.put(PROP_ERROR_CODE, errorCode);
        properties.put(PROP_ERROR_MESSAGE, (errorMessage != null) ? errorMessage : "");

        Event event = new Event(TOPIC_STEP_ERROR, properties);

        return event;
    }

}
