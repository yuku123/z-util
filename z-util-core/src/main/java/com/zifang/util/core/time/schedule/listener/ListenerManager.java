package com.zifang.util.core.time.schedule.listener;

import com.zifang.util.core.time.schedule.JobExecutionContextWrapper;
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
public class ListenerManager {

    private final org.quartz.Scheduler quartzScheduler;
    private final Map<String, JobListener> jobListeners = new ConcurrentHashMap<>();
    private final Map<String, TriggerListener> triggerListeners = new ConcurrentHashMap<>();
    private final List<SchedulerListener> schedulerListeners = Collections.synchronizedList(new ArrayList<>());

    ListenerManager(org.quartz.Scheduler quartzScheduler) {
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
    public ListenerManager addJobListener(JobListener listener) {
        return addJobListener(listener, Matcher.anyJobs());
    }

    /**
     * 添加带匹配规则的 Job 监听器。
     *
     * @param listener Job 监听器
     * @param matcher  匹配规则，决定监听器作用于哪些任务
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
    public Collection<JobListener> getJobListeners() {
        return Collections.unmodifiableCollection(jobListeners.values());
    }

    /**
     * 获取指定名称的 Job 监听器。
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
    public ListenerManager addTriggerListener(TriggerListener listener) {
        return addTriggerListener(listener, Matcher.anyTriggers());
    }

    /**
     * 添加带匹配规则的 Trigger 监听器。
     *
     * @param listener Trigger 监听器
     * @param matcher  匹配规则
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
    public Collection<TriggerListener> getTriggerListeners() {
        return Collections.unmodifiableCollection(triggerListeners.values());
    }

    /**
     * 获取指定名称的 Trigger 监听器。
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
        public String getName() {
            return delegate.getName();
        }

        @Override
        public void jobToBeExecuted(JobExecutionContext context) {
            delegate.jobToBeExecuted(new JobExecutionContextWrapper(context));
        }

        @Override
        public void jobExecutionVetoed(JobExecutionContext context) {
            delegate.jobExecutionVetoed(new JobExecutionContextWrapper(context));
        }

        @Override
        public void jobWasExecuted(JobExecutionContext context, JobExecutionException exception) {
            delegate.jobWasExecuted(new JobExecutionContextWrapper(context), exception);
        }
    }

    private static class TriggerListenerAdapter implements org.quartz.TriggerListener {
        private final TriggerListener delegate;

        TriggerListenerAdapter(TriggerListener delegate) {
            this.delegate = delegate;
        }

        @Override
        public String getName() {
            return delegate.getName();
        }

        @Override
        public void triggerFired(Trigger trigger, JobExecutionContext context) {
            delegate.triggerFired(trigger, new JobExecutionContextWrapper(context));
        }

        @Override
        public void triggerMisfired(Trigger trigger) {
            delegate.triggerMisfired(trigger);
        }

        @Override
        public void triggerComplete(Trigger trigger, JobExecutionContext context, int triggerInstructionCode) {
            delegate.triggerComplete(trigger, new JobExecutionContextWrapper(context), triggerInstructionCode);
        }

        @Override
        public Date getNextFireTime(Trigger trigger, Date nextFireTime) {
            return delegate.getNextFireTime(trigger, nextFireTime);
        }
    }

    private static class SchedulerListenerAdapter implements org.quartz.SchedulerListener {
        private final SchedulerListener delegate;

        SchedulerListenerAdapter(SchedulerListener delegate) {
            this.delegate = delegate;
        }

        @Override
        public void jobAdded(JobDetail jobDetail) {
            delegate.jobAdded(new JobDetail(jobDetail));
        }

        @Override
        public void jobDeleted(JobKey jobKey) {
            delegate.jobDeleted(jobKey);
        }

        @Override
        public void jobScheduled(Trigger trigger) {
            delegate.jobScheduled(trigger);
        }

        @Override
        public void jobUnscheduled(TriggerKey triggerKey) {
            delegate.jobUnscheduled(triggerKey);
        }

        @Override
        public void triggerPaused(TriggerKey triggerKey) {
            delegate.triggerPaused(triggerKey);
        }

        @Override
        public void triggersPaused(String triggerGroup) {
            delegate.triggersPaused(triggerGroup);
        }

        @Override
        public void triggerResumed(TriggerKey triggerKey) {
            delegate.triggerResumed(triggerKey);
        }

        @Override
        public void triggersResumed(String triggerGroup) {
            delegate.triggersResumed(triggerGroup);
        }

        @Override
        public void jobPaused(JobKey jobKey) {
            delegate.jobPaused(jobKey);
        }

        @Override
        public void jobsPaused(String jobGroup) {
            delegate.jobsPaused(jobGroup);
        }

        @Override
        public void jobResumed(JobKey jobKey) {
            delegate.jobResumed(jobKey);
        }

        @Override
        public void jobsResumed(String jobGroup) {
            delegate.jobsResumed(jobGroup);
        }

        @Override
        public void schedulerError(String msg, SchedulerException cause) {
            delegate.schedulerError(msg, cause);
        }

        @Override
        public void schedulerShutdown() {
            delegate.schedulerShutdown();
        }

        @Override
        public void triggerFinalized(Trigger trigger) {
            delegate.triggerFinalized(trigger);
        }

        @Override
        public void triggerAdded(Trigger trigger) {
            delegate.triggerAdded(trigger);
        }

        @Override
        public void schedulerCleared() {
            delegate.schedulerCleared();
        }

        @Override
        public void triggerDoesNotExist(TriggerKey triggerKey) {
            delegate.triggerDoesNotExist(triggerKey);
        }

        @Override
        public void jobDoesNotExist(JobKey jobKey) {
            delegate.jobDoesNotExist(jobKey);
        }

        @Override
        public void triggerPausedNone(TriggerKey triggerKey) {
            delegate.triggerPausedNone(triggerKey);
        }

        @Override
        public void triggersPausedNone(String triggerGroup) {
            delegate.triggersPausedNone(triggerGroup);
        }

        @Override
        public void jobPausedNone(JobKey jobKey) {
            delegate.jobPausedNone(jobKey);
        }

        @Override
        public void jobsPausedNone(String jobGroup) {
            delegate.jobsPausedNone(jobGroup);
        }
    }
}
