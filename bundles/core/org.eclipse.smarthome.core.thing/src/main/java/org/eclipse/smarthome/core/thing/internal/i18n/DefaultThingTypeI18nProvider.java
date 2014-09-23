package org.eclipse.smarthome.core.thing.internal.i18n;

import java.util.Locale;

import org.eclipse.smarthome.core.i18n.I18nProvider;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.i18n.ThingTypeI18nProvider;


public class DefaultThingTypeI18nProvider implements ThingTypeI18nProvider {

    private I18nProvider i18nProvider;

    @Override
    public String getLabel(ThingTypeUID thingTypeUID, String defaultLabel, Locale locale) {
        return i18nProvider.getText(thingTypeUID.getBindingId(), "thing-type." + thingTypeUID.getId() + ".label",
                defaultLabel, locale);
    }

    @Override
    public String getDescription(ThingTypeUID thingTypeUID, String defaultDescription, Locale locale) {
        return i18nProvider.getText(thingTypeUID.getBindingId(), "thing-type." + thingTypeUID.getId() + ".description",
                defaultDescription, locale);
    }

    protected void setI18nProvider(I18nProvider i18nProvider) {
        this.i18nProvider = i18nProvider;
    }

    protected void unsetI18nProvider(I18nProvider i18nProvider) {
        this.i18nProvider = null;
    }

}
