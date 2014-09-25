package org.eclipse.smarthome.core.thing.i18n;

import java.util.Locale;

import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;

public interface ThingTypeI18nProvider {

    String getLabel(ThingTypeUID thingTypeUID, String defaultLabel, Locale locale);

    String getDescription(ThingTypeUID thingTypeUID, String defaultDescription, Locale locale);

    String getChannelLabel(ChannelTypeUID channelTypeUID, String defaultLabel, Locale locale);

    String getChannelDescription(ChannelTypeUID channelTypeUID, String defaultDescription, Locale locale);
    
}