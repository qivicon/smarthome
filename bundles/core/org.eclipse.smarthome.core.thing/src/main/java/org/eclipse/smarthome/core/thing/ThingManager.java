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
package org.eclipse.smarthome.core.thing;

import org.eclipse.jdt.annotation.NonNull;

/**
 * {@link ThingManager} interface defines methods for managing a {@link Thing}.
 * 
 * @author Yordan Zhelev - Initial contribution
 */
public interface ThingManager {

    /**
     * Enable or disable the thing.
     * 
     * @param thingUID the {@link ThingUID} uniquely describing the {@link Thing} to be enabled or disabled.
     * @param enabled  {@code true} if the thing should be enabled, {@code false} if the thing should be disabled.
     */
    void enableThing(final @NonNull ThingUID thingUID, final boolean enabled);
}