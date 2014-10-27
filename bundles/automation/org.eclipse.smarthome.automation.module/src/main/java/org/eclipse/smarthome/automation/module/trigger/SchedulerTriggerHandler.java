/**
 * 
 */
package org.eclipse.smarthome.automation.module.trigger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.smarthome.automation.core.module.handler.ModuleContext;
import org.eclipse.smarthome.automation.core.module.handler.TriggerHandler;
import org.eclipse.smarthome.automation.core.module.handler.TriggerListener;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author niehues
 *
 */
public class SchedulerTriggerHandler implements TriggerHandler {

	private static final String JOB_GROUP_ID = "ScheduledRule";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SchedulerTriggerHandler.class);

	private static final Map<String, ModuleContextRuleHolder> rulesContextMap = Collections
			.synchronizedMap(new HashMap<String, ModuleContextRuleHolder>());

	private static final Map<String, JobKey> jobKeys = Collections
			.synchronizedMap(new HashMap<String, JobKey>());

	private static final SchedulerFactory schedulerFactory = new StdSchedulerFactory();

	private Scheduler scheduler;

	public SchedulerTriggerHandler() {
		try {
			this.scheduler = schedulerFactory.getScheduler();
		} catch (SchedulerException e) {
			LOGGER.error("Could not create Scheduler: {}", e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.smarthome.automation.core.module.handler.ModuleHandler#getName
	 * ()
	 */
	@Override
	public String getName() {
		return "timer";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.smarthome.automation.core.module.handler.TriggerHandler#
	 * addListener
	 * (org.eclipse.smarthome.automation.core.module.handler.ModuleContext,
	 * org.eclipse.smarthome.automation.core.module.handler.TriggerListener)
	 */
	@Override
	public void addListener(ModuleContext context, TriggerListener listener) {
		rulesContextMap.put(context.getId(), new ModuleContextRuleHolder(
				context, listener));

		String cron = (String) context.getInputParameter("cron");
		if (cron != null) {
			LOGGER.debug("adding cron job for ModuleContext {} with cron expression {}",context.getId(), cron);
			JobDetail job = JobBuilder.newJob(RuleJob.class)
					.usingJobData(RuleJob.RULE_CONTEXT_ID, context.getId())
					.withIdentity(context.getId(), JOB_GROUP_ID).build();
			jobKeys.put(context.getId(), job.getKey());
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(context.getId(), JOB_GROUP_ID)
					.withSchedule(CronScheduleBuilder.cronSchedule(cron))
					.build();
			try {
				scheduler.scheduleJob(job, trigger);
			} catch (SchedulerException e) {
				LOGGER.error("Problems while creating Cron Job for Rule {}"
						+ e.getMessage());
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.smarthome.automation.core.module.handler.TriggerHandler#
	 * removeListener
	 * (org.eclipse.smarthome.automation.core.module.handler.ModuleContext,
	 * org.eclipse.smarthome.automation.core.module.handler.TriggerListener)
	 */
	@Override
	public void removeListener(ModuleContext context, TriggerListener listener) {
		try {
			if (scheduler.deleteJob(jobKeys.get(context.getId()))){
				jobKeys.remove(context.getId());
				rulesContextMap.remove(context.getId());
			}
		} catch (SchedulerException e) {
			LOGGER.error("removing cron job failed for Context Id {}",
					context.getId());
		}
		
		
	}

	public static ModuleContextRuleHolder getModuleContextRuleHolder(
			String moduleContextId) {
		return rulesContextMap.get(moduleContextId);
	}
}
