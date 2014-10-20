/**
 * 
 */
package org.eclipse.smarthome.automation.core;

import org.eclipse.smarthome.automation.core.jsonmodel.ModuleRef;
import org.eclipse.smarthome.automation.core.runtimemodel.IModuleHandler;

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
	public static IModuleHandler resolve(ModuleRef moduleRef) {
		return null;
	}

}
