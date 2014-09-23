package org.eclipse.smarthome.core.thing.i18n;

import java.util.Locale;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

public interface ThingTypeI18nProvider {

    String getLabel(ThingTypeUID thingTypeUID, String defaultLabel, Locale locale);

    String getDescription(ThingTypeUID thingTypeUID, String defaultDescription, Locale locale);

}
