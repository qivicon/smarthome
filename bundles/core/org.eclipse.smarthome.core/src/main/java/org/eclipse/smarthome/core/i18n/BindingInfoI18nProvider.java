package org.eclipse.smarthome.core.i18n;

import java.util.Locale;

import org.osgi.framework.Bundle;

public interface BindingInfoI18nProvider {

    String getLabel(Bundle bundle, String bindingId, String defaultLabel, Locale locale);

    String getDescription(Bundle bundle, String bindingId, String defaultDescription, Locale locale);

}
