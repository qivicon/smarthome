/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.events;

/**
 * The {@link Topic} class wraps a topic of the primitive type {@code String} in an object. The goal of this
 * class is to simplify the access to the elements of a topic. A topic is structured into the following four elements:
 * 
 * <ul>
 * <li>{namespace}/{entityType}/{entityId}/{action}</li>
 * </ul>
 * 
 * Examples of Eclipse SmartHome event topics are: </p>
 * <ul>
 * <li>smarthome/items/{itemName}/added</li>
 * <li>smarthome/items/{itemName}/removed</li>
 * <li>smarthome/items/{itemName}/updated</li>
 * <li>smarthome/things/{thingUID}/added</li>
 * <li>smarthome/things/{thingUID}/removed</li>
 * <li>smarthome/things/{thingUID}/updated</li>
 * <li>smarthome/things/{thingUID}/status</li>
 * <li>...</li>
 * </ul>
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public class Topic {

    private final String SEPARATOR = "/";

    private String namespace;

    private String entityType;

    private String entityId;

    private String action;

    /**
     * Creates a new topic based on a String. The structure of the string must follow the Eclipse SmartHome topic
     * structure: {namespace}/{entityType}/{entityId}/{action}.
     * 
     * @param topic the topic
     */
    public Topic(String topic) {
        String[] segments = topic.split(SEPARATOR);
        if (segments.length != 4)
            throw new IllegalArgumentException("A topic must have four elements and follow the structure: "
                    + "c.f. {namespace}/{entityType}/{entityId}/{action}");
        this.namespace = segments[0];
        this.entityType = segments[1];
        this.entityId = segments[2];
        this.action = segments[3];
    }

    /**
     * Creates a new topic based on the four topic elements.
     * 
     * @param namespace the namespace
     * @param entityType the entity type
     * @param entityId the entity Id
     * @param action the action
     */
    public Topic(String namespace, String entityType, String entityId, String action) {
        this.namespace = namespace;
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
    }

    /**
     * Creates a new topic with the default namespace 'smarthome'.
     * 
     * @param entityType the entity type
     * @param entityId the entity Id
     * @param action the action
     */
    public Topic(String entityType, String entityId, String action) {
        this.namespace = "smarthome";
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
    }

    /**
     * Gets the namespace.
     * 
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Gets the entity type.
     * 
     * @return the entityType
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Gets the entity Id.
     * 
     * @return the entityId
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * Gets the action.
     * 
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * Gets the topic as string representation.
     * 
     * @return the topic as string
     */
    public String getAsString() {
        return namespace + SEPARATOR + entityType + SEPARATOR + entityId + SEPARATOR + action;
    }

}
