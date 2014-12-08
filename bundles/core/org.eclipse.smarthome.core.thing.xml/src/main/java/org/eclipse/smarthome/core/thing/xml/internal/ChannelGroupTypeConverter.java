package org.eclipse.smarthome.core.thing.xml.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.smarthome.config.xml.util.ConverterAttributeMapValidator;
import org.eclipse.smarthome.config.xml.util.NodeAttributes;
import org.eclipse.smarthome.config.xml.util.NodeIterator;
import org.eclipse.smarthome.config.xml.util.NodeValue;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;


// TODO:
public class ChannelGroupTypeConverter extends AbstractDescriptionTypeConverter<ChannelGroupTypeXmlResult> {

    protected ConverterAttributeMapValidator attributeMapValidator;


    public ChannelGroupTypeConverter() {
        super(ChannelGroupTypeXmlResult.class, "channel-group-type");

        this.attributeMapValidator = new ConverterAttributeMapValidator(new String[][] {
                { "id", "true" }});
    }

    private String readItemType(NodeIterator nodeIterator) throws ConversionException {
        return (String) nodeIterator.nextValue("item-type", true);
    }

    private Set<String> readTags(NodeIterator nodeIterator) throws ConversionException {
        Set<String> tags = null;

        List<?> tagsNode = nodeIterator.nextList("tags", false);

        if (tagsNode != null) {
            tags = new HashSet<>(tagsNode.size());

            for (Object tagNodeObject : tagsNode) {
                NodeValue tagNode = (NodeValue) tagNodeObject;

                if ("tag".equals(tagNode.getNodeName())) {
                    String tag = (String) tagNode.getValue();

                    if (tag != null) {
                        tags.add(tag);
                    }
                } else {
                    throw new ConversionException("The 'tags' node must only contain 'tag' nodes!");
                }
            }
        }

        return tags; 
    }

    @SuppressWarnings("unchecked")
    protected List<NodeAttributes> readChannelTypeDefinitions(NodeIterator nodeIterator)
            throws ConversionException {

        return (List<NodeAttributes>) nodeIterator.nextList("channels", true);
    }

    @Override
    protected ChannelGroupTypeXmlResult unmarshalType(HierarchicalStreamReader reader,
            UnmarshallingContext context, Map<String, String> attributes,
            NodeIterator nodeIterator) throws ConversionException {

        ChannelTypeUID channelTypeUID = new ChannelTypeUID(super.getUID(attributes, context));
        String itemType = readItemType(nodeIterator);
        String label = super.readLabel(nodeIterator);
        String description = super.readDescription(nodeIterator);
        Set<String> tags = readTags(nodeIterator);
        List<NodeAttributes> channelTypeDefinitions = readChannelTypeDefinitions(nodeIterator);
        Object[] configDescriptionObjects = super.getConfigDescriptionObjects(nodeIterator);

        ChannelGroupTypeXmlResult groupChannelType = new ChannelGroupTypeXmlResult(
                channelTypeUID,
                itemType,
                label,
                description,
                tags,
                channelTypeDefinitions,
                configDescriptionObjects);
System.out.println(groupChannelType);
        return groupChannelType;
    }

}
