package com.zifang.util.core.time;

import com.zifang.util.core.time.converter.TimeConverter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * java.time.ZonedDateTime 工具类
 * <p>
 * 只处理ZonedDateTime类型，提供ZonedDateTime的格式化、解析、计算、转换等功能
 */
public class ZonedDateTimeUtil {

    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_DATETIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static final DateTimeFormatter FMT_DEFAULT = DateTimeFormatter.ofPattern(PATTERN_DEFAULT);
    public static final DateTimeFormatter FMT_DATE = DateTimeFormatter.ofPattern(PATTERN_DATE);
    public static final DateTimeFormatter FMT_TIME = DateTimeFormatter.ofPattern(PATTERN_TIME);
    public static final DateTimeFormatter FMT_DATETIME_MS = DateTimeFormatter.ofPattern(PATTERN_DATETIME_MS);

    // ==================== 格式化 ====================

    public static String format(ZonedDateTime dateTime) {
        return format(dateTime, FMT_DEFAULT);
    }

    public static String format(ZonedDateTime dateTime, String pattern) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(ZonedDateTime dateTime, DateTimeFormatter formatter) {
        if (dateTime == null) return null;
        return dateTime.format(formatter);
    }

    public static String format(ZonedDateTime dateTime, ZoneId zoneId, String pattern) {
        if (dateTime == null) return null;
        return dateTime.withZoneSameInstant(zoneId).format(DateTimeFormatter.ofPattern(pattern));
    }

    // ==================== 解析 ====================

    public static ZonedDateTime parse(String dateTimeStr) {
        return parse(dateTimeStr, PATTERN_DEFAULT);
    }

