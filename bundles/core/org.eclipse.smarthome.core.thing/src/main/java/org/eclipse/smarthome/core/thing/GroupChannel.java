package org.eclipse.smarthome.core.thing;

import java.util.List;

public class GroupChannel extends Channel {

    private List<SimpleChannel> channels; 
    
    public GroupChannel(ChannelUID uid, List<SimpleChannel> channels) {
        super(uid);
        this.channels = channels;
    }
    
    public List<SimpleChannel> getChannels() {
        return channels;
    }

}
