package org.eclipse.smarthome.core.thing.decorator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.eclipse.smarthome.core.items.decorator.DecoratedItem;
import org.eclipse.smarthome.core.items.decorator.DecoratedItem.State;
import org.eclipse.smarthome.core.items.decorator.ItemDecorator;
import org.eclipse.smarthome.core.label.LabelRegistry;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.link.ItemChannelLinkRegistry;
import org.eclipse.smarthome.core.thing.type.ChannelType;
import org.eclipse.smarthome.core.thing.type.FunctionalChannelType;
import org.eclipse.smarthome.core.thing.type.StateDescription;
import org.eclipse.smarthome.core.thing.type.ThingTypeRegistry;

public class LinkBasedItemDecorator implements ItemDecorator {

    private static final String LABEL_ENTITY = "item";
    
    private ItemChannelLinkRegistry itemChannelLinkRegistry;
    
    private ItemRegistry itemRegistry;
    
    private LabelRegistry labelRegistry;
    
    private ThingTypeRegistry thingTypeRegistry;

    private DecoratedItem createDecoratedItem(Item item, Locale locale) {
        
        String label = labelRegistry.getLabel(LABEL_ENTITY, item.getName());
        String category = null;

        BigDecimal minimum = null;
        BigDecimal maximum = null;
        BigDecimal stepSize = null;

        String format = null;

        List<State> states = new ArrayList<>();

        Set<ChannelUID> boundChannels = itemChannelLinkRegistry.getBoundChannels(item.getName());
        
        if (boundChannels.size() == 1) {
            ChannelUID channelUID = boundChannels.iterator().next();
            ChannelType channelType = thingTypeRegistry.getChannelType(channelUID, locale);
            if(channelType instanceof FunctionalChannelType) {
                FunctionalChannelType functionalChannelType = (FunctionalChannelType) channelType;
                category = functionalChannelType.getCategory();
                StateDescription stateDescription = functionalChannelType.getStateDescription();
                if(stateDescription != null) {
                    minimum = stateDescription.getMinimum();
                    maximum = stateDescription.getMaximum();
                    stepSize = stateDescription.getStepSize();
                    format = stateDescription.getFormat();
                    List<StateDescription.State> stateDescriptionStates = stateDescription.getStates();
                    for (StateDescription.State state : stateDescriptionStates) {
                        states.add(new DecoratedItem.State(state.getValue(), state.getLabel()));
                    }
                }
            }
        }

        return new DecoratedItem(item, label, category, false, minimum, maximum, stepSize, format, states);
    }

    @Override
    public Collection<DecoratedItem> getItems() {
        return getItems(null);
    }

    @Override
    public Collection<DecoratedItem> getItems(Locale locale) {
        Collection<DecoratedItem> decoratedItems = new ArrayList<>();
        Collection<Item> items = itemRegistry.getAll();
        for (Item item : items) {
            decoratedItems.add(createDecoratedItem(item, locale));
        }
        return decoratedItems;
    }

}
