package com.zifang.util.core.schedule;

import com.zifang.util.core.schedule.listener.ListenerManager;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 调度器管理器，封装 Quartz Scheduler 的核心 API。
 * <p>
 * 这是使用调度功能的主要入口类。通过 {@link SchedulerBuilder} 创建实例。
 * <p>
 * <b>基本使用流程：</b>
 * <pre>
 * // 1. 创建调度器
 * SchedulerManager scheduler = SchedulerBuilder.newScheduler()
 *     .withName("my-scheduler")
 *     .withThreadCount(10)
 *     .build();
 *
 * // 2. 定义任务
 * JobDetail job = JobBuilder.newJob(MyJob.class)
 *     .withName("my-job")
 *     .usingJobData("key", "value")
 *     .build();
 *
 * // 3. 定义触发器
 * Trigger trigger = TriggerBuilder.newSimpleTrigger()
 *     .withName("my-trigger")
 *     .withIntervalInSeconds(5)
 *     .repeatForever()
 *     .build();
 *
 * // 4. 调度任务
 * scheduler.scheduleJob(job, trigger);
 *
 * // 5. 启动
 * scheduler.start();
 * </pre>
 *
 * @see SchedulerBuilder
 * @see JobBuilder
 * @see TriggerBuilder
 * @see Job
 * @see Trigger
 */
public class SchedulerManager {

    private final Scheduler delegate;
    private final ListenerManager listenerManager;

    SchedulerManager(Scheduler delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate must not be null");
        this.listenerManager = new ListenerManager(delegate);
    }

    // ==================== 生命周期 ====================

    /**
     * 启动调度器。
     * <p>
     * 调度器启动后，触发器会按照配置开始触发任务。
     *
     * @throws SchedulerException 启动失败
     */
    public void start() throws SchedulerException {
        if (!delegate.isStarted()) {
            delegate.start();
        }
    }

    /**
     * 关闭调度器。
     * <p>
     * 关闭后，所有正在执行的任务会等待完成。
     *
     * @param waitForJobsComplete 是否等待正在执行的任务完成
     */
    public void shutdown(boolean waitForJobsComplete) {
        try {
            delegate.shutdown(waitForJobsComplete);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to shutdown scheduler", e);
        }
    }

    /**
     * 关闭调度器（默认等待正在执行的任务完成）。
     */
    public void shutdown() {
        shutdown(true);
    }

    /**
     * 判断调度器是否已启动。
     */
    public boolean isStarted() {
        try {
            return delegate.isStarted();
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to check scheduler status", e);
        }
    }

    /**
     * 判断调度器是否已关闭。
     */
    public boolean isShutdown() {
        try {
            return delegate.isShutdown();
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to check scheduler status", e);
        }
    }

    // ==================== 任务调度 ====================

    /**
     * 调度一个任务及其触发器。
     * <p>
     * 如果同名同组的任务已存在，会被替换。
     *
     * @param job     任务描述
     * @param trigger 触发器
     * @return 下次触发时间（如果不再触发则返回 null）
     */
    public Date scheduleJob(JobDetail job, Trigger trigger) {
        try {
            return delegate.scheduleJob(job.getDelegate(), trigger.getDelegate());
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException(
                    "Failed to schedule job: " + job.getKey(), e);
        }
    }

    /**
     * 调度多个任务和触发器（原子操作）。
     *
     * @param jobsAndTriggers Map，key 为 JobDetail，value 为 Trigger 集合
     */
    public void scheduleJobs(Map<JobDetail, Trigger> jobsAndTriggers) {
        try {
            Map<org.quartz.JobDetail, Set<? extends org.quartz.Trigger>> map = new HashMap<>();
            for (Map.Entry<JobDetail, Trigger> entry : jobsAndTriggers.entrySet()) {
                map.put(entry.getKey().getDelegate(),
                        Collections.singleton(entry.getValue().getDelegate()));
            }
            delegate.scheduleJobs(map, true);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to schedule jobs", e);
        }
    }

    /**
     * 添加任务（但不关联任何触发器）。
     * <p>
     * 适用于 {@link JobDetail#isDurable()} 为 true 的持久任务。
     *
     * @param job 任务描述
     */
    public void addJob(JobDetail job) {
        try {
            delegate.addJob(job.getDelegate(), true);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to add job: " + job.getKey(), e);
        }
    }

