package org.eclipse.smarthome.core.items.decorator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.eclipse.smarthome.core.items.Item;
import org.eclipse.smarthome.core.types.Command;

public class DecoratedItem implements Item {

    public static final class State {
        
        private String label;
        private String value;

        public State(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public String getValue() {
            return value;
        }
    }

    private final String category;

    private final String format;

    private final Item item;

    private String label;

    private final BigDecimal maximum;

    private final BigDecimal minimum;

    private final boolean readOnly;

    private final List<State> states;

    private final BigDecimal stepSize;

    public DecoratedItem(Item item, String label, String category, boolean readOnly, BigDecimal minimum,
            BigDecimal maximum, BigDecimal stepSize, String format, List<State> states) {
        this.item = item;
        this.label = label;
        this.category = category;
        this.readOnly = readOnly;
        this.minimum = minimum;
        this.maximum = maximum;
        this.stepSize = stepSize;
        this.format = format;
        this.states = states;
    }

    public void addTag(String tag) {
        item.addTag(tag);
    }

    public List<Class<? extends Command>> getAcceptedCommandTypes() {
        return item.getAcceptedCommandTypes();
    }

    public List<Class<? extends org.eclipse.smarthome.core.types.State>> getAcceptedDataTypes() {
        return item.getAcceptedDataTypes();
    }

    public String getCategory() {
        return category;
    }

    public String getFormat() {
        return format;
    }

    public List<String> getGroupNames() {
        return item.getGroupNames();
    }

    public Item getItem() {
        return item;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public String getName() {
        return item.getName();
    }

    public org.eclipse.smarthome.core.types.State getState() {
        return item.getState();
    }

    public org.eclipse.smarthome.core.types.State getStateAs(
            Class<? extends org.eclipse.smarthome.core.types.State> typeClass) {
        return item.getStateAs(typeClass);
    }

    public List<State> getStates() {
        return states;
    }

    public BigDecimal getStepSize() {
        return stepSize;
    }

    public Set<String> getTags() {
        return item.getTags();
    }

    public String getType() {
        return item.getType();
    }

    public boolean hasTag(String tag) {
        return item.hasTag(tag);
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void removeAllTags() {
        item.removeAllTags();
    }

    public void removeTag(String tag) {
        item.removeTag(tag);
    }

}
