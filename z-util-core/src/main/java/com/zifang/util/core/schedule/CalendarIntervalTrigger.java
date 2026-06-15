package com.zifang.util.core.schedule;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.TriggerBuilder;

import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 日历区间触发器，按照日历单位（天、月、年等）递增的间隔重复执行。
 * <p>
 * 对应 Quartz 的 {@link org.quartz.CalendarIntervalTrigger}。
 * <p>
 * 与 {@link SimpleTrigger} 的固定毫秒间隔不同，CalendarIntervalTrigger 会自动处理
 * 夏令时切换、月份天数变化等日历语义。
 *
 * @see SimpleTrigger
 * @see CronTrigger
 * @see DailyTimeIntervalTrigger
 * @see TriggerBuilder
 */
public class CalendarIntervalTrigger implements Trigger {

    private final org.quartz.CalendarIntervalTrigger delegate;

    /**
     * CalendarIntervalTrigger方法。
     * * @param delegate org.quartz.CalendarIntervalTrigger类型参数
     */
    public CalendarIntervalTrigger(org.quartz.CalendarIntervalTrigger delegate) {
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
    public org.quartz.DateBuilder.IntervalUnit getIntervalUnit() {
        return delegate.getRepeatIntervalUnit();
    }

    /**
     * 是否在夏令时回拨时调整时间。
     */
    public boolean isSkipDayIfHourDoesNotExist() {
        return delegate.isSkipDayIfHourDoesNotExist();
    }

    // ==================== Builder ====================

    /**
     * CalendarIntervalTrigger 构建器。
     */
    public static class CalendarBuilder extends Builder<CalendarBuilder> {

        private int interval = 1;
        private org.quartz.DateBuilder.IntervalUnit intervalUnit =
                org.quartz.DateBuilder.IntervalUnit.DAY;
        private boolean skipDayIfHourDoesNotExist = false;

        /**
         * 设置按天递增。
         */
        public CalendarBuilder withIntervalInDays(int days) {
            this.interval = days;
            this.intervalUnit = org.quartz.DateBuilder.IntervalUnit.DAY;
            return this;
        }

        /**
         * 设置按周递增。
         */
        public CalendarBuilder withIntervalInWeeks(int weeks) {
            this.interval = weeks;
            this.intervalUnit = org.quartz.DateBuilder.IntervalUnit.WEEK;
            return this;
        }

        /**
         * 设置按月递增。
         */
        public CalendarBuilder withIntervalInMonths(int months) {
            this.interval = months;
            this.intervalUnit = org.quartz.DateBuilder.IntervalUnit.MONTH;
            return this;
        }

        /**
         * 设置按年递增。
         */
        public CalendarBuilder withIntervalInYears(int years) {
            this.interval = years;
            this.intervalUnit = org.quartz.DateBuilder.IntervalUnit.YEAR;
            return this;
        }

        /**
         * 设置按指定日历单位递增。
         *
         * @param interval 间隔数量
         * @param unit     间隔单位（非 TimeUnit，而是 Quartz 的 IntervalUnit）
         */
        public CalendarBuilder withInterval(int interval,
                                            org.quartz.DateBuilder.IntervalUnit unit) {
            this.interval = interval;
            this.intervalUnit = unit;
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
        /**
         * build方法。
         * @return CalendarIntervalTrigger类型返回值
         */
        public CalendarIntervalTrigger build() {
            CalendarIntervalScheduleBuilder scheduleBuilder =
                    CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                            .withInterval(interval, intervalUnit)
                            .skipDayIfHourDoesNotExist(skipDayIfHourDoesNotExist);

            // 应用 misfire 策略
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
                        break;
                }
            }

            // 时区设置在 scheduleBuilder 上
            if (timeZone != null) {
                scheduleBuilder.inTimeZone(timeZone);
            }

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
                            .build();

            return new CalendarIntervalTrigger(built);
        }
    }
}
