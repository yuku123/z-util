package com.zifang.util.core.time.schedule.listener;

import com.zifang.util.core.time.schedule.JobDetail;
import com.zifang.util.core.time.schedule.Trigger;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

/**
 * 调度器监听器接口，对应 Quartz 的 {@link org.quartz.SchedulerListener}。
 * <p>
 * 监听调度器级别的全局事件，包括任务/触发器的增删改、错误等。
 *
 * @see SchedulerManager#getListenerManager()
 * @see JobListener
 * @see TriggerListener
 */
public interface SchedulerListener {

    /**
     * 任务被添加到调度器。
     */
    default void jobAdded(JobDetail jobDetail) {
    }

    /**
     * 任务从调度器中删除。
     */
    default void jobDeleted(JobKey jobKey) {
    }

    /**
     * 任务即将被执行（调度器已发出执行指令）。
     */
    default void jobScheduled(Trigger trigger) {
    }

    /**
     * 任务被从调度器中移除（取消调度）。
     */
    default void jobUnscheduled(TriggerKey triggerKey) {
    }

    /**
     * 触发器被暂停。
     */
    default void triggerPaused(TriggerKey triggerKey) {
    }

    /**
     * 触发器组被暂停。
     */
    default void triggersPaused(String triggerGroup) {
    }

    /**
     * 触发器被恢复。
     */
    default void triggerResumed(TriggerKey triggerKey) {
    }

    /**
     * 触发器组被恢复。
     */
    default void triggersResumed(String triggerGroup) {
    }

    /**
     * 任务被暂停。
     */
    default void jobPaused(JobKey jobKey) {
    }

    /**
     * 任务组被暂停。
     */
    default void jobsPaused(String jobGroup) {
    }

    /**
     * 任务被恢复。
     */
    default void jobResumed(JobKey jobKey) {
    }

    /**
     * 任务组被恢复。
     */
    default void jobsResumed(String jobGroup) {
    }

    /**
     * 调度器中发生错误。
     *
     * @param msg   错误消息
     * @param cause 异常（可能为 null）
     */
    default void schedulerError(String msg, SchedulerException cause) {
    }

    /**
     * 调度器被关闭。
     */
    default void schedulerShutdown() {
    }

    /**
     * 触发器触发完成（所有触发次数用尽后 finalization）。
     */
    default void triggerFinalized(Trigger trigger) {
    }

    /**
     * 调度器的数据（任务和触发器）被清除。
     */
    default void schedulingDataCleared() {
    }

    /**
     * 调度器正在启动。
     */
    default void schedulerStarting() {
    }

    /**
     * 调度器已启动。
     */
    default void schedulerStarted() {
    }

    /**
     * 调度器处于待机状态（暂停状态，可被重启）。
     */
    default void schedulerInStandbyMode() {
    }

    /**
     * 调度器正在关闭。
     */
    default void schedulerShuttingdown() {
    }
}
