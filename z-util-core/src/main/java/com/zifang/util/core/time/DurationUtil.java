package com.zifang.util.core.time;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Duration 和 Period 工具类
 * <p>
 * Duration 处理时间量（秒、纳秒），Period 处理日期量（年、月、日）
 */
public class DurationUtil {

    // ==================== Duration 创建 ====================

    public static Duration ofMillis(long millis) {
        return Duration.ofMillis(millis);
    }

    public static Duration ofSeconds(long seconds) {
        return Duration.ofSeconds(seconds);
    }

    public static Duration ofMinutes(long minutes) {
        return Duration.ofMinutes(minutes);
    }

    public static Duration ofHours(long hours) {
        return Duration.ofHours(hours);
    }

    public static Duration ofDays(long days) {
        return Duration.ofDays(days);
    }

    public static Duration between(java.time.temporal.Temporal startInclusive, java.time.temporal.Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive);
    }

    // ==================== Duration 格式化 ====================

    public static String format(Duration duration) {
        if (duration == null) return null;
        long seconds = duration.getSeconds();
        long millis = duration.toMillisPart();
        if (millis == 0) {
            return String.format("%d秒", seconds);
        }
        return String.format("%d.%03d秒", seconds, millis);
    }

    public static String formatHMS(Duration duration) {
        if (duration == null) return null;
        long hours = duration.toHours();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String formatDHMS(Duration duration) {
        if (duration == null) return null;
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();
        if (days > 0) {
            return String.format("%d天 %02d:%02d:%02d", days, hours, minutes, seconds);
        }
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // ==================== Duration 计算 ====================

    public static Duration plusMillis(Duration duration, long millis) {
        return duration.plusMillis(millis);
    }

    public static Duration minusMillis(Duration duration, long millis) {
        return duration.minusMillis(millis);
    }

    public static Duration plusSeconds(Duration duration, long seconds) {
        return duration.plusSeconds(seconds);
    }

    public static Duration minusSeconds(Duration duration, long seconds) {
        return duration.minusSeconds(seconds);
    }

    public static Duration multipliedBy(Duration duration, long multiplicand) {
        return duration.multipliedBy(multiplicand);
    }

    public static Duration dividedBy(Duration duration, long divisor) {
        return duration.dividedBy(divisor);
    }

    public static Duration abs(Duration duration) {
        return duration.isNegative() ? duration.negated() : duration;
    }

    // ==================== Duration 比较 ====================

    public static boolean isBefore(Duration duration1, Duration duration2) {
        return duration1.compareTo(duration2) < 0;
    }

    public static boolean isAfter(Duration duration1, Duration duration2) {
        return duration1.compareTo(duration2) > 0;
    }

    public static boolean isNegative(Duration duration) {
        return duration.isNegative();
    }

    public static boolean isZero(Duration duration) {
        return duration.isZero();
    }

    // ==================== Duration 转换 ====================

    public static long toMillis(Duration duration) {
        if (duration == null) return 0;
        return duration.toMillis();
    }

    public static long toSeconds(Duration duration) {
        if (duration == null) return 0;
        return duration.getSeconds();
    }

    public static long toMinutes(Duration duration) {
        if (duration == null) return 0;
        return duration.toMinutes();
    }

    public static long toHours(Duration duration) {
        if (duration == null) return 0;
        return duration.toHours();
    }

    public static long toDays(Duration duration) {
        if (duration == null) return 0;
        return duration.toDays();
    }

    public static long toNanos(Duration duration) {
        if (duration == null) return 0;
        return duration.toNanos();
    }

    // ==================== Duration 与 ChronoUnit 互转 ====================

    public static Duration of(ChronoUnit unit, long amount) {
        return Duration.of(amount, unit);
    }

    public static long toUnit(Duration duration, ChronoUnit unit) {
        return unit.between(java.time.Instant.EPOCH, java.time.Instant.EPOCH.plus(duration));
    }

    // ==================== Period 创建 ====================

    public static Period ofDays(int days) {
        return Period.ofDays(days);
    }

    public static Period ofWeeks(int weeks) {
        return Period.ofWeeks(weeks);
    }

    public static Period ofMonths(int months) {
        return Period.ofMonths(months);
    }

    public static Period ofYears(int years) {
        return Period.ofYears(years);
    }

    public static Period of(int years, int months, int days) {
        return Period.of(years, months, days);
    }

    public static Period between(java.time.LocalDate start, java.time.LocalDate end) {
        return Period.between(start, end);
    }

    // ==================== Period 格式化 ====================

    public static String format(Period period) {
        if (period == null) return null;
        StringBuilder sb = new StringBuilder();
        if (period.getYears() > 0) {
            sb.append(period.getYears()).append("年");
        }
        if (period.getMonths() > 0) {
            sb.append(period.getMonths()).append("月");
        }
        if (period.getDays() > 0) {
            sb.append(period.getDays()).append("日");
        }
        return sb.length() > 0 ? sb.toString() : "0日";
    }

    public static String formatYMD(Period period) {
        if (period == null) return null;
        return String.format("%d年%d月%d日", period.getYears(), period.getMonths(), period.getDays());
    }

    // ==================== Period 计算 ====================

    public static Period plusDays(Period period, int days) {
        return period.plusDays(days);
    }

    public static Period minusDays(Period period, int days) {
        return period.minusDays(days);
    }

    public static Period plusMonths(Period period, int months) {
        return period.plusMonths(months);
    }

    public static Period minusMonths(Period period, int months) {
        return period.minusMonths(months);
    }

    public static Period plusYears(Period period, int years) {
        return period.plusYears(years);
    }

    public static Period minusYears(Period period, int years) {
        return period.minusYears(years);
    }

    public static Period multipliedBy(Period period, int multiplicand) {
        return period.multipliedBy(multiplicand);
    }

    public static Period normalized(Period period) {
        return period.normalized();
    }

    // ==================== Period 转换 ====================

    public static long toDays(Period period) {
        if (period == null) return 0;
        return period.getDays() + (long) period.getMonths() * 30 + (long) period.getYears() * 365;
    }

    public static int getYears(Period period) {
        return period.getYears();
    }

    public static int getMonths(Period period) {
        return period.getMonths();
    }

    public static int getDays(Period period) {
        return period.getDays();
    }
}
