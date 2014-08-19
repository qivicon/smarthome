/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow;


/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public interface SetupStepHandlerCallback {

    void sendStepSucceededEvent(SetupFlowContext context);

    void sendErrorOccurredEvent(SetupFlowContext context, int errorCode, String errorMessage);

    // void sendStepAbortedEvent(SetupFlowContext context);
    //
    // void sendStepStartedErrorEvent(SetupFlowContext context, int errorCode, String errorMessage);
    //
    // void sendStepAbortedErrorEvent(SetupFlowContext context, int errorCode, String errorMessage);

}
