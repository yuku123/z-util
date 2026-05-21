package com.zifang.util.core.time;

import com.zifang.util.core.time.converter.TimeConverter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * java.time.LocalTime 工具类
 * <p>
 * 只处理LocalTime类型，提供LocalTime的格式化、解析、计算等功能
 */
public class LocalTimeUtil {

    public static final String PATTERN_DEFAULT = "HH:mm:ss";
    public static final String PATTERN_COMPACT = "HHmmss";
    public static final String PATTERN_SHORT = "HH:mm";
    public static final String PATTERN_CHINESE = "HH时mm分ss秒";
    public static final String PATTERN_CHINESE_SHORT = "HH时mm分";

    public static final DateTimeFormatter FMT_DEFAULT = DateTimeFormatter.ofPattern(PATTERN_DEFAULT);
    public static final DateTimeFormatter FMT_COMPACT = DateTimeFormatter.ofPattern(PATTERN_COMPACT);
    public static final DateTimeFormatter FMT_SHORT = DateTimeFormatter.ofPattern(PATTERN_SHORT);
    public static final DateTimeFormatter FMT_CHINESE = DateTimeFormatter.ofPattern(PATTERN_CHINESE);

    // ==================== 格式化 ====================

    public static String format(LocalTime time) {
        return format(time, FMT_DEFAULT);
    }

    public static String format(LocalTime time, String pattern) {
        if (time == null) return null;
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalTime time, DateTimeFormatter formatter) {
        if (time == null) return null;
        return time.format(formatter);
    }

    // ==================== 解析 ====================

    public static LocalTime parse(String timeStr) {
        return parse(timeStr, PATTERN_DEFAULT);
    }

    public static LocalTime parse(String timeStr, String pattern) {
        if (timeStr == null || timeStr.trim().isEmpty()) return null;
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    // ==================== 获取 ====================

    public static LocalTime now() {
        return LocalTime.now();
    }

    public static LocalTime of(int hour, int minute) {
        return LocalTime.of(hour, minute);
    }

    public static LocalTime of(int hour, int minute, int second) {
        return LocalTime.of(hour, minute, second);
    }

    public static LocalTime of(int hour, int minute, int second, int nanoOfSecond) {
        return LocalTime.of(hour, minute, second, nanoOfSecond);
    }

    public static LocalTime ofSecondOfDay(long secondOfDay) {
        return LocalTime.ofSecondOfDay(secondOfDay);
    }

    public static LocalTime ofNanoOfDay(long nanoOfDay) {
        return LocalTime.ofNanoOfDay(nanoOfDay);
    }

    public static LocalTime midnight() {
        return LocalTime.MIDNIGHT;
    }

    public static LocalTime noon() {
        return LocalTime.NOON;
    }

    // ==================== 时间计算 ====================

    public static LocalTime plusHours(LocalTime time, long hours) {
        return time.plusHours(hours);
    }

    public static LocalTime minusHours(LocalTime time, long hours) {
        return time.minusHours(hours);
    }

    public static LocalTime plusMinutes(LocalTime time, long minutes) {
        return time.plusMinutes(minutes);
    }

    public static LocalTime minusMinutes(LocalTime time, long minutes) {
        return time.minusMinutes(minutes);
    }

    public static LocalTime plusSeconds(LocalTime time, long seconds) {
        return time.plusSeconds(seconds);
    }

    public static LocalTime minusSeconds(LocalTime time, long seconds) {
        return time.minusSeconds(seconds);
    }

    public static LocalTime plusNanos(LocalTime time, long nanos) {
        return time.plusNanos(nanos);
    }

    public static LocalTime minusNanos(LocalTime time, long nanos) {
        return time.minusNanos(nanos);
    }

    // ==================== 时间比较 ====================

    public static boolean isBefore(LocalTime time1, LocalTime time2) {
        return time1.isBefore(time2);
    }

    public static boolean isAfter(LocalTime time1, LocalTime time2) {
        return time1.isAfter(time2);
    }

    public static boolean isEqual(LocalTime time1, LocalTime time2) {
        return time1.equals(time2);
    }

    // ==================== 时间差 ====================

    public static long hoursBetween(LocalTime start, LocalTime end) {
        return java.time.temporal.ChronoUnit.HOURS.between(start, end);
    }

    public static long minutesBetween(LocalTime start, LocalTime end) {
        return java.time.temporal.ChronoUnit.MINUTES.between(start, end);
    }

    public static long secondsBetween(LocalTime start, LocalTime end) {
        return java.time.temporal.ChronoUnit.SECONDS.between(start, end);
    }

    public static long millisBetween(LocalTime start, LocalTime end) {
        return java.time.temporal.ChronoUnit.MILLIS.between(start, end);
    }

    public static long nanosBetween(LocalTime start, LocalTime end) {
        return java.time.temporal.ChronoUnit.NANOS.between(start, end);
    }

    // ==================== 获取时间部分 ====================

    public static int getHour(LocalTime time) {
        return time.getHour();
    }

    public static int getMinute(LocalTime time) {
        return time.getMinute();
    }

    public static int getSecond(LocalTime time) {
        return time.getSecond();
    }

    public static int getNano(LocalTime time) {
        return time.getNano();
    }

    public static long toSecondOfDay(LocalTime time) {
        return time.toSecondOfDay();
    }

    public static long toNanoOfDay(LocalTime time) {
        return time.toNanoOfDay();
    }

    // ==================== 判断 ====================

    public static boolean isMidnight(LocalTime time) {
        return time.equals(LocalTime.MIDNIGHT);
    }

    public static boolean isNoon(LocalTime time) {
        return time.equals(LocalTime.NOON);
    }

    // ==================== 转换到其他类型 ====================

    public static Date toDate(LocalTime time) {
        if (time == null) return null;
        return TimeConverter.toDate(LocalDate.now().atTime(time));
    }

    public static Instant toInstant(LocalTime time) {
        return TimeConverter.toInstant(time);
    }

    public static LocalDateTime toLocalDateTime(LocalTime time) {
        return TimeConverter.toLocalDateTime(time);
    }

    public static LocalDateTime toLocalDateTime(LocalDate date, LocalTime time) {
        return TimeConverter.toLocalDateTime(date, time);
    }

    // ==================== 从其他类型转换 ====================

    public static LocalTime fromDate(Date date) {
        return TimeConverter.toLocalTime(date);
    }

    public static LocalTime fromInstant(Instant instant) {
        return TimeConverter.toLocalTime(instant);
    }

    public static LocalTime fromLocalDateTime(LocalDateTime localDateTime) {
        return TimeConverter.toLocalTime(localDateTime);
    }

    public static LocalTime fromSecondOfDay(long secondOfDay) {
        return LocalTime.ofSecondOfDay(secondOfDay);
    }
}