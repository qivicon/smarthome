package org.eclipse.smarthome.core.thing.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.smarthome.core.thing.type.ChannelType;
import org.eclipse.smarthome.core.thing.type.SystemChannelTypeProvider;

public class SystemChannelTypeProviderImpl implements SystemChannelTypeProvider {

    private Map<String, ChannelType> channelTypes;

    public SystemChannelTypeProviderImpl() {
        this.channelTypes = new ConcurrentHashMap<String, ChannelType>();
    }

    @Override
    public Collection<ChannelType> getSystemChannelTypes() {
        return Collections.unmodifiableCollection(channelTypes.values());
    }

    @Override
    public boolean addSystemChannelType(ChannelType channelType) {
        String uid = channelType.getUID().getAsString();

        return channelTypes.put(uid, channelType) == null;
    }

    @Override
    public boolean removeSystemChannelType(ChannelType channelType) {
        String uid = channelType.getUID().getAsString();

        return channelTypes.remove(uid) != null;
    }

}
