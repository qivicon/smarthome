package org.eclipse.smarthome.core.internal.i18n;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.framework.Bundle;


public class LanguageResource {

    private static final String RESOURCE_DIRECTORY = "/ESH-INF/I18N";
    private static final String RESOURCE_FILE_PATTERN = "*.properties";

    private Bundle bundle;
    private ClassLoader resourceClassLoader;
    private List<String> resourceNames;


    public LanguageResource(Bundle bundle) {
        if (bundle == null) {
            throw new IllegalArgumentException("The Bundle must not be null!");
        }

        this.bundle = bundle;

        this.resourceClassLoader = new ResourceBundleClassLoader(
                bundle, RESOURCE_DIRECTORY, RESOURCE_FILE_PATTERN);

        this.resourceNames = determineResourceNames();
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public boolean containsResource(String resource) {
        return this.resourceNames.contains(resource);
    }

    public boolean containsResources() {
        return (this.resourceNames.size() > 0);
    }

    @SuppressWarnings("unchecked")
    private List<String> determineResourceNames() {
        List<String> resourceNames = new ArrayList<>();

        Enumeration<URL> resourceFiles = this.bundle.findEntries(
                RESOURCE_DIRECTORY, RESOURCE_FILE_PATTERN, true);

        if (resourceFiles != null) {
            while (resourceFiles.hasMoreElements()) {
                URL resourceURL = resourceFiles.nextElement();
                String resourcePath = resourceURL.getFile();
                File resourceFile = new File(resourcePath);
                String resourceFileName = resourceFile.getName();
                String baseName = resourceFileName.replaceFirst("[._]+.*", "");
    
                if (!resourceNames.contains(baseName)) {
                    resourceNames.add(baseName);
                }
            }
        }

        return resourceNames;
    }

    public String getText(String resource, String key, Locale locale) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(
                    resource, locale, this.resourceClassLoader);
    
            return resourceBundle.getString(key);
        } catch (Exception ex) {
            // nothing to do
        }

        return null;
    }

    public String getText(String key, Locale locale) {
        for (String resourceName : this.resourceNames) {
            try {
                ResourceBundle resourceBundle = ResourceBundle.getBundle(
                        resourceName, locale, this.resourceClassLoader);

                String text = resourceBundle.getString(key);
                if (text != null) {
                    return text;
                }
            } catch (Exception ex) {
                // nothing to do
            }
        }

        return null;
    }

}
