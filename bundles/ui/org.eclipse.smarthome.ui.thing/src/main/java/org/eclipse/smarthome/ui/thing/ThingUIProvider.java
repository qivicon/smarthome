package org.eclipse.smarthome.ui.thing;

import java.util.Locale;

import org.eclipse.smarthome.core.thing.ThingUID;

/**
 * {@link ThingUIProvider} provides UI information for things like a human
 * readable label.
 * 
 * @author Dennis Nobel - Initial contribution
 * 
 */
public interface ThingUIProvider {

    /**
     * Returns a human readable label for the given thing UID.
     * 
     * @param thingUID
     *            thing UID (must not be null)
     * @param locale
     *            locale of the label
     * @return label or null, if no label for the given UID exists
     */
    String getLabel(ThingUID thingUID, Locale locale);

}
