/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing;

/**
 * {@link ThingStatusDetail} defines possible status details of a {@link StatusInfo}.
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public enum ThingStatusDetail {
    NONE(0),
    HANDLER_MISSING_ERROR(1),
    HANDLER_INITIALIZING_ERROR(2),
    CONFIGURATION_PENDING(3),
    COMMUNICATION_ERROR(4),
    CONFIGURATION_ERROR(5),
    BRIDGE_OFFLINE(6),
    FIRMWARE_UPDATING(7),
    DUTY_CYCLE(8);

    private final int value;

    private ThingStatusDetail(final int newValue) {
        value = newValue;
    }

    /**
     * Gets the value of a thing status detail.
     * 
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
