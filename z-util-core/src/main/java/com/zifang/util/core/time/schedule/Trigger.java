package com.zifang.util.core.time.schedule;

import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 触发器接口，对应 Quartz 的 {@link Trigger}。
 * <p>
 * 触发器定义了任务的执行时间规则。常见的实现有：
 * <ul>
 *   <li>{@link SimpleTrigger} — 固定间隔或固定次数重复</li>
 *   <li>{@link CronTrigger} — cron 表达式定义复杂调度规则</li>
 *   <li>{@link CalendarIntervalTrigger} — 按日历单位（天、月、年）递增</li>
 *   <li>{@link DailyTimeIntervalTrigger} — 每天指定时间段内按间隔执行</li>
 * </ul>
 * <p>
 * 通过 {@link TriggerBuilder} 构建具体类型的触发器。
 *
 * @see SimpleTrigger
 * @see CronTrigger
 * @see CalendarIntervalTrigger
 * @see DailyTimeIntervalTrigger
 * @see TriggerBuilder
 */
public interface Trigger {

    /**
     * 获取触发器键（名称+分组）。
     */
    TriggerKey getKey();

    /**
     * 获取触发器名称。
     */
    String getName();

    /**
     * 获取触发器分组。
     */
    String getGroup();

    /**
     * 获取关联的任务键。
     */
    org.quartz.JobKey getJobKey();

    /**
     * 获取触发器描述。
     */
    String getDescription();

    /**
     * 获取下次触发时间。如果不再触发则返回 null。
     */
    Date getNextFireTime();

    /**
     * 获取上次触发时间。
     */
    Date getPreviousFireTime();

    /**
     * 获取优先级。多个触发器同时触发时，优先级高的先执行。
     */
    int getPriority();

    /**
     * 获取开始时间。
     */
    Date getStartTime();

    /**
     * 获取结束时间（可能为 null，表示无结束限制）。
     */
    Date getEndTime();

    /**
     * 获取 misfire 策略。
     */
    MisfirePolicy getMisfirePolicy();

    /**
     * 获取日历名称（用于排除特定日期）。
     *
     * @see ScheduleCalendar
     * @see SchedulerManager#addCalendar(String, ScheduleCalendar)
     */
    String getCalendarName();

    /**
     * 获取时区。
     */
    TimeZone getTimeZone();

    /**
     * 获取底层 Quartz Trigger 对象。
     */
    Trigger getDelegate();

    // ==================== 常用时间操作 ====================

    /**
     * 获取下次触发的 LocalDateTime 形式（带系统默认时区）。
     */
    default LocalDateTime getNextFireTimeAsLocalDateTime() {
        Date d = getNextFireTime();
        if (d == null) return null;
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 获取开始时间的 LocalDateTime 形式。
     */
    default LocalDateTime getStartTimeAsLocalDateTime() {
        return getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 获取结束时间的 LocalDateTime 形式。
     */
    default LocalDateTime getEndTimeAsLocalDateTime() {
        Date d = getEndTime();
        if (d == null) return null;
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    String toString();

    // ==================== 内部通用 Builder 基类 ====================

    /**
     * 触发器 Builder 基类，提供通用属性配置。
     */
    abstract class Builder<B extends Builder<B>> {
        protected String name;
        protected String group = Scheduler.DEFAULT_GROUP;
        protected String description;
        protected int priority = Trigger.DEFAULT_PRIORITY;
        protected Date startTime;
        protected Date endTime;
        protected String calendarName;
        protected TimeZone timeZone;
        protected MisfirePolicy misfirePolicy = MisfirePolicy.SMART_POLICY;
        protected org.quartz.JobKey jobKey;

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        /**
         * 设置触发器名称。
         */
        public B withName(String name) {
            this.name = Objects.requireNonNull(name, "trigger name must not be null");
            return self();
        }

        /**
         * 设置触发器分组。
         */
        public B withGroup(String group) {
            this.group = Objects.requireNonNull(group, "trigger group must not be null");
            return self();
        }

        /**
         * 设置触发器描述。
         */
        public B withDescription(String description) {
            this.description = description;
            return self();
        }

        /**
         * 设置优先级。
         */
        public B withPriority(int priority) {
            this.priority = priority;
            return self();
        }

        /**
         * 设置开始时间（java.util.Date）。
         */
        public B startingAt(Date startTime) {
            this.startTime = startTime;
            return self();
        }

        /**
         * 设置开始时间（LocalDateTime，自动转换）。
         */
        public B startingAt(LocalDateTime startTime) {
            this.startTime = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
            return self();
        }

        /**
         * 设置结束时间（java.util.Date）。
         */
        public B endingAt(Date endTime) {
            this.endTime = endTime;
            return self();
        }

        /**
         * 设置结束时间（LocalDateTime，自动转换）。
         */
        public B endingAt(LocalDateTime endTime) {
            this.endTime = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());
            return self();
        }

        /**
         * 关联到指定 Job（通过 JobKey）。
         */
        public B forJob(org.quartz.JobKey jobKey) {
            this.jobKey = jobKey;
            return self();
        }

        /**
         * 关联到指定 Job（通过 JobDetail）。
         */
        public B forJob(JobDetail jobDetail) {
            this.jobKey = jobDetail.getKey();
            return self();
        }

        /**
         * 关联到指定 Job（通过名称+分组）。
         */
        public B forJob(String jobName, String jobGroup) {
            this.jobKey = org.quartz.JobKey.jobKey(jobName, jobGroup);
            return self();
        }

        /**
         * 关联到指定 Job（通过名称，使用默认分组）。
         */
        public B forJob(String jobName) {
            this.jobKey = org.quartz.JobKey.jobKey(jobName);
            return self();
        }

        /**
         * 设置日历名称，用于排除特定日期。
         *
         * @see ScheduleCalendar
         * @see SchedulerManager#addCalendar(String, ScheduleCalendar)
         */
        public B modifiedByCalendar(String calendarName) {
            this.calendarName = calendarName;
            return self();
        }

        /**
         * 设置时区。
         */
        public B inTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
            return self();
        }

        /**
         * 设置时区（通过 ZoneId）。
         */
        public B inTimeZone(ZoneId zoneId) {
            this.timeZone = TimeZone.getTimeZone(zoneId);
            return self();
        }

        /**
         * 设置时区（通过 ZoneId 字符串，如 "Asia/Shanghai"）。
         */
        public B inTimeZone(String zoneId) {
            this.timeZone = TimeZone.getTimeZone(zoneId);
            return self();
        }

        /**
         * 设置 misfire 策略。
         */
        public B withMisfirePolicy(MisfirePolicy misfirePolicy) {
            this.misfirePolicy = misfirePolicy;
            return self();
        }

        /**
         * 构建触发器。子类必须实现。
         */
        public abstract Trigger build();
    }
}
