package org.eclipse.smarthome.automation.core.internal;

import java.io.InputStream;
import java.net.URL;

import org.eclipse.smarthome.automation.core.Rule;
import org.eclipse.smarthome.automation.core.RuleEngine;
import org.eclipse.smarthome.automation.core.parser.RulesParser;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator, Runnable {

    private BundleContext context;

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        new Thread(this).start();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void run() {
     
    }

}
