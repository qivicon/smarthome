package org.eclipse.smarthome.core.internal.i18n;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;


/**
 * The {@link ResourceBundleTracker} class tracks all <i>OSGi</i> bundles which are in the
 * {@link Bundle.RESOLVED} state or which it already passed. Only bundles which contains
 * i18n resource files are considered within this tracker. 
 * <p>
 * This tracker must be started by calling {@link #open()} and stopped
 * by calling {@link #close()}.
 * 
 * @author Michael Grammling - Initial Contribution
 */
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
                return languageResource;
            }
        }

        return null;
    }

    @Override
    public synchronized void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        if ((event == null) || (event.getType() == BundleEvent.UNRESOLVED)) {
System.out.println("### REMOVED: " + bundle.getBundleId());
            LanguageResource languageResource = this.bundleLanguageResourceMap.remove(bundle);
            if (languageResource != null) {
                languageResource.clearCache();
            }
        }
    }

    /**
     * Returns the {@link LanguageResource} instance for the specified bundle,
     * or {@code null} if it cannot be found within that tracker.
     * 
     * @param bundle the bundle which points to the specific resource manager (could be null)
     * @return the specific resource manager (could be null)
     */
    public LanguageResource getLanguageResource(Bundle bundle) {
        if (bundle != null) {
            return this.bundleLanguageResourceMap.get(bundle);
        }

        return null;
    }

    /**
     * Returns all {@link LanguageResource} instances managed by this tracker.
     * 
     * @return the list of all resource managers (not null, could be empty)
     */
    public Collection<LanguageResource> getAllLanguageResources() {
        return this.bundleLanguageResourceMap.values();
    }

}
