package com.zifang.util.core.time.schedule;

import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.DailyTimeIntervalTrigger;
import org.quartz.TriggerBuilder;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

/**
 * 每日时间区间触发器，在每天的指定时间段内按固定间隔重复执行。
 * <p>
 * 对应 Quartz 的 {@link DailyTimeIntervalTrigger}。
 * <p>
 * 与 {@link SimpleTrigger} 不同，DailyTimeIntervalTrigger 可以：
 * <ul>
 *   <li>限定只在每天的某个时间段内触发（如 9:00-18:00）</li>
 *   <li>限定在哪些天触发（如周一到周五）</li>
 *   <li>自动处理夏令时切换</li>
 * </ul>
 * <p>
 * 示例：工作日上午 9:00 到 18:00，每 30 分钟执行一次
 * <pre>
 * Trigger trigger = TriggerBuilder.newDailyTimeIntervalTrigger()
 *     .withName("daily-trigger")
 *     .forJob("my-job")
 *     .onDaysOfTheWeek(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY)
 *     .startingDailyAt(LocalTime.of(9, 0))
 *     .endingDailyAt(LocalTime.of(18, 0))
 *     .withIntervalInMinutes(30)
 *     .build();
 * </pre>
 *
 * @see SimpleTrigger
 * @see CronTrigger
 * @see CalendarIntervalTrigger
 * @see TriggerBuilder
 */
public class DailyTimeIntervalTrigger implements Trigger {

    private final DailyTimeIntervalTrigger delegate;

