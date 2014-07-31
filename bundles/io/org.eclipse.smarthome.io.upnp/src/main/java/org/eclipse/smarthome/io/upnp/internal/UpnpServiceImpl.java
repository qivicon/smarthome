/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.upnp.internal;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.ST;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;
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
public class UpnpServiceImpl extends ControlPoint implements NotifyListener, EventListener, SearchResponseListener,
        UpnpService {

	private static Logger logger = LoggerFactory.getLogger(UpnpServiceImpl.class);
	
	public UpnpServiceImpl() {
	}

    @Override
    public void addUpnpListener(UpnpListener upnpListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeUpnpListener(UpnpListener upnpListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void lookup() {
        // TODO Auto-generated method stub

    }

    @Override
    public void lookup(ST st) {
        // TODO Auto-generated method stub

    }

    public void activate() {

    }

    public void deactivate() {

    }

    @Override
    public void deviceSearchResponseReceived(SSDPPacket arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void eventNotifyReceived(String arg0, long arg1, String arg2, String arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deviceNotifyReceived(SSDPPacket arg0) {
        // TODO Auto-generated method stub

    }
	
}
