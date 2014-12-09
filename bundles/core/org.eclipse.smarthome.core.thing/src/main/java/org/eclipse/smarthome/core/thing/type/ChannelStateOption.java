package org.eclipse.smarthome.core.thing.type;

public final class ChannelStateOption {
    private String value;
    private String label;

    public ChannelStateOption(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ChannelStateOption [value=" + value + ", label=" + label + "]";
    }
}