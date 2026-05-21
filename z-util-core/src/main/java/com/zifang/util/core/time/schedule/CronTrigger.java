package com.zifang.util.core.time.schedule;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;

import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Cron 触发器，使用 cron 表达式定义复杂的调度规则。
 * <p>
 * 对应 Quartz 的 {@link CronTrigger}。
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
 *   <li>{@code "0 0 10 ? * 6L"}         — 每月最后一个星期五上午 10:00 执行</li>
 * </ul>
 * <p>
 * 示例：
 * <pre>
 * Trigger trigger = TriggerBuilder.newCronTrigger()
 *     .withName("my-cron-trigger")
 *     .forJob("my-job")
 *     .withCronExpression("0 30 9 ? * MON-FRI")
 *     .withMisfirePolicy(MisfirePolicy.DO_NOTHING)
 *     .build();
 * </pre>
 *
 * @see SimpleTrigger
 * @see CalendarIntervalTrigger
 * @see DailyTimeIntervalTrigger
 * @see TriggerBuilder
 */
public class CronTrigger implements Trigger {

    private final CronTrigger delegate;

    CronTrigger(CronTrigger delegate) {
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
        int ins = delegate.getMisfireInstruction();
        for (MisfirePolicy p : MisfirePolicy.values()) {
            if (p.toQuartzInstruction() == ins) {
                return p;
            }
        }
        return MisfirePolicy.SMART_POLICY;
    }

    @Override
    public String getCalendarName() {
        return delegate.getCalendarName();
    }

    @Override
    public TimeZone getTimeZone() {
        return delegate.getTimeZone();
    }

    @Override
    public CronTrigger getDelegate() {
        return delegate;
    }

    @Override
    public String toString() {
        return "CronTrigger{" +
                "key=" + getKey() +
                ", cronExpression='" + getCronExpression() + '\'' +
                ", nextFireTime=" + getNextFireTime() +
                '}';
    }

    // ==================== 特有属性 ====================

    /**
     * 获取 cron 表达式。
     */
    public String getCronExpression() {
        return delegate.getCronExpression();
    }

    /**
     * 获取表达式验证器。
     */
    public org.quartz.CronExpression getCronExpressionObject() {
        return delegate.getCronExpressionObject();
    }

    /**
     * 是否支持秒精度（Quartz 特有）。
     */
    public boolean isSecondBased() {
        return delegate.isSecondBased();
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
         * @param cronExpression 6-7 位 cron 表达式
         */
        public CronBuilder withCronExpression(String cronExpression) {
            this.cronExpression = Objects.requireNonNull(cronExpression, "cron expression must not be null");
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
         * 设置时区，自动更新 cron 表达式的时区。
         */
        @Override
        public CronBuilder inTimeZone(TimeZone timeZone) {
            super.inTimeZone(timeZone);
            return this;
        }

        @Override
        public CronTrigger build() {
            if (cronExpression == null && cronExpressionObject == null) {
                throw new IllegalStateException(
                        "cronExpression must be set before building CronTrigger. " +
                                "Use withCronExpression(String) method.");
            }

            CronScheduleBuilder scheduleBuilder;
            if (cronExpressionObject != null) {
                scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpressionObject);
            } else {
                scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            }

            scheduleBuilder.withMisfireHandling(getMisfireInstruction());

            org.quartz.CronTrigger built = TriggerBuilder.newTrigger()
                    .withIdentity(name, group)
                    .withDescription(description)
                    .withPriority(priority)
                    .startAt(startTime != null ? startTime : new Date())
                    .endAt(endTime)
                    .withSchedule(scheduleBuilder)
                    .forJob(jobKey)
                    .modifiedByCalendar(calendarName)
                    .inTimeZone(timeZone != null ? timeZone : TimeZone.getDefault())
                    .build();

            return new CronTrigger((org.quartz.CronTrigger) built);
        }

        private int getMisfireInstruction() {
            if (misfirePolicy == null) {
                return org.quartz.Trigger.MISFIRE_INSTRUCTION_SMART_POLICY;
            }
            return misfirePolicy.toQuartzInstruction();
        }
    }
}
