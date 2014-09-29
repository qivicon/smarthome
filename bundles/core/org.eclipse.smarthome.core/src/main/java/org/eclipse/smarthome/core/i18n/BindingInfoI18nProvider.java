package org.eclipse.smarthome.core.i18n;

import java.util.Locale;

import org.osgi.framework.Bundle;

/**
 * The {@link BindingInfoI18nProvider} provides localized labels and descriptions for bindings.
 * 
 * @author Dennis Nobel - Initial contribution
 */
public interface BindingInfoI18nProvider {

    /**
     * Returns a localized label for the given binding ID and locale.
     *
     * @param bundle the bundle (must not be null)
     * @param bindingId the binding ID (must not be null)
     * @param defaultLabel the default label or constant (can be null)
     * @param locale the locale (can be null)
     *
     * @return the localized label, or the default label if no label was found
     *     (could be null or empty)
     */
    String getLabel(Bundle bundle, String bindingId, String defaultLabel, Locale locale);

    /**
     * Returns a localized description for the given binding ID and locale.
     *
     * @param bundle the bundle (must not be null)
     * @param bindingId the binding ID (must not be null)
     * @param defaultDescription the default description or constant (can be null)
     * @param locale the locale (can be null)
     *
     * @return the localized description, or the default description if no label was found
     *     (could be null or empty)
     */
    String getDescription(Bundle bundle, String bindingId, String defaultDescription, Locale locale);

}
