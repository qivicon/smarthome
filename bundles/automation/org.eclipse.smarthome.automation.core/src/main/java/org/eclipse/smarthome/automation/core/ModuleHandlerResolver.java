/**
 * 
 */
package org.eclipse.smarthome.automation.core;

import java.util.Collection;

import org.eclipse.smarthome.automation.core.jsonmodel.ModuleRef;
import org.eclipse.smarthome.automation.core.runtimemodel.IModuleHandler;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * A class with static methods to resolve a ModuleHandler from the OSGI-Stack
 * 
 * @author niehues
 *
 */
public class ModuleHandlerResolver {

	/**
	 * resolves a moduleRef to an implementations of IModuleHandler on the
	 * OSGI-Stack
	 * 
	 * @param moduleRef
	 * @return
	 */
	public static IModuleHandler resolve(Class<? extends IModuleHandler> moduleHandlerClass, ModuleRef moduleRef, BundleContext bundleContext) {
		
	    try {
	        Collection<?> serviceReferences = bundleContext.getServiceReferences(moduleHandlerClass, "(module.name="+moduleRef.getType()+")");
            for (Object serviceReference : serviceReferences) {
                return (IModuleHandler) bundleContext.getService((ServiceReference<?>) serviceReference);
            }
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
	    return null;
	}

}
