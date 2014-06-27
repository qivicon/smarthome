package org.eclipse.smarthome.config.setupflow;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * {@link BaseSetupStepHandler} is an abstract {@link SetupStepHandler}
 * implementation. The class registers aborted flow processes and provides an
 * {@link #isAborted(SetupFlowContext)} method for subclasses to check if the
 * current step is aborted.
 * 
 * @author Oliver Libutzki
 * 
 */
public abstract class BaseSetupStepHandler implements SetupStepHandler {

    private BundleContext bundleContext;

    private Map<String, TaskExecutionThread> taskExecutionThreadMap = new HashMap<String, TaskExecutionThread>();

    public BaseSetupStepHandler(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public void start(SetupFlowContext setupFlowContext, SetupStepHandlerCallback callback) {
        startInternal(setupFlowContext, callback);

    }

    protected abstract void startInternal(SetupFlowContext setupFlowContext,
            SetupStepHandlerCallback callback);

    protected BundleContext getBundleContext() {
        return bundleContext;
    }

    @Override
    public void abort(String flowProcessId) {
        final TaskExecutionThread taskExecutionThread = taskExecutionThreadMap.get(flowProcessId);
        if (taskExecutionThread != null) {
            taskExecutionThread.abort();
        }
    }

    protected ThingRegistry getThingRegistry() {
        ServiceReference<?> serviceReference = bundleContext
                .getServiceReference(ThingRegistry.class.getName());
        return (ThingRegistry) bundleContext.getService(serviceReference);
    }

    protected void runTask(Task runnable, SetupFlowContext setupFlowContext) {
        final String contextId = setupFlowContext.getContextId();
        TaskExecutionThread taskExecutionThread = taskExecutionThreadMap.get(contextId);
        if (taskExecutionThread != null) {
            throw new RuntimeException("A task execution is already running.");
        }
        taskExecutionThread = new TaskExecutionThread(runnable);
        taskExecutionThread.start();
    }

    protected interface Task {
        String getName();

        void executeTask(TaskExecutionState taskExecutionState);
    }

    protected abstract class AbstractTask implements Task {
        private String name;

        protected AbstractTask(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    protected interface TaskExecutionState {
        boolean isAborted();
    }

    private class TaskExecutionThread extends Thread {
        private Task task;

        private boolean isAborted = false;

        public TaskExecutionThread(Task task) {
            super(task.getName());
            this.task = task;
        }

        @Override
        public void run() {
            task.executeTask(new TaskExecutionState() {

                @Override
                public boolean isAborted() {
                    return isAborted;
                }
            });
        }

        public void abort() {
            isAborted = true;
        }

    }

}
