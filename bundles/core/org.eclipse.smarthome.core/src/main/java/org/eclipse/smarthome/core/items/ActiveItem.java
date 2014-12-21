package org.eclipse.smarthome.core.items;

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
     * @param tag - a tag that is to be removed from item's tags. 
     */
    public void removeTag(String tag);
    
    
    /**
     * Clears all tags of this item.
     */
    public void removeAllTags();
}
