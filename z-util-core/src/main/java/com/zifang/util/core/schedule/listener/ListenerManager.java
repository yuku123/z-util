package com.zifang.util.core.schedule.listener;

import com.zifang.util.core.schedule.JobDetail;
import com.zifang.util.core.schedule.JobExecutionContextWrapper;
import com.zifang.util.core.schedule.MisfirePolicy;
import com.zifang.util.core.schedule.SchedulerRuntimeException;
import org.quartz.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听器管理器，统一管理 JobListener、TriggerListener 和 SchedulerListener。
 * <p>
 * 通过 {@link SchedulerManager#getListenerManager()} 获取实例。
 * <p>
 * 示例：
 * <pre>
 * ListenerManager lm = scheduler.getListenerManager();
 *
 * // 添加 Job 监听器
 * lm.addJobListener(new MyJobListener());
 *
 * // 添加 Trigger 监听器
 * lm.addTriggerListener(new MyTriggerListener());
 *
 * // 添加 Scheduler 监听器
 * lm.addSchedulerListener(new MySchedulerListener());
 *
 * // 添加全局监听器（匹配所有任务/触发器）
 * lm.addJobListener(globalListener, MatcherFactory.anyJobs());
 * </pre>
 *
 * @see SchedulerManager#getListenerManager()
 */
/**
 * ListenerManager类。
 */
public class ListenerManager {

    private final org.quartz.Scheduler quartzScheduler;
    private final Map<String, JobListener> jobListeners = new ConcurrentHashMap<>();
    private final Map<String, TriggerListener> triggerListeners = new ConcurrentHashMap<>();
    private final List<SchedulerListener> schedulerListeners = Collections.synchronizedList(new ArrayList<>());

    /**
     * ListenerManager方法。
     *      * @param quartzScheduler org.quartz.Scheduler类型参数
     */
    public ListenerManager(org.quartz.Scheduler quartzScheduler) {
        this.quartzScheduler = Objects.requireNonNull(quartzScheduler);
    }

    // ==================== Job 监听器 ====================

    /**
     * 添加 Job 监听器。
     * <p>
     * 匹配规则：默认匹配所有任务。
     *
     * @param listener Job 监听器
     * @see #addJobListener(JobListener, Matcher)
     */
    /**
     * addJobListener方法。
     *      * @param listener JobListener类型参数
     * @return ListenerManager类型返回值
     */
    public ListenerManager addJobListener(JobListener listener) {
        return addJobListener(listener, Matcher.anyJobs());
    }

    /**
     * 添加带匹配规则的 Job 监听器。
     *
     * @param listener Job 监听器
     * @param matcher  匹配规则，决定监听器作用于哪些任务
     */
    /**
     * addJobListener方法。
     *      * @param listener JobListener类型参数
     * @param matcher Matcher类型参数
     * @return ListenerManager类型返回值
     */
    public ListenerManager addJobListener(JobListener listener, Matcher matcher) {
        Objects.requireNonNull(listener, "listener must not be null");
        Objects.requireNonNull(matcher, "matcher must not be null");

        try {
            org.quartz.impl.matchers.GroupMatcher<JobKey> jobMatcher =
                    toQuartzJobMatcher(matcher);

            quartzScheduler.getListenerManager().addJobListener(
                    new JobListenerAdapter(listener),
                    jobMatcher
            );
            jobListeners.put(listener.getName(), listener);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to add job listener: " + listener.getName(), e);
        }
        return this;
    }

    /**
     * 移除 Job 监听器。
     */
    /**
     * removeJobListener方法。
     *      * @param name String类型参数
     * @return ListenerManager类型返回值
     */
    public ListenerManager removeJobListener(String name) {
        try {
            quartzScheduler.getListenerManager().removeJobListener(name);
            jobListeners.remove(name);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to remove job listener: " + name, e);
        }
        return this;
    }

    /**
     * 获取所有已注册的 Job 监听器。
     */
    /**
     * getJobListeners方法。
     * @return Collection<JobListener>类型返回值
     */
    public Collection<JobListener> getJobListeners() {
        return Collections.unmodifiableCollection(jobListeners.values());
    }

    /**
     * 获取指定名称的 Job 监听器。
     */
    /**
     * getJobListener方法。
     *      * @param name String类型参数
     * @return JobListener类型返回值
     */
    public JobListener getJobListener(String name) {
        return jobListeners.get(name);
    }

    // ==================== Trigger 监听器 ====================

    /**
     * 添加 Trigger 监听器。
     *
     * @param listener Trigger 监听器
     */
    /**
     * addTriggerListener方法。
     *      * @param listener TriggerListener类型参数
     * @return ListenerManager类型返回值
     */
    public ListenerManager addTriggerListener(TriggerListener listener) {
        return addTriggerListener(listener, Matcher.anyTriggers());
    }

    /**
     * 添加带匹配规则的 Trigger 监听器。
     *
     * @param listener Trigger 监听器
     * @param matcher  匹配规则
     */
    /**
     * addTriggerListener方法。
     *      * @param listener TriggerListener类型参数
     * @param matcher Matcher类型参数
     * @return ListenerManager类型返回值
     */
    public ListenerManager addTriggerListener(TriggerListener listener, Matcher matcher) {
        Objects.requireNonNull(listener, "listener must not be null");
        Objects.requireNonNull(matcher, "matcher must not be null");

        try {
            org.quartz.impl.matchers.GroupMatcher<TriggerKey> triggerMatcher =
                    toQuartzTriggerMatcher(matcher);

            quartzScheduler.getListenerManager().addTriggerListener(
                    new TriggerListenerAdapter(listener),
                    triggerMatcher
            );
            triggerListeners.put(listener.getName(), listener);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to add trigger listener: " + listener.getName(), e);
        }
        return this;
    }

    /**
     * 移除 Trigger 监听器。
     */
    /**
     * removeTriggerListener方法。
     *      * @param name String类型参数
     * @return ListenerManager类型返回值
     */
    public ListenerManager removeTriggerListener(String name) {
        try {
            quartzScheduler.getListenerManager().removeTriggerListener(name);
            triggerListeners.remove(name);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to remove trigger listener: " + name, e);
        }
        return this;
    }

    /**
     * 获取所有已注册的 Trigger 监听器。
     */
    /**
     * getTriggerListeners方法。
     * @return Collection<TriggerListener>类型返回值
     */
    public Collection<TriggerListener> getTriggerListeners() {
        return Collections.unmodifiableCollection(triggerListeners.values());
    }

    /**
     * 获取指定名称的 Trigger 监听器。
     */
    /**
     * getTriggerListener方法。
     *      * @param name String类型参数
     * @return TriggerListener类型返回值
     */
    public TriggerListener getTriggerListener(String name) {
        return triggerListeners.get(name);
    }

    // ==================== Scheduler 监听器 ====================

    /**
     * 添加 Scheduler 监听器。
     *
     * @param listener Scheduler 监听器
     */
    /**
     * addSchedulerListener方法。
     *      * @param listener SchedulerListener类型参数
     * @return ListenerManager类型返回值
     */
    public ListenerManager addSchedulerListener(SchedulerListener listener) {
        Objects.requireNonNull(listener, "listener must not be null");
        try {
            quartzScheduler.getListenerManager().addSchedulerListener(
                    new SchedulerListenerAdapter(listener)
            );
            schedulerListeners.add(listener);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to add scheduler listener", e);
        }
        return this;
    }

    /**
     * 移除 Scheduler 监听器。
     */
    /**
     * removeSchedulerListener方法。
     *      * @param listener SchedulerListener类型参数
     * @return ListenerManager类型返回值
     */
    public ListenerManager removeSchedulerListener(SchedulerListener listener) {
        try {
            quartzScheduler.getListenerManager().removeSchedulerListener(
                    new SchedulerListenerAdapter(listener)
            );
            schedulerListeners.remove(listener);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to remove scheduler listener", e);
        }
        return this;
    }

    /**
     * 获取所有已注册的 Scheduler 监听器。
     */
    /**
     * getSchedulerListeners方法。
     * @return List<SchedulerListener>类型返回值
     */
    public List<SchedulerListener> getSchedulerListeners() {
        return Collections.unmodifiableList(new ArrayList<>(schedulerListeners));
    }

    // ==================== 内部适配器 ====================

    private org.quartz.impl.matchers.GroupMatcher<JobKey> toQuartzJobMatcher(Matcher matcher) {
        if (matcher instanceof Matcher.GroupMatcherWrapper) {
            return ((Matcher.GroupMatcherWrapper) matcher).toQuartzJobMatcher();
        }
        // 默认：匹配所有
        return org.quartz.impl.matchers.GroupMatcher.anyJobGroup();
    }

    private org.quartz.impl.matchers.GroupMatcher<TriggerKey> toQuartzTriggerMatcher(Matcher matcher) {
        if (matcher instanceof Matcher.GroupMatcherWrapper) {
            return ((Matcher.GroupMatcherWrapper) matcher).toQuartzTriggerMatcher();
        }
        // 默认：匹配所有
        return org.quartz.impl.matchers.GroupMatcher.anyTriggerGroup();
    }

    // ==================== 内部适配器类 ====================

    private static class JobListenerAdapter implements org.quartz.JobListener {
        private final JobListener delegate;

        JobListenerAdapter(JobListener delegate) {
            this.delegate = delegate;
        }

        @Override
    /**
     * getName方法。
     * @return String类型返回值
     */
        public String getName() {
            return delegate.getName();
        }

        @Override
    /**
     * jobToBeExecuted方法。
     *      * @param context org.quartz.JobExecutionContext类型参数
     */
        public void jobToBeExecuted(org.quartz.JobExecutionContext context) {
            delegate.jobToBeExecuted(new JobExecutionContextWrapper(context));
        }

        @Override
    /**
     * jobExecutionVetoed方法。
     *      * @param context org.quartz.JobExecutionContext类型参数
     */
        public void jobExecutionVetoed(org.quartz.JobExecutionContext context) {
            delegate.jobExecutionVetoed(new JobExecutionContextWrapper(context));
        }

        @Override
    /**
     * jobWasExecuted方法。
     *      * @param context org.quartz.JobExecutionContext类型参数
     * @param exception org.quartz.JobExecutionException类型参数
     */
        public void jobWasExecuted(org.quartz.JobExecutionContext context,
                                   org.quartz.JobExecutionException exception) {
            delegate.jobWasExecuted(new JobExecutionContextWrapper(context), exception);
        }
    }

    private static class TriggerListenerAdapter implements org.quartz.TriggerListener {
        private final TriggerListener delegate;

        TriggerListenerAdapter(TriggerListener delegate) {
            this.delegate = delegate;
        }

        @Override
    /**
     * getName方法。
     * @return String类型返回值
     */
        public String getName() {
            return delegate.getName();
        }

        @Override
    /**
     * triggerFired方法。
     *      * @param trigger org.quartz.Trigger类型参数
     * @param context org.quartz.JobExecutionContext类型参数
     */
        public void triggerFired(org.quartz.Trigger trigger,
                                 org.quartz.JobExecutionContext context) {
            delegate.triggerFired(
                    toWrapper(trigger),
                    new JobExecutionContextWrapper(context));
        }

        @Override
    /**
     * vetoJobExecution方法。
     *      * @param trigger org.quartz.Trigger类型参数
     * @param context org.quartz.JobExecutionContext类型参数
     * @return boolean类型返回值
     */
        public boolean vetoJobExecution(org.quartz.Trigger trigger,
                                        org.quartz.JobExecutionContext context) {
            return delegate.vetoJobExecution(
                    toWrapper(trigger),
                    new JobExecutionContextWrapper(context));
        }

        @Override
    /**
     * triggerMisfired方法。
     *      * @param trigger org.quartz.Trigger类型参数
     */
        public void triggerMisfired(org.quartz.Trigger trigger) {
            delegate.triggerMisfired(toWrapper(trigger));
        }

        @Override
    /**
     * triggerComplete方法。
     *      * @param trigger org.quartz.Trigger类型参数
     * @param context org.quartz.JobExecutionContext类型参数
     * @param instruction org.quartz.Trigger.CompletedExecutionInstruction类型参数
     */
        public void triggerComplete(org.quartz.Trigger trigger,
                                    org.quartz.JobExecutionContext context,
                                    org.quartz.Trigger.CompletedExecutionInstruction instruction) {
            delegate.triggerComplete(
                    toWrapper(trigger),
                    new JobExecutionContextWrapper(context),
                    instruction);
        }

        private com.zifang.util.core.schedule.Trigger toWrapper(org.quartz.Trigger t) {
            // 简单的按类型分发包装，避免循环依赖
            if (t instanceof org.quartz.SimpleTrigger) {
                return new com.zifang.util.core.schedule.SimpleTrigger(
                        (org.quartz.SimpleTrigger) t);
            } else if (t instanceof org.quartz.CronTrigger) {
                return new com.zifang.util.core.schedule.CronTrigger(
                        (org.quartz.CronTrigger) t);
            } else if (t instanceof org.quartz.CalendarIntervalTrigger) {
                return new com.zifang.util.core.schedule.CalendarIntervalTrigger(
                        (org.quartz.CalendarIntervalTrigger) t);
            } else if (t instanceof org.quartz.DailyTimeIntervalTrigger) {
                return new com.zifang.util.core.schedule.DailyTimeIntervalTrigger(
                        (org.quartz.DailyTimeIntervalTrigger) t);
            } else {
                // 通用包装
                return new com.zifang.util.core.schedule.Trigger() {
                    @Override public org.quartz.TriggerKey getKey() { return t.getKey(); }
                    @Override public String getName() { return t.getKey().getName(); }
                    @Override public String getGroup() { return t.getKey().getGroup(); }
                    @Override public org.quartz.JobKey getJobKey() { return t.getJobKey(); }
                    @Override public String getDescription() { return t.getDescription(); }
                    @Override public Date getNextFireTime() { return t.getNextFireTime(); }
                    @Override public Date getPreviousFireTime() { return t.getPreviousFireTime(); }
                    @Override public int getPriority() { return t.getPriority(); }
                    @Override public Date getStartTime() { return t.getStartTime(); }
                    @Override public Date getEndTime() { return t.getEndTime(); }
                    @Override public MisfirePolicy getMisfirePolicy() { return MisfirePolicy.SMART_POLICY; }
                    @Override public String getCalendarName() { return t.getCalendarName(); }
                    @Override public java.util.TimeZone getTimeZone() { return java.util.TimeZone.getDefault(); }
                    @Override public org.quartz.Trigger getDelegate() { return t; }
                    @Override public String toString() { return t.toString(); }
                };
            }
        }
    }

    private static class SchedulerListenerAdapter implements org.quartz.SchedulerListener {
        private final SchedulerListener delegate;

        SchedulerListenerAdapter(SchedulerListener delegate) {
            this.delegate = delegate;
        }

        @Override
    /**
     * jobAdded方法。
     *      * @param jobDetail org.quartz.JobDetail类型参数
     */
        public void jobAdded(org.quartz.JobDetail jobDetail) {
            delegate.jobAdded(new JobDetail(jobDetail));
        }

        @Override
    /**
     * jobDeleted方法。
     *      * @param jobKey org.quartz.JobKey类型参数
     */
        public void jobDeleted(org.quartz.JobKey jobKey) {
            delegate.jobDeleted(jobKey);
        }

        @Override
    /**
     * jobScheduled方法。
     *      * @param trigger org.quartz.Trigger类型参数
     */
        public void jobScheduled(org.quartz.Trigger trigger) {
            delegate.jobScheduled(toWrapper(trigger));
        }

        @Override
    /**
     * jobUnscheduled方法。
     *      * @param triggerKey org.quartz.TriggerKey类型参数
     */
        public void jobUnscheduled(org.quartz.TriggerKey triggerKey) {
            delegate.jobUnscheduled(triggerKey);
        }

        @Override
    /**
     * triggerPaused方法。
     *      * @param triggerKey org.quartz.TriggerKey类型参数
     */
        public void triggerPaused(org.quartz.TriggerKey triggerKey) {
            delegate.triggerPaused(triggerKey);
        }

        @Override
    /**
     * triggersPaused方法。
     *      * @param triggerGroup String类型参数
     */
        public void triggersPaused(String triggerGroup) {
            delegate.triggersPaused(triggerGroup);
        }

        @Override
    /**
     * triggerResumed方法。
     *      * @param triggerKey org.quartz.TriggerKey类型参数
     */
        public void triggerResumed(org.quartz.TriggerKey triggerKey) {
            delegate.triggerResumed(triggerKey);
        }

        @Override
    /**
     * triggersResumed方法。
     *      * @param triggerGroup String类型参数
     */
        public void triggersResumed(String triggerGroup) {
            delegate.triggersResumed(triggerGroup);
        }

        @Override
    /**
     * jobPaused方法。
     *      * @param jobKey org.quartz.JobKey类型参数
     */
        public void jobPaused(org.quartz.JobKey jobKey) {
            delegate.jobPaused(jobKey);
        }

        @Override
    /**
     * jobsPaused方法。
     *      * @param jobGroup String类型参数
     */
        public void jobsPaused(String jobGroup) {
            delegate.jobsPaused(jobGroup);
        }

        @Override
    /**
     * jobResumed方法。
     *      * @param jobKey org.quartz.JobKey类型参数
     */
        public void jobResumed(org.quartz.JobKey jobKey) {
            delegate.jobResumed(jobKey);
        }

        @Override
    /**
     * jobsResumed方法。
     *      * @param jobGroup String类型参数
     */
        public void jobsResumed(String jobGroup) {
            delegate.jobsResumed(jobGroup);
        }

        @Override
    /**
     * schedulerError方法。
     *      * @param msg String类型参数
     * @param cause org.quartz.SchedulerException类型参数
     */
        public void schedulerError(String msg, org.quartz.SchedulerException cause) {
            delegate.schedulerError(msg, cause);
        }

        @Override
    /**
     * schedulerShutdown方法。
     */
        public void schedulerShutdown() {
            delegate.schedulerShutdown();
        }

        @Override
    /**
     * triggerFinalized方法。
     *      * @param trigger org.quartz.Trigger类型参数
     */
        public void triggerFinalized(org.quartz.Trigger trigger) {
            delegate.triggerFinalized(toWrapper(trigger));
        }

        @Override
    /**
     * schedulingDataCleared方法。
     */
        public void schedulingDataCleared() {
            delegate.schedulingDataCleared();
        }

        @Override
    /**
     * schedulerStarting方法。
     */
        public void schedulerStarting() {
            delegate.schedulerStarting();
        }

        @Override
    /**
     * schedulerStarted方法。
     */
        public void schedulerStarted() {
            delegate.schedulerStarted();
        }

        @Override
    /**
     * schedulerInStandbyMode方法。
     */
        public void schedulerInStandbyMode() {
            delegate.schedulerInStandbyMode();
        }

        @Override
    /**
     * schedulerShuttingdown方法。
     */
        public void schedulerShuttingdown() {
            delegate.schedulerShuttingdown();
        }

        private com.zifang.util.core.schedule.Trigger toWrapper(org.quartz.Trigger t) {
            if (t instanceof org.quartz.SimpleTrigger) {
                return new com.zifang.util.core.schedule.SimpleTrigger(
                        (org.quartz.SimpleTrigger) t);
            } else if (t instanceof org.quartz.CronTrigger) {
                return new com.zifang.util.core.schedule.CronTrigger(
                        (org.quartz.CronTrigger) t);
            } else if (t instanceof org.quartz.CalendarIntervalTrigger) {
                return new com.zifang.util.core.schedule.CalendarIntervalTrigger(
                        (org.quartz.CalendarIntervalTrigger) t);
            } else if (t instanceof org.quartz.DailyTimeIntervalTrigger) {
                return new com.zifang.util.core.schedule.DailyTimeIntervalTrigger(
                        (org.quartz.DailyTimeIntervalTrigger) t);
            } else {
                return new com.zifang.util.core.schedule.Trigger() {
                    @Override public org.quartz.TriggerKey getKey() { return t.getKey(); }
                    @Override public String getName() { return t.getKey().getName(); }
                    @Override public String getGroup() { return t.getKey().getGroup(); }
                    @Override public org.quartz.JobKey getJobKey() { return t.getJobKey(); }
                    @Override public String getDescription() { return t.getDescription(); }
                    @Override public Date getNextFireTime() { return t.getNextFireTime(); }
                    @Override public Date getPreviousFireTime() { return t.getPreviousFireTime(); }
                    @Override public int getPriority() { return t.getPriority(); }
                    @Override public Date getStartTime() { return t.getStartTime(); }
                    @Override public Date getEndTime() { return t.getEndTime(); }
                    @Override public MisfirePolicy getMisfirePolicy() { return MisfirePolicy.SMART_POLICY; }
                    @Override public String getCalendarName() { return t.getCalendarName(); }
                    @Override public java.util.TimeZone getTimeZone() { return java.util.TimeZone.getDefault(); }
                    @Override public org.quartz.Trigger getDelegate() { return t; }
                    @Override public String toString() { return t.toString(); }
                };
            }
        }
    }
}
