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
import java.util.Map.Entry;

import org.eclipse.smarthome.core.common.osgi.ServiceBinder.Bind;
import org.eclipse.smarthome.core.common.osgi.ServiceBinder.Unbind;
import org.eclipse.smarthome.core.i18n.I18nProvider;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.ThingTypeProvider;
import org.eclipse.smarthome.core.thing.i18n.ThingTypeI18nUtil;
import org.eclipse.smarthome.core.thing.type.BridgeType;
import org.eclipse.smarthome.core.thing.type.ChannelDefinition;
import org.eclipse.smarthome.core.thing.type.ChannelGroupDefinition;
import org.eclipse.smarthome.core.thing.type.ChannelGroupType;
import org.eclipse.smarthome.core.thing.type.ChannelGroupTypeUID;
import org.eclipse.smarthome.core.thing.type.ChannelState;
import org.eclipse.smarthome.core.thing.type.ChannelStateOption;
import org.eclipse.smarthome.core.thing.type.ChannelType;
import org.eclipse.smarthome.core.thing.type.ChannelTypeUID;
import org.eclipse.smarthome.core.thing.type.ThingType;
import org.osgi.framework.Bundle;

/**
 * The {@link XmlThingTypeProvider} is a concrete implementation of the
 * {@link ThingTypeProvider} service interface.
 * <p>
 * This implementation manages any {@link ThingType} objects associated to
 * specific modules. If a specific module disappears, any registered
 * {@link ThingType} objects associated with that module are released.
 * 
 * @author Michael Grammling - Initial Contribution
 * @author Dennis Nobel - Added locale support
 */
public class XmlThingTypeProvider implements ThingTypeProvider {

    private Map<Bundle, List<ThingType>> bundleThingTypesMap;

    private ThingTypeI18nUtil thingTypeI18nUtil;

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
     * Adds a {@link ThingType} object to the internal list associated with the
     * specified module.
     * <p>
     * This method returns silently, if any of the parameters is {@code null}.
     * 
     * @param bundle
     *            the module to which the Thing type to be added
     * @param thingType
     *            the Thing type to be added
     */
    public synchronized void addThingType(Bundle bundle, ThingType thingType) {
        if (thingType != null) {
            List<ThingType> thingTypes = acquireThingTypes(bundle);

            if (thingTypes != null) {
                thingTypes.add(thingType);
            }
        }
    }

    private ChannelDefinition createLocalizedChannelDefinition(Bundle bundle, ChannelDefinition channelDefinition,
            Locale locale) {

        if (this.thingTypeI18nUtil != null) {
            ChannelType channelType = channelDefinition.getType();

            ChannelTypeUID channelTypeUID = channelType.getUID();

            String label = this.thingTypeI18nUtil.getChannelLabel(bundle, channelTypeUID, channelType.getLabel(),
                    locale);
            String description = this.thingTypeI18nUtil.getChannelDescription(bundle, channelTypeUID,
                    channelType.getDescription(), locale);

            ChannelState state = createLocalizedChannelState(bundle, channelType, channelTypeUID, locale);

            ChannelType localizedChannelType = new ChannelType(channelTypeUID, channelType.isAdvanced(),
                    channelType.getItemType(), label, description, channelType.getCategory(), channelType.getTags(),
                    state, channelType.getConfigDescriptionURI());

            return new ChannelDefinition(channelDefinition.getId(), localizedChannelType);
        } else {
            return channelDefinition;
        }
    }

    private ChannelGroupDefinition createLocalizedChannelGroupDefinition(Bundle bundle,
            ChannelGroupDefinition channelGroupDefinition, Locale locale) {
        ChannelGroupType channelGroupType = channelGroupDefinition.getType();

        List<ChannelDefinition> localizedChannelDefinitions = new ArrayList<>();
        List<ChannelDefinition> channelDefinitions = channelGroupType.getChannelDefinitions();
        
        for (ChannelDefinition channelDefinition : channelDefinitions) {
            ChannelDefinition localizedChannelDefinition = createLocalizedChannelDefinition(bundle, channelDefinition, locale);
            localizedChannelDefinitions.add(localizedChannelDefinition);
        }
        

        ChannelGroupTypeUID channelGroupTypeUID = channelGroupType.getUID();
        
        String label = this.thingTypeI18nUtil.getChannelGroupLabel(bundle, channelGroupTypeUID,
                channelGroupType.getLabel(), locale);
        String description = this.thingTypeI18nUtil.getChannelGroupLabel(bundle, channelGroupTypeUID,
                channelGroupType.getDescription(), locale);  

        ChannelGroupType localizedChannelGroupType = new ChannelGroupType(channelGroupTypeUID,
                channelGroupType.isAdvanced(), label, description, localizedChannelDefinitions);
        
        return new ChannelGroupDefinition(channelGroupDefinition.getId(), localizedChannelGroupType);
    }

