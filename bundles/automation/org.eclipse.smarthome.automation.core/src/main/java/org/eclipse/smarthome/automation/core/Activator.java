package org.eclipse.smarthome.automation.core;

import java.io.InputStream;
import java.net.URL;

import org.eclipse.smarthome.automation.core.jsonmodel.Rule;
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
        try {
            Thread.sleep(2000);
            URL url = context.getBundle().getEntry("rule.json");
            InputStream inputStream = url.openStream();
            Rule rule;
            rule = new RulesParser().parseRule(inputStream);

            inputStream.close();
            RuleEngine ruleEngine = new RuleEngine(context);
            ruleEngine.register(rule);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
