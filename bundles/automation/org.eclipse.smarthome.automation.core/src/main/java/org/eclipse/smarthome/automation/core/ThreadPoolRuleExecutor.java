package org.eclipse.smarthome.automation.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.smarthome.automation.core.module.handler.ModuleContext;
import org.eclipse.smarthome.automation.core.module.handler.TriggerListener;

public class ThreadPoolRuleExecutor {
    
    private static ExecutorService executor = Executors.newFixedThreadPool(10);
    
    public static void execute(ModuleContext context, TriggerListener triggerListener){
        
        executor.execute(new RuleRunner(context, triggerListener));
    }
    
    public static void shutdown(){
        executor.shutdown();
    }

    
    private static class RuleRunner implements Runnable {

        private ModuleContext context;
        private TriggerListener rule;
        public RuleRunner(ModuleContext context, TriggerListener rule){
            this.context = context;
            this.rule = rule;
        }
        
        @Override
        public void run() {
            this.rule.execute(context);
        }
        
    }
}
