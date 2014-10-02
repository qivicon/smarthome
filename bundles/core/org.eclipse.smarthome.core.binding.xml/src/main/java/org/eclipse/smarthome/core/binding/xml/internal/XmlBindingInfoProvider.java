package org.eclipse.smarthome.core.binding.xml.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.smarthome.core.binding.BindingInfo;
import org.eclipse.smarthome.core.binding.BindingInfoProvider;
import org.eclipse.smarthome.core.common.ServiceBinder.Bind;
import org.eclipse.smarthome.core.common.ServiceBinder.Unbind;
import org.eclipse.smarthome.core.i18n.BindingInfoI18nProvider;
import org.osgi.framework.Bundle;


/**
 * The {@link XmlBindingInfoProvider} is a concrete implementation of the
 * {@link BindingInfoProvider} service interface.
 * <p>
 * This implementation manages any {@link BindingInfo} objects associated
 * to specific modules. If a specific module disappears, any registered
 * {@link BindingInfo} objects associated with that module are released.
 * 
 * @author Michael Grammling - Initial Contribution
 * @author Michael Grammling - Refactoring: Provider/Registry pattern is used, added locale support
 */
public class XmlBindingInfoProvider implements BindingInfoProvider {

    private Map<Bundle, List<BindingInfo>> bundleBindingInfoMap;
    private BindingInfoI18nProvider bindingInfoI18nProvider;


    public XmlBindingInfoProvider() {
        this.bundleBindingInfoMap = new HashMap<>(10);
    }

    private List<BindingInfo> acquireBindingInfos(Bundle bundle) {
        if (bundle != null) {
            List<BindingInfo> bindingInfos = this.bundleBindingInfoMap.get(bundle);

            if (bindingInfos == null) {
                bindingInfos = new ArrayList<BindingInfo>(10);

                this.bundleBindingInfoMap.put(bundle, bindingInfos);
            }

            return bindingInfos;
        }

        return null;
    }

    /**
     * Adds a {@link BindingInfo} object to the internal list associated with the specified module.
     * <p>
     * This method returns silently, if any of the parameters is {@code null}.
     * 
     * @param bundle the module to which the binding information to be added
     * @param bindingInfo the binding information to be added
     */
    public synchronized void addBindingInfo(Bundle bundle, BindingInfo bindingInfo) {
        if (bindingInfo != null) {
            List<BindingInfo> bindingInfos = acquireBindingInfos(bundle);

            if (bindingInfos != null) {
                bindingInfos.add(bindingInfo);
            }
        }
    }

    /**
     * Removes all {@link BindingInfo} objects from the internal list
     * associated with the specified module.
     * <p>
     * This method returns silently if the module is {@code null}.
     * 
     * @param bundle the module for which all associated binding informations to be removed
     */
    public synchronized void removeAllBindingInfos(Bundle bundle) {
        if (bundle != null) {
            List<BindingInfo> bindingInfos = this.bundleBindingInfoMap.get(bundle);

            if (bindingInfos != null) {
                this.bundleBindingInfoMap.remove(bundle);
            }
        }
    }

    @Override
    public synchronized BindingInfo getBindingInfo(String id, Locale locale) {
        Collection<Entry<Bundle, List<BindingInfo>>> bindingInfoList =
                this.bundleBindingInfoMap.entrySet();

        if (bindingInfoList != null) {
            for (Entry<Bundle, List<BindingInfo>> bindingInfos : bindingInfoList) {
                for (BindingInfo bindingInfo : bindingInfos.getValue()) {
                    if (bindingInfo.getId().equals(id)) {
                        return createLocalizedBindingInfo(bindingInfos.getKey(), bindingInfo, locale);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public synchronized Collection<BindingInfo> getBindingInfos(Locale locale) {
        List<BindingInfo> allBindingInfos = new ArrayList<>(10);

        Collection<Entry<Bundle, List<BindingInfo>>> bindingInfoList =
                this.bundleBindingInfoMap.entrySet();

        if (bindingInfoList != null) {
            for (Entry<Bundle, List<BindingInfo>> bindingInfos : bindingInfoList) {
                for (BindingInfo bindingInfo : bindingInfos.getValue()) {
                    BindingInfo localizedBindingInfo = createLocalizedBindingInfo(
                            bindingInfos.getKey(), bindingInfo, locale);

                    allBindingInfos.add(localizedBindingInfo);
                }
            }
        }

        return allBindingInfos;
    }

    @Bind
    @Unbind
    public void setBindingInfoI18nProvider(BindingInfoI18nProvider bindingInfoI18nProvider) {
        this.bindingInfoI18nProvider = bindingInfoI18nProvider;
    }

    private BindingInfo createLocalizedBindingInfo(
            Bundle bundle, BindingInfo bindingInfo, Locale locale) {

        BindingInfoI18nProvider bindingInfoI18nProvider = this.bindingInfoI18nProvider;
        if (bindingInfoI18nProvider != null) {
            String label = bindingInfoI18nProvider.getLabel(
                    bundle, bindingInfo.getId(), bindingInfo.getName(), locale);
            String description = bindingInfoI18nProvider.getDescription(
                    bundle, bindingInfo.getId(), bindingInfo.getDescription(), locale);

            return new BindingInfo(bindingInfo.getId(), label, description,
                    bindingInfo.getAuthor(), bindingInfo.getConfigDescriptionURI());
        } else {
            return bindingInfo;
        }
    }

}
