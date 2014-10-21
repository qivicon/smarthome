package org.eclipse.smarthome.ui.thing.internal;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.ui.thing.ThingUIProvider;
import org.eclipse.smarthome.ui.thing.ThingUIRegistry;

/**
 * Implementation for {@link ThingUIRegistry}.
 * 
 * @author Dennis Nobel - Initial contribution
 * 
 */
public class ThingUIRegistryImpl implements ThingUIRegistry {

    private List<ThingUIProvider> thingUIProviders = new CopyOnWriteArrayList<>();

    @Override
    public String getLabel(ThingUID thingUID, Locale locale) {

        if (thingUID == null) {
            throw new IllegalArgumentException("Thing UID must not be null");
        }

        return getLabelFromProviders(thingUID, locale);
    }

    private String getLabelFromProviders(ThingUID thingUID, Locale locale) {
        for (ThingUIProvider thingUIProvider : thingUIProviders) {
            String label = thingUIProvider.getLabel(thingUID, locale);
            if (label != null) {
                return label;
            }
        }
        return null;
    }

    protected void addThingUIProvider(ThingUIProvider thingUIProvider) {
        thingUIProviders.add(thingUIProvider);
    }

    protected void removeThingUIProvider(ThingUIProvider thingUIProvider) {
        thingUIProviders.remove(thingUIProvider);
    }

}
