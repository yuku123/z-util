package com.zifang.util.core.schedule;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 触发器统一构建工厂。
 * <p>
 * 提供静态入口方法创建各种类型触发器的 Builder：
 * <ul>
 *   <li>{@link #newSimpleTrigger()} — 固定间隔或固定次数重复</li>
 *   <li>{@link #newCronTrigger()} — cron 表达式</li>
 *   <li>{@link #newCalendarIntervalTrigger()} — 日历单位递增</li>
 *   <li>{@link #newDailyTimeIntervalTrigger()} — 每日时间段内重复</li>
 * </ul>
 * <p>
 * 示例：
 * <pre>
 * // 简单触发器
 * Trigger t1 = TriggerBuilder.newTrigger()
 *     .withName("t1").forJob("myJob")
 *     .withIntervalInSeconds(5).repeatForever()
 *     .build();
 *
 * // Cron 触发器
 * Trigger t2 = TriggerBuilder.newTrigger()
 *     .withName("t2").forJob("myJob")
 *     .withCronExpression("0 0 8 * * ?")
 *     .build();
 * </pre>
 *
 * @see SimpleTrigger
 * @see CronTrigger
 * @see CalendarIntervalTrigger
 * @see DailyTimeIntervalTrigger
 */
public class TriggerBuilder {

    private TriggerBuilder() {
        // 工具类不实例化
    }

    // ==================== 工厂方法 ====================

    /**
     * 创建简单触发器 Builder。
     * <p>
     * 示例：每 5 秒执行一次
     * <pre>
     * SimpleTrigger trigger = TriggerBuilder.newSimpleTrigger()
     *     .withName("my-trigger")
     *     .forJob("my-job")
     *     .withIntervalInSeconds(5)
     *     .repeatForever()
     *     .build();
     * </pre>
     */
    public static SimpleTrigger.SimpleBuilder newSimpleTrigger() {
        return SimpleTrigger.newSimpleTrigger();
    }

    /**
     * 创建 Cron 触发器 Builder。
     * <p>
     * 示例：工作日每天上午 9:00
     * <pre>
     * CronTrigger trigger = TriggerBuilder.newCronTrigger()
     *     .withName("my-trigger")
     *     .forJob("my-job")
     *     .withCronExpression("0 0 9 ? * MON-FRI")
     *     .build();
     * </pre>
     */
    public static CronTrigger.CronBuilder newCronTrigger() {
        return CronTrigger.newCronTrigger();
    }

    /**
     * 创建日历区间触发器 Builder。
     * <p>
     * 示例：每月 1 日凌晨
     * <pre>
     * CalendarIntervalTrigger trigger = TriggerBuilder.newCalendarIntervalTrigger()
     *     .withName("my-trigger")
     *     .forJob("my-job")
     *     .withIntervalInMonths(1)
     *     .build();
     * </pre>
     */
    public static CalendarIntervalTrigger.CalendarBuilder newCalendarIntervalTrigger() {
        return CalendarIntervalTrigger.newCalendarIntervalTrigger();
    }

    /**
     * 创建每日时间区间触发器 Builder。
     * <p>
     * 示例：工作日上午 9-18 点，每 30 分钟
     * <pre>
     * DailyTimeIntervalTrigger trigger = TriggerBuilder.newDailyTimeIntervalTrigger()
     *     .withName("my-trigger")
     *     .forJob("my-job")
     *     .onWeekdays()
     *     .startingDailyAt(9, 0)
     *     .endingDailyAt(18, 0)
     *     .withIntervalInMinutes(30)
     *     .build();
     * </pre>
     */
    public static DailyTimeIntervalTrigger.DailyBuilder newDailyTimeIntervalTrigger() {
        return DailyTimeIntervalTrigger.newDailyTimeIntervalTrigger();
    }
}
