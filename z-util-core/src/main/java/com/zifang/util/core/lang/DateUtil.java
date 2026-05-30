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
/**
 * DateUtil类。
 */
public class DateUtil {

    public static final String DATE_FORMAT_WHIFFLETREE_SECOND = com.zifang.util.core.time.DateUtil.PATTERN_DEFAULT;
    public static final String DATE_FORMAT_WHIFFLETREE_DAY = com.zifang.util.core.time.DateUtil.PATTERN_DATE;
    public static final String DATE_FORMAT_TIME = com.zifang.util.core.time.DateUtil.PATTERN_TIME;
    public static final String DATE_FORMAT_WHIFFLETREE_MILLIS = com.zifang.util.core.time.DateUtil.PATTERN_DATETIME_MS;

    private static final Map<String, SimpleDateFormat> DATE_FORMAT_MAP = new HashMap<>();

    @Deprecated
    /**
     * format方法。
     *      * @param date Date类型参数
     * @param pattern String类型参数
     * @return static String类型返回值
     */
    public static String format(Date date, String pattern) {
        return com.zifang.util.core.time.DateUtil.format(date, pattern);
    }

    @Deprecated
    /**
     * parse方法。
     *      * @param dateStr String类型参数
     * @param pattern String类型参数
     * @return static Date类型返回值
     */
    public static Date parse(String dateStr, String pattern) throws ParseException {
        return com.zifang.util.core.time.DateUtil.parse(dateStr, pattern);
    }

    @Deprecated
    /**
     * parse方法。
     *      * @param dateStr String类型参数
     * @return static Date类型返回值
     */
    public static Date parse(String dateStr) throws ParseException {
        return com.zifang.util.core.time.DateUtil.parse(dateStr);
    }

    @Deprecated
    /**
     * getDayStart方法。
     *      * @param offset int类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime getDayStart(int offset) {
        return com.zifang.util.core.time.DateUtil.getDayStart(offset);
    }

    @Deprecated
    /**
     * getDayEnd方法。
     *      * @param offset int类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime getDayEnd(int offset) {
        return com.zifang.util.core.time.DateUtil.getDayEnd(offset);
    }

    @Deprecated
    /**
     * dateToLocalDateTime方法。
     *      * @param date Date类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return com.zifang.util.core.time.DateUtil.toLocalDateTime(date);
    }

    @Deprecated
    /**
     * dateToLocalDateTime方法。
     *      * @param date Date类型参数
     * @param zoneId ZoneId类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime dateToLocalDateTime(Date date, ZoneId zoneId) {
        return com.zifang.util.core.time.DateUtil.toLocalDateTime(date, zoneId);
    }

    @Deprecated
    /**
     * localDateTimeToDate方法。
     *      * @param localDateTime LocalDateTime类型参数
     * @param zoneId ZoneId类型参数
     * @return static Date类型返回值
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime, ZoneId zoneId) {
        return com.zifang.util.core.time.DateUtil.fromLocalDateTime(localDateTime, zoneId);
    }

    @Deprecated
    /**
     * localDateTimeToDate方法。
     *      * @param localDateTime LocalDateTime类型参数
     * @return static Date类型返回值
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return com.zifang.util.core.time.DateUtil.fromLocalDateTime(localDateTime);
    }

    @Deprecated
    /**
     * getMicrosecond方法。
     *      * @param dateTime String类型参数
     * @return static long类型返回值
     */
    public static long getMicrosecond(String dateTime) {
        return com.zifang.util.core.time.DateUtil.getMicrosecond(dateTime);
    }

    @Deprecated
    /**
     * getDefaultLocalDateTime方法。
     *      * @param dateTime String类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime getDefaultLocalDateTime(String dateTime) {
        return com.zifang.util.core.time.DateUtil.parseLocalDateTime(dateTime);
    }

    @Deprecated
    /**
     * getDefaultLocalTime方法。
     *      * @param time String类型参数
     * @return static LocalTime类型返回值
     */
    public static LocalTime getDefaultLocalTime(String time) {
        return com.zifang.util.core.time.DateUtil.parseLocalTime(time);
    }

    @Deprecated
    /**
     * timestampToLocalDateTime方法。
     *      * @param timestamp long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime timestampToLocalDateTime(Long timestamp) {
        return com.zifang.util.core.time.DateUtil.timestampToLocalDateTime(timestamp);
    }

    @Deprecated
    /**
     * timestampToLocalDateTime方法。
     *      * @param timestamp long类型参数
     * @param zoneOffset int类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime timestampToLocalDateTime(Long timestamp, int zoneOffset) {
        return com.zifang.util.core.time.DateUtil.timestampToLocalDateTime(timestamp, zoneOffset);
    }

    @Deprecated
    /**
     * localDateTimeToTimestamp方法。
     *      * @param localDateTime LocalDateTime类型参数
     * @return static Long类型返回值
     */
    public static Long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        return com.zifang.util.core.time.DateUtil.localDateTimeToTimestamp(localDateTime);
    }

    @Deprecated
    /**
     * localDateTimeToMilliTimestamp方法。
     *      * @param localDateTime LocalDateTime类型参数
     * @return static Long类型返回值
     */
    public static Long localDateTimeToMilliTimestamp(LocalDateTime localDateTime) {
        return com.zifang.util.core.time.DateUtil.localDateTimeToMilliTimestamp(localDateTime);
    }

    @Deprecated
    /**
     * getNextDayStart方法。
     * @return static String类型返回值
     */
    public static String getNextDayStart() {
        return com.zifang.util.core.time.DateUtil.format(
            com.zifang.util.core.time.DateUtil.plusDays(
                com.zifang.util.core.time.DateUtil.todayStart(), 1));
    }

    @Deprecated
    /**
     * getTodayEnd方法。
     * @return static String类型返回值
     */
    public static String getTodayEnd() {
        return com.zifang.util.core.time.DateUtil.format(com.zifang.util.core.time.DateUtil.todayEnd());
    }

    @Deprecated
    /**
     * getTodayStart方法。
     * @return static String类型返回值
     */
    public static String getTodayStart() {
        return com.zifang.util.core.time.DateUtil.format(com.zifang.util.core.time.DateUtil.todayStart());
    }

    @Deprecated
    /**
     * getFormat方法。
     *      * @param pattern final类型参数
     * @return static SimpleDateFormat类型返回值
     */
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