package org.eclipse.smarthome.config.core.i18n;

import java.net.URI;
import java.util.Locale;

import org.osgi.framework.Bundle;

public interface ConfigDescriptionI18nProvider {

    String getParameterLabel(Bundle bundle, URI configDescriptionURI, String parameterName, String defaultLabel,
            Locale locale);

    String getParameterDescription(Bundle bundle, URI configDescriptionURI, String parameterName,
            String defaultDescription, Locale locale);

}
