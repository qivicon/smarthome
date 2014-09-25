package org.eclipse.smarthome.core.internal.i18n;

import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.smarthome.core.i18n.I18nProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;


/**
 * The {@link I18nProviderImpl} is a concrete implementation of the {@link I18nProvider}
 * service interface.
 * <p>
 * This implementation uses the i18n mechanism of Java ({@link ResourceBundle}) to translate a
 * given key into text. The resources must be placed under a specific directory within the certain
 * modules. Each module is tracked in the platform by using the {@link ResourceBundleTracker} and
 * managed by using one certain {@link LanguageResource} which is responsible for the translation.
 *
 * @author Michael Grammling - Initial Contribution
 */
public class I18nProviderImpl implements I18nProvider {

    private ResourceBundleTracker resourceBundleTracker;


    protected void activate(BundleContext bundleContext) {
        this.resourceBundleTracker = new ResourceBundleTracker(bundleContext);
        this.resourceBundleTracker.open();
    }

    protected void deactivate(BundleContext bundleContext) {
        this.resourceBundleTracker.close();
    }

    @Override
    public String getText(String resource, String key, String defaultText, Locale locale) {
        if (resource != null) {
            Collection<LanguageResource> languageResources =
                    this.resourceBundleTracker.getAllLanguageResources();
    
            for (LanguageResource languageResource : languageResources) {
                if (languageResource.containsResource(resource)) {
                    String text = languageResource.getText(resource, key, locale);
                    if (text != null) {
                        return text;   // otherwise continue, another bundle could contain the resource
                    }
                }
            }
        }

        return (defaultText != null) ? defaultText : (key != null) ? key : "";
    }

    @Override
    public String getText(Bundle bundle, String key, String defaultText, Locale locale) {
        LanguageResource languageResource = this.resourceBundleTracker.getLanguageResource(bundle);
        if (languageResource != null) {
            String text = languageResource.getText(key, locale);
            if (text != null) {
                return text;
            }
        }

        return (defaultText != null) ? defaultText : (key != null) ? key : "";
    }

}
