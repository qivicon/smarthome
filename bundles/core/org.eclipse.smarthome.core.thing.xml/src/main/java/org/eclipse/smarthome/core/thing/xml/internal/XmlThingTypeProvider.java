/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.core.thing.xml.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.ThingTypeProvider;
import org.eclipse.smarthome.core.thing.type.ThingType;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The {@link XmlThingTypeProvider} is a concrete implementation of the {@link ThingTypeProvider}
 * service interface.
 * <p>
 * This implementation manages any {@link ThingType} objects associated to specific modules.
 * If a specific module disappears, any registered {@link ThingType} objects associated with
 * that module are released.
 * 
 * @author Michael Grammling - Initial Contribution
 */
public class XmlThingTypeProvider implements ThingTypeProvider {

    private Logger logger = LoggerFactory.getLogger(XmlThingTypeProvider.class);

    private Map<Bundle, List<ThingType>> bundleThingTypesMap;

    public XmlThingTypeProvider() {
        this.bundleThingTypesMap = new HashMap<>(10);
    }

    private List<ThingType> acquireThingTypes(Bundle bundle) {
        if (bundle != null) {
            List<ThingType> thingTypes = this.bundleThingTypesMap.get(bundle);

            if (thingTypes == null) {
                thingTypes = new ArrayList<ThingType>(10);

                this.bundleThingTypesMap.put(bundle, thingTypes);
            }

            return thingTypes;
        }

        return null;
    }

    /**
     * Adds a {@link ThingType} object to the internal list associated with the specified module.
     * <p>
     * The added {@link ThingType} object leads to an event.
     * <p>
     * This method returns silently, if any of the parameters is {@code null}.
     * 
     * @param bundle the module to which the Thing type to be added
     * @param thingType the Thing type to be added
     */
    public synchronized void addThingType(Bundle bundle, ThingType thingType) {
        if (thingType != null) {
            List<ThingType> thingTypes = acquireThingTypes(bundle);

            if (thingTypes != null) {
                thingTypes.add(thingType);
            }
        }
    }

    /**
     * Removes all {@link ThingType} objects from the internal list associated
     * with the specified module.
     * <p>
     * Any removed {@link ThingType} object leads to a separate event.
     * <p>
     * This method returns silently if the module is {@code null}.
     * 
     * @param bundle the module for which all associated Thing types to be removed
     */
    public synchronized void removeAllThingTypes(Bundle bundle) {
        if (bundle != null) {
            List<ThingType> thingTypes = this.bundleThingTypesMap.get(bundle);

            if (thingTypes != null) {
                this.bundleThingTypesMap.remove(bundle);
            }
        }
    }


    @Override
    public synchronized Collection<ThingType> getThingTypes(Locale locale) {
        List<ThingType> allThingTypes = new ArrayList<>();

        Collection<List<ThingType>> thingTypesList = this.bundleThingTypesMap.values();

        if (thingTypesList != null) {
            for (List<ThingType> thingTypes : thingTypesList) {
                allThingTypes.addAll(thingTypes);
            }
        }

        return allThingTypes;
    }

    @Override
    public ThingType getThingType(ThingTypeUID thingTypeUID, Locale locale) {

        Collection<List<ThingType>> thingTypesList = this.bundleThingTypesMap.values();

        if (thingTypesList != null) {
            for (List<ThingType> thingTypes : thingTypesList) {
                for (ThingType thingType : thingTypes) {
                    if (thingType.getUID().equals(thingTypeUID)) {
                        return thingType;
                    }
                }
            }
        }
        return null;
    }

}
