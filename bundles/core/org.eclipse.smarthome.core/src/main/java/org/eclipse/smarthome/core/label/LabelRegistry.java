package org.eclipse.smarthome.core.label;

public interface LabelRegistry {
    
    void setLabel(String entity, String id, String label);

    String getLabel(String entity, String id);
    
}
