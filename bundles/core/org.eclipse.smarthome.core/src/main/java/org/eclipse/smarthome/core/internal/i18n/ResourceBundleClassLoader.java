package org.eclipse.smarthome.core.internal.i18n;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.Bundle;


public class ResourceBundleClassLoader extends ClassLoader {

    private Bundle bundle;
    private String path;
    private String filePattern;


    public ResourceBundleClassLoader(Bundle bundle, String path, String filePattern)
            throws IllegalArgumentException {

        if (bundle == null) {
            throw new IllegalArgumentException("The bundle must not be null!");
        }

        this.bundle = bundle;
        this.path = (path != null) ? path : "/";
        this.filePattern = (filePattern != null) ? filePattern : "*";
    }

    @Override
    @SuppressWarnings("unchecked")
    public URL getResource(String name) {
        Enumeration<URL> resourceFiles = this.bundle.findEntries(this.path, this.filePattern, true);

        if (resourceFiles != null) {
            while (resourceFiles.hasMoreElements()) {
                URL resourceURL = resourceFiles.nextElement();
                String resourcePath = resourceURL.getFile();
                File resourceFile = new File(resourcePath);
                String resourceFileName = resourceFile.getName();

                if (resourceFileName.equals(name)) {
                    return resourceURL;
                }
            }
        }

        return null;
    }

}
