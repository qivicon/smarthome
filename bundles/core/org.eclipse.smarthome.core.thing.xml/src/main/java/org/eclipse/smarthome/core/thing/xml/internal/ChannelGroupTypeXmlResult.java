package org.eclipse.smarthome.core.thing.xml.internal;

import java.util.List;

import org.eclipse.smarthome.config.xml.util.NodeAttributes;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;


// TODO:
public class ChannelGroupTypeXmlResult {

    private ChannelTypeUID channelTypeUID;
    private String label;
    private String description;
    private List<NodeAttributes> channelDefinitionTypes;


    public ChannelGroupTypeXmlResult(ChannelTypeUID channelTypeUID, String label,
            String description, List<NodeAttributes> channelDefinitionTypes) { 

        this.channelTypeUID = channelTypeUID;
        this.label = label;
        this.description = description;
        this.channelDefinitionTypes = channelDefinitionTypes;
    }

    @Override
    public String toString() {
        return "ChannelGroupTypeXmlResult [channelTypeUID=" + channelTypeUID
                + ", label=" + label + ", description=" + description
                + ", channelDefinitionTypes=" + channelDefinitionTypes + "]";
    }

}
