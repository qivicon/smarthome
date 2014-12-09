package org.eclipse.smarthome.core.thing.xml.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.smarthome.config.xml.util.ConverterAttributeMapValidator;
import org.eclipse.smarthome.config.xml.util.GenericUnmarshaller;
import org.eclipse.smarthome.config.xml.util.NodeIterator;
import org.eclipse.smarthome.config.xml.util.NodeList;
import org.eclipse.smarthome.config.xml.util.NodeValue;
import org.eclipse.smarthome.core.thing.type.ChannelState;
import org.eclipse.smarthome.core.thing.type.ChannelStateOption;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;


// TODO:
public class ChannelStateConverter extends GenericUnmarshaller<ChannelState> {

    protected ConverterAttributeMapValidator attributeMapValidator;


    public ChannelStateConverter() {
        super(ChannelState.class);
        
        this.attributeMapValidator = new ConverterAttributeMapValidator(new String[][] {
                { "min", "false" },
                { "max", "false" },
                { "step", "false" },
                { "pattern", "false" },
                { "readOnly", "false" }});
    }

    private BigDecimal toBigDecimal(
            Map<String, String> attributes, String attribute, BigDecimal defaultValue)
            throws ConversionException {

        String attrValueText = attributes.get(attribute);

        if (attrValueText != null) {
            try {
                return new BigDecimal(attrValueText);
            } catch (NumberFormatException nfe) {
                throw new ConversionException("The attribute '" + attribute
                        + "' has not a valid decimal number format!", nfe);
            }
        }

        return defaultValue;
    }

    private boolean toBoolean(Map<String, String> attributes, String attribute, boolean defaultValue) {
        String attrValueText = attributes.get(attribute);

        if (attrValueText != null) {
            return Boolean.valueOf(attrValueText);
        }

        return defaultValue;
    }

    private List<ChannelStateOption> toListOfChannelState(NodeList nodeList)
            throws ConversionException {

        if ("options".equals(nodeList.getNodeName())) {
            List<ChannelStateOption> channelStateOptions = new ArrayList<>();

            for (Object nodeObject : nodeList.getList()) {
                channelStateOptions.add(toChannelStateOption((NodeValue) nodeObject));
            }
    
            return channelStateOptions;
        }

        throw new ConversionException("Unknown type '" + nodeList.getNodeName() + "'!");
    }

    private ChannelStateOption toChannelStateOption(NodeValue nodeValue) throws ConversionException {
        if ("option".equals(nodeValue.getNodeName())) {
            String value;
            String label;

            Map<String, String> attributes = nodeValue.getAttributes();
            if ((attributes != null) && (attributes.containsKey("value"))) {
                value = attributes.get("value");
            } else {
                throw new ConversionException("The node 'option' requires the attribute 'value'!");
            }

            label = (String) nodeValue.getValue();

            return new ChannelStateOption(value, label);
        }

        throw new ConversionException("Unknown type in the list of 'options'!");
    }

    @Override
    public final Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Map<String, String> attributes = this.attributeMapValidator.readValidatedAttributes(reader);

        BigDecimal minimum = toBigDecimal(attributes, "min", null);
        BigDecimal maximum = toBigDecimal(attributes, "max", null);
        BigDecimal step = toBigDecimal(attributes, "step", null);
        String pattern = attributes.get("pattern");
        boolean readOnly = toBoolean(attributes, "readOnly", false);

        List<ChannelStateOption> channelOptions = null;

        NodeList nodes = (NodeList) context.convertAnother(context, NodeList.class);
        NodeIterator nodeIterator = new NodeIterator(nodes.getList());

        NodeList optionNodes = (NodeList) nodeIterator.next();
        if (optionNodes != null) {
            channelOptions = toListOfChannelState(optionNodes);
        }

        nodeIterator.assertEndOfType();

        ChannelState channelState = new ChannelState(
                minimum,
                maximum,
                step,
                pattern,
                readOnly,
                channelOptions);

        return channelState;
    }

}
