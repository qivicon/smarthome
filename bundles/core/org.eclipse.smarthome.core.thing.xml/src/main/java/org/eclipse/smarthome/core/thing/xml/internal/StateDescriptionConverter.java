package org.eclipse.smarthome.core.thing.xml.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.smarthome.config.xml.util.GenericUnmarshaller;
import org.eclipse.smarthome.config.xml.util.NodeAttributes;
import org.eclipse.smarthome.config.xml.util.NodeIterator;
import org.eclipse.smarthome.config.xml.util.NodeList;
import org.eclipse.smarthome.config.xml.util.NodeName;
import org.eclipse.smarthome.core.thing.type.StateDescription;
import org.eclipse.smarthome.core.thing.type.StateDescription.State;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;


public class StateDescriptionConverter extends GenericUnmarshaller<StateDescription> {

    public StateDescriptionConverter() {
        super(StateDescription.class);
    }

    private StateDescription readStateDescription(NodeIterator nodeIterator)
            throws ConversionException {

        BigDecimal minRange = null;
        BigDecimal maxRange = null;
        BigDecimal stepSize = null;
        String formatPattern = null;
        List<State> states = null;

        while (nodeIterator.hasNext()) {
            Object nextNode = nodeIterator.next();

            if (nextNode instanceof NodeName) {
               NodeName nextNodeName = (NodeName) nextNode;

               switch (nextNodeName.getNodeName()) {
                   case "range":
                       Map<String, String> rangeAttrs = ((NodeAttributes) nextNode).getAttributes();
                       minRange = BigDecimal.valueOf(Double.valueOf(rangeAttrs.get("min")));
                       maxRange = BigDecimal.valueOf(Double.valueOf(rangeAttrs.get("max")));
                       continue;
                   case "step":
                       Map<String, String> stepAttrs = ((NodeAttributes) nextNode).getAttributes();
                       stepSize = BigDecimal.valueOf(Double.valueOf(stepAttrs.get("size")));
                       continue;
                   case "format":
                       Map<String, String> formatAttrs = ((NodeAttributes) nextNode).getAttributes();
                       formatPattern = formatAttrs.get("pattern");
                       continue;
                   case "states":
                       states = readStates((NodeList) nextNode);
                       continue;
                   default:
                        throw new ConversionException("The node '" + nextNode
                                + "' is unknown within the 'state-description' tag!");
                }
            }
        }

        return new StateDescription(minRange, maxRange, stepSize, formatPattern, states);
    }

    @SuppressWarnings("unchecked")
    private List<State> readStates(NodeList stateList) {
        List<State> states = new ArrayList<>();

        for (NodeAttributes stateAttrs : (List<NodeAttributes>) stateList.getList()) {
            State state = new State(
                    stateAttrs.getAttribute("value"),
                    stateAttrs.getAttribute("label"));

            states.add(state);
        }

        return states;
    }

    @Override
    public final Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        List<?> nodes = (List<?>) context.convertAnother(context, List.class);
        NodeIterator nodeIterator = new NodeIterator(nodes);

        return readStateDescription(nodeIterator);
    }

}
