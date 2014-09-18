/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.smarthome.config.setupflow.internal;

import org.eclipse.smarthome.config.core.ConfigDescriptionRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The {@link ConfigDescriptionRegistryTracker} tracks registered {@link ConfigDescriptionRegistry} services.
 * 
 * @author Oliver Libutzki - Initial contribution
 *
 */
public class ConfigDescriptionRegistryTracker extends ServiceTracker<ConfigDescriptionRegistry, ConfigDescriptionRegistry> {

	public ConfigDescriptionRegistryTracker(BundleContext bundleContext) {
		super(bundleContext, ConfigDescriptionRegistry.class.getName(), null);
	}

}