    /**
     * 添加任务（不设置 replace，意味着如果已存在同名同组任务则抛异常）。
     */
    public void addJob(JobDetail job, boolean replace) {
        try {
            delegate.addJob(job.getDelegate(), replace);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to add job: " + job.getKey(), e);
        }
    }

    /**
     * 删除任务。
     *
     * @param jobKey 任务键
     * @return 是否成功删除（任务不存在也返回 true）
     */
    public boolean deleteJob(JobKey jobKey) {
        try {
            return delegate.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to delete job: " + jobKey, e);
        }
    }

    /**
     * 删除任务（按名称和分组）。
     */
    public boolean deleteJob(String name, String group) {
        return deleteJob(JobKey.jobKey(name, group));
    }

    /**
     * 删除任务（按名称，使用默认分组）。
     */
    public boolean deleteJob(String name) {
        return deleteJob(JobKey.jobKey(name));
    }

    // ==================== 任务查询 ====================

    /**
     * 获取任务详情。
     *
     * @param jobKey 任务键
     * @return 任务详情（如果不存在则返回 null）
     */
    public JobDetail getJob(JobKey jobKey) {
        try {
            org.quartz.JobDetail detail = delegate.getJobDetail(jobKey);
            return detail != null ? new JobDetail(detail) : null;
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to get job: " + jobKey, e);
        }
    }

    /**
     * 获取任务详情（按名称和分组）。
     */
    public JobDetail getJob(String name, String group) {
        return getJob(JobKey.jobKey(name, group));
    }

    /**
     * 获取任务详情（按名称，使用默认分组）。
     */
    public JobDetail getJob(String name) {
        return getJob(JobKey.jobKey(name));
    }

    /**
     * 检查任务是否存在。
     */
    public boolean checkExists(JobKey jobKey) {
        try {
            return delegate.checkExists(jobKey);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to check job existence: " + jobKey, e);
        }
    }

    /**
     * 获取所有任务键。
     */
    public List<JobKey> getJobKeys() {
        try {
            return delegate.getJobKeys(GroupMatcher.anyJobGroup())
                    .stream().collect(Collectors.toList());
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to get job keys", e);
        }
    }

    /**
     * 获取所有分组的任务键。
     */
    public List<String> getJobGroupNames() {
        try {
            return delegate.getJobGroupNames();
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to get job group names", e);
        }
    }

    // ==================== 触发器操作 ====================

    /**
     * 获取触发器。
     */
    public Trigger getTrigger(TriggerKey triggerKey) {
        try {
            org.quartz.Trigger t = delegate.getTrigger(triggerKey);
            return t != null ? wrapTrigger(t) : null;
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to get trigger: " + triggerKey, e);
        }
    }

    /**
     * 获取触发器（按名称和分组）。
     */
    public Trigger getTrigger(String name, String group) {
        return getTrigger(TriggerKey.triggerKey(name, group));
    }

    /**
     * 获取触发器（按名称，使用默认分组）。
     */
    public Trigger getTrigger(String name) {
        return getTrigger(TriggerKey.triggerKey(name));
    }

    /**
     * 获取触发器关联的任务键。
     */
    public JobKey getTriggerJobKey(TriggerKey triggerKey) {
        try {
            org.quartz.Trigger t = delegate.getTrigger(triggerKey);
            if (t == null) {
                throw new SchedulerRuntimeException(
                        "Trigger not found: " + triggerKey);
            }
            return t.getJobKey();
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to get trigger job key: " + triggerKey, e);
        }
    }

    /**
     * 暂停触发器。
     */
    public void pauseTrigger(TriggerKey triggerKey) {
        try {
            delegate.pauseTrigger(triggerKey);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to pause trigger: " + triggerKey, e);
        }
    }

    /**
     * 暂停触发器（按名称和分组）。
     */
    public void pauseTrigger(String name, String group) {
        pauseTrigger(TriggerKey.triggerKey(name, group));
    }

    /**
     * 暂停触发器（按名称，使用默认分组）。
     */
    public void pauseTrigger(String name) {
        pauseTrigger(TriggerKey.triggerKey(name));
    }

    /**
     * 恢复触发器。
     */
    public void resumeTrigger(TriggerKey triggerKey) {
        try {
            delegate.resumeTrigger(triggerKey);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to resume trigger: " + triggerKey, e);
        }
    }

    /**
     * 恢复触发器（按名称和分组）。
     */
    public void resumeTrigger(String name, String group) {
        resumeTrigger(TriggerKey.triggerKey(name, group));
    }

