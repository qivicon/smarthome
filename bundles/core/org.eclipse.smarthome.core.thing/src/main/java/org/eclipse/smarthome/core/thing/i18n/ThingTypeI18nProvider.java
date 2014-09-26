package org.eclipse.smarthome.core.thing.i18n;

import java.util.Locale;

import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;
import org.osgi.framework.Bundle;

public interface ThingTypeI18nProvider {

    String getLabel(Bundle bundle, ThingTypeUID thingTypeUID, String defaultLabel, Locale locale);

    String getDescription(Bundle bundle, ThingTypeUID thingTypeUID, String defaultDescription, Locale locale);

    String getChannelLabel(Bundle bundle, ChannelTypeUID channelTypeUID, String defaultLabel, Locale locale);

    String getChannelDescription(Bundle bundle, ChannelTypeUID channelTypeUID, String defaultDescription, Locale locale);
    
}