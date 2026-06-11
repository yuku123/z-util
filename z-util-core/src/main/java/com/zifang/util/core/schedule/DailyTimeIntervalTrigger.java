package com.zifang.util.core.schedule;

import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.TriggerBuilder;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

/**
 * 每日时间区间触发器，在每天的指定时间段内按固定间隔重复执行。
 * <p>
 * 对应 Quartz 的 {@link org.quartz.DailyTimeIntervalTrigger}。
 * <p>
 * 与 {@link SimpleTrigger} 不同，DailyTimeIntervalTrigger 可以：
 * <ul>
 *   <li>限定只在每天的某个时间段内触发（如 9:00-18:00）</li>
 *   <li>限定在哪些天触发（如周一到周五）</li>
 *   <li>自动处理夏令时切换</li>
 * </ul>
 *
 * @see SimpleTrigger
 * @see CronTrigger
 * @see CalendarIntervalTrigger
 * @see TriggerBuilder
 */
public class DailyTimeIntervalTrigger implements Trigger {

    private final org.quartz.DailyTimeIntervalTrigger delegate;

    /**
     * DailyTimeIntervalTrigger方法。
     *      * @param delegate org.quartz.DailyTimeIntervalTrigger类型参数
     */
    public DailyTimeIntervalTrigger(org.quartz.DailyTimeIntervalTrigger delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    // ==================== 工厂方法 ====================

    /**
     * 创建新的 DailyTimeIntervalTrigger Builder。
     */
    public static DailyBuilder newDailyTimeIntervalTrigger() {
        return new DailyBuilder();
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

    /**
     * DailyTimeIntervalTrigger 不直接暴露时区，返回系统默认时区。
     * 如需时区支持，请使用 {@link CronTrigger}。
     */
    @Override
    /**
     * getTimeZone方法。
     * @return TimeZone类型返回值
     */
    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
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
        return "DailyTimeIntervalTrigger{" +
                "key=" + getKey() +
                ", startTimeOfDay=" + getStartTimeOfDay() +
                ", endTimeOfDay=" + getEndTimeOfDay() +
                ", interval=" + getRepeatInterval() + " " + getRepeatIntervalUnit() +
                ", nextFireTime=" + getNextFireTime() +
                '}';
    }

    // ==================== 特有属性 ====================

    /**
     * 获取每日开始时间。
     */
    public org.quartz.TimeOfDay getStartTimeOfDay() {
        return delegate.getStartTimeOfDay();
    }

    /**
     * 获取每日结束时间。
     */
    public org.quartz.TimeOfDay getEndTimeOfDay() {
        return delegate.getEndTimeOfDay();
    }

    /**
     * 获取重复间隔数量。
     */
    public int getRepeatInterval() {
        return delegate.getRepeatInterval();
    }

    /**
     * 获取重复间隔单位。
     */
    public org.quartz.DateBuilder.IntervalUnit getRepeatIntervalUnit() {
        return delegate.getRepeatIntervalUnit();
    }

    /**
     * 获取触发在一周中的哪些天。
     */
    public Set<Integer> getDaysOfWeek() {
        return delegate.getDaysOfWeek();
    }

    /**
     * 获取最大触发次数。
     */
    public int getRepeatCount() {
        return delegate.getRepeatCount();
    }

    /**
     * 获取已触发次数。
     */
    public int getTimesTriggered() {
        return delegate.getTimesTriggered();
    }

    // ==================== Builder ====================

    /**
     * DailyTimeIntervalTrigger 构建器。
     */
    public static class DailyBuilder extends Builder<DailyBuilder> {

        private org.quartz.TimeOfDay startTimeOfDay;
        private org.quartz.TimeOfDay endTimeOfDay;
        private int repeatInterval = 1;
        private org.quartz.DateBuilder.IntervalUnit repeatIntervalUnit =
                org.quartz.DateBuilder.IntervalUnit.MINUTE;
        private Set<Integer> daysOfWeek = null; // null 表示每天

        /**
         * 设置每日开始时间。
         */
        public DailyBuilder startingDailyAt(org.quartz.TimeOfDay time) {
            this.startTimeOfDay = Objects.requireNonNull(time);
            return this;
        }

        /**
         * 设置每日结束时间。
         */
        public DailyBuilder endingDailyAt(org.quartz.TimeOfDay time) {
            this.endTimeOfDay = Objects.requireNonNull(time);
            return this;
        }

        /**
         * 设置每日开始时间（小时:分钟:秒）。
         */
        public DailyBuilder startingDailyAt(int hour, int minute, int second) {
            this.startTimeOfDay = org.quartz.TimeOfDay.hourMinuteAndSecondOfDay(hour, minute, second);
            return this;
        }

        /**
         * 设置每日结束时间（小时:分钟:秒）。
         */
        public DailyBuilder endingDailyAt(int hour, int minute, int second) {
            this.endTimeOfDay = org.quartz.TimeOfDay.hourMinuteAndSecondOfDay(hour, minute, second);
            return this;
        }

        /**
         * 设置按秒重复间隔。
         */
        public DailyBuilder withIntervalInSeconds(int seconds) {
            this.repeatInterval = seconds;
            this.repeatIntervalUnit = org.quartz.DateBuilder.IntervalUnit.SECOND;
            return this;
        }

        /**
         * 设置按分钟重复间隔。
         */
        public DailyBuilder withIntervalInMinutes(int minutes) {
            this.repeatInterval = minutes;
            this.repeatIntervalUnit = org.quartz.DateBuilder.IntervalUnit.MINUTE;
            return this;
        }

        /**
         * 设置按小时重复间隔。
         */
        public DailyBuilder withIntervalInHours(int hours) {
            this.repeatInterval = hours;
            this.repeatIntervalUnit = org.quartz.DateBuilder.IntervalUnit.HOUR;
            return this;
        }

        /**
         * 设置按指定单位重复间隔。
         */
        public DailyBuilder withInterval(int interval,
                                          org.quartz.DateBuilder.IntervalUnit unit) {
            this.repeatInterval = interval;
            this.repeatIntervalUnit = unit;
            return this;
        }

        /**
         * 设置在一周的哪些天触发。
         *
         * @param days java.util.Calendar 常量，如 Calendar.MONDAY, Calendar.TUESDAY 等
         */
        @SafeVarargs
    /**
     * onDaysOfTheWeek方法。
     *      * @param days Integer...类型参数
     * @return final DailyBuilder类型返回值
     */
        public final DailyBuilder onDaysOfTheWeek(Integer... days) {
            this.daysOfWeek = new java.util.HashSet<>(java.util.Arrays.asList(days));
            return this;
        }

        /**
         * 设置工作日（周一到周五）。
         */
        public DailyBuilder onWeekdays() {
            this.daysOfWeek = DailyTimeIntervalScheduleBuilder.MONDAY_THROUGH_FRIDAY;
            return this;
        }

        /**
         * 设置周末（周六、周日）。
         */
        public DailyBuilder onWeekendDays() {
            this.daysOfWeek = DailyTimeIntervalScheduleBuilder.SATURDAY_AND_SUNDAY;
            return this;
        }

        /**
         * 设置每天（默认）。
         */
        public DailyBuilder onEveryDay() {
            this.daysOfWeek = null;
            return this;
        }

        @Override
    /**
     * build方法。
     * @return DailyTimeIntervalTrigger类型返回值
     */
        public DailyTimeIntervalTrigger build() {
            DailyTimeIntervalScheduleBuilder sb =
                    DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule()
                            .withInterval(repeatInterval, repeatIntervalUnit);

            // 开始时间
            if (startTimeOfDay != null) {
                sb.startingDailyAt(startTimeOfDay);
            }

            // 结束时间
            if (endTimeOfDay != null) {
                sb.endingDailyAt(endTimeOfDay);
            }

            // 触发日期
            if (daysOfWeek != null && !daysOfWeek.isEmpty()) {
                sb.onDaysOfTheWeek(daysOfWeek);
            }

            // 应用 misfire 策略
            if (misfirePolicy != null) {
                switch (misfirePolicy) {
                    case IGNORE_MISFIRE_FIRES_NOW:
                        sb.withMisfireHandlingInstructionIgnoreMisfires();
                        break;
                    case DO_NOTHING:
                        sb.withMisfireHandlingInstructionDoNothing();
                        break;
                    case FIRE_NOW:
                        sb.withMisfireHandlingInstructionFireAndProceed();
                        break;
                    default:
                        break;
                }
            }

            org.quartz.DailyTimeIntervalTrigger built =
                    TriggerBuilder.newTrigger()
                            .withIdentity(name, group)
                            .withDescription(description)
                            .withPriority(priority)
                            .startAt(startTime != null ? startTime : new Date())
                            .endAt(endTime)
                            .withSchedule(sb)
                            .forJob(jobKey)
                            .modifiedByCalendar(calendarName)
                            .build();

            return new DailyTimeIntervalTrigger(built);
        }
    }
}
