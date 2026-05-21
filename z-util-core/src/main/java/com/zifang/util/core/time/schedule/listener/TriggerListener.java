package com.zifang.util.core.time.schedule.listener;

import org.quartz.Trigger;

import java.util.Date;

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
     * @param trigger  触发器信息
     * @param context  任务执行上下文
     * @return 是否继续触发任务（返回 false 将阻止任务执行）
     */
    default boolean triggerFired(Trigger trigger, JobExecutionContextWrapper context) {
        return true;
    }

    /**
     * 触发器触发被否决（在 triggerFired 之后，任务执行之前）。
     * <p>
     * 此时任务不会被执行。
     *
     * @param trigger 触发器信息
     */
    default void triggerMisfired(Trigger trigger) {
    }

    /**
     * 触发器触发完成（任务执行完毕后调用，无论成功或失败）。
     *
     * @param trigger       触发器信息
     * @param context       任务执行上下文
     * @param triggerInstructionCode 触发器指令码，表示后续动作
     *                              （如 {@code Trigger.INSTRUCTION_NOOP} 表示无操作）
     */
    default void triggerComplete(Trigger trigger, JobExecutionContextWrapper context,
                                int triggerInstructionCode) {
    }

    /**
     * 触发器将获得下次触发时间。
     * <p>
     * 可用于在触发器获得下次触发时间之前进行干预。
     */
    default Date getNextFireTime(Trigger trigger, Date nextFireTime) {
        return nextFireTime;
    }
}