    /**
     * 恢复触发器（按名称，使用默认分组）。
     */
    public void resumeTrigger(String name) {
        resumeTrigger(TriggerKey.triggerKey(name));
    }

    /**
     * 暂停所有触发器。
     */
    public void pauseAllTriggers() {
        try {
            delegate.pauseAll();
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to pause all triggers", e);
        }
    }

    /**
     * 恢复所有触发器。
     */
    public void resumeAllTriggers() {
        try {
            delegate.resumeAll();
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to resume all triggers", e);
        }
    }

    /**
     * 获取所有触发器键。
     */
    public List<TriggerKey> getTriggerKeys() {
        try {
            return delegate.getTriggerKeys(GroupMatcher.anyTriggerGroup())
                    .stream().collect(Collectors.toList());
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to get trigger keys", e);
        }
    }

    /**
     * 获取所有触发器分组。
     */
    public List<String> getTriggerGroupNames() {
        try {
            return delegate.getTriggerGroupNames();
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to get trigger group names", e);
        }
    }

    /**
     * 移除触发器（任务保留）。
     *
     * @param triggerKey 触发器键
     * @return 被移除的触发器
     */
    public Trigger unscheduleJob(TriggerKey triggerKey) {
        try {
            org.quartz.Trigger t = delegate.getTrigger(triggerKey);
            if (t == null) {
                return null;
            }
            boolean removed = delegate.unscheduleJob(triggerKey);
            return removed ? wrapTrigger(t) : null;
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to unschedule job: " + triggerKey, e);
        }
    }

    /**
     * 移除触发器（按名称和分组）。
     */
    public Trigger unscheduleJob(String name, String group) {
        return unscheduleJob(TriggerKey.triggerKey(name, group));
    }

    /**
     * 移除触发器（按名称，使用默认分组）。
     */
    public Trigger unscheduleJob(String name) {
        return unscheduleJob(TriggerKey.triggerKey(name));
    }

    /**
     * 重置触发器（删除旧触发器，添加新触发器，任务保持不变）。
     */
    public Date rescheduleJob(TriggerKey triggerKey, Trigger newTrigger) {
        try {
            return delegate.rescheduleJob(triggerKey, newTrigger.getDelegate());
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to reschedule job: " + triggerKey, e);
        }
    }

    // ==================== 任务操作 ====================

    /**
     * 暂停任务（所有触发该任务的触发器都被暂停）。
     */
    public void pauseJob(JobKey jobKey) {
        try {
            delegate.pauseJob(jobKey);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to pause job: " + jobKey, e);
        }
    }

    /**
     * 暂停任务（按名称和分组）。
     */
    public void pauseJob(String name, String group) {
        pauseJob(JobKey.jobKey(name, group));
    }

    /**
     * 暂停任务（按名称，使用默认分组）。
     */
    public void pauseJob(String name) {
        pauseJob(JobKey.jobKey(name));
    }

    /**
     * 恢复任务。
     */
    public void resumeJob(JobKey jobKey) {
        try {
            delegate.resumeJob(jobKey);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to resume job: " + jobKey, e);
        }
    }

    /**
     * 恢复任务（按名称和分组）。
     */
    public void resumeJob(String name, String group) {
        resumeJob(JobKey.jobKey(name, group));
    }

    /**
     * 恢复任务（按名称，使用默认分组）。
     */
    public void resumeJob(String name) {
        resumeJob(JobKey.jobKey(name));
    }

    /**
     * 立即触发一次任务执行（忽略调度器状态，即使暂停也会执行）。
     *
     * @param jobKey 任务键
     * @param data   额外的 JobData（会合并到 JobDataMap）
     */
    public void triggerJob(JobKey jobKey, Map<String, ?> data) {
        try {
            org.quartz.JobDataMap map = new org.quartz.JobDataMap();
            if (data != null) {
                map.putAll(data);
            }
            delegate.triggerJob(jobKey, map);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to trigger job: " + jobKey, e);
        }
    }

    /**
     * 立即触发一次任务执行。
     */
    public void triggerJob(JobKey jobKey) {
        triggerJob(jobKey, null);
    }

    // ==================== 日历 ====================

    /**
     * 添加日历。
     *
     * @param name           日历名称
     * @param calendar       日历实例
     * @param replace        是否替换已存在的同名日历
     * @param updateTriggers 是否更新使用此日历的触发器
     */
    public void addCalendar(String name, ScheduleCalendar calendar,
                            boolean replace, boolean updateTriggers) {
        try {
            delegate.addCalendar(name, calendar.getDelegate(), replace, updateTriggers);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to add calendar: " + name, e);
        }
    }

