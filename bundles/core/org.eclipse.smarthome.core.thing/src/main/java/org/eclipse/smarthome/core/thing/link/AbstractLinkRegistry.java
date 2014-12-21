package org.eclipse.smarthome.core.thing.link;

import org.eclipse.smarthome.core.common.registry.AbstractRegistry;
import org.eclipse.smarthome.core.thing.UID;

public class AbstractLinkRegistry<L extends AbstractLink> extends AbstractRegistry<L> {

    /**
     * Returns if an item for a given item is linked to a channel or thing for a
     * given UID.
     * 
     * @param itemName
     *            item name
     * @param uid
     *            UID
     * @return true if linked, false otherwise
     */
    public boolean isLinked(String itemName, UID uid) {

        for (AbstractLink link : getAll()) {
            if (link.getUID().equals(uid) && link.getItemName().equals(itemName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the item name, which is bound to the given UID.
     * 
     * @param uid
     *            UID
     * @return item name or null if no item is bound to the given UID
     */
    public String getBoundItem(UID uid) {
        for (AbstractLink link : getAll()) {
            if (link.getUID().equals(uid)) {
                return link.getItemName();
            }
        }
        return null;
    }
}
