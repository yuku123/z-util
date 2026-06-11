package com.zifang.util.core.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * {@link Job} 到 {@link RunnableJob} 的双向适配器。
 * <p>
 * 内部使用，将用户的 Job 实现类或 lambda 风格的任务转换为 Quartz 可识别的 Job 实例。
 *
 * @see JobBuilder
 * @see Job
 * @see RunnableJob
 */
public class RunnableJobAdapter implements org.quartz.Job, Job {

    private static final String RUNNABLE_KEY = "_runnable";
    private static final String JOB_CLASS_KEY = "_jobClass";

    @Override
    /**
     * execute方法。
     *      * @param context JobExecutionContext类型参数
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobExecutionContextWrapper wrapper = new JobExecutionContextWrapper(context);

        // 优先检查 lambda 模式
        Object rawRunnable = context.getMergedJobDataMap().get(RUNNABLE_KEY);
        if (rawRunnable instanceof RunnableJob) {
            try {
                ((RunnableJob) rawRunnable).execute(wrapper);
                return;
            } catch (Exception e) {
                rethrow(e, context);
                return; // unreachable
            }
        }

        // 否则检查接口实现类模式
        Object rawClassName = context.getMergedJobDataMap().get(JOB_CLASS_KEY);
        if (rawClassName == null) {
            throw new JobExecutionException(
                    "Neither RunnableJob nor Job class found in JobDataMap. " +
                    "Use JobBuilder.newJob(YourJob.class) or JobBuilder.newJob(runnable).");
        }

        Class<?> clazz;
        if (rawClassName instanceof Class) {
            clazz = (Class<?>) rawClassName;
        } else {
            // JobDataMap 反序列化后可能是 String（类名字符串）
            try {
                clazz = Class.forName(String.valueOf(rawClassName));
            } catch (ClassNotFoundException e) {
                throw new JobExecutionException("Job class not found: " + rawClassName, e);
            }
        }

        try {
            Job userJob = (Job) clazz.newInstance();
            userJob.execute(wrapper.getDelegate());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JobExecutionException("Failed to instantiate Job class: " + clazz.getName(), e);
        } catch (Exception e) {
            rethrow(e, context);
        }
    }

    private void rethrow(Exception e, JobExecutionContext context) throws JobExecutionException {
        if (e instanceof JobExecutionException) {
            throw (JobExecutionException) e;
        }
        boolean nonConcurrent = !Boolean.TRUE.equals(
                context.getJobDetail().isConcurrentExectionDisallowed());
        throw new JobExecutionException("Job execution failed: " + e.getMessage(), e, nonConcurrent);
    }
}
