package org.eclipse.smarthome.core.thing.i18n;

import java.util.Locale;

import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;
import org.osgi.framework.Bundle;

/**
 * The {@link ThingTypeI18nProvider} provides localized labels and descriptions for thing types.
 * 
 * @author Dennis Nobel - Initial contribution
 */
public interface ThingTypeI18nProvider {

    /**
     * Returns a localized label for the given thing type UID and locale.
     * 
     * @param bundle
     *            bundle (must not be null)
     * @param thingTypeUID
     *            thing type UID (must not be null)
     * @param defaultLabel
     *            default label or constant (can be null)
     * @param locale
     *            (can be null)
     * @return localized label, or the default label if no label was found
     */
    String getLabel(Bundle bundle, ThingTypeUID thingTypeUID, String defaultLabel, Locale locale);

    /**
     * Returns a localized description for the given thing type UID and locale.
     * 
     * @param bundle
     *            bundle (must not be null)
     * @param thingTypeUID
     *            thing type UID (must not be null)
     * @param defaultDescription
     *            default description or constant (can be null)
     * @param locale
     *            (can be null)
     * @return localized description, or the default label if no label was found
     */
    String getDescription(Bundle bundle, ThingTypeUID thingTypeUID, String defaultDescription, Locale locale);
   
    /**
     * Returns a localized label for the given thing type UID and locale.
     * 
     * @param bundle
     *            bundle (must not be null)
     * @param channelTypeUID
     *            channel type UID (must not be null)
     * @param defaultLabel
     *            default label or constant (can be null)
     * @param locale
     *            (can be null)
     * @return localized label, or the default label if no label was found
     */
    String getChannelLabel(Bundle bundle, ChannelTypeUID channelTypeUID, String defaultLabel, Locale locale);
    
    /**
     * Returns a localized description for the given channel type UID and locale.
     * 
     * @param bundle
     *            bundle (must not be null)
     * @param channelTypeUID
     *            channel type UID (must not be null)
     * @param defaultDescription
     *            default description or constant (can be null)
     * @param locale
     *            (can be null)
     * @return localized description, or the default label if no label was found
     */
    String getChannelDescription(Bundle bundle, ChannelTypeUID channelTypeUID, String defaultDescription, Locale locale);

}