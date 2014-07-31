/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.upnp.internal;

import org.eclipse.smarthome.io.upnp.UpnpFilter;
import org.eclipse.smarthome.io.upnp.UpnpListener;
import org.eclipse.smarthome.io.upnp.UpnpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Andre Fuechsel
 * 
 */
public class UpnpServiceImpl implements UpnpService {

	private static Logger logger = LoggerFactory.getLogger(UpnpServiceImpl.class);
	
    @Override
    public void addUpnpListener(UpnpFilter upnpFilter, UpnpListener upnpListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeUpnpListener(UpnpFilter upnpFilter, UpnpListener upnpListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void searchNow(UpnpFilter upnpFilter, UpnpListener upnpListener) {
        // TODO Auto-generated method stub

    }

	
}
