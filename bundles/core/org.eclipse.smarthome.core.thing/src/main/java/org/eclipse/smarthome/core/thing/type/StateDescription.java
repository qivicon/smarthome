package org.eclipse.smarthome.core.thing.type;

import java.math.BigDecimal;
import java.util.List;

public class StateDescription {

    public static class State {
        private String value;
        private String label;

        public State(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return "State [value=" + value + ", label=" + label + "]";
        }
    }

    private BigDecimal minimum;
    private BigDecimal maximum;
    private BigDecimal stepSize;
    private String format;
    private List<State> states;


    public StateDescription(BigDecimal minimum, BigDecimal maximum, BigDecimal stepSize, String format,
            List<State> states) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.stepSize = stepSize;
        this.format = format;
        this.states = states;
    }
    
    public BigDecimal getMinimum() {
        return minimum;
    }
    
    public BigDecimal getMaximum() {
        return maximum;
    }
    
    public String getFormat() {
        return format;
    }
    
    public List<State> getStates() {
        return states;
    }
    
    public BigDecimal getStepSize() {
        return stepSize;
    }

    @Override
    public String toString() {
        return "StateDescription [minimum=" + minimum + ", maximum=" + maximum
                + ", stepSize=" + stepSize + ", format=" + format + ", states="
                + states + "]";
    }

}
