package org.eclipse.smarthome.core.thing.type;

import java.util.Collection;

/**
 * @author Ivan Iliev
 * 
 */
public interface SystemChannelTypeProvider {

    public static final String NAMESPACE_PREFIX = "system.";

    public static final String NAMESPACE = "system";

    public Collection<ChannelType> getSystemChannelTypes();

    public boolean addSystemChannelType(ChannelType channelType);

    public boolean removeSystemChannelType(ChannelType channelType);
}