/*
 * Copyright (C) 2013 4th Line GmbH, Switzerland
 *
 * The contents of this file are subject to the terms of either the GNU
 * Lesser General Public License Version 2 or later ("LGPL") or the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.eclipse.smarthome.io.upnp.device;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.ActionArgument;
import org.fourthline.cling.model.meta.StateVariable;
import org.osgi.service.upnp.UPnPAction;
import org.osgi.service.upnp.UPnPStateVariable;

public class UPnPActionImpl implements UPnPAction {

    private Action<?> action;

    public UPnPActionImpl(Action<?> action) {
        this.action = action;
    }

    @Override
    public String getName() {
        return action.getName();
    }

    @Override
    public String getReturnArgumentName() {
        String name = null;

        for (ActionArgument<?> argument : action.getArguments()) {
            if (argument.isReturnValue()) {
                name = argument.getName();
                break;
            }
        }

        return name;
    }

    @Override
    public String[] getInputArgumentNames() {
        List<String> list = new ArrayList<String>();
        for (ActionArgument<?> argument : action.getInputArguments()) {
            list.add(argument.getName());
        }

        return list.size() != 0 ? (String[]) list.toArray(new String[list.size()]) : null;
    }

    @Override
    public String[] getOutputArgumentNames() {
        List<String> list = new ArrayList<String>();
        for (ActionArgument<?> argument : action.getOutputArguments()) {
            list.add(argument.getName());
        }

        return list.size() != 0 ? (String[]) list.toArray(new String[list.size()]) : null;
    }

    @Override
    public UPnPStateVariable getStateVariable(String argumentName) {
        StateVariable variable = null;

        ActionArgument<?> argument = action.getInputArgument(argumentName);
        if (argument == null) {
            argument = action.getOutputArgument(argumentName);
        }
        if (argument != null) {
            String name = argument.getRelatedStateVariableName();
            variable = action.getService().getStateVariable(name);
        }

        return (argument != null) ? new UPnPStateVariableImpl(variable) : null;
    }

    @Override
    public Dictionary invoke(Dictionary args) throws Exception {
        throw new Exception("Not yet implemented");
    }
}
