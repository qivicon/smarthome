package org.eclipse.smarthome.core.thing.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.smarthome.core.thing.UID;

public class ChannelGroupType extends AbstractDescriptionType {

    private final boolean advanced;
    private final List<ChannelDefinition> channelDefinitions;

    public ChannelGroupType(UID uid, String label, String description, List<ChannelDefinition> channelDefinitions,
            boolean advanced) throws IllegalArgumentException {
        super(uid, label, description);
        if (channelDefinitions != null) {
            this.channelDefinitions = Collections.unmodifiableList(channelDefinitions);
        } else {
            this.channelDefinitions = Collections.unmodifiableList(
                    new ArrayList<ChannelDefinition>(0));
        }
        this.advanced = advanced;
    }

    public List<ChannelDefinition> getChannelDefinitions() {
        return channelDefinitions;
    }

    public boolean isAdvanced() {
        return advanced;
    }
}
