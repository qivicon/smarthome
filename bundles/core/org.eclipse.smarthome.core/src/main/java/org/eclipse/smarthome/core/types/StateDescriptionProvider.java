package org.eclipse.smarthome.core.types;

import java.util.Locale;

public interface StateDescriptionProvider {

    /**
     * Returns the state description for an item name
     * 
     * @param itemName
     *            item name (must not be null)
     * @param locale
     *            locale (can be null)
     * @return state description or null if no state description could be found
     */
    StateDescription getStateDescription(String itemName, Locale locale);

}
