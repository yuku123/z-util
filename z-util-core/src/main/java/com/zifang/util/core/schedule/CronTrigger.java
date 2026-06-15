package com.zifang.util.core.schedule;

import org.quartz.CronScheduleBuilder;
import org.quartz.TriggerBuilder;

import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Cron 触发器，使用 cron 表达式定义复杂的调度规则。
 * <p>
 * 对应 Quartz 的 {@link org.quartz.CronTrigger}。
 * <p>
 * cron 表达式格式（从左到右，空格分隔）：
 * <pre>
 * 秒 分 时 日 月 周(星期) [年]
 * </pre>
 * 示例：
 * <ul>
 *   <li>{@code "0 0 8 * * ?"}          — 每天上午 8:00 执行</li>
 *   <li>{@code "0 30 9 ? * MON-FRI"}    — 工作日上午 9:30 执行</li>
 *   <li>{@code "0 0/15 * * * ?"}        — 每 15 分钟执行</li>
 *   <li>{@code "0 0 12 1 * ?"}          — 每月 1 日中午 12:00 执行</li>
 * </ul>
 *
 * @see SimpleTrigger
 * @see CalendarIntervalTrigger
 * @see DailyTimeIntervalTrigger
 * @see TriggerBuilder
 */
public class CronTrigger implements Trigger {

    private final org.quartz.CronTrigger delegate;

    /**
     * CronTrigger方法。
     * * @param delegate org.quartz.CronTrigger类型参数
     */
    public CronTrigger(org.quartz.CronTrigger delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    // ==================== 工厂方法 ====================

    /**
     * 创建新的 CronTrigger Builder。
     */
    public static CronBuilder newCronTrigger() {
        return new CronBuilder();
    }

    // ==================== Trigger 接口实现 ====================

    @Override
    /**
     * getKey方法。
     * @return org.quartz.TriggerKey类型返回值
     */
    public org.quartz.TriggerKey getKey() {
        return delegate.getKey();
    }

    @Override
    /**
     * getName方法。
     * @return String类型返回值
     */
    public String getName() {
        return delegate.getKey().getName();
    }

    @Override
    /**
     * getGroup方法。
     * @return String类型返回值
     */
    public String getGroup() {
        return delegate.getKey().getGroup();
    }

    @Override
    /**
     * getJobKey方法。
     * @return org.quartz.JobKey类型返回值
     */
    public org.quartz.JobKey getJobKey() {
        return delegate.getJobKey();
    }

    @Override
    /**
     * getDescription方法。
     * @return String类型返回值
     */
    public String getDescription() {
        return delegate.getDescription();
    }

    @Override
    /**
     * getNextFireTime方法。
     * @return Date类型返回值
     */
    public Date getNextFireTime() {
        return delegate.getNextFireTime();
    }

    @Override
    /**
     * getPreviousFireTime方法。
     * @return Date类型返回值
     */
    public Date getPreviousFireTime() {
        return delegate.getPreviousFireTime();
    }

    @Override
    /**
     * getPriority方法。
     * @return int类型返回值
     */
    public int getPriority() {
        return delegate.getPriority();
    }

    @Override
    /**
     * getStartTime方法。
     * @return Date类型返回值
     */
    public Date getStartTime() {
        return delegate.getStartTime();
    }

    @Override
    /**
     * getEndTime方法。
     * @return Date类型返回值
     */
    public Date getEndTime() {
        return delegate.getEndTime();
    }

    @Override
    /**
     * getMisfirePolicy方法。
     * @return MisfirePolicy类型返回值
     */
    public MisfirePolicy getMisfirePolicy() {
        int ins = delegate.getMisfireInstruction();
        for (MisfirePolicy p : MisfirePolicy.values()) {
            if (p.toQuartzInstruction() == ins) {
                return p;
            }
        }
        return MisfirePolicy.SMART_POLICY;
    }

    @Override
    /**
     * getCalendarName方法。
     * @return String类型返回值
     */
    public String getCalendarName() {
        return delegate.getCalendarName();
    }

    @Override
    /**
     * getTimeZone方法。
     * @return TimeZone类型返回值
     */
    public TimeZone getTimeZone() {
        return delegate.getTimeZone();
    }

    @Override
    /**
     * getDelegate方法。
     * @return org.quartz.Trigger类型返回值
     */
    public org.quartz.Trigger getDelegate() {
        return delegate;
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "CronTrigger{" +
                "key=" + getKey() +
                ", cronExpression='" + getCronExpression() + '\'' +
                ", nextFireTime=" + getNextFireTime() +
                '}';
    }

    // ==================== 特有属性 ====================

    /**
     * 获取 cron 表达式字符串。
     */
    public String getCronExpression() {
        return delegate.getExpressionSummary();
    }

    // ==================== Builder ====================

    /**
     * CronTrigger 构建器。
     */
    public static class CronBuilder extends Builder<CronBuilder> {

        private String cronExpression;
        private org.quartz.CronExpression cronExpressionObject;

        /**
         * 设置 cron 表达式。
         *
         * @param cronExpression 6-7 位 cron 表达式字符串
         */
        public CronBuilder withCronExpression(String cronExpression) {
            this.cronExpression = Objects.requireNonNull(cronExpression,
                    "cron expression must not be null");
            return this;
        }

        /**
         * 设置 cron 表达式（使用 CronExpression 对象，支持更多特性）。
         */
        public CronBuilder withCronExpressionObject(org.quartz.CronExpression cronExpressionObject) {
            this.cronExpressionObject = Objects.requireNonNull(cronExpressionObject);
            this.cronExpression = cronExpressionObject.getCronExpression();
            return this;
        }

        /**
         * 设置时区。
         */
        @Override
        /**
         * inTimeZone方法。
         *      * @param timeZone TimeZone类型参数
         * @return CronBuilder类型返回值
         */
        public CronBuilder inTimeZone(TimeZone timeZone) {
            super.inTimeZone(timeZone);
            return this;
        }

        @Override
        /**
         * build方法。
         * @return CronTrigger类型返回值
         */
        public CronTrigger build() {
            if (cronExpression == null && cronExpressionObject == null) {
                throw new IllegalStateException(
                        "cronExpression must be set before building CronTrigger. "
                                + "Use withCronExpression(String) method.");
            }

            CronScheduleBuilder scheduleBuilder;
            if (cronExpressionObject != null) {
                scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpressionObject);
            } else {
                scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            }

            // 应用 misfire 策略（使用命名方法）
            if (misfirePolicy != null) {
                switch (misfirePolicy) {
                    case IGNORE_MISFIRE_FIRES_NOW:
                        scheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
                        break;
                    case DO_NOTHING:
                        scheduleBuilder.withMisfireHandlingInstructionDoNothing();
                        break;
                    case FIRE_NOW:
                        scheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
                        break;
                    default:
                        // SMART_POLICY 不需要额外处理
                        break;
                }
            }

            // 时区设置在 CronScheduleBuilder 上
            if (timeZone != null) {
                scheduleBuilder.inTimeZone(timeZone);
            }

            org.quartz.CronTrigger built = TriggerBuilder.newTrigger()
                    .withIdentity(name, group)
                    .withDescription(description)
                    .withPriority(priority)
                    .startAt(startTime != null ? startTime : new Date())
                    .endAt(endTime)
                    .withSchedule(scheduleBuilder)
                    .forJob(jobKey)
                    .modifiedByCalendar(calendarName)
                    .build();

            return new CronTrigger(built);
        }
    }
}
