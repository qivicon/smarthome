package org.eclipse.smarthome.config.core.internal;

import java.net.URI;
import java.util.Locale;

import org.eclipse.smarthome.config.core.i18n.ConfigDescriptionI18nProvider;
import org.eclipse.smarthome.core.i18n.ContextSpecificI18nProvider;
import org.eclipse.smarthome.core.i18n.I18nProvider;
import org.osgi.framework.Bundle;

/**
 * The {@link DefaultConfigDescriptionI18nProvider} is the default
 * {@link ConfigDescriptionI18nProvider} implementation that uses the
 * {@link I18nProvider} to resolve the localized texts.
 * It automatically infers the key if the default text is not a constant.
 * 
 * @author Dennis Nobel - Initial contribution
 */
public class DefaultConfigDescriptionI18nProvider extends ContextSpecificI18nProvider
        implements ConfigDescriptionI18nProvider {

    private I18nProvider i18nProvider;

    @Override
    public String getParameterDescription(Bundle bundle, URI configDescriptionURI,
            String parameterName, String defaultDescription, Locale locale) {
        String key = isConstant(defaultDescription) ? stripConstant(defaultDescription) : inferKey(
                configDescriptionURI, parameterName, "description");
        return i18nProvider.getText(bundle, key, defaultDescription, locale);
    }

    @Override
    public String getParameterLabel(Bundle bundle, URI configDescriptionURI, String parameterName,
            String defaultLabel, Locale locale) {
        String key = isConstant(defaultLabel) ? stripConstant(defaultLabel) : inferKey(
                configDescriptionURI, parameterName, "label");
        return i18nProvider.getText(bundle, key, defaultLabel, locale);
    }

    protected void setI18nProvider(I18nProvider i18nProvider) {
        this.i18nProvider = i18nProvider;
    }

    protected void unsetI18nProvider(I18nProvider i18nProvider) {
        this.i18nProvider = null;
    }

    private String inferKey(URI configDescriptionURI, String parameterName, String lastSegment) {
        String uri = configDescriptionURI.getSchemeSpecificPart().replace(":", ".");
        // TODO: remove this workaround when URI fixed is applied
        uri = uri.replace("//", "");
        return configDescriptionURI.getScheme()
                + ".config." + uri + "." + parameterName + "." + lastSegment;
    }

}
