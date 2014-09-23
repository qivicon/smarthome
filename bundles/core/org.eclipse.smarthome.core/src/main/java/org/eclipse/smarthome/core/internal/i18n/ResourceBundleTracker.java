package org.eclipse.smarthome.core.internal.i18n;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;


public class ResourceBundleTracker extends BundleTracker {

    private Map<Bundle, LanguageResource> bundleLanguageResourceMap;


    public ResourceBundleTracker(BundleContext bundleContext) {
        super(bundleContext, Bundle.RESOLVED | Bundle.ACTIVE, null);

        if (bundleContext == null) {
            throw new IllegalArgumentException("The BundleContext must not be null!");
        }

        this.bundleLanguageResourceMap = new LinkedHashMap<>();
    }

    @Override
    public synchronized void open() {
        super.open();
    }

    @Override
    public synchronized void close() {
        super.close();
        this.bundleLanguageResourceMap.clear();
    }

    @Override
    public synchronized Object addingBundle(Bundle bundle, BundleEvent event) {
        if (!this.bundleLanguageResourceMap.containsKey(bundle)) {
            LanguageResource languageResource = new LanguageResource(bundle);

            if (languageResource.containsResources()) {
                this.bundleLanguageResourceMap.put(bundle, languageResource);
System.out.println("### ADDED: " + bundle.getBundleId());
                return bundle;
            }
        }

        return null;
    }

    @Override
    public synchronized void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        if ((event == null) || (event.getType() == BundleEvent.UNRESOLVED)) {
System.out.println("### REMOVED: " + bundle.getBundleId());
            this.bundleLanguageResourceMap.remove(bundle);
        }
    }

    public LanguageResource getLanguageResource(Bundle bundle) {
        return this.bundleLanguageResourceMap.get(bundle);
    }

    public Collection<LanguageResource> getAllLanguageResources() {
        return this.bundleLanguageResourceMap.values();
    }

}