    private ChannelState createLocalizedChannelState(Bundle bundle, ChannelType channelType,
            ChannelTypeUID channelTypeUID, Locale locale) {

        ChannelState state = channelType.getState();

        if (state != null) {
            String pattern = this.thingTypeI18nUtil.getChannelStatePattern(bundle, channelTypeUID, state.getPattern(),
                    locale);

            List<ChannelStateOption> localizedOptions = new ArrayList<>();
            List<ChannelStateOption> options = state.getOptions();
            for (ChannelStateOption channelStateOption : options) {
                String optionLabel = this.thingTypeI18nUtil.getChannelStateOption(bundle, channelTypeUID,
                        channelStateOption.getValue(), channelStateOption.getLabel(), locale);
                localizedOptions.add(new ChannelStateOption(channelStateOption.getValue(), optionLabel));
            }

            return new ChannelState(state.getMinimum(), state.getMaximum(), state.getStep(), pattern,
                    state.isReadOnly(), localizedOptions);
        } else {
            return null;
        }
    }

    private ThingType createLocalizedThingType(Bundle bundle, ThingType thingType, Locale locale) {
        if (this.thingTypeI18nUtil != null) {
            String label = this.thingTypeI18nUtil.getLabel(
                    bundle, thingType.getUID(), thingType.getLabel(), locale);
            String description = this.thingTypeI18nUtil.getDescription(
                    bundle, thingType.getUID(), thingType.getDescription(), locale);

            List<ChannelDefinition> localizedChannelDefinitions = new ArrayList<>(
                    thingType.getChannelDefinitions().size());

            for (ChannelDefinition channelDefinition : thingType.getChannelDefinitions()) {
                ChannelDefinition localizedChannelDefinition =
                        createLocalizedChannelDefinition(bundle, channelDefinition, locale);
                localizedChannelDefinitions.add(localizedChannelDefinition);
            }
            
            List<ChannelGroupDefinition> localizedChannelGroupDefinitions = new ArrayList<>(thingType
                    .getChannelDefinitions().size());
            for (ChannelGroupDefinition channelGroupDefinition : thingType.getChannelGroupDefinitions()) {
                ChannelGroupDefinition localizedchannelGroupDefinition = createLocalizedChannelGroupDefinition(bundle,
                        channelGroupDefinition, locale);
                localizedChannelGroupDefinitions.add(localizedchannelGroupDefinition);
            }

            if (thingType instanceof BridgeType) {
                BridgeType bridgeType = (BridgeType) thingType;
                return new BridgeType(bridgeType.getUID(), bridgeType.getSupportedBridgeTypeUIDs(), label, description,
                        localizedChannelDefinitions, localizedChannelGroupDefinitions,
                        bridgeType.getConfigDescriptionURI());
            } else {
                return new ThingType(thingType.getUID(), thingType.getSupportedBridgeTypeUIDs(), label, description,
                        localizedChannelDefinitions, localizedChannelGroupDefinitions,
                        thingType.getConfigDescriptionURI());
            }
        } else {
            return thingType;
        }
    }
    
    @Override
    public ThingType getThingType(ThingTypeUID thingTypeUID, Locale locale) {
        Collection<Entry<Bundle, List<ThingType>>> thingTypesList =
                this.bundleThingTypesMap.entrySet();

        if (thingTypesList != null) {
            for (Entry<Bundle, List<ThingType>> thingTypes : thingTypesList) {
                for (ThingType thingType : thingTypes.getValue()) {
                    if (thingType.getUID().equals(thingTypeUID)) {
                        return createLocalizedThingType(thingTypes.getKey(), thingType, locale);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public synchronized Collection<ThingType> getThingTypes(Locale locale) {
        List<ThingType> allThingTypes = new ArrayList<>(10);

        Collection<Entry<Bundle, List<ThingType>>> thingTypesList =
                this.bundleThingTypesMap.entrySet();

        if (thingTypesList != null) {
            for (Entry<Bundle, List<ThingType>> thingTypes : thingTypesList) {
                for (ThingType thingType : thingTypes.getValue()) {
                    ThingType localizedThingType = createLocalizedThingType(
                            thingTypes.getKey(), thingType, locale);

                    allThingTypes.add(localizedThingType);
                }
            }
        }

        return allThingTypes;
    }

    /**
     * Removes all {@link ThingType} objects from the internal list associated
     * with the specified module.
     * <p>
     * This method returns silently if the module is {@code null}.
     * 
     * @param bundle
     *            the module for which all associated Thing types to be removed
     */
    public synchronized void removeAllThingTypes(Bundle bundle) {
        if (bundle != null) {
            List<ThingType> thingTypes = this.bundleThingTypesMap.get(bundle);

            if (thingTypes != null) {
                this.bundleThingTypesMap.remove(bundle);
            }
        }
    }

    @Bind
    public void setI18nProvider(I18nProvider i18nProvider) {
        this.thingTypeI18nUtil = new ThingTypeI18nUtil(i18nProvider);
    }

    @Unbind
    public void unsetI18nProvider(I18nProvider i18nProvider) {
        this.thingTypeI18nUtil = null;
    }

}
