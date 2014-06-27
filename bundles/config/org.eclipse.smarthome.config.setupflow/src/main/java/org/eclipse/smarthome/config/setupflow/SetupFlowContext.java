/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 * @author Oliver Libutzki - Introduction of UIDs
 */
public class SetupFlowContext {

	private static final int PROCESS_RANDOM_RANGE = 0xffff;

	private String contextId;

	private String flowId;
	private String bindingId;
	// TODO rename to thingTypeUID
	private String thingTypeId;
	// TODO rename to thingUID
	private String thingId;
	// TODO rename to bridgeUID
	private String bridgeId;
	private String stepId;
	private String nextStepId; // null = end of flow

	private Map<String, Object> properties;

	// TODO: initialize values!
	public SetupFlowContext() {
	}

	public SetupFlowContext(String contextId, String flowId, String thingTypeUID,
			String thingUID, String bridgeUID, Map<String, Object> properties)
			throws IllegalArgumentException {

		if (flowId == null) {
			throw new IllegalArgumentException("The flow ID must not be null!");
		}

		if (thingTypeUID == null) {
			throw new IllegalArgumentException(
					"The Thing type must not be null!");
		}

		this.contextId = (contextId != null) ? contextId : createContextId(
				flowId, this.bindingId);

		this.flowId = flowId;
        ThingTypeUID thingTypeUID2 = new ThingTypeUID(thingTypeUID);
        this.bindingId = thingTypeUID2.getBindingId();
        this.thingTypeId = thingTypeUID2.toString();

		this.thingId = thingUID;
		this.bridgeId = bridgeUID;
		setProperties(properties);
	}

	private String createContextId(String flowId, String bindingId) {
		Random random = new Random();
		int randomNumber = random.nextInt(PROCESS_RANDOM_RANGE);

		try {
			String identifier = String.format("%d-%s",
					System.currentTimeMillis(),
					Integer.toHexString(randomNumber));

			URI processURI = new URI("setup-flow-process", flowId, "/"
					+ bindingId, identifier);

			return processURI.toString();
		} catch (URISyntaxException use) {
			throw new IllegalArgumentException(
					"Setup flow process ID cannot be created!", use);
		}
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getContextId() {
		return this.contextId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getFlowId() {
		return this.flowId;
	}

	public void setBindingId(String bindingId) {
		this.bindingId = bindingId;
	}

	public String getBindingId() {
		return this.bindingId;
	}

	public void setThingTypeId(String thingTypeId) {
		this.thingTypeId = thingTypeId;
	}

	public String getThingTypeId() {
		return this.thingTypeId;
	}

	public void setThingId(String thingId) {
		this.thingId = thingId;
	}

	public String getThingId() {
		return this.thingId;
	}

	public void setBridgeId(String bridgeId) {
		this.bridgeId = bridgeId;
	}

	public String getBridgeId() {
		return bridgeId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getStepId() {
		return this.stepId;
	}

	public void setNextStepId(String nextStepId) {
		this.nextStepId = nextStepId;
	}

	public String getNextStepId() {
		return this.nextStepId;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = (properties == null) ? new HashMap<String, Object>()
				: properties;
	}

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	@Override
	public boolean equals(Object obj) {
		return ((SetupFlowContext) obj).getContextId().equals(getContextId());
	}

	@Override
	public String toString() {
		return "SetupFlowContext [contextId=" + contextId + ", flowId="
				+ flowId + ", bindingId=" + bindingId + ", thingTypeId="
				+ thingTypeId + ", thingId=" + thingId + ", bridgeId=" + bridgeId
				+ ", stepId=" + stepId + ", nextStepId=" + nextStepId
				+ ", properties=" + properties + "]";
	}

}
