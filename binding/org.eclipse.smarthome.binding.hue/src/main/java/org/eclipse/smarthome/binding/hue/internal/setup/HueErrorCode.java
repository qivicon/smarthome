/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.binding.hue.internal.setup;

/**
 * Hue error codes
 * 
 * @author Oliver Libutzki
 * 
 */
public enum HueErrorCode {

    BRIDGE_NOT_RESPONDING(1,
            "The hue bridge doesn't respond. Please doublecheck the provided ip address."),
 CONNECTION_FAILED(
            2, "The connection to the hue bridge failed."), PUSHLINK_BUTTON_NOT_PRESSED(3,
            "The pushlink button on the hue bridge has not been pressed"), UNEXPECTED_ERROR(4,
            "An unexpected error occurred."), IP_ADDRESS_MISSING(1,
            "No ip address for the hue bridge has been provided."), ;
    
    private int code;
    private String message;
    
    private HueErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
