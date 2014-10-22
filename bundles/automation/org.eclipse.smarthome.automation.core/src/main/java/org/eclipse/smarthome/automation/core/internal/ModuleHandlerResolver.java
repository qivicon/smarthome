/**
 * 
 */
package org.eclipse.smarthome.automation.core.internal;

import java.util.Collection;

import org.eclipse.smarthome.automation.core.ActionRef;
import org.eclipse.smarthome.automation.core.ConditionRef;
import org.eclipse.smarthome.automation.core.ModuleRef;
import org.eclipse.smarthome.automation.core.TriggerRef;
import org.eclipse.smarthome.automation.core.module.handler.ActionHandler;
import org.eclipse.smarthome.automation.core.module.handler.ConditionHandler;
import org.eclipse.smarthome.automation.core.module.handler.ModuleHandler;
import org.eclipse.smarthome.automation.core.module.handler.TriggerHandler;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class with static methods to resolve a ModuleHandler from the OSGI-Stack
 * 
 * @author niehues
 *
 */
public class ModuleHandlerResolver {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ModuleHandlerResolver.class);

	/**
	 * resolves a moduleRef to an implementations of IModuleHandler on the
	 * OSGI-Stack
	 * 
	 * @param moduleRef
	 * @return
	 */
	public static ModuleHandler resolve(ModuleRef moduleRef,
			BundleContext bundleContext) {

		Class<? extends ModuleHandler> moduleHandlerClass = determineHandlerClass(moduleRef);
		if (moduleHandlerClass == null) {
			LOGGER.error("Module is not of the expected Type: {}", moduleRef
					.getClass().getName());
			return null;
		}
		try {
			Collection<?> serviceReferences = bundleContext
					.getServiceReferences(moduleHandlerClass, "(module.name="
							+ moduleRef.getType() + ")");
			for (Object serviceReference : serviceReferences) {
				return (ModuleHandler) bundleContext
						.getService((ServiceReference<?>) serviceReference);
			}
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Class<? extends ModuleHandler> determineHandlerClass(
			ModuleRef moduleRef) {
		if (moduleRef instanceof TriggerRef) {
			return TriggerHandler.class;
		} else if (moduleRef instanceof ActionRef) {
			return ActionHandler.class;
		} else if (moduleRef instanceof ConditionRef) {
			return ConditionHandler.class;
		}
		return null;
	}
}
