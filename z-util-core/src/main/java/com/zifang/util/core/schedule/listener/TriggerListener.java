package com.zifang.util.core.schedule.listener;

import com.zifang.util.core.schedule.JobExecutionContextWrapper;
import com.zifang.util.core.schedule.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;

/**
 * 触发器监听器接口，对应 Quartz 的 {@link org.quartz.TriggerListener}。
 * <p>
 * 监听触发器的各个生命周期阶段。
 *
 * @see SchedulerManager#getListenerManager()
 * @see JobListener
 * @see SchedulerListener
 */
public interface TriggerListener {

    /**
     * 获取监听器名称。
     */
    String getName();

    /**
     * 触发器被触发（即将执行关联的任务）。
     *
     * @param trigger 触发器信息（wrapper）
     * @param context 任务执行上下文
     */
    default void triggerFired(Trigger trigger, JobExecutionContextWrapper context) {
    }

    /**
     * 触发器触发被否决（在 triggerFired 之后，任务执行之前由 Listener 否决）。
     * <p>
     * 如果返回 true，任务将不会执行。
     *
     * @param trigger 触发器信息
     * @param context 任务执行上下文
     * @return 是否否决任务执行（true = 否决，false = 允许执行）
     */
    default boolean vetoJobExecution(Trigger trigger, JobExecutionContextWrapper context) {
        return false;
    }

    /**
     * 触发器错过触发（misfire）。
     *
     * @param trigger 触发器信息
     */
    default void triggerMisfired(Trigger trigger) {
    }

    /**
     * 触发器触发完成。
     *
     * @param trigger     触发器信息
     * @param context     任务执行上下文
     * @param instruction 触发器指令码，表示后续动作
     */
    default void triggerComplete(Trigger trigger, JobExecutionContextWrapper context,
                                 CompletedExecutionInstruction instruction) {
    }
}
