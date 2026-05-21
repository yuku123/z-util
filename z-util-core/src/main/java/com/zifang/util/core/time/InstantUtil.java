package com.zifang.util.core.time;

import com.zifang.util.core.time.converter.TimeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * java.time.Instant 工具类
 * <p>
 * 只处理Instant类型，提供Instant的格式化、解析、计算、转换等功能
 */
public class InstantUtil {

    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    public static final DateTimeFormatter FMT_DEFAULT = DateTimeFormatter.ofPattern(PATTERN_DEFAULT);
    public static final DateTimeFormatter FMT_DATE = DateTimeFormatter.ofPattern(PATTERN_DATE);

    // ==================== 格式化 ====================

    public static String format(Instant instant) {
        return format(instant, FMT_DEFAULT);
    }

    public static String format(Instant instant, String pattern) {
        if (instant == null) return null;
        return format(instant, DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(Instant instant, DateTimeFormatter formatter) {
        if (instant == null) return null;
        return formatter.format(instant.atZone(ZoneId.systemDefault()));
    }

    public static String format(Instant instant, ZoneId zoneId, String pattern) {
        if (instant == null) return null;
        return formatter(zoneId, pattern).format(instant.atZone(zoneId));
    }

    private static DateTimeFormatter formatter(ZoneId zoneId, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(zoneId);
    }

    // ==================== 解析 ====================

    public static Instant parse(String dateStr) {
        return parse(dateStr, PATTERN_DEFAULT);
    }

    public static Instant parse(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        return Instant.from(DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).parse(dateStr));
    }

    // ==================== 获取 ====================

    public static Instant now() {
        return Instant.now();
    }

    public static Instant ofEpochSecond(long epochSecond) {
        return Instant.ofEpochSecond(epochSecond);
    }

    public static Instant ofEpochMilli(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli);
    }

    // ==================== 日期时间计算 ====================

    public static Instant plusMillis(Instant instant, long millis) {
        return instant.plusMillis(millis);
    }

    public static Instant minusMillis(Instant instant, long millis) {
        return instant.minusMillis(millis);
    }

    public static Instant plusSeconds(Instant instant, long seconds) {
        return instant.plusSeconds(seconds);
    }

    public static Instant minusSeconds(Instant instant, long seconds) {
        return instant.minusSeconds(seconds);
    }

    public static Instant plusNanos(Instant instant, long nanos) {
        return instant.plusNanos(nanos);
    }

    public static Instant minusNanos(Instant instant, long nanos) {
        return instant.minusNanos(nanos);
    }

    // ==================== 日期时间比较 ====================

    public static boolean isBefore(Instant instant1, Instant instant2) {
        return instant1.isBefore(instant2);
    }

    public static boolean isAfter(Instant instant1, Instant instant2) {
        return instant1.isAfter(instant2);
    }

    public static boolean isEqual(Instant instant1, Instant instant2) {
        return instant1.equals(instant2);
    }

    // ==================== 日期时间差 ====================

    public static long millisBetween(Instant start, Instant end) {
        return java.time.temporal.ChronoUnit.MILLIS.between(start, end);
    }

    public static long secondsBetween(Instant start, Instant end) {
        return java.time.temporal.ChronoUnit.SECONDS.between(start, end);
    }

    public static long nanosBetween(Instant start, Instant end) {
        return java.time.temporal.ChronoUnit.NANOS.between(start, end);
    }

    // ==================== 获取日期时间部分 ====================

    public static long toEpochMilli(Instant instant) {
        if (instant == null) return 0;
        return instant.toEpochMilli();
    }

    public static long toEpochSecond(Instant instant) {
        if (instant == null) return 0;
        return instant.getEpochSecond();
    }

    public static int getNano(Instant instant) {
        return instant.getNano();
    }

    // ==================== 转换到其他类型 ====================

    public static Date toDate(Instant instant) {
        return TimeConverter.toDate(instant);
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return TimeConverter.toLocalDateTime(instant);
    }

    public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zoneId) {
        return TimeConverter.toLocalDateTime(instant, zoneId);
    }

    public static LocalDate toLocalDate(Instant instant) {
        return TimeConverter.toLocalDate(instant);
    }

    public static LocalDate toLocalDate(Instant instant, ZoneId zoneId) {
        return TimeConverter.toLocalDate(instant, zoneId);
    }

    public static LocalTime toLocalTime(Instant instant) {
        return TimeConverter.toLocalTime(instant);
    }

    public static LocalTime toLocalTime(Instant instant, ZoneId zoneId) {
        return TimeConverter.toLocalTime(instant, zoneId);
    }

    public static ZonedDateTime toZonedDateTime(Instant instant) {
        return instant.atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTime(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId);
    }

    // ==================== 从其他类型转换 ====================

    public static Instant fromDate(Date date) {
        return TimeConverter.toInstant(date);
    }

    public static Instant fromLocalDateTime(LocalDateTime localDateTime) {
        return TimeConverter.toInstant(localDateTime);
    }

    public static Instant fromLocalDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
        return TimeConverter.toInstant(localDateTime, zoneId);
    }

    public static Instant fromLocalDate(LocalDate localDate) {
        return TimeConverter.toInstant(localDate);
    }

    public static Instant fromLocalDate(LocalDate localDate, ZoneId zoneId) {
        return TimeConverter.toInstant(localDate, zoneId);
    }

    public static Instant fromLocalTime(LocalTime localTime) {
        return TimeConverter.toInstant(localTime);
    }

    public static Instant fromLocalTime(LocalTime localTime, ZoneId zoneId) {
        return TimeConverter.toInstant(localTime, zoneId);
    }

    public static Instant fromZonedDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant();
    }

    public static Instant fromEpochMilli(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli);
    }

    public static Instant fromEpochSecond(long epochSecond) {
        return Instant.ofEpochSecond(epochSecond);
    }
}
