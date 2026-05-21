package com.zifang.util.core.time;

import com.zifang.util.core.time.converter.TimeConverter;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * java.sql.Time 工具类
 * <p>
 * 只处理 java.sql.Time 类型，提供格式化、解析、转换等功能
 * 注意：java.sql.Time 只有时间部分，日期部分被截断为基准日期（1970-01-01）
 */
public class SqlTimeUtil {

    public static final String PATTERN_DEFAULT = "HH:mm:ss";
    public static final String PATTERN_SHORT = "HH:mm";
    public static final String PATTERN_COMPACT = "HHmmss";
    public static final String PATTERN_CHINESE = "HH时mm分ss秒";

    public static final DateTimeFormatter FMT_DEFAULT = DateTimeFormatter.ofPattern(PATTERN_DEFAULT);
    public static final DateTimeFormatter FMT_SHORT = DateTimeFormatter.ofPattern(PATTERN_SHORT);
    public static final DateTimeFormatter FMT_COMPACT = DateTimeFormatter.ofPattern(PATTERN_COMPACT);
    public static final DateTimeFormatter FMT_CHINESE = DateTimeFormatter.ofPattern(PATTERN_CHINESE);

    // ==================== 格式化 ====================

    public static String format(Time time) {
        return format(time, PATTERN_DEFAULT);
    }

    public static String format(Time time, String pattern) {
        if (time == null) return null;
        return new java.text.SimpleDateFormat(pattern).format(time);
    }

    public static String format(Time time, DateTimeFormatter formatter) {
        if (time == null) return null;
        return formatter.format(toLocalTime(time));
    }

    // ==================== 解析 ====================

    public static Time parse(String timeStr) {
        return parse(timeStr, PATTERN_DEFAULT);
    }

    public static Time parse(String timeStr, String pattern) {
        if (timeStr == null || timeStr.trim().isEmpty()) return null;
        try {
            return new Time(new java.text.SimpleDateFormat(pattern).parse(timeStr).getTime());
        } catch (Exception e) {
            return null;
        }
    }

    // ==================== 获取 ====================

    public static Time now() {
        return new Time(System.currentTimeMillis());
    }

    // ==================== 时间计算 ====================

    public static Time plusHours(Time time, long hours) {
        if (time == null) return null;
        LocalTime lt = toLocalTime(time);
        return toSqlTime(lt.plusHours(hours));
    }

    public static Time minusHours(Time time, long hours) {
        if (time == null) return null;
        LocalTime lt = toLocalTime(time);
        return toSqlTime(lt.minusHours(hours));
    }

    public static Time plusMinutes(Time time, long minutes) {
        if (time == null) return null;
        LocalTime lt = toLocalTime(time);
        return toSqlTime(lt.plusMinutes(minutes));
    }

    public static Time minusMinutes(Time time, long minutes) {
        if (time == null) return null;
        LocalTime lt = toLocalTime(time);
        return toSqlTime(lt.minusMinutes(minutes));
    }

    public static Time plusSeconds(Time time, long seconds) {
        if (time == null) return null;
        LocalTime lt = toLocalTime(time);
        return toSqlTime(lt.plusSeconds(seconds));
    }

    public static Time minusSeconds(Time time, long seconds) {
        if (time == null) return null;
        LocalTime lt = toLocalTime(time);
        return toSqlTime(lt.minusSeconds(seconds));
    }

    // ==================== 时间比较 ====================

    public static boolean isBefore(Time time1, Time time2) {
        return time1.before(time2);
    }

    public static boolean isAfter(Time time1, Time time2) {
        return time1.after(time2);
    }

    // ==================== 时间差 ====================

    public static long millisBetween(Time start, Time end) {
        return end.getTime() - start.getTime();
    }

    public static long secondsBetween(Time start, Time end) {
        return (end.getTime() - start.getTime()) / 1000;
    }

    public static long minutesBetween(Time start, Time end) {
        return (end.getTime() - start.getTime()) / 60000;
    }

    public static long hoursBetween(Time start, Time end) {
        return (end.getTime() - start.getTime()) / 3600000;
    }

    // ==================== 获取时间部分 ====================

    public static int getHour(Time time) {
        return toLocalTime(time).getHour();
    }

    public static int getMinute(Time time) {
        return toLocalTime(time).getMinute();
    }

    public static int getSecond(Time time) {
        return toLocalTime(time).getSecond();
    }

    // ==================== 转换到其他类型 ====================

    public static Date toDate(Time time) {
        if (time == null) return null;
        return new Date(time.getTime());
    }

    public static LocalTime toLocalTime(Time time) {
        if (time == null) return null;
        return time.toLocalTime();
    }

    public static LocalDate toLocalDate(Time time) {
        if (time == null) return null;
        return LocalDate.ofEpochDay(time.getTime() / 86400000);
    }

    public static LocalDateTime toLocalDateTime(Time time) {
        if (time == null) return null;
        return toLocalDate(time).atTime(toLocalTime(time));
    }

    public static Instant toInstant(Time time) {
        if (time == null) return null;
        return Instant.ofEpochMilli(time.getTime());
    }

    public static long toEpochMilli(Time time) {
        if (time == null) return 0;
        return time.getTime();
    }

    // ==================== 从其他类型转换 ====================

    public static Time fromDate(Date date) {
        if (date == null) return null;
        return new Time(date.getTime());
    }

    public static Time fromLocalTime(LocalTime localTime) {
        if (localTime == null) return null;
        return Time.valueOf(localTime);
    }

    public static Time fromLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Time.valueOf(localDateTime.toLocalTime());
    }

    public static Time fromInstant(Instant instant) {
        if (instant == null) return null;
        return new Time(instant.toEpochMilli());
    }

    public static Time fromEpochMilli(long epochMilli) {
        return new Time(epochMilli);
    }

    // ==================== 内部方法 ====================

    private static Time toSqlTime(LocalTime localTime) {
        return Time.valueOf(localTime);
    }
}
