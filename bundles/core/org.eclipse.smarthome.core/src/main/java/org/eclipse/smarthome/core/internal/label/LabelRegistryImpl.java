package org.eclipse.smarthome.core.internal.label;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.smarthome.core.label.DefaultLabelProvider;
import org.eclipse.smarthome.core.label.LabelRegistry;
import org.eclipse.smarthome.core.storage.Storage;
import org.eclipse.smarthome.core.storage.StorageService;

public class LabelRegistryImpl implements LabelRegistry {

    private Storage<String> storage;
    private Map<String, DefaultLabelProvider> defaultLabelProviders = new ConcurrentHashMap<>();

    protected void setStorageService(StorageService storageService) {
        this.storage = storageService.getStorage("org.eclipse.smarthome.label", this.getClass().getClassLoader());
    }

    protected void unsetStorageService(StorageService storageService) {
        this.storage = null;
    }

    @Override
    public void setLabel(String entity, String id, String label) {
        storage.put(getKey(entity, id), label);
    }

    private String getKey(String entity, String id) {
        return entity + id;
    }

    @Override
    public String getLabel(String entity, String id) {
        String label = storage.get(getKey(entity, id));

        if (label == null) {
            label = getDefaultLabel(entity, id, label);
        }

        return label;
    }

    private String getDefaultLabel(String entity, String id, String label) {
        DefaultLabelProvider defaultLabelProvider = defaultLabelProviders.get(entity);
        if (defaultLabelProvider != null) {
            label = defaultLabelProvider.getDefaultLabel(id);
        }
        return label;
    }

    public void addDefaultLabelProviders(DefaultLabelProvider defaultLabelProvider) {
        this.defaultLabelProviders.put(defaultLabelProvider.getEntity(), defaultLabelProvider);
    }

    public void removeDefaultLabelProviders(DefaultLabelProvider defaultLabelProvider) {
        this.defaultLabelProviders.remove(defaultLabelProvider.getEntity());
    }
}