    /**
     * 添加日历（替换已存在的同名日历，更新触发器）。
     */
    public void addCalendar(String name, ScheduleCalendar calendar) {
        addCalendar(name, calendar, true, true);
    }

    /**
     * 获取日历。
     *
     * @param name 日历名称
     * @return 日历实例（如果不存在则返回 null）
     */
    public ScheduleCalendar getCalendar(String name) {
        try {
            org.quartz.Calendar cal = delegate.getCalendar(name);
            return cal != null ? new ScheduleCalendar(cal) : null;
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to get calendar: " + name, e);
        }
    }

    /**
     * 删除日历。
     */
    public boolean deleteCalendar(String name) {
        try {
            return delegate.deleteCalendar(name);
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to delete calendar: " + name, e);
        }
    }

    /**
     * 获取所有日历名称。
     */
    public List<String> getCalendarNames() {
        try {
            return delegate.getCalendarNames();
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to get calendar names", e);
        }
    }

    // ==================== 监听器 ====================

    /**
     * 获取监听器管理器。
     */
    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    // ==================== 元数据 ====================

    /**
     * 获取调度器元数据。
     */
    public SchedulerMetaData getMetaData() {
        try {
            return delegate.getMetaData();
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to get meta data", e);
        }
    }

    /**
     * 获取调度器名称。
     */
    public String getSchedulerName() {
        try {
            return delegate.getSchedulerName();
        } catch (SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to get scheduler name", e);
        }
    }

    /**
     * 获取原始 Quartz Scheduler 对象。
     * <p>
     * 谨慎使用。仅在需要直接操作 Quartz API 时调用。
     */
    public Scheduler getDelegate() {
        return delegate;
    }

    // ==================== 内部工具 ====================

    private Trigger wrapTrigger(org.quartz.Trigger t) {
        if (t instanceof org.quartz.SimpleTrigger) {
            return new SimpleTrigger((org.quartz.SimpleTrigger) t);
        } else if (t instanceof org.quartz.CronTrigger) {
            return new CronTrigger((org.quartz.CronTrigger) t);
        } else if (t instanceof org.quartz.CalendarIntervalTrigger) {
            return new CalendarIntervalTrigger((org.quartz.CalendarIntervalTrigger) t);
        } else if (t instanceof org.quartz.DailyTimeIntervalTrigger) {
            return new DailyTimeIntervalTrigger((org.quartz.DailyTimeIntervalTrigger) t);
        } else {
            // 未知类型，返回通用包装
            return new GenericTrigger(t);
        }
    }

    /**
     * 通用触发器包装器（用于未知类型的触发器）。
     */
    private static class GenericTrigger implements Trigger {
        private final org.quartz.Trigger delegate;

        GenericTrigger(org.quartz.Trigger delegate) {
            this.delegate = delegate;
        }

        @Override
        public org.quartz.TriggerKey getKey() {
            return delegate.getKey();
        }

        @Override
        public String getName() {
            return delegate.getKey().getName();
        }

        @Override
        public String getGroup() {
            return delegate.getKey().getGroup();
        }

        @Override
        public org.quartz.JobKey getJobKey() {
            return delegate.getJobKey();
        }

        @Override
        public String getDescription() {
            return delegate.getDescription();
        }

        @Override
        public Date getNextFireTime() {
            return delegate.getNextFireTime();
        }

        @Override
        public Date getPreviousFireTime() {
            return delegate.getPreviousFireTime();
        }

        @Override
        public int getPriority() {
            return delegate.getPriority();
        }

        @Override
        public Date getStartTime() {
            return delegate.getStartTime();
        }

        @Override
        public Date getEndTime() {
            return delegate.getEndTime();
        }

        @Override
        public MisfirePolicy getMisfirePolicy() {
            return MisfirePolicy.SMART_POLICY;
        }

        @Override
        public String getCalendarName() {
            return delegate.getCalendarName();
        }

        @Override
        public java.util.TimeZone getTimeZone() {
            if (delegate instanceof org.quartz.CronTrigger) {
                return ((org.quartz.CronTrigger) delegate).getTimeZone();
            }
            return java.util.TimeZone.getDefault();
        }

        @Override
        public org.quartz.Trigger getDelegate() {
            return delegate;
        }

        @Override
        /**
         * toString方法。
         * @return String类型返回值
         */
        public String toString() {
            return "GenericTrigger{key=" + getKey() + "}";
        }
    }
}
