package org.eclipse.smarthome.core.items;

import java.util.Collection;

public interface ActiveItem extends Item {

    /**
     * Sets the label of an item
     * 
     * @param label label (can be null)
     */
    void setLabel(String label);
    
    /**
     * Sets the category of the item (can be null)
     * 
     * @param category
     *            category
     */
    void setCategory(String category);
   
    /**
     * 
     * @param tag - a tag that is to be added to item's tags.
     */
    public void addTag(String tag);
    
    /**
     * 
     * @param tags - tags that are to be added to item's tags.
     */
    public void addTags(String... tags);
    
    /**
     * 
     * @param tags - tags that are to be added to item's tags.
     */
    public void addTags(Collection<String> tags);
    
    /**
     * 
     * @param tag - a tag that is to be removed from item's tags. 
     */
    public void removeTag(String tag);
    
    
    /**
     * Clears all tags of this item.
     */
    public void removeAllTags();

    /**
     * Removes the according item from a group.
     * 
     * @param groupItemName name of the group (must not be null)
     */
    public abstract void removeGroupName(String groupItemName);

    /**
     * Assigns the according item to a group.
     * 
     * @param groupItemName name of the group (must not be null)
     */
    public abstract void addGroupName(String groupItemName);
}
