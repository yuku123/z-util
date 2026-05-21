package com.zifang.util.core.time.converter;

import java.time.*;
import java.util.Date;

/**
 * 时间类型转换器
 * <p>
 * 提供所有Java时间类型之间的互相转换能力
 */
public class TimeConverter {

    private TimeConverter() {
    }

    // ==================== Date <-> LocalDateTime ====================

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId) {
        if (date == null) return null;
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime, ZoneId zoneId) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    // ==================== Date <-> LocalDate ====================

    public static LocalDate toLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDate toLocalDate(Date date, ZoneId zoneId) {
        if (date == null) return null;
        return date.toInstant().atZone(zoneId).toLocalDate();
    }

    public static Date toDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDate localDate, ZoneId zoneId) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(zoneId).toInstant());
    }

    // ==================== Date <-> LocalTime ====================

    public static LocalTime toLocalTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    public static LocalTime toLocalTime(Date date, ZoneId zoneId) {
        if (date == null) return null;
        return date.toInstant().atZone(zoneId).toLocalTime();
    }

    // ==================== Date <-> Instant ====================

    public static Instant toInstant(Date date) {
        if (date == null) return null;
        return date.toInstant();
    }

    public static Date toDate(Instant instant) {
        if (instant == null) return null;
        return Date.from(instant);
    }

    // ==================== Date <-> ZonedDateTime ====================

    public static ZonedDateTime toZonedDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTime(Date date, ZoneId zoneId) {
        if (date == null) return null;
        return date.toInstant().atZone(zoneId);
    }

    public static Date toDate(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return null;
        return Date.from(zonedDateTime.toInstant());
    }

    // ==================== LocalDateTime <-> LocalDate ====================

    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        if (localDate == null) return null;
        return localDate.atStartOfDay();
    }

    public static LocalDateTime toLocalDateTime(LocalDate localDate, LocalTime localTime) {
        if (localDate == null) return null;
        return localDate.atTime(localTime != null ? localTime : LocalTime.MIDNIGHT);
    }

    // ==================== LocalDateTime <-> LocalTime ====================

    public static LocalTime toLocalTime(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.toLocalTime();
    }

    public static LocalDateTime toLocalDateTime(LocalTime localTime) {
        if (localTime == null) return null;
        return LocalDate.now().atTime(localTime);
    }

    // ==================== LocalDateTime <-> Instant ====================

    public static Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static Instant toInstant(LocalDateTime localDateTime, ZoneId zoneId) {
        if (localDateTime == null) return null;
        return localDateTime.atZone(zoneId).toInstant();
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) return null;
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zoneId) {
        if (instant == null) return null;
        return instant.atZone(zoneId).toLocalDateTime();
    }

    // ==================== LocalDateTime <-> ZonedDateTime ====================

    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
        if (localDateTime == null) return null;
        return localDateTime.atZone(zoneId);
    }

    public static LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return null;
        return zonedDateTime.toLocalDateTime();
    }

    // ==================== LocalDate <-> Instant ====================

    public static Instant toInstant(LocalDate localDate) {
        if (localDate == null) return null;
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    public static Instant toInstant(LocalDate localDate, ZoneId zoneId) {
        if (localDate == null) return null;
        return localDate.atStartOfDay(zoneId).toInstant();
    }

    public static LocalDate toLocalDate(Instant instant) {
        if (instant == null) return null;
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDate toLocalDate(Instant instant, ZoneId zoneId) {
        if (instant == null) return null;
        return instant.atZone(zoneId).toLocalDate();
    }

    // ==================== LocalTime <-> Instant ====================

    public static Instant toInstant(LocalTime localTime) {
        if (localTime == null) return null;
        return LocalDate.now().atTime(localTime).atZone(ZoneId.systemDefault()).toInstant();
    }

    public static Instant toInstant(LocalTime localTime, ZoneId zoneId) {
        if (localTime == null) return null;
        return LocalDate.now().atTime(localTime).atZone(zoneId).toInstant();
    }

    public static LocalTime toLocalTime(Instant instant) {
        if (instant == null) return null;
        return instant.atZone(ZoneId.systemDefault()).toLocalTime();
    }

    public static LocalTime toLocalTime(Instant instant, ZoneId zoneId) {
        if (instant == null) return null;
        return instant.atZone(zoneId).toLocalTime();
    }

    // ==================== Epoch Milliseconds ====================

    public static long toEpochMilli(Date date) {
        if (date == null) return 0;
        return date.getTime();
    }

    public static Date toDateFromEpochMilli(long epochMilli) {
        return new Date(epochMilli);
    }

    public static long toEpochMilli(LocalDateTime localDateTime) {
        if (localDateTime == null) return 0;
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime fromEpochMilli(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static long toEpochSecond(LocalDateTime localDateTime) {
        if (localDateTime == null) return 0;
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    public static LocalDateTime fromEpochSecond(long epochSecond) {
        return Instant.ofEpochSecond(epochSecond).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // ==================== Duration <-> Long ====================

    public static long toMillis(Duration duration) {
        if (duration == null) return 0;
        return duration.toMillis();
    }

    public static Duration toDuration(long millis) {
        return Duration.ofMillis(millis);
    }

    public static long toSeconds(Duration duration) {
        if (duration == null) return 0;
        return duration.toSeconds();
    }

    public static Duration toDurationSeconds(long seconds) {
        return Duration.ofSeconds(seconds);
    }
}