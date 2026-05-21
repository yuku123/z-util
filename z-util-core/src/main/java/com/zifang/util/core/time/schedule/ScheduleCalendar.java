package com.zifang.util.core.time.schedule;

import org.quartz.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Quartz {@link Calendar} 的封装类，支持日期排除规则。
 * <p>
 * 用于从触发器的执行日期中排除特定日期，常用于定义节假日日历。
 * <p>
 * 支持的日历类型：
 * <ul>
 *   <li>{@link #excludeDates(Set)} — 排除指定日期集合</li>
 *   <li>{@link #excludeRange(LocalDate, LocalDate)} — 排除日期区间（含首尾）</li>
 *   <li>{@link #excludeWeekdays(Set)} — 排除一周中的某些天</li>
 *   <li>{@link #chainCalendar(ScheduleCalendar)} — 链接另一个日历</li>
 * </ul>
 * <p>
 * 示例：排除所有周末
 * <pre>
 * ScheduleCalendar holidayCal = ScheduleCalendar.excludeWeekdays(
 *     EnumSet.of(java.util.Calendar.SATURDAY, java.util.Calendar.SUNDAY)
 * );
 * scheduler.addCalendar("holidays", holidayCal, true, true);
 * </pre>
 * <p>
 * 示例：排除特定日期
 * <pre>
 * ScheduleCalendar cal = ScheduleCalendar.excludeDates(
 *     java.util.HashSet<>(Arrays.asList(
 *         LocalDate.of(2026, 1, 1),
 *         LocalDate.of(2026, 5, 1)
 *     ))
 * );
 * </pre>
 *
 * @see SchedulerManager#addCalendar(String, ScheduleCalendar)
 * @see TriggerBuilder#modifiedByCalendar(String)
 */
public class ScheduleCalendar {

    private final org.quartz.Calendar delegate;

    /**
     * 内部构造器。
     */
    ScheduleCalendar(org.quartz.Calendar delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate must not be null");
    }

    // ==================== 工厂方法 ====================

    /**
     * 创建一个排除指定日期集合的日历。
     * <p>
     * 被排除的日期不会触发任何任务执行。
     *
     * @param excludedDates 要排除的日期集合
     */
    public static ScheduleCalendar excludeDates(Set<LocalDate> excludedDates) {
        AnnualCalendar calendar = new AnnualCalendar();
        List<Calendar> exclusions = new ArrayList<>();
        for (LocalDate date : excludedDates) {
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
            exclusions.add(c);
        }
        calendar.setDaysExcluded(exclusions);
        return new ScheduleCalendar(calendar);
    }

    /**
     * 创建一个排除指定日期区间的日历（含首尾）。
     *
     * @param start 区间开始日期（包含）
     * @param end   区间结束日期（包含）
     */
    public static ScheduleCalendar excludeRange(LocalDate start, LocalDate end) {
        Set<LocalDate> dates = new HashSet<>();
        LocalDate cursor = start;
        while (!cursor.isAfter(end)) {
            dates.add(cursor);
            cursor = cursor.plusDays(1);
        }
        return excludeDates(dates);
    }

    /**
     * 创建一个排除指定星期几的日历。
     *
     * @param weekdays 要排除的星期几，使用 {@link java.util.Calendar} 常量
     *                 如 {@code Calendar.MONDAY}, {@code Calendar.TUESDAY} 等
     */
    public static ScheduleCalendar excludeWeekdays(Set<Integer> weekdays) {
        WeeklyCalendar calendar = new WeeklyCalendar();
        for (Integer day : weekdays) {
            int quartzDay = toQuartzDayOfWeek(day);
            calendar.setDayExcluded(quartzDay, true);
        }
        return new ScheduleCalendar(calendar);
    }

    /**
     * 创建一个排除周末（周六、周日）的日历。
     */
    public static ScheduleCalendar excludeWeekends() {
        return excludeWeekdays(EnumSet.of(java.util.Calendar.SATURDAY, java.util.Calendar.SUNDAY));
    }

    /**
     * 创建一个排除除给定日期外的所有日期的日历。
     *
     * @param includedDates 只包含的日期集合（其他日期全部排除）
     */
    public static ScheduleCalendar includeOnly(Set<LocalDate> includedDates) {
        // 创建一个排除所有日期的年历，然后添加例外
        AnnualCalendar calendar = new AnnualCalendar();
        // 先排除所有天
        List<Calendar> emptyList = new ArrayList<>();
        calendar.setDaysExcluded(emptyList);

        // 使用 HolidayCalendar 包含特定日期
        HolidayCalendar holidayCal = new HolidayCalendar();
        for (LocalDate date : includedDates) {
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), 0, 0, 0);
            c.set(java.util.Calendar.MILLISECOND, 0);
            holidayCal.addExcludedDate(c.getTime());
        }
        return new ScheduleCalendar(holidayCal);
    }

    /**
     * 创建一个月度日历，排除指定日期。
     *
     * @param excludedDays 一个月中的日期（如每月 1 日、15 日），使用 1-31
     */
    public static ScheduleCalendar excludeDaysOfMonth(Set<Integer> excludedDays) {
        MonthlyCalendar calendar = new MonthlyCalendar();
        for (Integer day : excludedDays) {
            if (day >= 1 && day <= 31) {
                calendar.setDayExcluded(day - 1, true);
            }
        }
        return new ScheduleCalendar(calendar);
    }

    /**
     * 创建一个空日历（不过滤任何日期）。
     */
    public static ScheduleCalendar none() {
        return new ScheduleCalendar(new BaseCalendar(new java.util.Calendar.Builder().build()));
    }

    // ==================== 操作方法 ====================

    /**
     * 添加另一个日历，形成链式过滤。
     * <p>
     * 新添加的日历在当前日历之后生效。
     *
     * @param calendar 要链接的日历
     * @return 链接后的新日历实例
     */
    public ScheduleCalendar chainCalendar(ScheduleCalendar calendar) {
        org.quartz.Calendar base = delegate;
        org.quartz.Calendar toChain = calendar.getDelegate();
        toChain.setBaseCalendar(base);
        return new ScheduleCalendar(toChain);
    }

    /**
     * 获取底层 Quartz Calendar 对象。
     * <p>
     * 谨慎使用。
     */
    public org.quartz.Calendar getDelegate() {
        return delegate;
    }

    // ==================== 工具方法 ====================

    private static int toQuartzDayOfWeek(int javaUtilDay) {
        // java.util.Calendar: SUNDAY=1, MONDAY=2, ...
        // Quartz/CronExpression: SUNDAY=1, MONDAY=2, ...
        // 两者相同，但月度日历是 0-based
        return javaUtilDay;
    }
}
