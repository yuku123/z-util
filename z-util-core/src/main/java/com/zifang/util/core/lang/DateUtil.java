package com.zifang.util.core.lang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期时间工具类（已废弃）
 * <p>
 * 请使用 {@link com.zifang.util.core.time.DateUtil} 或其他 core.time 包下的工具类
 *
 * @deprecated 请使用 {@link com.zifang.util.core.time.DateUtil}
 */
@Deprecated
public class DateUtil {

    public static final String DATE_FORMAT_WHIFFLETREE_SECOND = com.zifang.util.core.time.DateUtil.PATTERN_DEFAULT;
    public static final String DATE_FORMAT_WHIFFLETREE_DAY = com.zifang.util.core.time.DateUtil.PATTERN_DATE;
    public static final String DATE_FORMAT_TIME = com.zifang.util.core.time.DateUtil.PATTERN_TIME;
    public static final String DATE_FORMAT_WHIFFLETREE_MILLIS = com.zifang.util.core.time.DateUtil.PATTERN_DATETIME_MS;

    private static final Map<String, SimpleDateFormat> DATE_FORMAT_MAP = new HashMap<>();

    @Deprecated
    public static String format(Date date, String pattern) {
        return com.zifang.util.core.time.DateUtil.format(date, pattern);
    }

    @Deprecated
    public static Date parse(String dateStr, String pattern) throws ParseException {
        return com.zifang.util.core.time.DateUtil.parse(dateStr, pattern);
    }

    @Deprecated
    public static Date parse(String dateStr) throws ParseException {
        return com.zifang.util.core.time.DateUtil.parse(dateStr);
    }

    @Deprecated
    public static LocalDateTime getDayStart(int offset) {
        return com.zifang.util.core.time.DateUtil.getDayStart(offset);
    }

    @Deprecated
    public static LocalDateTime getDayEnd(int offset) {
        return com.zifang.util.core.time.DateUtil.getDayEnd(offset);
    }

    @Deprecated
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return com.zifang.util.core.time.DateUtil.toLocalDateTime(date);
    }

    @Deprecated
    public static LocalDateTime dateToLocalDateTime(Date date, ZoneId zoneId) {
        return com.zifang.util.core.time.DateUtil.toLocalDateTime(date, zoneId);
    }

    @Deprecated
    public static Date localDateTimeToDate(LocalDateTime localDateTime, ZoneId zoneId) {
        return com.zifang.util.core.time.DateUtil.fromLocalDateTime(localDateTime, zoneId);
    }

    @Deprecated
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return com.zifang.util.core.time.DateUtil.fromLocalDateTime(localDateTime);
    }

    @Deprecated
    public static long getMicrosecond(String dateTime) {
        return com.zifang.util.core.time.DateUtil.getMicrosecond(dateTime);
    }

    @Deprecated
    public static LocalDateTime getDefaultLocalDateTime(String dateTime) {
        return com.zifang.util.core.time.DateUtil.parseLocalDateTime(dateTime);
    }

    @Deprecated
    public static LocalTime getDefaultLocalTime(String time) {
        return com.zifang.util.core.time.DateUtil.parseLocalTime(time);
    }

    @Deprecated
    public static LocalDateTime timestampToLocalDateTime(Long timestamp) {
        return com.zifang.util.core.time.DateUtil.timestampToLocalDateTime(timestamp);
    }

    @Deprecated
    public static LocalDateTime timestampToLocalDateTime(Long timestamp, int zoneOffset) {
        return com.zifang.util.core.time.DateUtil.timestampToLocalDateTime(timestamp, zoneOffset);
    }

    @Deprecated
    public static Long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        return com.zifang.util.core.time.DateUtil.localDateTimeToTimestamp(localDateTime);
    }

    @Deprecated
    public static Long localDateTimeToMilliTimestamp(LocalDateTime localDateTime) {
        return com.zifang.util.core.time.DateUtil.localDateTimeToMilliTimestamp(localDateTime);
    }

    @Deprecated
    public static String getNextDayStart() {
        return com.zifang.util.core.time.DateUtil.format(
            com.zifang.util.core.time.DateUtil.plusDays(
                com.zifang.util.core.time.DateUtil.todayStart(), 1));
    }

    @Deprecated
    public static String getTodayEnd() {
        return com.zifang.util.core.time.DateUtil.format(com.zifang.util.core.time.DateUtil.todayEnd());
    }

    @Deprecated
    public static String getTodayStart() {
        return com.zifang.util.core.time.DateUtil.format(com.zifang.util.core.time.DateUtil.todayStart());
    }

    @Deprecated
    public static SimpleDateFormat getFormat(final String pattern) {
        String p = (pattern == null || pattern.trim().isEmpty()) ? com.zifang.util.core.time.DateUtil.PATTERN_DEFAULT : pattern;
        SimpleDateFormat format = DATE_FORMAT_MAP.get(p);
        if (format == null) {
            format = new SimpleDateFormat(p);
            DATE_FORMAT_MAP.put(p, format);
        }
        return format;
    }
}