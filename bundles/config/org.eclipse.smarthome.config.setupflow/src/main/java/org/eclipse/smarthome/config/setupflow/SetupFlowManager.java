/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow;

import java.util.List;
import java.util.Map;

import org.eclipse.smarthome.config.setupflow.SetupFlow;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;


/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public interface SetupFlowManager {

    Map<String, List<String>> getSupportedThingTypeIds();

    SetupFlow getSetupFlow(ThingTypeUID thingTypeUID);

    SetupFlowContext createSetupFlowContext(String flowId, ThingTypeUID thingTypeUID,
            ThingUID thingUID, ThingUID bridgeUID) throws IllegalArgumentException;

    void startSetupStep(SetupFlowContext context) throws IllegalArgumentException;

    boolean abortSetupStep(SetupFlowContext context);

}
