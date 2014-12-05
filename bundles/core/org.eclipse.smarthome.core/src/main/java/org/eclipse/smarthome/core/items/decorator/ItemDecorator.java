package org.eclipse.smarthome.core.items.decorator;

import java.util.Collection;
import java.util.Locale;

public interface ItemDecorator {

    /**
     * This method retrieves all decorated items that are currently available in
     * the registry
     * 
     * @return a collection of all available items
     */
    public Collection<DecoratedItem> getItems();

    /**
     * This method retrieves all decorated items that are currently available in the
     * registry
     * 
     * @param locale the locale
     * 
     * @return a collection of all available items
     */
    public Collection<DecoratedItem> getItems(Locale locale);
}
