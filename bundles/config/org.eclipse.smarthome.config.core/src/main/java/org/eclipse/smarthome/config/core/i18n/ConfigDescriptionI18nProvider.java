package org.eclipse.smarthome.config.core.i18n;

import java.net.URI;
import java.util.Locale;

import org.eclipse.smarthome.config.core.ConfigDescription;
import org.osgi.framework.Bundle;

/**
 * The {@link ConfigDescriptionI18nProvider} provides localized labels and
 * descriptions for {@link ConfigDescription}s.
 * 
 * @author Dennis Nobel - Initial contribution
 */
public interface ConfigDescriptionI18nProvider {

    /**
     * Returns a localized label for the given config description URI and locale.
     * 
     * @param bundle
     *            bundle (must not be null)
     * @param configDescriptionURI
     *            config description URI (must not be null)
     * @param parameterName
     *            parameter name (must not be null)
     * @param defaultLabel
     *            default label (can be null)
     * @param locale
     *            locale (can be null)
     * @return localized label, or the default label if no label was found (can be null or empty)
     */
    String getParameterLabel(Bundle bundle, URI configDescriptionURI, String parameterName,
            String defaultLabel, Locale locale);

    /**
     * Returns a localized description for the given config description URI and locale.
     * 
     * @param bundle
     *            bundle (must not be null)
     * @param configDescriptionURI
     *            config description URI (must not be null)
     * @param parameterName
     *            parameter name (must not be null)
     * @param defaultDescription
     *            default description (can be null)
     * @param locale
     *            locale (can be null)
     * @return localized label, or the default label if no label was found (can be null or empty)
     */
    String getParameterDescription(Bundle bundle, URI configDescriptionURI, String parameterName,
            String defaultDescription, Locale locale);

}
