package org.eclipse.smarthome.automation.core;

import org.eclipse.smarthome.automation.core.module.handler.ModuleContext;
import org.eclipse.smarthome.automation.core.module.handler.TriggerListener;

public interface RuleExecutionService {

    /**
     * 
     * @param context
     * @param triggerListener
     */
    public void execute(ModuleContext context, TriggerListener triggerListener);

}
