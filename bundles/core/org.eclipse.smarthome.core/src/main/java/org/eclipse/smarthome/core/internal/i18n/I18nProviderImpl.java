package org.eclipse.smarthome.core.internal.i18n;

import java.util.Collection;
import java.util.Locale;

import org.eclipse.smarthome.core.i18n.I18nProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;


public class I18nProviderImpl implements I18nProvider {

    private ResourceBundleTracker resourceBundleTracker;


    protected void activate(BundleContext bundleContext) {
        this.resourceBundleTracker = new ResourceBundleTracker(bundleContext);
        this.resourceBundleTracker.open();
    }

    protected void deactivate(BundleContext bundleContext) {
        this.resourceBundleTracker.close();
    }

    public String getText(String resource, String key, String defaultText, Locale locale) {
        String text = null;

        Collection<LanguageResource> languageResources =
                this.resourceBundleTracker.getAllLanguageResources();

        for (LanguageResource languageResource : languageResources) {
            if (languageResource.containsResource(resource)) {
                text = languageResource.getText(resource, key, locale);
                if (text != null) {
                    return text;   // otherwise continue, another bundle could contain the resource
                }
            }
        }

        return (text != null) ? text : defaultText;
    }

    public String getText(Bundle bundle, String key, String defaultText, Locale locale) {
        String text = null;

        LanguageResource languageResource = this.resourceBundleTracker.getLanguageResource(bundle);
        if (languageResource != null) {
            text = languageResource.getText(key, locale);
        }

        return (text != null) ? text : defaultText;
    }

}
