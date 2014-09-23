package org.eclipse.smarthome.core.i18n;

import java.util.Locale;

public interface I18nProvider {

    String getText(String resource, String key, String defaultText, Locale locale);

}
