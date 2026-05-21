package com.zifang.util.core.schedule.listener;

import com.zifang.util.core.schedule.JobExecutionContextWrapper;
import org.quartz.JobExecutionException;

/**
 * 任务监听器接口，对应 Quartz 的 {@link org.quartz.JobListener}。
 * <p>
 * 监听任务的各个生命周期阶段。
 * <p>
 * 示例：记录任务执行日志
 * <pre>
 * JobListener listener = new JobListener() {
 *     public String getName() { return "my-job-listener"; }
 *
 *     public void jobToBeExecuted(JobExecutionContextWrapper ctx) {
 *         System.out.println("任务即将执行: " + ctx.getJobName());
 *     }
 *
 *     public void jobWasExecuted(JobExecutionContextWrapper ctx, JobExecutionException e) {
 *         if (e != null) {
 *             System.err.println("任务执行失败: " + ctx.getJobName() + ", 错误: " + e.getMessage());
 *         } else {
 *             System.out.println("任务执行成功: " + ctx.getJobName());
 *         }
 *     }
 *
 *     public void jobExecutionVetoed(JobExecutionContextWrapper ctx) {
 *         System.out.println("任务被否决: " + ctx.getJobName());
 *     }
 * };
 *
 * scheduler.getListenerManager().addJobListener(listener);
 * </pre>
 *
 * @see SchedulerManager#getListenerManager()
 * @see TriggerListener
 * @see SchedulerListener
 */
public interface JobListener {

    /**
     * 获取监听器名称。
     */
    String getName();

    /**
     * 任务即将被执行（在 Trigger 触发之后，Job.execute 之前调用）。
     * <p>
     * 如果注册了多个 JobListener，会按优先级顺序调用。
     */
    default void jobToBeExecuted(JobExecutionContextWrapper context) {
    }

    /**
     * 任务执行被否决（在 Trigger 触发之后，Job.execute 之前被 Listener 否决）。
     * <p>
     * 任务不会执行，Trigger 会等待下次触发时间。
     */
    default void jobExecutionVetoed(JobExecutionContextWrapper context) {
    }

    /**
     * 任务执行完成（Job.execute 返回后调用，无论是正常返回还是抛出异常）。
     *
     * @param context   任务执行上下文
     * @param exception 任务执行中抛出的异常（如果任务正常完成则为 null）
     */
    default void jobWasExecuted(JobExecutionContextWrapper context,
                                JobExecutionException exception) {
    }
}
