package com.zifang.util.core.schedule;

/**
 * Lambda 友好的任务接口。
 * <p>
 * 与 {@link Job} 接口相比，此接口使用更简洁，适合 lambda 表达式和 method reference。
 * <p>
 * 示例：
 * <pre>
 * // 使用 lambda
 * SchedulerManager.schedule(
 *     JobBuilder.newJob((RunnableJob) ctx -> System.out.println("Hello!"))
 *         .withName("hello-job")
 *         .usingJobData("msg", "Hello world")
 *         .build(),
 *     TriggerBuilder.newSimpleTrigger()
 *         .withName("hello-trigger")
 *         .withIntervalInSeconds(5)
 *         .repeatForever()
 *         .build()
 * );
 *
 * // 使用 method reference
 * SchedulerManager.schedule(
 *     JobBuilder.newJob(this::myMethod)
 *         .withName("ref-job")
 *         .build(),
 *     trigger
 * );
 * </pre>
 *
 * @see Job
 * @see StatefulJob
 * @see JobBuilder
 */
@FunctionalInterface
/**
 * RunnableJob接口。
 */
/**
 * RunnableJob接口。
 */
public interface RunnableJob {

    /**
     * 任务执行逻辑。
     *
     * @param context 任务执行上下文，包含触发时间、JobDataMap 等信息
     */
    void execute(JobExecutionContextWrapper context) throws Exception;
}
