/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.rest.sse;

/**
 * Represents an event topic consisting of namespace, object and type. The topic
 * is the formed in the following way: namespace/object/type
 * 
 * @author Ivan Iliev - Initial contribution and API
 * 
 */
public interface EventType {

    /**
     * Returns the namespace for this EventType.
     * 
     * @return
     */
    public String getNamespace();

    /**
     * Returns the object for this EventType.
     * 
     * @return
     */
    public String getObject();

    /**
     * Returns the type for this EventType.
     * 
     * @return
     */
    public String getType();

}
