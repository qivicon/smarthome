package org.eclipse.smarthome.ui.thing.internal;

import java.util.List;

import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.ui.thing.ThingUIProvider;
import org.eclipse.smarthome.ui.thing.ThingUIRegistry;

import com.google.common.collect.Lists;

/**
 * Implementation for {@link ThingUIRegistry}.
 * 
 * @author Dennis Nobel - Initial contribution
 * 
 */
public class ThingUIRegistryImpl implements ThingUIRegistry {

    private List<ThingUIProvider> thingUIProviders = Lists.newCopyOnWriteArrayList();

    @Override
    public String getLabel(ThingUID thingUID) {

        if (thingUID == null) {
            throw new IllegalArgumentException("Thing UID must not be null");
        }

        return getLabelFromProviders(thingUID);
    }

    private String getLabelFromProviders(ThingUID thingUID) {
        for (ThingUIProvider thingUIProvider : thingUIProviders) {
            String label = thingUIProvider.getLabel(thingUID);
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
