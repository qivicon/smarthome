package org.eclipse.smarthome.core.thing.internal.i18n;

import java.util.Locale;

import org.eclipse.smarthome.core.i18n.ContextSpecificI18nProvider;
import org.eclipse.smarthome.core.i18n.I18nProvider;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.i18n.ThingTypeI18nProvider;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;
import org.osgi.framework.Bundle;

public class DefaultThingTypeI18nProvider extends ContextSpecificI18nProvider implements ThingTypeI18nProvider {

    private I18nProvider i18nProvider;

    @Override
    public String getChannelDescription(Bundle bundle, ChannelTypeUID channelTypeUID, String defaultDescription,
            Locale locale) {
        String key = isConstant(defaultDescription) ? stripConstant(defaultDescription) : inferChannelKey(
                channelTypeUID, "description");
        return i18nProvider.getText(bundle, key, defaultDescription, locale);
    }

    @Override
    public String getChannelLabel(Bundle bundle, ChannelTypeUID channelTypeUID, String defaultLabel, Locale locale) {
        String key = isConstant(defaultLabel) ? stripConstant(defaultLabel) : inferChannelKey(channelTypeUID, "label");
        return i18nProvider.getText(bundle, key, defaultLabel, locale);
    }

    @Override
    public String getDescription(Bundle bundle, ThingTypeUID thingTypeUID, String defaultDescription, Locale locale) {
        String key = isConstant(defaultDescription) ? stripConstant(defaultDescription) : inferThingTypeKey(
                thingTypeUID, "description");
        return i18nProvider.getText(bundle, key, defaultDescription, locale);
    }

    @Override
    public String getLabel(Bundle bundle, ThingTypeUID thingTypeUID, String defaultLabel, Locale locale) {
        String key = isConstant(defaultLabel) ? stripConstant(defaultLabel) : inferThingTypeKey(thingTypeUID, "label");
        return i18nProvider.getText(bundle, key, defaultLabel, locale);
    }

    protected void setI18nProvider(I18nProvider i18nProvider) {
        this.i18nProvider = i18nProvider;
    }

    protected void unsetI18nProvider(I18nProvider i18nProvider) {
        this.i18nProvider = null;
    }

    private String inferChannelKey(ChannelTypeUID channelTypeUID, String lastSegment) {
        return "channel-type." + channelTypeUID.getBindingId() + "." + channelTypeUID.getId() + "." + lastSegment;
    }

    private String inferThingTypeKey(ThingTypeUID thingTypeUID, String lastSegment) {
        return "thing-type." + thingTypeUID.getBindingId() + "." + thingTypeUID.getId() + "." + lastSegment;
    }

}
