/**
 * 
 */
package org.eclipse.smarthome.automation.module.trigger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author niehues
 *
 */
public class RuleJob implements Job {

	public static final String RULE_CONTEXT_ID = "RuleContextId";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext jobContext)
			throws JobExecutionException {
		String ruleContextId = jobContext.getJobDetail().getJobDataMap()
				.getString(RULE_CONTEXT_ID);
		ModuleContextRuleHolder moduleContextRule = SchedulerTriggerHandler
				.getModuleContextRuleHolder(ruleContextId);
		moduleContextRule.getRule().execute(moduleContextRule.getContext());
	}

}
