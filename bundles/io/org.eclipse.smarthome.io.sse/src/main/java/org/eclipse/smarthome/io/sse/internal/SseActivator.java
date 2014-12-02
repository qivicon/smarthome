package org.eclipse.smarthome.io.sse.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bundle activator for Eclipse Smarthome SSE bundle.
 * 
 * @author ivan.iliev
 * 
 */
public class SseActivator implements BundleActivator {

    private static final Logger logger = LoggerFactory.getLogger(SseActivator.class);

    private static BundleContext context;

    /**
     * Called whenever the OSGi framework starts our bundle
     */
    public void start(BundleContext bc) throws Exception {
        context = bc;
        logger.debug("SSE API has been started.");
    }

    /**
     * Called whenever the OSGi framework stops our bundle
     */
    public void stop(BundleContext bc) throws Exception {
        context = null;
        logger.debug("SSE API has been stopped.");
    }

    /**
     * Returns the bundle context of this bundle
     * 
     * @return the bundle context
     */
    public static BundleContext getContext() {
        return context;
    }
}
