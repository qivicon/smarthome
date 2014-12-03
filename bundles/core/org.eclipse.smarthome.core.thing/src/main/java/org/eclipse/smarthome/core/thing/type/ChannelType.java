/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.type;

import java.net.URI;

import org.eclipse.smarthome.core.thing.Channel;


/**
 * The {@link ChannelType} describes a concrete type of a {@link Channel}.
 * <p>
 * This description is used as template definition for the creation
 * of the according concrete {@link Channel} object.
 * <p>
 * <b>Hint:</b> This class is immutable.
 * 
 * @author Michael Grammling - Initial Contribution
 */
public class ChannelType extends AbstractDescriptionType {

    /**
     * Creates a new instance of this class with the specified parameters.
     *
     * @param uid the unique identifier which identifies this Channel type within
     *     the overall system (must not be null)
     *
     * @param label the human readable label for the according type
     *     (must neither be null nor empty)
     *
     * @param description the human readable description for the according type
     *     (could be null or empty)
     *
     * @param configDescriptionURI the link to the concrete ConfigDescription (could be null)
     *
     * @throws IllegalArgumentException if the UID is null, or the label is null or empty
     */
    public ChannelType(ChannelTypeUID uid, String label, String description,
            URI configDescriptionURI) throws IllegalArgumentException {

        super(uid, label, description, configDescriptionURI);
    }

    @Override
    public ChannelTypeUID getUID() {
        return (ChannelTypeUID) super.getUID();
    }

    @Override
    public String toString() {
        return super.getUID().toString();
    }

}
