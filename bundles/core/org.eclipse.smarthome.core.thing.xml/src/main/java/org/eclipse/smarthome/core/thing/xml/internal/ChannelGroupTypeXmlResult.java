package org.eclipse.smarthome.core.thing.xml.internal;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.eclipse.smarthome.config.core.ConfigDescription;
import org.eclipse.smarthome.config.xml.util.NodeAttributes;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;


// TODO:
public class ChannelGroupTypeXmlResult {

    private ChannelTypeUID channelTypeUID;
    private String itemType;
    private String label;
    private String description;
    private Set<String> tags;
    private List<NodeAttributes> channelDefinitionTypes;
    private URI configDescriptionURI;
    private ConfigDescription configDescription;


    public ChannelGroupTypeXmlResult(ChannelTypeUID channelTypeUID, String itemType, String label,
            String description, Set<String> tags, List<NodeAttributes> channelDefinitionTypes,
            Object[] configDescriptionObjects) { 

        this.channelTypeUID = channelTypeUID;
        this.itemType = itemType;
        this.label = label;
        this.description = description;
        this.tags = tags;
        this.channelDefinitionTypes = channelDefinitionTypes;
        this.configDescriptionURI = (URI) configDescriptionObjects[0];
        this.configDescription = (ConfigDescription) configDescriptionObjects[1];
    }

    @Override
    public String toString() {
        return "ChannelGroupTypeXmlResult [channelTypeUID=" + channelTypeUID
                + ", itemType=" + itemType + ", label=" + label
                + ", description=" + description + ", tags=" + tags
                + ", channelDefinitionTypes=" + channelDefinitionTypes
                + ", configDescriptionURI=" + configDescriptionURI
                + ", configDescription=" + configDescription + "]";
    }

}