    public static ZonedDateTime parse(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) return null;
        return ZonedDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()));
    }

    public static ZonedDateTime parse(String dateTimeStr, String pattern, ZoneId zoneId) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) return null;
        return ZonedDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern).withZone(zoneId));
    }

    // ==================== 获取 ====================

    public static ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    public static ZonedDateTime now(ZoneId zoneId) {
        return ZonedDateTime.now(zoneId);
    }

    public static ZonedDateTime of(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime.atZone(zoneId);
    }

    public static ZonedDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second, ZoneId zoneId) {
        return ZonedDateTime.of(year, month, dayOfMonth, hour, minute, second, 0, zoneId);
    }

    public static ZonedDateTime ofInstant(Instant instant, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(instant, zoneId);
    }

    // ==================== 日期时间计算 ====================

    public static ZonedDateTime plusYears(ZonedDateTime dateTime, long years) {
        return dateTime.plusYears(years);
    }

    public static ZonedDateTime minusYears(ZonedDateTime dateTime, long years) {
        return dateTime.minusYears(years);
    }

    public static ZonedDateTime plusMonths(ZonedDateTime dateTime, long months) {
        return dateTime.plusMonths(months);
    }

    public static ZonedDateTime minusMonths(ZonedDateTime dateTime, long months) {
        return dateTime.minusMonths(months);
    }

    public static ZonedDateTime plusDays(ZonedDateTime dateTime, long days) {
        return dateTime.plusDays(days);
    }

    public static ZonedDateTime minusDays(ZonedDateTime dateTime, long days) {
        return dateTime.minusDays(days);
    }

    public static ZonedDateTime plusHours(ZonedDateTime dateTime, long hours) {
        return dateTime.plusHours(hours);
    }

    public static ZonedDateTime minusHours(ZonedDateTime dateTime, long hours) {
        return dateTime.minusHours(hours);
    }

    public static ZonedDateTime plusMinutes(ZonedDateTime dateTime, long minutes) {
        return dateTime.plusMinutes(minutes);
    }

    public static ZonedDateTime minusMinutes(ZonedDateTime dateTime, long minutes) {
        return dateTime.minusMinutes(minutes);
    }

    public static ZonedDateTime plusSeconds(ZonedDateTime dateTime, long seconds) {
        return dateTime.plusSeconds(seconds);
    }

    public static ZonedDateTime minusSeconds(ZonedDateTime dateTime, long seconds) {
        return dateTime.minusSeconds(seconds);
    }

    // ==================== 日期调整 ====================

    public static ZonedDateTime firstDayOfMonth(ZonedDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static ZonedDateTime lastDayOfMonth(ZonedDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static ZonedDateTime firstDayOfYear(ZonedDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfYear());
    }

    public static ZonedDateTime lastDayOfYear(ZonedDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.lastDayOfYear());
    }

    public static ZonedDateTime withHour(ZonedDateTime dateTime, int hour) {
        return dateTime.withHour(hour);
    }

    public static ZonedDateTime withMinute(ZonedDateTime dateTime, int minute) {
        return dateTime.withMinute(minute);
    }

    public static ZonedDateTime withSecond(ZonedDateTime dateTime, int second) {
        return dateTime.withSecond(second);
    }

    public static ZonedDateTime withZoneSameInstant(ZonedDateTime dateTime, ZoneId zoneId) {
        return dateTime.withZoneSameInstant(zoneId);
    }

    public static ZonedDateTime withZoneSameLocal(ZonedDateTime dateTime, ZoneId zoneId) {
        return dateTime.withZoneSameLocal(zoneId);
    }

    // ==================== 日期时间比较 ====================

    public static boolean isBefore(ZonedDateTime dateTime1, ZonedDateTime dateTime2) {
        return dateTime1.isBefore(dateTime2);
    }

    public static boolean isAfter(ZonedDateTime dateTime1, ZonedDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2);
    }

    public static boolean isEqual(ZonedDateTime dateTime1, ZonedDateTime dateTime2) {
        return dateTime1.isEqual(dateTime2);
    }

    public static boolean isSameDay(ZonedDateTime dateTime1, ZonedDateTime dateTime2) {
        return dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate());
    }

    // ==================== 日期时间差 ====================

    public static long daysBetween(ZonedDateTime start, ZonedDateTime end) {
        return java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }

    public static long hoursBetween(ZonedDateTime start, ZonedDateTime end) {
        return java.time.temporal.ChronoUnit.HOURS.between(start, end);
    }

    public static long minutesBetween(ZonedDateTime start, ZonedDateTime end) {
        return java.time.temporal.ChronoUnit.MINUTES.between(start, end);
    }

    public static long secondsBetween(ZonedDateTime start, ZonedDateTime end) {
        return java.time.temporal.ChronoUnit.SECONDS.between(start, end);
    }

    // ==================== 获取日期时间部分 ====================

    public static int getYear(ZonedDateTime dateTime) {
        return dateTime.getYear();
    }

    public static int getMonth(ZonedDateTime dateTime) {
        return dateTime.getMonthValue();
    }

    public static int getDayOfMonth(ZonedDateTime dateTime) {
        return dateTime.getDayOfMonth();
    }

    public static int getHour(ZonedDateTime dateTime) {
        return dateTime.getHour();
    }

    public static int getMinute(ZonedDateTime dateTime) {
        return dateTime.getMinute();
    }

    public static int getSecond(ZonedDateTime dateTime) {
        return dateTime.getSecond();
    }

    public static DayOfWeek getDayOfWeek(ZonedDateTime dateTime) {
        return dateTime.getDayOfWeek();
    }

    public static ZoneOffset getOffset(ZonedDateTime dateTime) {
        return dateTime.getOffset();
    }

    public static ZoneId getZone(ZonedDateTime dateTime) {
        return dateTime.getZone();
    }

    // ==================== 转换到其他类型 ====================

    public static Date toDate(ZonedDateTime dateTime) {
        return TimeConverter.toDate(dateTime);
    }

    public static LocalDateTime toLocalDateTime(ZonedDateTime dateTime) {
        return dateTime.toLocalDateTime();
    }

    public static LocalDate toLocalDate(ZonedDateTime dateTime) {
        return dateTime.toLocalDate();
    }

    public static LocalTime toLocalTime(ZonedDateTime dateTime) {
        return dateTime.toLocalTime();
    }

    public static Instant toInstant(ZonedDateTime dateTime) {
        return dateTime.toInstant();
    }

    public static long toEpochMilli(ZonedDateTime dateTime) {
        return dateTime.toInstant().toEpochMilli();
    }

    public static ZonedDateTime toZonedDateTime(ZonedDateTime dateTime, ZoneId zoneId) {
        return dateTime.withZoneSameInstant(zoneId);
    }

    // ==================== 从其他类型转换 ====================

    public static ZonedDateTime fromDate(Date date) {
        return TimeConverter.toZonedDateTime(date);
    }

    public static ZonedDateTime fromDate(Date date, ZoneId zoneId) {
        return TimeConverter.toZonedDateTime(date, zoneId);
    }

    public static ZonedDateTime fromLocalDateTime(LocalDateTime localDateTime) {
        return TimeConverter.toZonedDateTime(localDateTime);
    }

    public static ZonedDateTime fromLocalDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
        return TimeConverter.toZonedDateTime(localDateTime, zoneId);
    }

    public static ZonedDateTime fromInstant(Instant instant) {
        return instant.atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime fromInstant(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId);
    }

    public static ZonedDateTime fromLocalDate(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.systemDefault());
    }

    public static ZonedDateTime fromLocalDate(LocalDate localDate, ZoneId zoneId) {
        return localDate.atStartOfDay(zoneId);
    }

    public static ZonedDateTime fromEpochMilli(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime fromEpochMilli(long epochMilli, ZoneId zoneId) {
        return Instant.ofEpochMilli(epochMilli).atZone(zoneId);
    }
}
