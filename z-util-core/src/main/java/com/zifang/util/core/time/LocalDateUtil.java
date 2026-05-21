package com.zifang.util.core.time;

import com.zifang.util.core.time.converter.TimeConverter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * java.time.LocalDate 工具类
 * <p>
 * 只处理LocalDate类型，提供LocalDate的格式化、解析、计算等功能
 */
public class LocalDateUtil {

    public static final String PATTERN_DEFAULT = "yyyy-MM-dd";
    public static final String PATTERN_COMPACT = "yyyyMMdd";
    public static final String PATTERN_MONTH = "yyyy-MM";
    public static final String PATTERN_MONTH_COMPACT = "yyyyMM";
    public static final String PATTERN_CHINESE = "yyyy年MM月dd日";
    public static final String PATTERN_CHINESE_MONTH = "yyyy年MM月";

    public static final DateTimeFormatter FMT_DEFAULT = DateTimeFormatter.ofPattern(PATTERN_DEFAULT);
    public static final DateTimeFormatter FMT_COMPACT = DateTimeFormatter.ofPattern(PATTERN_COMPACT);
    public static final DateTimeFormatter FMT_MONTH = DateTimeFormatter.ofPattern(PATTERN_MONTH);
    public static final DateTimeFormatter FMT_CHINESE = DateTimeFormatter.ofPattern(PATTERN_CHINESE);

    // ==================== 格式化 ====================

    public static String format(LocalDate date) {
        return format(date, FMT_DEFAULT);
    }

    public static String format(LocalDate date, String pattern) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDate date, DateTimeFormatter formatter) {
        if (date == null) return null;
        return date.format(formatter);
    }

    // ==================== 解析 ====================

    public static LocalDate parse(String dateStr) {
        return parse(dateStr, PATTERN_DEFAULT);
    }

    public static LocalDate parse(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate parseStrict(String dateStr, String... patterns) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        for (String pattern : patterns) {
            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
            } catch (Exception e) {
                // 尝试下一个模式
            }
        }
        return null;
    }

    // ==================== 获取 ====================

    public static LocalDate now() {
        return LocalDate.now();
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static LocalDate of(int year, int month, int dayOfMonth) {
        return LocalDate.of(year, month, dayOfMonth);
    }

    public static LocalDate ofYearDay(int year, int dayOfYear) {
        return LocalDate.ofYearDay(year, dayOfYear);
    }

    // ==================== 日期计算 ====================

    public static LocalDate plusDays(LocalDate date, long days) {
        return date.plusDays(days);
    }

    public static LocalDate minusDays(LocalDate date, long days) {
        return date.minusDays(days);
    }

    public static LocalDate plusWeeks(LocalDate date, long weeks) {
        return date.plusWeeks(weeks);
    }

    public static LocalDate minusWeeks(LocalDate date, long weeks) {
        return date.minusWeeks(weeks);
    }

    public static LocalDate plusMonths(LocalDate date, long months) {
        return date.plusMonths(months);
    }

    public static LocalDate minusMonths(LocalDate date, long months) {
        return date.minusMonths(months);
    }

    public static LocalDate plusYears(LocalDate date, long years) {
        return date.plusYears(years);
    }

    public static LocalDate minusYears(LocalDate date, long years) {
        return date.minusYears(years);
    }

    // ==================== 日期调整 ====================

    public static LocalDate firstDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate lastDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate firstDayOfYear(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfYear());
    }

    public static LocalDate lastDayOfYear(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfYear());
    }

    public static LocalDate firstDayOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public static LocalDate lastDayOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }

    public static LocalDate nextWorkingDay(LocalDate date) {
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
    }

    // ==================== 日期比较 ====================

    public static boolean isBefore(LocalDate date1, LocalDate date2) {
        return date1.isBefore(date2);
    }

    public static boolean isAfter(LocalDate date1, LocalDate date2) {
        return date1.isAfter(date2);
    }

    public static boolean isEqual(LocalDate date1, LocalDate date2) {
        return date1.isEqual(date2);
    }

    public static boolean isWeekend(LocalDate date) {
        DayOfWeek dow = date.getDayOfWeek();
        return dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;
    }

    public static boolean isLeapYear(LocalDate date) {
        return date.isLeapYear();
    }

    // ==================== 日期差 ====================

    public static long daysBetween(LocalDate start, LocalDate end) {
        return java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }

    public static long weeksBetween(LocalDate start, LocalDate end) {
        return java.time.temporal.ChronoUnit.WEEKS.between(start, end);
    }

    public static long monthsBetween(LocalDate start, LocalDate end) {
        return java.time.temporal.ChronoUnit.MONTHS.between(start, end);
    }

    public static long yearsBetween(LocalDate start, LocalDate end) {
        return java.time.temporal.ChronoUnit.YEARS.between(start, end);
    }

    // ==================== 获取日期部分 ====================

    public static int getYear(LocalDate date) {
        return date.getYear();
    }

    public static int getMonth(LocalDate date) {
        return date.getMonthValue();
    }

    public static int getDayOfMonth(LocalDate date) {
        return date.getDayOfMonth();
    }

    public static int getDayOfYear(LocalDate date) {
        return date.getDayOfYear();
    }

    public static DayOfWeek getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek();
    }

    public static String getWeekdayName(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.CHINESE);
    }

    public static String getMonthName(LocalDate date) {
        return date.getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.CHINESE);
    }

    // ==================== 转换到其他类型 ====================

    public static Date toDate(LocalDate date) {
        return TimeConverter.toDate(date);
    }

    public static LocalDateTime toLocalDateTime(LocalDate date) {
        return TimeConverter.toLocalDateTime(date);
    }

    public static LocalDateTime toLocalDateTime(LocalDate date, LocalTime localTime) {
        return TimeConverter.toLocalDateTime(date, localTime);
    }

    public static Instant toInstant(LocalDate date) {
        return TimeConverter.toInstant(date);
    }

    public static long toEpochMilli(LocalDate date) {
        if (date == null) return 0;
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String toEpochDay(LocalDate date) {
        return String.valueOf(date.toEpochDay());
    }

    // ==================== 从其他类型转换 ====================

    public static LocalDate fromDate(Date date) {
        return TimeConverter.toLocalDate(date);
    }

    public static LocalDate fromLocalDateTime(LocalDateTime localDateTime) {
        return TimeConverter.toLocalDate(localDateTime);
    }

    public static LocalDate fromEpochDay(long epochDay) {
        return LocalDate.ofEpochDay(epochDay);
    }

    public static LocalDate fromInstant(Instant instant) {
        return TimeConverter.toLocalDate(instant);
    }
}