    DailyTimeIntervalTrigger(DailyTimeIntervalTrigger delegate) {
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
    public DailyTimeIntervalTrigger getDelegate() {
        return delegate;
    }

    @Override
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
    public Date getStartTimeOfDay() {
        return delegate.getStartTimeOfDayTime();
    }

    /**
     * 获取每日结束时间。
     */
    public Date getEndTimeOfDay() {
        return delegate.getEndTimeOfDayTime();
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
    public java.util.concurrent.TimeUnit getRepeatIntervalUnit() {
        return delegate.getRepeatIntervalUnit();
    }

    /**
     * 获取触发在一周中的哪些天。
     */
    @SuppressWarnings("unchecked")
    public Set<Integer> getDaysOfWeek() {
        return delegate.getDaysOfWeek();
    }

    /**
     * 获取最大触发次数。
     */
    public int getRepeatCount() {
        return delegate.getRepeatCount();
    }

    // ==================== Builder ====================

    /**
     * DailyTimeIntervalTrigger 构建器。
     */
    public static class DailyBuilder extends Builder<DailyBuilder> {

        private java.time.LocalTime startTimeOfDay = java.time.LocalTime.of(0, 0, 0);
        private java.time.LocalTime endTimeOfDay = java.time.LocalTime.of(23, 59, 59);
        private int repeatInterval = 1;
        private java.util.concurrent.TimeUnit repeatIntervalUnit = java.util.concurrent.TimeUnit.MINUTES;
        private Set<Integer> daysOfWeek = null; // null 表示每天

        /**
         * 设置每日开始时间（LocalTime）。
         */
        public DailyBuilder startingDailyAt(java.time.LocalTime time) {
            this.startTimeOfDay = Objects.requireNonNull(time);
            return this;
        }

        /**
         * 设置每日开始时间（小时:分钟）。
         */
        public DailyBuilder startingDailyAt(int hour, int minute) {
            this.startTimeOfDay = java.time.LocalTime.of(hour, minute);
            return this;
        }

        /**
         * 设置每日结束时间（LocalTime）。
         */
        public DailyBuilder endingDailyAt(java.time.LocalTime time) {
            this.endTimeOfDay = Objects.requireNonNull(time);
            return this;
        }

        /**
         * 设置每日结束时间（小时:分钟）。
         */
        public DailyBuilder endingDailyAt(int hour, int minute) {
            this.endTimeOfDay = java.time.LocalTime.of(hour, minute);
            return this;
        }

        /**
         * 设置按秒重复间隔。
         */
        public DailyBuilder withIntervalInSeconds(int seconds) {
            this.repeatInterval = seconds;
            this.repeatIntervalUnit = java.util.concurrent.TimeUnit.SECONDS;
            return this;
        }

        /**
         * 设置按分钟重复间隔。
         */
        public DailyBuilder withIntervalInMinutes(int minutes) {
            this.repeatInterval = minutes;
            this.repeatIntervalUnit = java.util.concurrent.TimeUnit.MINUTES;
            return this;
        }

        /**
         * 设置按小时重复间隔。
         */
        public DailyBuilder withIntervalInHours(int hours) {
            this.repeatInterval = hours;
            this.repeatIntervalUnit = java.util.concurrent.TimeUnit.HOURS;
            return this;
        }

        /**
         * 设置按指定单位重复间隔。
         */
        public DailyBuilder withInterval(int interval, java.util.concurrent.TimeUnit unit) {
            this.repeatInterval = interval;
            this.repeatIntervalUnit = unit;
            return this;
        }

        /**
         * 设置在一周的哪些天触发。
         *
         * @param days java.util.Calendar 常量，如 Calendar.MONDAY, Calendar.TUESDAY 等
         */
        public DailyBuilder onDaysOfTheWeek(Integer... days) {
            this.daysOfWeek = new java.util.HashSet<>(java.util.Arrays.asList(days));
            return this;
        }

        /**
         * 设置工作日（周一到周五）。
         */
        public DailyBuilder onWeekdays() {
            return onDaysOfTheWeek(
                    java.util.Calendar.MONDAY,
                    java.util.Calendar.TUESDAY,
                    java.util.Calendar.WEDNESDAY,
                    java.util.Calendar.THURSDAY,
                    java.util.Calendar.FRIDAY
            );
        }

        /**
         * 设置周末（周六、周日）。
         */
        public DailyBuilder onWeekendDays() {
            return onDaysOfTheWeek(
                    java.util.Calendar.SATURDAY,
                    java.util.Calendar.SUNDAY
            );
        }

        /**
         * 设置每天。
         */
        public DailyBuilder onEveryDay() {
            this.daysOfWeek = null;
            return this;
        }

        @Override
        public DailyTimeIntervalTrigger build() {
            DailyTimeIntervalScheduleBuilder scheduleBuilder =
                    DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule()
                            .startingDailyAt(
                                    org.quartz.TimeOfDay.dayTime(startTimeOfDay.getHour(),
                                            startTimeOfDay.getMinute(), startTimeOfDay.getSecond()))
                            .endingDailyAt(
                                    org.quartz.TimeOfDay.dayTime(endTimeOfDay.getHour(),
                                            endTimeOfDay.getMinute(), endTimeOfDay.getSecond()))
                            .withIntervalInMinutes(1) // 默认，会被覆盖
                            .withMisfireHandlingInstruction(getMisfireInstruction());

            // 应用间隔配置
            switch (repeatIntervalUnit) {
                case SECONDS:
                    scheduleBuilder.withIntervalInSeconds(repeatInterval);
                    break;
                case MINUTES:
                    scheduleBuilder.withIntervalInMinutes(repeatInterval);
                    break;
                case HOURS:
                    scheduleBuilder.withIntervalInHours(repeatInterval);
                    break;
                default:
                    scheduleBuilder.withIntervalInMinutes(repeatInterval);
            }

            if (daysOfWeek != null && !daysOfWeek.isEmpty()) {
                scheduleBuilder.onDaysOfTheWeek(daysOfWeek);
            }

            org.quartz.DailyTimeIntervalTrigger built =
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

            return new DailyTimeIntervalTrigger(built);
        }

        private int getMisfireInstruction() {
            if (misfirePolicy == null) {
                return org.quartz.Trigger.MISFIRE_INSTRUCTION_SMART_POLICY;
            }
            return misfirePolicy.toQuartzInstruction();
        }
    }
}
