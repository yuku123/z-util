package com.zifang.util.core.schedule;

import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;

import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 简单触发器，基于固定间隔或固定次数的重复策略。
 * <p>
 * 对应 Quartz 的 {@link org.quartz.SimpleTrigger}。
 * <p>
 * 示例：每秒执行一次，永远重复
 * <pre>
 * Trigger trigger = TriggerBuilder.newSimpleTrigger()
 *     .withName("my-trigger")
 *     .forJob("my-job")
 *     .withIntervalInSeconds(1)
 *     .repeatForever()
 *     .build();
 * </pre>
 *
 * @see CronTrigger
 * @see CalendarIntervalTrigger
 * @see DailyTimeIntervalTrigger
 * @see TriggerBuilder
 */
/**
 * SimpleTrigger类。
 */
public class SimpleTrigger implements Trigger {

    private final org.quartz.SimpleTrigger delegate;

    /**
     * SimpleTrigger方法。
     *      * @param delegate org.quartz.SimpleTrigger类型参数
     */
    public SimpleTrigger(org.quartz.SimpleTrigger delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    // ==================== 工厂方法 ====================

    /**
     * 创建新的 SimpleTrigger Builder。
     */
    /**
     * newSimpleTrigger方法。
     * @return static SimpleBuilder类型返回值
     */
    public static SimpleBuilder newSimpleTrigger() {
        return new SimpleBuilder();
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
     * SimpleTrigger 不直接暴露时区，返回系统默认时区。
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
        return "SimpleTrigger{" +
                "key=" + getKey() +
                ", nextFireTime=" + getNextFireTime() +
                ", repeatCount=" + delegate.getRepeatCount() +
                ", repeatInterval=" + delegate.getRepeatInterval() +
                '}';
    }

    // ==================== 特有属性 ====================

    /**
     * 获取重复次数。
     * -1 表示无限重复；0 表示不重复；n>0 表示总共执行 n+1 次。
     */
    /**
     * getRepeatCount方法。
     * @return int类型返回值
     */
    public int getRepeatCount() {
        return delegate.getRepeatCount();
    }

    /**
     * 获取重复间隔（毫秒）。
     */
    /**
     * getRepeatInterval方法。
     * @return long类型返回值
     */
    public long getRepeatInterval() {
        return delegate.getRepeatInterval();
    }

    /**
     * 获取已触发的次数。
     */
    /**
     * getTimesTriggered方法。
     * @return int类型返回值
     */
    public int getTimesTriggered() {
        return delegate.getTimesTriggered();
    }

    // ==================== Builder ====================

    /**
     * SimpleTrigger 构建器。
     */
    public static class SimpleBuilder extends Builder<SimpleBuilder> {

        private long intervalInMillis = 0;
        private int repeatCount = 0;

    /**
     * withIntervalInSeconds方法。
     *      * @param seconds int类型参数
     * @return SimpleBuilder类型返回值
     */
        public SimpleBuilder withIntervalInSeconds(int seconds) {
            this.intervalInMillis = seconds * 1000L;
            return this;
        }

    /**
     * withIntervalInMinutes方法。
     *      * @param minutes int类型参数
     * @return SimpleBuilder类型返回值
     */
        public SimpleBuilder withIntervalInMinutes(int minutes) {
            this.intervalInMillis = minutes * 60 * 1000L;
            return this;
        }

    /**
     * withIntervalInHours方法。
     *      * @param hours int类型参数
     * @return SimpleBuilder类型返回值
     */
        public SimpleBuilder withIntervalInHours(int hours) {
            this.intervalInMillis = hours * 3600 * 1000L;
            return this;
        }

    /**
     * withIntervalInMillis方法。
     *      * @param millis long类型参数
     * @return SimpleBuilder类型返回值
     */
        public SimpleBuilder withIntervalInMillis(long millis) {
            this.intervalInMillis = millis;
            return this;
        }

        /**
         * 设置永久重复（无限次）。
         */
    /**
     * repeatForever方法。
     * @return SimpleBuilder类型返回值
     */
        public SimpleBuilder repeatForever() {
            this.repeatCount = org.quartz.SimpleTrigger.REPEAT_INDEFINITELY;
            return this;
        }

        /**
         * 设置重复次数。
         * 0 表示只执行一次；n>0 表示总共执行 n+1 次。
         */
    /**
     * withRepeatCount方法。
     *      * @param repeatCount int类型参数
     * @return SimpleBuilder类型返回值
     */
        public SimpleBuilder withRepeatCount(int repeatCount) {
            this.repeatCount = repeatCount;
            return this;
        }

        @Override
    /**
     * build方法。
     * @return SimpleTrigger类型返回值
     */
        public SimpleTrigger build() {
            SimpleScheduleBuilder sb = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMilliseconds(intervalInMillis)
                    .withRepeatCount(repeatCount);

            // 应用 misfire 策略
            if (misfirePolicy != null) {
                switch (misfirePolicy) {
                    case IGNORE_MISFIRE_FIRES_NOW:
                        sb.withMisfireHandlingInstructionIgnoreMisfires();
                        break;
                    case FIRE_NOW:
                        sb.withMisfireHandlingInstructionFireNow();
                        break;
                    default:
                        // 其余策略暂不单独处理
                        break;
                }
            }

            org.quartz.SimpleTrigger built = TriggerBuilder.newTrigger()
                    .withIdentity(name, group)
                    .withDescription(description)
                    .withPriority(priority)
                    .startAt(startTime != null ? startTime : new Date())
                    .endAt(endTime)
                    .withSchedule(sb)
                    .forJob(jobKey)
                    .modifiedByCalendar(calendarName)
                    .build();

            return new SimpleTrigger(built);
        }
    }
}
