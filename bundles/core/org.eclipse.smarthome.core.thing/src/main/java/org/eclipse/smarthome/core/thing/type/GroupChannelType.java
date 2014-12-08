package org.eclipse.smarthome.core.thing.type;

import java.net.URI;
import java.util.List;
import java.util.Set;

public class GroupChannelType extends ChannelType {

    private final List<ChannelDefinition> channelDefinitions;
    private String itemType;
    private Set<String> tags;

    public GroupChannelType(ChannelTypeUID uid, String itemType, String label, String description, Set<String> tags,
            List<ChannelDefinition> channelDefinitions, URI configDescriptionURI) throws IllegalArgumentException {
        super(uid, label, description, configDescriptionURI);
        this.channelDefinitions = channelDefinitions;
    }

    public List<ChannelDefinition> getChannelDefinitions() {
        return channelDefinitions;
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
    
}