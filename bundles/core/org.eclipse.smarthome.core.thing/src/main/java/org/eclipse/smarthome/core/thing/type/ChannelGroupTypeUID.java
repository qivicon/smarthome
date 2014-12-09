/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.type;

import org.eclipse.smarthome.core.thing.UID;


/**
 * {@link ChannelGroupTypeUID} represents a unique identifier for channel group types.
 * 
 * @author Michael Grammling - Initial contribution.
 */
public class ChannelGroupTypeUID extends UID {

    public ChannelGroupTypeUID(String channelGroupUid) {
        super(channelGroupUid);
    }

    public ChannelGroupTypeUID(String bindingId, String id) {
        super(bindingId, id);
    }

    @Override
    protected int getMinimalNumberOfSegments() {
        return 2;
    }

    public String getId() {
        return getSegment(1);
    }

}
