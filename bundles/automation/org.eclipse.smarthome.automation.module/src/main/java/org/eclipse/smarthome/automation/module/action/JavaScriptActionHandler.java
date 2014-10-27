/**
 * 
 */
package org.eclipse.smarthome.automation.module.action;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.eclipse.smarthome.automation.core.module.handler.ActionHandler;
import org.eclipse.smarthome.automation.core.module.handler.ModuleContext;
import org.eclipse.smarthome.core.events.EventPublisher;
import org.eclipse.smarthome.core.items.ItemRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author niehues
 *
 */
public class JavaScriptActionHandler implements ActionHandler {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JavaScriptActionHandler.class);

	private ItemRegistry itemRegistry;
	
	private EventPublisher eventPublisher;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.smarthome.automation.core.module.handler.ModuleHandler#getName
	 * ()
	 */
	@Override
	public String getName() {
		return "JavaScript";
	}
	
	protected void setEventPublisher(EventPublisher eventPublisher){
		this.eventPublisher = eventPublisher;
	}

	protected void unsetEventPublisher(){
		this.eventPublisher=null;
	}
	
	protected void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	protected void unsetItemRegistry() {
		this.itemRegistry = null;
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
		LOGGER.debug("ececuting JavaScript");
		String script = (String) context.getInputParameter("script");
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine engine = scriptEngineManager.getEngineByName("JavaScript");
		ScriptContext scriptContext = new SimpleScriptContext();
		Bindings bindings = scriptContext
				.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.put("itemRegistry", itemRegistry);
		bindings.put("eventPublisher", eventPublisher);
		try {
			engine.eval(script, scriptContext);
		} catch (ScriptException e) {
			LOGGER.error("Problems occured during evaluation of JavaScript {}",
					e.getMessage());
		}
	}

}
