package org.eclipse.smarthome.core.thing.link;

import org.eclipse.smarthome.core.thing.UID;

public abstract class AbstractLink {

    private final String itemName;

    AbstractLink() {
        this.itemName = null;
    }

    public AbstractLink(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    /**
     * Returns the link ID for a given item name and UID
     * 
     * @param itemName
     *            item name
     * @param uid
     *            UID
     * @return the item channel link ID
     */
    public static String getIDFor(String itemName, UID uid) {
        return itemName + " -> " + uid.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractLink) {
            AbstractLink link = (AbstractLink) obj;
            return this.getID().equals(link.getID());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.itemName.hashCode() * this.getUID().hashCode();
    }

    public String getID() {
        return getIDFor(getItemName(), getUID());
    }
    
    @Override
    public String toString() {
        return getID();
    }
    
    public abstract UID getUID();
}
