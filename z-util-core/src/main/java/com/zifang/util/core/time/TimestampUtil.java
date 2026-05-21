package com.zifang.util.core.time;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * java.sql.Timestamp 工具类
 * <p>
 * 只处理Timestamp类型，提供Timestamp的格式化、解析、转换等功能
 */
public class TimestampUtil {

    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATETIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_COMPACT = "yyyyMMddHHmmss";

    public static final DateTimeFormatter FMT_DEFAULT = DateTimeFormatter.ofPattern(PATTERN_DEFAULT);
    public static final DateTimeFormatter FMT_DATETIME_MS = DateTimeFormatter.ofPattern(PATTERN_DATETIME_MS);

    // ==================== 格式化 ====================

    public static String format(Timestamp timestamp) {
        return format(timestamp, PATTERN_DEFAULT);
    }

    public static String format(Timestamp timestamp, String pattern) {
        if (timestamp == null) return null;
        return new SimpleDateFormat(pattern).format(timestamp);
    }

    public static String format(Timestamp timestamp, DateTimeFormatter formatter) {
        if (timestamp == null) return null;
        return formatter.format(toLocalDateTime(timestamp));
    }

    // ==================== 解析 ====================

    public static Timestamp parse(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        return Timestamp.valueOf(dateStr);
    }

    public static Timestamp parse(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return new Timestamp(new SimpleDateFormat(pattern).parse(dateStr).getTime());
        } catch (Exception e) {
            return null;
        }
    }

    // ==================== 获取 ====================

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp of(long time) {
        return new Timestamp(time);
    }

    public static Timestamp of(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    public static Timestamp of(Instant instant) {
        return Timestamp.from(instant);
    }

    // ==================== 时间计算 ====================

    public static Timestamp plusMillis(Timestamp timestamp, long millis) {
        return new Timestamp(timestamp.getTime() + millis);
    }

    public static Timestamp minusMillis(Timestamp timestamp, long millis) {
        return new Timestamp(timestamp.getTime() - millis);
    }

    public static Timestamp plusSeconds(Timestamp timestamp, long seconds) {
        return new Timestamp(timestamp.getTime() + seconds * 1000);
    }

    public static Timestamp minusSeconds(Timestamp timestamp, long seconds) {
        return new Timestamp(timestamp.getTime() - seconds * 1000);
    }

    // ==================== 时间比较 ====================

    public static boolean isBefore(Timestamp ts1, Timestamp ts2) {
        return ts1.before(ts2);
    }

    public static boolean isAfter(Timestamp ts1, Timestamp ts2) {
        return ts1.after(ts2);
    }

    // ==================== 转换到其他类型 ====================

    public static Date toDate(Timestamp timestamp) {
        if (timestamp == null) return null;
        return new Date(timestamp.getTime());
    }

    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) return null;
        return timestamp.toLocalDateTime();
    }

    public static LocalDate toLocalDate(Timestamp timestamp) {
        if (timestamp == null) return null;
        return timestamp.toLocalDateTime().toLocalDate();
    }

    public static Instant toInstant(Timestamp timestamp) {
        if (timestamp == null) return null;
        return timestamp.toInstant();
    }

    public static long toEpochMilli(Timestamp timestamp) {
        if (timestamp == null) return 0;
        return timestamp.getTime();
    }

    // ==================== 从其他类型转换 ====================

    public static Timestamp fromDate(Date date) {
        if (date == null) return null;
        return new Timestamp(date.getTime());
    }

    public static Timestamp fromLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Timestamp.valueOf(localDateTime);
    }

    public static Timestamp fromLocalDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Timestamp.valueOf(localDate.atStartOfDay());
    }

    public static Timestamp fromInstant(Instant instant) {
        if (instant == null) return null;
        return Timestamp.from(instant);
    }

    public static Timestamp fromEpochMilli(long epochMilli) {
        return new Timestamp(epochMilli);
    }
}
