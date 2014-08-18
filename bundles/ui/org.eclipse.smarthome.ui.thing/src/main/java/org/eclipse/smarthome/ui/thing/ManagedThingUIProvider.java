package org.eclipse.smarthome.ui.thing;

import org.eclipse.smarthome.core.thing.ThingUID;

/**
 * {@link ManagedThingUIProvider} implements the {@link ThingUIProvider}
 * interface and allows to set labels for {@link ThingUID}s.
 * 
 * @author Dennis Nobel - Initial contribution
 * 
 */
public interface ManagedThingUIProvider extends ThingUIProvider {

    /**
     * Sets a label for the given thing UID.
     * 
     * @param thingUID
     *            thing UID (must not be null)
     * @param label
     *            (must not be null).
     */
    void setLabel(ThingUID thingUID, String label);

}
