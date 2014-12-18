/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ChannelGroupType extends AbstractDescriptionType {

    private final boolean advanced;
    private final List<ChannelDefinition> channelDefinitions;


    public ChannelGroupType(ChannelGroupTypeUID uid, boolean advanced, String label, String description,
            List<ChannelDefinition> channelDefinitions) throws IllegalArgumentException {

        super(uid, label, description);

        this.advanced = advanced;

        if (channelDefinitions != null) {
            this.channelDefinitions = Collections.unmodifiableList(channelDefinitions);
        } else {
            this.channelDefinitions = Collections.unmodifiableList(
                    new ArrayList<ChannelDefinition>(0));
        }
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public List<ChannelDefinition> getChannelDefinitions() {
        return channelDefinitions;
    }

    @Override
    public ChannelGroupTypeUID getUID() {
        return (ChannelGroupTypeUID) super.getUID();
    }

    @Override
    public String toString() {
        return super.getUID().toString();
    }

}
