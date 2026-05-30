package com.zifang.util.core.schedule;

/**
 * 有状态任务标记接口，对应 Quartz 的 {@link org.quartz.StatefulJob}。
 * <p>
 * 实现此接口的任务为有状态任务，其特点：
 * <ul>
 *   <li>不允许并发执行：同一 JobDetail 实例在上一批次执行完毕前，不会触发下一批次</li>
 *   <li>JobDataMap 会在执行之间持久化：任务执行后对 JobDataMap 的修改会被保存</li>
 * </ul>
 * <p>
 * 无状态任务（实现 {@link Job}）可以在同一 Trigger 被触发多次时并发执行，
 * 且 JobDataMap 在每次执行后不会保留修改。
 * <p>
 * 示例：
 * <pre>
 * public class MyStatefulJob implements Job, StatefulJob {
 *     public void execute(JobExecutionContext ctx) throws JobExecutionException {
 *         // 同一实例不会并发执行
 *     }
 * }
 * </pre>
 *
 * @see Job
 */
/**
 * StatefulJob接口。
 */
public interface StatefulJob {
}
