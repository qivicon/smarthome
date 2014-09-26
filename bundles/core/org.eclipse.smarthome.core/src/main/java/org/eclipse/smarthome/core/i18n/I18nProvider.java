package org.eclipse.smarthome.core.i18n;

import java.util.Locale;

import org.osgi.framework.Bundle;


/**
 * The {@link I18nProvider} is a service interface for internationalization support within
 * the platform. This service can be used to translate specific keys into its according
 * text by considering the specified {@link Locale} (language). Any module which supports
 * resource files are managed by this provider and used for translation. This service uses
 * the i18n mechanism of Java. 
 * 
 * @author Michael Grammling - Initial Contribution
 */
public interface I18nProvider {

    /**
     * Returns a translation for the specified key in the specified locale (language) by only
     * considering the translations within the specified module.
     * <p>
     * If no translation could be found, the specified default text is returned. If the default
     * text is {@code null}, the key is returned and if the key is {@code null}, an empty string
     * is returned.
     * 
     * @param bundle the module to be used for the look-up (could be null)
     * @param key the key to be translated (could be null or empty)
     * @param defaultText the default text to be used (could be null or empty)
     * @param locale the locale (language) to be used (could be null)
     *
     * @return the translated text, or the default text, or an empty text
     */
    String getText(Bundle bundle, String key, String defaultText, Locale locale);

}
