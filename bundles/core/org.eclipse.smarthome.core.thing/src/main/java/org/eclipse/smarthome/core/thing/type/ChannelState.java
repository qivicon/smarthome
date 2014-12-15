package org.eclipse.smarthome.core.thing.type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ChannelState {

    private final BigDecimal minimum;
    private final BigDecimal maximum;
    private final BigDecimal step;
    private final String pattern;
    private final boolean readOnly;

    private final List<ChannelStateOption> channelStateOptions;


    public ChannelState(BigDecimal minimum, BigDecimal maximum, BigDecimal step, String pattern,
            boolean readOnly, List<ChannelStateOption> channelStateOptions) {

        this.minimum = minimum;
        this.maximum = maximum;
        this.step = step;
        this.pattern = pattern;
        this.readOnly = readOnly;
        if (channelStateOptions != null) {
            this.channelStateOptions = Collections.unmodifiableList(new ArrayList<ChannelStateOption>(channelStateOptions));
        } else {
            this.channelStateOptions = Collections.unmodifiableList(new ArrayList<ChannelStateOption>(0));
        }
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public BigDecimal getStep() {
        return step;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public List<ChannelStateOption> getOptions() {
        return channelStateOptions;
    }

    @Override
    public String toString() {
        return "ChannelState [minimum=" + minimum + ", maximum=" + maximum
                + ", step=" + step + ", pattern=" + pattern + ", readOnly="
                + readOnly + ", channelStateOptions=" + channelStateOptions
                + "]";
    }

}
