package org.eclipse.smarthome.core.thing.type;

public class ChannelGroupDefinition {

    private String id;
    private ChannelGroupType type;

    public ChannelGroupDefinition(String id, ChannelGroupType type) throws IllegalArgumentException {
        if ((id == null) || (id.isEmpty())) {
          throw new IllegalArgumentException("The ID must neither be null nor empty!");  
        }

        if (type == null) {
            throw new IllegalArgumentException("The channel type must not be null");  
        }

        this.id = id;
        this.type = type;
    }

    /**
     * Returns the identifier of the channel group.
     * 
     * @return the identifier of the channel group (neither null, nor empty) 
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the type of the channel group.
     * 
     * @return the type of the channel group (not null)
     */
    public ChannelGroupType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "ChannelGroupDefinition [id=" + id + ", type=" + type + "]";
    }

}
