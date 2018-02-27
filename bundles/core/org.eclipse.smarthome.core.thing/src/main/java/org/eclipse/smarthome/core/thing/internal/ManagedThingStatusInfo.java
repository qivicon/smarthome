/**
 * Copyright (c) 2014,2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.smarthome.core.thing.internal;

/**
 * {@link ManagedThingStatusInfo} is used for persistence of information regarding thing's status.
 * 
 * @author Yordan Zhelev - Initial contribution
 */
public class ManagedThingStatusInfo {

    /**
     * Whether the thing is enabled or disabled.
     */
    private final boolean enabled;

    public ManagedThingStatusInfo(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return {@code true} if the thing is enabled. Returns {@code false} otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }
}