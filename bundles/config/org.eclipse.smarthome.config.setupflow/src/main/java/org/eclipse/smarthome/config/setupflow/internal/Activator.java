/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow.internal;

import javax.xml.bind.JAXBContext;

import org.eclipse.smarthome.config.core.util.XMLTypeParser;
import org.eclipse.smarthome.config.setupflow.SetupFlowManager;
import org.eclipse.smarthome.config.setupflow.SetupFlows;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventAdmin;


//TODO: Initialize the code in an own thread since it takes some.
/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public class Activator implements BundleActivator, SimpleServiceTrackerListener<EventAdmin> {

    /** The constant defining the schema file used for validation. */
    private static final String SETUP_FLOW_XSD_FILE = "org.eclipse.smarthome.config.setupflow.schema.xsd";

    private BundleContext bundleContext;

    private SimpleServiceTracker<EventAdmin> eventAdminTracker;

    private DeclarativeSetupFlowTracker setupFlowTracker;

    private SetupFlowProcessManager setupFlowProcessManager;

    private SetupFlowManagerImpl setupFlowManager;
    private ServiceRegistration setupFlowManagerReg;



    @Override
    public synchronized void start(BundleContext context) throws Exception {
        this.bundleContext = context;

        // track EventAdmin and register SetupFlowManager
        this.eventAdminTracker = new SimpleServiceTracker<>(context, EventAdmin.class, this);
        this.eventAdminTracker.open();
    }

    @Override
    public synchronized void stop(BundleContext context) throws Exception {
        // untrack EventAdmin and unregister SetupFlowManager
        this.eventAdminTracker.close();
    }

    @Override
    public synchronized void serviceBindingPerformed(SimpleServiceTracker<EventAdmin> source,
            Event event, EventAdmin service) throws Exception {

        if (event == Event.SERVICE_AVAILABLE) {
            initialize(this.bundleContext, service);
        } else {
            uninitialize(this.bundleContext, service);
        }
    }

    private void initialize(BundleContext context, EventAdmin eventAdmin) throws Exception {
        // register SetupFlowManager
        XMLTypeParser setupFlowParser = new XMLTypeParser(
                context.getBundle().getResource(SETUP_FLOW_XSD_FILE),
                JAXBContext.newInstance(SetupFlows.class.getPackage().getName()));

        this.setupFlowProcessManager = new SetupFlowProcessManager(eventAdmin);

        this.setupFlowManager = new SetupFlowManagerImpl(
                context, this.setupFlowProcessManager);

        this.setupFlowTracker = new DeclarativeSetupFlowTracker(
                context, setupFlowParser, this.setupFlowManager);

        this.setupFlowTracker.open();

        this.setupFlowManagerReg = context.registerService(
                SetupFlowManager.class.getName(), this.setupFlowManager, null);
    }

    private void uninitialize(BundleContext context, EventAdmin eventAdmin) throws Exception {
        // unregister SetupFlowManager
        this.setupFlowManagerReg.unregister();
        this.setupFlowTracker.close();
        this.setupFlowManager.release();
        this.setupFlowProcessManager.abortAll();
    }

}
