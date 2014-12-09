package org.eclipse.smarthome.core.thing.xml.internal;

import java.util.List;
import java.util.Map;

import org.eclipse.smarthome.config.xml.util.ConverterAttributeMapValidator;
import org.eclipse.smarthome.config.xml.util.NodeAttributes;
import org.eclipse.smarthome.config.xml.util.NodeIterator;
import org.eclipse.smarthome.core.thing.type.ChannelGroupTypeUID;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;


// TODO:
public class ChannelGroupTypeConverter
        extends AbstractDescriptionTypeConverter<ChannelGroupTypeXmlResult> {

    public ChannelGroupTypeConverter() {
        super(ChannelGroupTypeXmlResult.class, "channel-group-type");

        super.attributeMapValidator = new ConverterAttributeMapValidator(new String[][] {
                { "id", "true" },
                { "advanced", "false" }});
    }

    private boolean isAdvanced(Map<String, String> attributes, boolean defaultValue) {
        Object advancedObj = attributes.get("advanced");

        if (advancedObj != null) {
            return Boolean.parseBoolean((String) advancedObj);
        }

        return defaultValue;
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

        ChannelGroupTypeUID channelGroupTypeUID = new ChannelGroupTypeUID(
                super.getUID(attributes, context));

        boolean advanced = isAdvanced(attributes, false);

        String label = super.readLabel(nodeIterator);
        String description = super.readDescription(nodeIterator);
        List<NodeAttributes> channelTypeDefinitions = readChannelTypeDefinitions(nodeIterator);

        ChannelGroupTypeXmlResult groupChannelType = new ChannelGroupTypeXmlResult(
                channelGroupTypeUID,
                advanced,
                label,
                description,
                channelTypeDefinitions);

        return groupChannelType;
    }

}
