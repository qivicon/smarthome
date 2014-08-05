/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow.internal;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.config.core.util.XMLTypeParser;
import org.eclipse.smarthome.config.setupflow.SetupFlow;
import org.eclipse.smarthome.config.setupflow.SetupFlows;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TODO: Add an XML file filter for the search.
/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public class DeclarativeSetupFlowTracker extends BundleTracker {

    private Logger logger = LoggerFactory.getLogger(DeclarativeSetupFlowTracker.class);

    private static final String DIRECTORY_ESH_SETUP_FLOW = "/ESH-INF/setup/";

    private XMLTypeParser setupFlowParser;

    private SetupFlowManagerImpl setupFlowManager;
    private Map<Bundle, SetupFlowProvider> setupFlowProviderMap;


    public DeclarativeSetupFlowTracker(BundleContext bundleContext,
            XMLTypeParser setupFlowParser, SetupFlowManagerImpl setupFlowManager) {

        super(bundleContext, Bundle.ACTIVE, null);

        if (bundleContext == null) {
            throw new IllegalArgumentException("The BundleContext must not be null!");
        }
        if (setupFlowParser == null) {
            throw new IllegalArgumentException("The setup flow parser must not be null!");
        }
        if (setupFlowManager == null) {
            throw new IllegalArgumentException("The SetupFlowManager must not be null!");
        }

        this.setupFlowParser = setupFlowParser;
        this.setupFlowManager = setupFlowManager;

        this.setupFlowProviderMap = new HashMap<>();
    }

    @Override
    public final synchronized void open() {
        super.open();
    }

    @Override
    public final synchronized void close() {
        super.close();
        this.setupFlowProviderMap.clear();
    }

    private SetupFlowProvider acquireSetupFlowProvider(Bundle bundle) {
        if (bundle != null) {
            SetupFlowProvider setupFlowProvider = this.setupFlowProviderMap.get(bundle);

            if (setupFlowProvider == null) {
                setupFlowProvider = new SetupFlowProvider(bundle);

                this.logger.debug("Create an empty SetupFlowProvider for the module '{}'.",
                        bundle.getSymbolicName());

                this.setupFlowProviderMap.put(bundle, setupFlowProvider);

                this.setupFlowManager.addSetupFlowProvider(setupFlowProvider);
            }

            return setupFlowProvider;
        }

        return null;
    }

    private void releaseSetupFlowProvider(Bundle bundle) {
        if (bundle != null) {
            SetupFlowProvider setupFlowProvider = this.setupFlowProviderMap.get(bundle);

            if (setupFlowProvider != null) {
                this.logger.debug("Release the SetupFlowProvider for the module '{}'.",
                        bundle.getSymbolicName());

                this.setupFlowManager.removeSetupFlowProvider(setupFlowProvider);

                this.setupFlowProviderMap.remove(bundle);
            }
        }
    }

    private void addSetupFlow(Bundle bundle, SetupFlow setupFlow) {
        SetupFlowProvider setupFlowProvider = acquireSetupFlowProvider(bundle);

        if (setupFlowProvider != null) {
            setupFlowProvider.addSetupFlow(setupFlow);
        }
    }

    private void addSetupFlow(Bundle bundle, SetupFlows setupFlows) {
        SetupFlowProvider setupFlowProvider = acquireSetupFlowProvider(bundle);

        if (setupFlowProvider != null) {
            setupFlowProvider.addSetupFlows(setupFlows);
        }
    }

    @Override
    public final synchronized Bundle addingBundle(Bundle bundle, BundleEvent event) {
        Enumeration<String> setupFlowPaths = bundle.getEntryPaths(DIRECTORY_ESH_SETUP_FLOW);

        if (setupFlowPaths != null) {
            int numberOfParsedSetupFlows = 0;

            while (setupFlowPaths.hasMoreElements()) {
                String moduleName = bundle.getSymbolicName();
                String setupFlowPath = setupFlowPaths.nextElement();
                URL setupFlowURL = bundle.getEntry(setupFlowPath);
                String setupFlowFile = setupFlowURL.getFile();

                try {
                    this.logger.debug(
                            "Parsing the setup flow definition file '{}' in module '{}'...",
                            setupFlowFile, moduleName);

                    Object setupFlowObj = this.setupFlowParser.parse(setupFlowURL);

                    if (setupFlowObj instanceof SetupFlow) {
                        addSetupFlow(bundle, (SetupFlow) setupFlowObj);
                    } else if (setupFlowObj instanceof SetupFlows) {
                        addSetupFlow(bundle, (SetupFlows) setupFlowObj);
                    }

                    numberOfParsedSetupFlows++;
                } catch (Exception ex) {
                    this.logger.error("The file '" + setupFlowFile + "' in module '" + moduleName
                            + "' does not contain a valid setup flow!", ex);
                }
            }

            if (numberOfParsedSetupFlows > 0) {
                return bundle;
            }
        }

        return null;
    }

    @Override
    public final synchronized void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        this.logger.debug("Unregistering the setup flow definition(s) for module '{}'...",
                bundle.getSymbolicName());

        releaseSetupFlowProvider(bundle);
    }

}
