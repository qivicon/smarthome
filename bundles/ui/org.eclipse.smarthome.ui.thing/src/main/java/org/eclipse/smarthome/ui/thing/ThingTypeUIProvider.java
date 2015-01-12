package org.eclipse.smarthome.ui.thing;

import java.util.Locale;

import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

public interface ThingTypeUIProvider {

    String getLabel(ThingTypeUID thingTypeUID, Locale locale);
    
    String getIcon(ThingTypeUID thingTypeUID);
    
    String getChannelLabel(ChannelUID channelUID, Locale locale);
}
