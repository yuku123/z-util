package com.zifang.util.core.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

/**
 * {@link RunnableJob} 到 {@link Job} 的适配器。
 * <p>
 * 内部使用，将 lambda 风格的任务转换为 Quartz 可识别的 Job 实例。
 *
 * @see RunnableJob
 * @see JobBuilder
 */
public class RunnableJobAdapter implements Job {

    private static final String RUNNABLE_KEY = "_runnable";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Object raw = context.getMergedJobDataMap().get(RUNNABLE_KEY);
        if (!(raw instanceof RunnableJob)) {
            throw new JobExecutionException(
                    "RunnableJob not found in JobDataMap. " +
                            "Ensure the Job was built using JobBuilder.newJob(RunnableJob).");
        }

        RunnableJob runnable = (RunnableJob) raw;
        JobExecutionContextWrapper wrapper = new JobExecutionContextWrapper(context);

        try {
            runnable.execute(wrapper);
        } catch (Exception e) {
            if (e instanceof JobExecutionException) {
                throw (JobExecutionException) e;
            }
            throw new JobExecutionException("RunnableJob execution failed: " + e.getMessage(), e,
                    !Boolean.TRUE.equals(context.getJobDetail().isConcurrentExecutionDisallowed()));
        }
    }
}
