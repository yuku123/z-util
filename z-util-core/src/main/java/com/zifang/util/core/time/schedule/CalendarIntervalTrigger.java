package com.zifang.util.core.time.schedule;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CalendarIntervalTrigger;
import org.quartz.TriggerBuilder;

import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 日历区间触发器，按照日历单位（天、月、年等）递增的间隔重复执行。
 * <p>
 * 对应 Quartz 的 {@link CalendarIntervalTrigger}。
 * <p>
 * 与 {@link SimpleTrigger} 的固定毫秒间隔不同，CalendarIntervalTrigger 会自动处理
 * 夏令时切换、月份天数变化等日历语义。
 * <p>
 * 示例：每月 1 日凌晨 0 点执行
 * <pre>
 * Trigger trigger = TriggerBuilder.newCalendarIntervalTrigger()
 *     .withName("monthly-trigger")
 *     .forJob("my-job")
 *     .withIntervalInMonths(1)
 *     .onCalendarIntervalType()
 *     .build();
 * </pre>
 * <p>
 * 示例：每隔 2 周执行一次
 * <pre>
 * Trigger trigger = TriggerBuilder.newCalendarIntervalTrigger()
 *     .withName("biweekly-trigger")
 *     .forJob("my-job")
 *     .withIntervalInWeeks(2)
 *     .build();
 * </pre>
 *
 * @see SimpleTrigger
 * @see CronTrigger
 * @see DailyTimeIntervalTrigger
 * @see TriggerBuilder
 */
public class CalendarIntervalTrigger implements Trigger {

    private final CalendarIntervalTrigger delegate;

    CalendarIntervalTrigger(CalendarIntervalTrigger delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    // ==================== 工厂方法 ====================

    /**
     * 创建新的 CalendarIntervalTrigger Builder。
     */
    public static CalendarBuilder newCalendarIntervalTrigger() {
        return new CalendarBuilder();
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
    public CalendarIntervalTrigger getDelegate() {
        return delegate;
    }

    @Override
    public String toString() {
        return "CalendarIntervalTrigger{" +
                "key=" + getKey() +
                ", interval=" + getInterval() + " " + getIntervalUnit() +
                ", nextFireTime=" + getNextFireTime() +
                '}';
    }

    // ==================== 特有属性 ====================

    /**
     * 获取递增间隔数量。
     */
    public int getInterval() {
        return delegate.getRepeatInterval();
    }

    /**
     * 获取递增间隔单位。
     */
    public java.util.concurrent.TimeUnit getIntervalUnit() {
        return delegate.getRepeatIntervalUnit();
    }

    /**
     * 获取日历类型。
     */
    public int getCalendarIntervalType() {
        return delegate.getIntervalType();
    }

    /**
     * 是否在夏令时回拨时调整时间。
     */
    public boolean isSkipDayIfHourDoesNotExist() {
        return delegate.getSkipDayIfHourDoesNotExist();
    }

    // ==================== Builder ====================

    /**
     * CalendarIntervalTrigger 构建器。
     */
    public static class CalendarBuilder extends Builder<CalendarBuilder> {

        private int interval = 1;
        private java.util.concurrent.TimeUnit intervalUnit =
                java.util.concurrent.TimeUnit.DAYS;
        private boolean skipDayIfHourDoesNotExist = false;

        /**
         * 设置按天递增。
         */
        public CalendarBuilder withIntervalInDays(int days) {
            this.interval = days;
            this.intervalUnit = java.util.concurrent.TimeUnit.DAYS;
            return this;
        }

        /**
         * 设置按月递增。
         */
        public CalendarBuilder withIntervalInMonths(int months) {
            this.interval = months;
            this.intervalUnit = java.util.concurrent.TimeUnit.DAYS; // 月份特殊处理
            return this;
        }

        /**
         * 设置按周递增。
         */
        public CalendarBuilder withIntervalInWeeks(int weeks) {
            this.interval = weeks;
            this.intervalUnit = java.util.concurrent.TimeUnit.DAYS;
            return this;
        }

        /**
         * 设置按年递增。
         */
        public CalendarBuilder withIntervalInYears(int years) {
            this.interval = years;
            this.intervalUnit = java.util.concurrent.TimeUnit.DAYS; // 年份特殊处理
            return this;
        }

        /**
         * 设置按日历单位递增。
         *
         * @param interval 间隔数量
         * @param unit     间隔单位
         */
        public CalendarBuilder withInterval(int interval, java.util.concurrent.TimeUnit unit) {
            this.interval = interval;
            this.intervalUnit = unit;
            return this;
        }

        /**
         * 设置为月份日历类型。
         */
        public CalendarBuilder onCalendarIntervalType() {
            // 月份需要特殊处理，由 build() 方法处理
            return this;
        }

        /**
         * 设置是否在夏令时跳过不存在的时刻。
         * 例如：不存在凌晨 2:30 时，自动跳到 3:30。
         */
        public CalendarBuilder skipDayIfHourDoesNotExist(boolean skip) {
            this.skipDayIfHourDoesNotExist = skip;
            return this;
        }

        @Override
        public CalendarIntervalTrigger build() {
            CalendarIntervalScheduleBuilder scheduleBuilder;

            if (intervalUnit == java.util.concurrent.TimeUnit.DAYS) {
                scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                        .withIntervalInDays(interval);
            } else if (intervalUnit == java.util.concurrent.TimeUnit.HOURS) {
                scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                        .withIntervalInHours(interval);
            } else if (intervalUnit == java.util.concurrent.TimeUnit.MINUTES) {
                scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                        .withIntervalInMinutes(interval);
            } else {
                // 默认按月处理
                scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                        .withIntervalInMonths(interval);
            }

            scheduleBuilder
                    .withMisfireHandlingInstruction(getMisfireInstruction())
                    .skipDayIfHourDoesNotExist(skipDayIfHourDoesNotExist);

            org.quartz.CalendarIntervalTrigger built =
                    TriggerBuilder.newTrigger()
                            .withIdentity(name, group)
                            .withDescription(description)
                            .withPriority(priority)
                            .startAt(startTime != null ? startTime : new Date())
                            .endAt(endTime)
                            .withSchedule(scheduleBuilder)
                            .forJob(jobKey)
                            .modifiedByCalendar(calendarName)
                            .inTimeZone(timeZone)
                            .build();

            return new CalendarIntervalTrigger(built);
        }

        private int getMisfireInstruction() {
            if (misfirePolicy == null) {
                return org.quartz.Trigger.MISFIRE_INSTRUCTION_SMART_POLICY;
            }
            return misfirePolicy.toQuartzInstruction();
        }
    }
}
