package org.eclipse.smarthome.core.thing;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.config.core.Configuration;

public class FunctionalChannel extends Channel {

    private String acceptedItemType;
    private Configuration configuration;
    private Set<String> defaultTags;
    
    public FunctionalChannel(ChannelUID uid, String acceptedItemType, Configuration configuration) {
        this(uid, acceptedItemType, configuration, new HashSet<String>(0));
    }

    public FunctionalChannel(ChannelUID uid, String acceptedItemType, Set<String> defaultTags) {
        this(uid, acceptedItemType, null, defaultTags == null ? new HashSet<String>(0)
                : defaultTags);
    }

    public FunctionalChannel(ChannelUID uid, String acceptedItemType, Configuration configuration,
            Set<String> defaultTags) {
        super(uid);
        this.acceptedItemType = acceptedItemType;
        this.configuration = configuration;
        this.defaultTags = Collections.<String> unmodifiableSet(new HashSet<String>(defaultTags));
        if (this.configuration==null){
            this.configuration=new Configuration();
        }
    }

    /**
     * Returns the accepted item type.
     * 
     * @return accepted item type
     */
    public String getAcceptedItemType() {
        return this.acceptedItemType;
    }

    /**
     * Returns the channel configuration
     * 
     * @return channel configuration or null
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Returns default tags of this channel.
     * 
     * @return default tags of this channel.
     */
    public Set<String> getDefaultTags() {
        return defaultTags;
    }

}
