/**
 * 
 */
package org.eclipse.smarthome.automation.module.action;

import org.eclipse.smarthome.automation.core.module.handler.ActionHandler;
import org.eclipse.smarthome.automation.core.module.handler.ModuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author niehues
 *
 */
public class LogActionHandler implements ActionHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LogActionHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.smarthome.automation.core.module.handler.ModuleHandler#getName
	 * ()
	 */
	@Override
	public String getName() {
		return "log";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.smarthome.automation.core.module.handler.ActionHandler#execute
	 * (org.eclipse.smarthome.automation.core.module.handler.ModuleContext)
	 */
	@Override
	public void execute(ModuleContext context) {
		String level = (String) context.getInputParameter("level");
		String message = (String) context.getInputParameter("message");
		switch (level.toUpperCase()) {
		case "INFO":
			LOGGER.info(message);
			break;
		case "WARN":
			LOGGER.warn(message);
			break;
		case "ERROR":
			LOGGER.error(message);
			break;
		case "DEBUG":
			LOGGER.debug(message);
			break;
		default:
			break;
		}
	}

}
