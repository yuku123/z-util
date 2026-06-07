package com.zifang.util.core.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 任务接口，对应 Quartz 的 {@link org.quartz.Job}。
 * <p>
 * 所有需要被调度执行的任务类都必须实现此接口。
 * <p>
 * 示例：
 * <pre>
 * public class MyJob implements Job {
 *     public void execute(JobExecutionContext ctx) throws JobExecutionException {
 *         System.out.println("执行任务，参数：" + ctx.getMergedJobDataMap().get("key"));
 *     }
 * }
 * </pre>
 *
 * @see StatefulJob
 * @see JobBuilder
 */
/**
 * Job接口。
 */
/**
 * Job接口。
 */
public interface Job {

    /**
     * 任务被执行时调用的方法。
     *
     * @param context 任务执行上下文，包含了触发时间、JobDetail、Trigger 等信息
     * @throws JobExecutionException 执行过程中发生的业务异常，应当记录并可能导致任务标记为失败
     */
    void execute(JobExecutionContext context) throws JobExecutionException;
}
