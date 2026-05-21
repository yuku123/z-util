package com.zifang.util.core.time.schedule;

import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;

import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 简单触发器，基于固定间隔或固定次数的重复策略。
 * <p>
 * 对应 Quartz 的 {@link SimpleTrigger}。
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
 * <p>
 * 示例：5分钟后执行一次，只执行一次（不需要 repeatForever）
 * <pre>
 * Trigger trigger = TriggerBuilder.newSimpleTrigger()
 *     .withName("my-trigger")
 *     .forJob("my-job")
 *     .startAt(LocalDateTime.now().plusMinutes(5))
 *     .withIntervalInMinutes(1)
 *     .withRepeatCount(0)  // 0 表示不重复，即只执行一次
 *     .build();
 * </pre>
 *
 * @see CronTrigger
 * @see CalendarIntervalTrigger
 * @see DailyTimeIntervalTrigger
 * @see TriggerBuilder
 */
public class SimpleTrigger implements Trigger {

    private final SimpleTrigger delegate;

    SimpleTrigger(SimpleTrigger delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    // ==================== 工厂方法 ====================

    /**
     * 创建新的 SimpleTrigger Builder。
     */
    public static SimpleBuilder newSimpleTrigger() {
        return new SimpleBuilder();
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
    public SimpleTrigger getDelegate() {
        return delegate;
    }

    @Override
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
    public int getRepeatCount() {
        return delegate.getRepeatCount();
    }

    /**
     * 获取重复间隔（毫秒）。
     */
    public long getRepeatInterval() {
        return delegate.getRepeatInterval();
    }

    /**
     * 获取已触发的次数。
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
        private int repeatCount = 0; // 0 = 执行一次

        public SimpleBuilder withIntervalInSeconds(int seconds) {
            this.intervalInMillis = seconds * 1000L;
            return this;
        }

        public SimpleBuilder withIntervalInMinutes(int minutes) {
            this.intervalInMillis = minutes * 60 * 1000L;
            return this;
        }

        public SimpleBuilder withIntervalInHours(int hours) {
            this.intervalInMillis = hours * 3600 * 1000L;
            return this;
        }

        public SimpleBuilder withIntervalInMillis(long millis) {
            this.intervalInMillis = millis;
            return this;
        }

        /**
         * 设置永久重复（无限次）。
         */
        public SimpleBuilder repeatForever() {
            this.repeatCount = SimpleTrigger.REPEAT_INDEFINITELY;
            return this;
        }

        /**
         * 设置重复次数。
         * 0 表示只执行一次；n>0 表示总共执行 n+1 次。
         */
        public SimpleBuilder withRepeatCount(int repeatCount) {
            this.repeatCount = repeatCount;
            return this;
        }

        @Override
        public SimpleTrigger build() {
            org.quartz.SimpleTrigger built = TriggerBuilder.newTrigger()
                    .withIdentity(name, group)
                    .withDescription(description)
                    .withPriority(priority)
                    .startAt(startTime != null ? startTime : new Date())
                    .endAt(endTime)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInMilliseconds(intervalInMillis)
                            .withRepeatCount(repeatCount)
                            .withMisfireHandlingInstruction(getMisfireInstruction()))
                    .forJob(jobKey)
                    .modifiedByCalendar(calendarName)
                    .inTimeZone(timeZone)
                    .build();

            return new SimpleTrigger((org.quartz.SimpleTrigger) built);
        }

        private int getMisfireInstruction() {
            if (misfirePolicy == null) {
                return org.quartz.Trigger.MISFIRE_INSTRUCTION_SMART_POLICY;
            }
            return misfirePolicy.toQuartzInstruction();
        }
    }
}
