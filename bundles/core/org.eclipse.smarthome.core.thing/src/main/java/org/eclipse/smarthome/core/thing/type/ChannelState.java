package org.eclipse.smarthome.core.thing.type;

import java.math.BigDecimal;
import java.util.List;

public class ChannelState {

    private final List<ChannelStateOption> channelStateOptions;
    private final BigDecimal maximum;
    private final BigDecimal minimum;
    private final String pattern;
    private boolean readOnly;
    private final BigDecimal step;

    public ChannelState(boolean readOnly, BigDecimal minimum, BigDecimal maximum, BigDecimal step, String pattern,
            List<ChannelStateOption> channelStateOptions) {
        this.readOnly = readOnly;
        this.minimum = minimum;
        this.maximum = maximum;
        this.step = step;
        this.pattern = pattern;
        this.channelStateOptions = channelStateOptions;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public List<ChannelStateOption> getOptions() {
        return channelStateOptions;
    }

    public String getPattern() {
        return pattern;
    }

    public BigDecimal getStep() {
        return step;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
}
