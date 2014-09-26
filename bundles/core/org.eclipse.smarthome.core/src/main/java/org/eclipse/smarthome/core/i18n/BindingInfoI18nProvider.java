package org.eclipse.smarthome.core.i18n;

import java.util.Locale;

import org.osgi.framework.Bundle;

/**
 * {@link BindingInfoI18nProvider} provides localized labels and descriptions
 * for bindings.
 * 
 * @author Dennis Nobel - Initial contribution
 */
public interface BindingInfoI18nProvider {

    /**
     * Returns a localized label for the given binding id and locale.
     * 
     * @param bundle
     *            bundle (must not be null)
     * @param bindingId
     *            binding id (must not be null)
     * @param defaultLabel
     *            default label or constant (can be null)
     * @param locale
     *            (can be null)
     * @return localized label, or the default label if no label was found
     */
    String getLabel(Bundle bundle, String bindingId, String defaultLabel, Locale locale);

    /**
     * Returns a localized description for the given binding id and locale.
     * 
     * @param bundle
     *            bundle (must not be null)
     * @param bindingId
     *            binding id (must not be null)
     * @param defaultDescription
     *            default description or constant (can be null)
     * @param locale
     *            (can be null)
     * @return localized description, or the default description if no label was
     *         found
     */
    String getDescription(Bundle bundle, String bindingId, String defaultDescription, Locale locale);

}
