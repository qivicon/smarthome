package org.eclipse.smarthome.core.internal.i18n;

import java.util.Locale;

import org.eclipse.smarthome.core.i18n.BindingInfoI18nProvider;
import org.eclipse.smarthome.core.i18n.ContextSpecificI18nProvider;
import org.eclipse.smarthome.core.i18n.I18nProvider;
import org.osgi.framework.Bundle;

public class DefaultBindingInfoI18nProvider extends ContextSpecificI18nProvider implements BindingInfoI18nProvider {

    private I18nProvider i18nProvider;

    @Override
    public String getDescription(Bundle bundle, String bindingId, String defaultDescription, Locale locale) {
        String key = isConstant(defaultDescription) ? stripConstant(defaultDescription) : inferKey(bindingId,
                "description");
        return i18nProvider.getText(bundle, key, defaultDescription, locale);
    }

    @Override
    public String getLabel(Bundle bundle, String bindingId, String defaultLabel, Locale locale) {
        String key = isConstant(defaultLabel) ? stripConstant(defaultLabel) : inferKey(bindingId, "label");
        return i18nProvider.getText(bundle, key, defaultLabel, locale);
    }

    private String inferKey(String bindingId, String lastSegment) {
        return "binding." + bindingId + "." + lastSegment;
    }

    protected void setI18nProvider(I18nProvider i18nProvider) {
        this.i18nProvider = i18nProvider;
    }

    protected void unsetI18nProvider(I18nProvider i18nProvider) {
        this.i18nProvider = null;
    }

}
