package org.eclipse.smarthome.core.thing;

import java.util.List;

public class GroupChannel extends Channel {

    private List<FunctionalChannel> channels; 
    
    public GroupChannel(ChannelUID uid, List<FunctionalChannel> channels) {
        super(uid);
        this.channels = channels;
    }
    
    public List<FunctionalChannel> getChannels() {
        return channels;
    }

}
