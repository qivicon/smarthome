/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.items.events;

import java.lang.reflect.Method;

import org.eclipse.smarthome.core.events.AbstractEventFactory;
import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.Type;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

/**
 * An {@link ItemEventFactory} is responsible for creating item event instances, e.g. {@link ItemCommandEvent}s and
 * {@link ItemStateEvent}s.
 * 
 * @author Stefan Bußweiler - Initial contribution
 */
public class ItemEventFactory extends AbstractEventFactory {

    private static final String ITEM_COMAND_EVENT_TOPIC = "smarthome/items/{itemName}/command";

    private static final String ITEM_STATE_EVENT_TOPIC = "smarthome/items/{itemName}/state";

    /**
     * Constructs a new ItemEventFactory.
     */
    public ItemEventFactory() {
        super(Sets.newHashSet(ItemCommandEvent.TYPE, ItemStateEvent.TYPE));
    }

    @Override
    protected Event createEventByType(String eventType, String topic, String payload, String source) throws Exception {
        Event event = null;
        if (eventType.equals(ItemCommandEvent.TYPE)) {
            event = createCommandEvent(eventType, topic, payload, source);
        } else if (eventType.equals(ItemStateEvent.TYPE)) {
            event = createStateEvent(eventType, topic, payload, source);
        }
        return event;
    }

    private Event createCommandEvent(String eventType, String topic, String payload, String source) {
        ItemEventPayloadBean bean = deserializePayload(payload, ItemEventPayloadBean.class);
        Command command = null;
        try {
            command = (Command) parse(bean.getClazz(), bean.getValue());
        } catch (Exception e) {
            throw new IllegalArgumentException("Parsing of item command event failed.", e);
        }
        return new ItemCommandEvent(topic, payload, bean.getName(), command, source);
    }

    private Event createStateEvent(String eventType, String topic, String payload, String source) {
        ItemEventPayloadBean bean = deserializePayload(payload, ItemEventPayloadBean.class);
        State state = null;
        try {
            state = (State) parse(bean.getClazz(), bean.getValue());
        } catch (Exception e) {
            throw new IllegalArgumentException("Parsing of item state event failed.", e);
        }
        return new ItemStateEvent(topic, payload, bean.getName(), state, source);
    }

    private Object parse(String className, String valueToParse) throws Exception {
        Class<?> stateClass = Class.forName(className);
        Method valueOfMethod = stateClass.getMethod("valueOf", String.class);
        return valueOfMethod.invoke(stateClass, valueToParse);
    }

    /**
     * Creates an item command event.
     * 
     * @param itemName the name of the item to send the command for
     * @param command the command to send
     * @param source the name of the source identifying the sender, can be null
     * 
     * @return the created item command event
     */
    public static ItemCommandEvent createCommandEvent(String itemName, Command command, String source) {
        checkArguments(itemName, command, "command");
        String topic = ITEM_COMAND_EVENT_TOPIC.replace("{itemName}", itemName);
        ItemEventPayloadBean bean = new ItemEventPayloadBean(itemName, command.getClass().getName(), command.toString());
        String payload = serializePayload(bean);
        return new ItemCommandEvent(topic, payload, itemName, command, source);
    }

    /**
     * Creates an item command event.
     * 
     * @param itemName the name of the item to send the command for
     * @param command the command to send
     * 
     * @return the created item command event
     */
    public static ItemCommandEvent createCommandEvent(String itemName, Command command) {
        return createCommandEvent(itemName, command, null);
    }

    /**
     * Creates an item state event.
     * 
     * @param itemName the name of the item to send the state update for
     * @param state the new state to send
     * @param source the name of the source identifying the sender, can be null
     * 
     * @return the created item state event
     */
    public static ItemStateEvent createStateEvent(String itemName, State state, String source) {
        checkArguments(itemName, state, "state");
        String topic = ITEM_STATE_EVENT_TOPIC.replace("{itemName}", itemName);
        ItemEventPayloadBean bean = new ItemEventPayloadBean(itemName, state.getClass().getName(), state.toString());
        String payload = serializePayload(bean);
        return new ItemStateEvent(topic, payload, itemName, state, source);
    }

    /**
     * Creates an item state event.
     * 
     * @param itemName the name of the item to send the state update for
     * @param state the new state to send
     * 
     * @return the created item state event
     */
    public static ItemStateEvent createStateEvent(String itemName, State state) {
        return createStateEvent(itemName, state, null);
    }

    private static void checkArguments(String itemName, Type type, String typeArgumentName) {
        Preconditions.checkArgument(itemName != null && !itemName.isEmpty(),
                "The argument 'itemName' must not be null or empty.");
        Preconditions.checkArgument(type != null, "The argument '" + typeArgumentName + "' must not be null or empty.");
    }

    /**
     * This is a java bean that is used to serialize/deserialize item event payload.
     */
    private static class ItemEventPayloadBean {
        private String name;
        private String clazz;
        private String value;

        public ItemEventPayloadBean(String name, String clazz, String value) {
            this.name = name;
            this.clazz = clazz;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getClazz() {
            return clazz;
        }

        public String getValue() {
            return value;
        }

    }

}
