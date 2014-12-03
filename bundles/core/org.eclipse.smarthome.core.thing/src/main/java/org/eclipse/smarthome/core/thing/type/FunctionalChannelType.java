package org.eclipse.smarthome.core.thing.type;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FunctionalChannelType extends ChannelType {

    private final String itemType;
    private final Set<String> tags;
    private final String category;
    private final StateDescription stateDescription;

    public FunctionalChannelType(ChannelTypeUID uid, String itemType, String label, String description,
            String category, Set<String> tags, URI configDescriptionURI, StateDescription stateDescription)
            throws IllegalArgumentException {
        super(uid, label, description, configDescriptionURI);
        
        this.category = category;

        if ((itemType == null) || (itemType.isEmpty())) {
            throw new IllegalArgumentException("The item type must neither be null nor empty!");
        }

        this.itemType = itemType;

        if (tags != null) {
            this.tags = Collections.unmodifiableSet(new HashSet<String>(tags));
        } else {
            this.tags = Collections.unmodifiableSet(new HashSet<String>(0));
        }
        
        this.stateDescription = stateDescription;
    }

    /**
     * Returns the item type of this {@link ChannelType}, e.g. {@code ColorItem}.
     * 
     * @return the item type of this Channel type, e.g. {@code ColorItem} (neither null nor empty)
     */
    public String getItemType() {
        return this.itemType;
    }

    /**
     * Returns all tags of this {@link ChannelType}, e.g. {@code Alarm}.
     * 
     * @return all tags of this Channel type, e.g. {@code Alarm} (not null, could be empty)
     */
    public Set<String> getTags() {
        return this.tags;
    }
    
    public String getCategory() {
        return category;
    }

}
