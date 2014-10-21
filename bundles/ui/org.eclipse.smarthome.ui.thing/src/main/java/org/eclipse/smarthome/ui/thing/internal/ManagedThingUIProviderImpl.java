package org.eclipse.smarthome.ui.thing.internal;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.smarthome.core.storage.Storage;
import org.eclipse.smarthome.core.storage.StorageService;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.ui.thing.ManagedThingUIProvider;

/**
 * Implementation for {@link ManagedThingUIProvider} (Locale is not supported yet). 
 * 
 * @author Dennis Nobel - Initial contribution
 * 
 */
public class ManagedThingUIProviderImpl implements ManagedThingUIProvider {

    private static final String STORAGE_NAME = "org.eclipse.smarthome.core.thing.Label";
    private Storage<String> storage;
    private Map<ThingUID, String> cache = new ConcurrentHashMap<>();
    
    @Override
    public String getLabel(ThingUID thingUID, Locale locale) {
        
        if (thingUID == null) {
            throw new IllegalArgumentException("Thing UID must not be null");
        }
        
        String label = cache.get(thingUID);
        if(label != null) {
            return label;
        }

        label = storage.get(thingUID.toString());
        
        if(label != null) {
            cache.put(thingUID, label);
        }
        
        return label;
    }

    @Override
    public void setLabel(ThingUID thingUID, String label, Locale locale) {

        if (thingUID == null || label == null) {
            throw new IllegalArgumentException("Thing UID and label must not be null");
        }

        storage.put(thingUID.toString(), label);
        cache.put(thingUID, label);
    }

    protected void setStorageService(StorageService storageService) {
        storage = storageService.getStorage(STORAGE_NAME, this.getClass().getClassLoader());
    }

    protected void unsetStorageService(StorageService storageService) {
        storage = null;
    }

}
