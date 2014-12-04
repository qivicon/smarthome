/**
 * 
 */
package org.eclipse.smarthome.automation.core.module.handler;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.automation.core.ThreadPoolRuleExecutor;
import org.eclipse.smarthome.automation.core.internal.RuntimeRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author niehues
 *
 */
public abstract class AbstractTriggerHandler implements TriggerHandler{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTriggerHandler.class);

    private Map<ModuleContext, TriggerListener> listeners = new HashMap<>();

    @Override
    public void addListener(ModuleContext context, TriggerListener listener) {
        String itemName = (String) context.getInputParameter("itemName");
        if (itemName != null && !itemName.trim().isEmpty()) {
            listeners.put(context, listener);
            LOGGER.debug("Listener added to UpdateTrigger: {}",  (context.getId()));
        }
    }
    
    @Override
    public void removeListener(ModuleContext context, TriggerListener listener) {
        listeners.remove(context);
    }
    
    protected void executeRule(ModuleContext context, RuntimeRule rule){
        ThreadPoolRuleExecutor.execute(context, rule);
    }

}
