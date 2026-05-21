package com.zifang.util.core.time;

import com.zifang.util.core.time.converter.TimeConverter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * java.time.LocalDateTime 工具类
 * <p>
 * 只处理LocalDateTime类型，提供LocalDateTime的格式化、解析、计算等功能
 */
public class LocalDateTimeUtil {

    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_DATETIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_DATETIME_COMPACT = "yyyyMMddHHmmss";
    public static final String PATTERN_ISO = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String PATTERN_ISO_MS = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static final DateTimeFormatter FMT_DEFAULT = DateTimeFormatter.ofPattern(PATTERN_DEFAULT);
    public static final DateTimeFormatter FMT_DATE = DateTimeFormatter.ofPattern(PATTERN_DATE);
    public static final DateTimeFormatter FMT_TIME = DateTimeFormatter.ofPattern(PATTERN_TIME);
    public static final DateTimeFormatter FMT_DATETIME_MS = DateTimeFormatter.ofPattern(PATTERN_DATETIME_MS);

    // ==================== 格式化 ====================

    public static String format(LocalDateTime dateTime) {
        return format(dateTime, FMT_DEFAULT);
    }

    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
        if (dateTime == null) return null;
        return dateTime.format(formatter);
    }

    // ==================== 解析 ====================

    public static LocalDateTime parse(String dateTimeStr) {
        return parse(dateTimeStr, PATTERN_DEFAULT);
    }

    public static LocalDateTime parse(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) return null;
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseStrict(String dateTimeStr, String... patterns) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) return null;
        for (String pattern : patterns) {
            try {
                return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
            } catch (Exception e) {
                // 尝试下一个模式
            }
        }
        return null;
    }

    // ==================== 获取 ====================

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static LocalDateTime todayStart() {
        return LocalDate.now().atStartOfDay();
    }

    public static LocalDateTime todayEnd() {
        return LocalDate.now().atTime(LocalTime.MAX);
    }

    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute);
    }

    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
    }

    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond);
    }

    public static LocalDateTime of(LocalDate date, LocalTime time) {
        return date.atTime(time);
    }

    // ==================== 日期时间计算 ====================

    public static LocalDateTime plusYears(LocalDateTime dateTime, long years) {
        return dateTime.plusYears(years);
    }

    public static LocalDateTime minusYears(LocalDateTime dateTime, long years) {
        return dateTime.minusYears(years);
    }

    public static LocalDateTime plusMonths(LocalDateTime dateTime, long months) {
        return dateTime.plusMonths(months);
    }

    public static LocalDateTime minusMonths(LocalDateTime dateTime, long months) {
        return dateTime.minusMonths(months);
    }

    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        return dateTime.plusDays(days);
    }

    public static LocalDateTime minusDays(LocalDateTime dateTime, long days) {
        return dateTime.minusDays(days);
    }

    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        return dateTime.plusHours(hours);
    }

    public static LocalDateTime minusHours(LocalDateTime dateTime, long hours) {
        return dateTime.minusHours(hours);
    }

    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime.plusMinutes(minutes);
    }

    public static LocalDateTime minusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime.minusMinutes(minutes);
    }

    public static LocalDateTime plusSeconds(LocalDateTime dateTime, long seconds) {
        return dateTime.plusSeconds(seconds);
    }

    public static LocalDateTime minusSeconds(LocalDateTime dateTime, long seconds) {
        return dateTime.minusSeconds(seconds);
    }

    // ==================== 日期调整 ====================

    public static LocalDateTime firstDayOfMonth(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDateTime lastDayOfMonth(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDateTime firstDayOfYear(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfYear());
    }

    public static LocalDateTime lastDayOfYear(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.lastDayOfYear());
    }

    public static LocalDateTime firstDayOfWeek(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public static LocalDateTime lastDayOfWeek(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }

    public static LocalDateTime withHour(LocalDateTime dateTime, int hour) {
        return dateTime.withHour(hour);
    }

    public static LocalDateTime withMinute(LocalDateTime dateTime, int minute) {
        return dateTime.withMinute(minute);
    }

    public static LocalDateTime withSecond(LocalDateTime dateTime, int second) {
        return dateTime.withSecond(second);
    }

    // ==================== 日期时间比较 ====================

    public static boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isBefore(dateTime2);
    }

    public static boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2);
    }

    public static boolean isEqual(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isEqual(dateTime2);
    }

    public static boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate());
    }

    // ==================== 日期时间差 ====================

    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }

    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.HOURS.between(start, end);
    }

    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.MINUTES.between(start, end);
    }

    public static long secondsBetween(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.SECONDS.between(start, end);
    }

    public static long millisBetween(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.MILLIS.between(start, end);
    }

    // ==================== 获取日期时间部分 ====================

    public static int getYear(LocalDateTime dateTime) {
        return dateTime.getYear();
    }

    public static int getMonth(LocalDateTime dateTime) {
        return dateTime.getMonthValue();
    }

    public static int getDayOfMonth(LocalDateTime dateTime) {
        return dateTime.getDayOfMonth();
    }

    public static int getHour(LocalDateTime dateTime) {
        return dateTime.getHour();
    }

    public static int getMinute(LocalDateTime dateTime) {
        return dateTime.getMinute();
    }

    public static int getSecond(LocalDateTime dateTime) {
        return dateTime.getSecond();
    }

    public static DayOfWeek getDayOfWeek(LocalDateTime dateTime) {
        return dateTime.getDayOfWeek();
    }

    // ==================== 转换到其他类型 ====================

    public static Date toDate(LocalDateTime dateTime) {
        return TimeConverter.toDate(dateTime);
    }

    public static Date toDate(LocalDateTime dateTime, ZoneId zoneId) {
        return TimeConverter.toDate(dateTime, zoneId);
    }

    public static LocalDate toLocalDate(LocalDateTime dateTime) {
        return TimeConverter.toLocalDate(dateTime);
    }

    public static LocalTime toLocalTime(LocalDateTime dateTime) {
        return TimeConverter.toLocalTime(dateTime);
    }

    public static Instant toInstant(LocalDateTime dateTime) {
        return TimeConverter.toInstant(dateTime);
    }

    public static Instant toInstant(LocalDateTime dateTime, ZoneId zoneId) {
        return TimeConverter.toInstant(dateTime, zoneId);
    }

    public static long toEpochMilli(LocalDateTime dateTime) {
        return TimeConverter.toEpochMilli(dateTime);
    }

    public static long toEpochSecond(LocalDateTime dateTime) {
        return TimeConverter.toEpochSecond(dateTime);
    }

    public static ZonedDateTime toZonedDateTime(LocalDateTime dateTime) {
        return TimeConverter.toZonedDateTime(dateTime);
    }

    public static ZonedDateTime toZonedDateTime(LocalDateTime dateTime, ZoneId zoneId) {
        return TimeConverter.toZonedDateTime(dateTime, zoneId);
    }

    // ==================== 从其他类型转换 ====================

    public static LocalDateTime fromDate(Date date) {
        return TimeConverter.toLocalDateTime(date);
    }

    public static LocalDateTime fromDate(Date date, ZoneId zoneId) {
        return TimeConverter.toLocalDateTime(date, zoneId);
    }

    public static LocalDateTime fromLocalDate(LocalDate date) {
        return TimeConverter.toLocalDateTime(date);
    }

    public static LocalDateTime fromLocalDate(LocalDate date, LocalTime localTime) {
        return TimeConverter.toLocalDateTime(date, localTime);
    }

    public static LocalDateTime fromLocalTime(LocalTime localTime) {
        return TimeConverter.toLocalDateTime(localTime);
    }

    public static LocalDateTime fromInstant(Instant instant) {
        return TimeConverter.toLocalDateTime(instant);
    }

    public static LocalDateTime fromInstant(Instant instant, ZoneId zoneId) {
        return TimeConverter.toLocalDateTime(instant, zoneId);
    }

    public static LocalDateTime fromEpochMilli(long epochMilli) {
        return TimeConverter.fromEpochMilli(epochMilli);
    }

    public static LocalDateTime fromZonedDateTime(ZonedDateTime zonedDateTime) {
        return TimeConverter.toLocalDateTime(zonedDateTime);
    }
}