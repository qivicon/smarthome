package org.eclipse.smarthome.automation.core.internal;

import java.util.Dictionary;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.eclipse.smarthome.automation.core.RuleExecutionService;
import org.eclipse.smarthome.automation.core.module.handler.ModuleContext;
import org.eclipse.smarthome.automation.core.module.handler.TriggerListener;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;

public class RuleExecutionServiceImpl implements RuleExecutionService, ManagedService {
    
    private static final String THREAD_POOL_SIZE__KEY = "threadPoolSize";

    private ExecutorService executor = null;

    @Override
    public void execute(ModuleContext context, TriggerListener triggerListener) {
        ExecutorService executor = this.executor;
        if (executor != null) {
            executor.execute(new RuleRunner(context, triggerListener));            
        } else {
            throw new IllegalStateException("Rule execution service is not initialized.");
        }
    }

    private static class RuleRunner implements Runnable {

        private ModuleContext context;
        private TriggerListener rule;

        public RuleRunner(ModuleContext context, TriggerListener rule) {
            this.context = context;
            this.rule = rule;
        }

        @Override
        public void run() {
            this.rule.execute(context);
        }
    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        initNewExecuterService(properties);
    }
    
    protected void activate(ComponentContext context){
        Dictionary<String, ?> properties = context.getProperties();
        initNewExecuterService(properties);
    }

    private void initNewExecuterService(Dictionary<String, ?> properties) {
        Object property = properties.get(THREAD_POOL_SIZE__KEY);
        Integer newThreadPoolSize = (property instanceof Integer) ? (Integer) property : Runtime
                .getRuntime().availableProcessors();
        if (this.executor instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) this.executor;
            if (threadPoolExecutor.getPoolSize() == newThreadPoolSize) {
                return; // no need to re-initialize the executer
            }
        }
        ExecutorService executor = this.executor;
        this.executor = Executors.newFixedThreadPool(newThreadPoolSize);
        if (executor != null) {
            executor.shutdown();
        }
    }

    protected void deactivate(ComponentContext context){
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }
}
