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
 * 只处理Instant类型，提供Instant的格式化、解析、计算、转换等功能。
 * Instant表示时间线上的一个瞬时点，类似于java.util.Date，
 * 但精度更高（纳秒级），且没有时区概念。
 * </p>
 * <p>
 * 该工具类支持：
 * <ul>
 *   <li>格式化与解析：支持多种日期时间模式</li>
 *   <li>时间计算：加减毫秒、秒、纳秒等</li>
 *   <li>类型转换：与Date、LocalDateTime、LocalDate、LocalTime、ZonedDateTime互转</li>
 *   <li>时间比较：判断先后、计算时间差</li>
 * </ul>
 * </p>
 *
 * @author zifang
 * @see java.time.Instant
 * @see DateUtil
 * @see TimestampUtil
 */
/**
 * InstantUtil类。
 */
/**
 * InstantUtil类。
 */
public class InstantUtil {

    /**
     * 默认时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    /**
     * ISO格式：yyyy-MM-dd'T'HH:mm:ss.SSSXXX
     */
    public static final String PATTERN_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    /**
     * 日期格式：yyyy-MM-dd
     */
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    /**
     * 默认DateTimeFormatter，格式为 {@link #PATTERN_DEFAULT}
     */
    /**
     * DateTimeFormatter.ofPattern方法。
     *      * @param PATTERN_DEFAULT Object类型参数
     * @return static final DateTimeFormatter FMT_DEFAULT =类型返回值
     */
    /**
     * DateTimeFormatter.ofPattern方法。
     *      * @param PATTERN_DEFAULT Object类型参数
     * @return static final DateTimeFormatter FMT_DEFAULT =类型返回值
     */
    public static final DateTimeFormatter FMT_DEFAULT = DateTimeFormatter.ofPattern(PATTERN_DEFAULT);

    /**
     * 日期DateTimeFormatter，格式为 {@link #PATTERN_DATE}
     */
    /**
     * DateTimeFormatter.ofPattern方法。
     *      * @param PATTERN_DATE Object类型参数
     * @return static final DateTimeFormatter FMT_DATE =类型返回值
     */
    /**
     * DateTimeFormatter.ofPattern方法。
     *      * @param PATTERN_DATE Object类型参数
     * @return static final DateTimeFormatter FMT_DATE =类型返回值
     */
    public static final DateTimeFormatter FMT_DATE = DateTimeFormatter.ofPattern(PATTERN_DATE);

    // ==================== 格式化 ====================

    /**
     * 将Instant格式化为字符串，使用默认格式 {@link #PATTERN_DEFAULT}
     *
     * @param instant 要格式化的时间点，不能为null
     * @return 格式化后的时间字符串
     * @throws NullPointerException if instant is null
     */
    /**
     * format方法。
     *      * @param instant Instant类型参数
     * @return static String类型返回值
     */
    /**
     * format方法。
     *      * @param instant Instant类型参数
     * @return static String类型返回值
     */
    public static String format(Instant instant) {
        return format(instant, FMT_DEFAULT);
    }

    /**
     * 将Instant格式化为指定格式的字符串
     *
     * @param instant  要格式化的时间点，不能为null
     * @param pattern 时间格式模式，如 "yyyy-MM-dd HH:mm:ss"
     * @return 格式化后的时间字符串
     * @throws NullPointerException if instant is null
     */
    /**
     * format方法。
     *      * @param instant Instant类型参数
     * @param pattern String类型参数
     * @return static String类型返回值
     */
    /**
     * format方法。
     *      * @param instant Instant类型参数
     * @param pattern String类型参数
     * @return static String类型返回值
     */
    public static String format(Instant instant, String pattern) {
        if (instant == null) return null;
        return format(instant, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将Instant格式化为字符串，使用指定的DateTimeFormatter
     *
     * @param instant   要格式化的时间点，不能为null
     * @param formatter 日期时间格式化器，不能为null
     * @return 格式化后的时间字符串
     * @throws NullPointerException if instant or formatter is null
     */
    /**
     * format方法。
     *      * @param instant Instant类型参数
     * @param formatter DateTimeFormatter类型参数
     * @return static String类型返回值
     */
    /**
     * format方法。
     *      * @param instant Instant类型参数
     * @param formatter DateTimeFormatter类型参数
     * @return static String类型返回值
     */
    public static String format(Instant instant, DateTimeFormatter formatter) {
        if (instant == null) return null;
        return formatter.format(instant.atZone(ZoneId.systemDefault()));
    }

    /**
     * 将Instant格式化为字符串，使用指定的时区和格式
     *
     * @param instant  要格式化的时间点，不能为null
     * @param zoneId  时区ID，不能为null
     * @param pattern 时间格式模式，如 "yyyy-MM-dd HH:mm:ss"
     * @return 格式化后的时间字符串
     * @throws NullPointerException if instant or zoneId is null
     */
    /**
     * format方法。
     *      * @param instant Instant类型参数
     * @param zoneId ZoneId类型参数
     * @param pattern String类型参数
     * @return static String类型返回值
     */
    /**
     * format方法。
     *      * @param instant Instant类型参数
     * @param zoneId ZoneId类型参数
     * @param pattern String类型参数
     * @return static String类型返回值
     */
    public static String format(Instant instant, ZoneId zoneId, String pattern) {
        if (instant == null) return null;
        return formatter(zoneId, pattern).format(instant.atZone(zoneId));
    }

    /**
     * 根据时区和模式创建DateTimeFormatter
     *
     * @param zoneId  时区ID
     * @param pattern 时间格式模式
     * @return 配置好的DateTimeFormatter
     */
    private static DateTimeFormatter formatter(ZoneId zoneId, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(zoneId);
    }

    // ==================== 解析 ====================

    /**
     * 解析时间字符串，使用默认格式 {@link #PATTERN_DEFAULT}
     *
     * @param dateStr 时间字符串，格式需符合 {@link #PATTERN_DEFAULT}
     * @return 解析后的Instant，如果输入为空则返回null
     * @throws IllegalArgumentException if the string does not match the expected format
     */
    /**
     * parse方法。
     *      * @param dateStr String类型参数
     * @return static Instant类型返回值
     */
    /**
     * parse方法。
     *      * @param dateStr String类型参数
     * @return static Instant类型返回值
     */
    public static Instant parse(String dateStr) {
        return parse(dateStr, PATTERN_DEFAULT);
    }

    /**
     * 解析指定格式的时间字符串
     *
     * @param dateStr 时间字符串
     * @param pattern 时间格式模式，如 "yyyy-MM-dd HH:mm:ss"
     * @return 解析后的Instant，解析失败返回null
     */
    /**
     * parse方法。
     *      * @param dateStr String类型参数
     * @param pattern String类型参数
     * @return static Instant类型返回值
     */
    /**
     * parse方法。
     *      * @param dateStr String类型参数
     * @param pattern String类型参数
     * @return static Instant类型返回值
     */
    public static Instant parse(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        return Instant.from(DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).parse(dateStr));
    }

    // ==================== 获取 ====================

    /**
     * 获取当前时刻的Instant
     *
     * @return 当前时刻的Instant对象
     */
    /**
     * now方法。
     * @return static Instant类型返回值
     */
    /**
     * now方法。
     * @return static Instant类型返回值
     */
    public static Instant now() {
        return Instant.now();
    }

    /**
     * 根据Unix时间戳（秒）创建Instant
     *
     * @param epochSecond Unix时间戳，表示自1970年1月1日以来的秒数
     * @return 对应的Instant对象
     */
    /**
     * ofEpochSecond方法。
     *      * @param epochSecond long类型参数
     * @return static Instant类型返回值
     */
    /**
     * ofEpochSecond方法。
     *      * @param epochSecond long类型参数
     * @return static Instant类型返回值
     */
    public static Instant ofEpochSecond(long epochSecond) {
        return Instant.ofEpochSecond(epochSecond);
    }

    /**
     * 根据Unix时间戳（毫秒）创建Instant
     *
     * @param epochMilli Unix时间戳，表示自1970年1月1日以来的毫秒数
     * @return 对应的Instant对象
     */
    /**
     * ofEpochMilli方法。
     *      * @param epochMilli long类型参数
     * @return static Instant类型返回值
     */
    /**
     * ofEpochMilli方法。
     *      * @param epochMilli long类型参数
     * @return static Instant类型返回值
     */
    public static Instant ofEpochMilli(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli);
    }

    // ==================== 日期时间计算 ====================

    /**
     * 为Instant增加指定的毫秒数
     *
     * @param instant 原时间点，不能为null
     * @param millis  要增加的毫秒数，可以为负数
     * @return 增加后的新Instant对象
     * @throws NullPointerException if instant is null
     */
    /**
     * plusMillis方法。
     *      * @param instant Instant类型参数
     * @param millis long类型参数
     * @return static Instant类型返回值
     */
    /**
     * plusMillis方法。
     *      * @param instant Instant类型参数
     * @param millis long类型参数
     * @return static Instant类型返回值
     */
    public static Instant plusMillis(Instant instant, long millis) {
        return instant.plusMillis(millis);
    }

    /**
     * 为Instant减少指定的毫秒数
     *
     * @param instant 原时间点，不能为null
     * @param millis  要减少的毫秒数，可以为负数
     * @return 减少后的新Instant对象
     * @throws NullPointerException if instant is null
     */
    /**
     * minusMillis方法。
     *      * @param instant Instant类型参数
     * @param millis long类型参数
     * @return static Instant类型返回值
     */
    /**
     * minusMillis方法。
     *      * @param instant Instant类型参数
     * @param millis long类型参数
     * @return static Instant类型返回值
     */
    public static Instant minusMillis(Instant instant, long millis) {
        return instant.minusMillis(millis);
    }

    /**
     * 为Instant增加指定的秒数
     *
     * @param instant 原时间点，不能为null
     * @param seconds 要增加的秒数，可以为负数
     * @return 增加后的新Instant对象
     * @throws NullPointerException if instant is null
     */
    /**
     * plusSeconds方法。
     *      * @param instant Instant类型参数
     * @param seconds long类型参数
     * @return static Instant类型返回值
     */
    /**
     * plusSeconds方法。
     *      * @param instant Instant类型参数
     * @param seconds long类型参数
     * @return static Instant类型返回值
     */
    public static Instant plusSeconds(Instant instant, long seconds) {
        return instant.plusSeconds(seconds);
    }

    /**
     * 为Instant减少指定的秒数
     *
     * @param instant 原时间点，不能为null
     * @param seconds 要减少的秒数，可以为负数
     * @return 减少后的新Instant对象
     * @throws NullPointerException if instant is null
     */
    /**
     * minusSeconds方法。
     *      * @param instant Instant类型参数
     * @param seconds long类型参数
     * @return static Instant类型返回值
     */
    /**
     * minusSeconds方法。
     *      * @param instant Instant类型参数
     * @param seconds long类型参数
     * @return static Instant类型返回值
     */
    public static Instant minusSeconds(Instant instant, long seconds) {
        return instant.minusSeconds(seconds);
    }

    /**
     * 为Instant增加指定的纳秒数
     *
     * @param instant 原时间点，不能为null
     * @param nanos   要增加的纳秒数，可以为负数
     * @return 增加后的新Instant对象
     * @throws NullPointerException if instant is null
     */
    /**
     * plusNanos方法。
     *      * @param instant Instant类型参数
     * @param nanos long类型参数
     * @return static Instant类型返回值
     */
    /**
     * plusNanos方法。
     *      * @param instant Instant类型参数
     * @param nanos long类型参数
     * @return static Instant类型返回值
     */
    public static Instant plusNanos(Instant instant, long nanos) {
        return instant.plusNanos(nanos);
    }

    /**
     * 为Instant减少指定的纳秒数
     *
     * @param instant 原时间点，不能为null
     * @param nanos   要减少的纳秒数，可以为负数
     * @return 减少后的新Instant对象
     * @throws NullPointerException if instant is null
     */
    /**
     * minusNanos方法。
     *      * @param instant Instant类型参数
     * @param nanos long类型参数
     * @return static Instant类型返回值
     */
    /**
     * minusNanos方法。
     *      * @param instant Instant类型参数
     * @param nanos long类型参数
     * @return static Instant类型返回值
     */
    public static Instant minusNanos(Instant instant, long nanos) {
        return instant.minusNanos(nanos);
    }

    // ==================== 日期时间比较 ====================

    /**
     * 判断第一个Instant是否在第二个之前
     *
     * @param instant1 第一个时间点，不能为null
     * @param instant2 第二个时间点，不能为null
     * @return 如果instant1在instant2之前则返回true
     * @throws NullPointerException if instant1 or instant2 is null
     */
    /**
     * isBefore方法。
     *      * @param instant1 Instant类型参数
     * @param instant2 Instant类型参数
     * @return static boolean类型返回值
     */
    /**
     * isBefore方法。
     *      * @param instant1 Instant类型参数
     * @param instant2 Instant类型参数
     * @return static boolean类型返回值
     */
    public static boolean isBefore(Instant instant1, Instant instant2) {
        return instant1.isBefore(instant2);
    }

    /**
     * 判断第一个Instant是否在第二个之后
     *
     * @param instant1 第一个时间点，不能为null
     * @param instant2 第二个时间点，不能为null
     * @return 如果instant1在instant2之后则返回true
     * @throws NullPointerException if instant1 or instant2 is null
     */
    /**
     * isAfter方法。
     *      * @param instant1 Instant类型参数
     * @param instant2 Instant类型参数
     * @return static boolean类型返回值
     */
    /**
     * isAfter方法。
     *      * @param instant1 Instant类型参数
     * @param instant2 Instant类型参数
     * @return static boolean类型返回值
     */
    public static boolean isAfter(Instant instant1, Instant instant2) {
        return instant1.isAfter(instant2);
    }

    /**
     * 判断两个Instant是否相等
     *
     * @param instant1 第一个时间点，不能为null
     * @param instant2 第二个时间点，不能为null
     * @return 如果两者相等则返回true
     * @throws NullPointerException if instant1 or instant2 is null
     */
    /**
     * isEqual方法。
     *      * @param instant1 Instant类型参数
     * @param instant2 Instant类型参数
     * @return static boolean类型返回值
     */
    /**
     * isEqual方法。
     *      * @param instant1 Instant类型参数
     * @param instant2 Instant类型参数
     * @return static boolean类型返回值
     */
    public static boolean isEqual(Instant instant1, Instant instant2) {
        return instant1.equals(instant2);
    }

    // ==================== 日期时间差 ====================

    /**
     * 计算两个Instant之间的毫秒差
     *
     * @param start 开始时间点，不能为null
     * @param end   结束时间点，不能为null
     * @return 毫秒差（end - start）
     * @throws NullPointerException if start or end is null
     */
    /**
     * millisBetween方法。
     *      * @param start Instant类型参数
     * @param end Instant类型参数
     * @return static long类型返回值
     */
    /**
     * millisBetween方法。
     *      * @param start Instant类型参数
     * @param end Instant类型参数
     * @return static long类型返回值
     */
    public static long millisBetween(Instant start, Instant end) {
        return java.time.temporal.ChronoUnit.MILLIS.between(start, end);
    }

    /**
     * 计算两个Instant之间的秒差
     *
     * @param start 开始时间点，不能为null
     * @param end   结束时间点，不能为null
     * @return 秒差（end - start）
     * @throws NullPointerException if start or end is null
     */
    /**
     * secondsBetween方法。
     *      * @param start Instant类型参数
     * @param end Instant类型参数
     * @return static long类型返回值
     */
    /**
     * secondsBetween方法。
     *      * @param start Instant类型参数
     * @param end Instant类型参数
     * @return static long类型返回值
     */
    public static long secondsBetween(Instant start, Instant end) {
        return java.time.temporal.ChronoUnit.SECONDS.between(start, end);
    }

    /**
     * 计算两个Instant之间的纳秒差
     *
     * @param start 开始时间点，不能为null
     * @param end   结束时间点，不能为null
     * @return 纳秒差（end - start）
     * @throws NullPointerException if start or end is null
     */
    /**
     * nanosBetween方法。
     *      * @param start Instant类型参数
     * @param end Instant类型参数
     * @return static long类型返回值
     */
    /**
     * nanosBetween方法。
     *      * @param start Instant类型参数
     * @param end Instant类型参数
     * @return static long类型返回值
     */
    public static long nanosBetween(Instant start, Instant end) {
        return java.time.temporal.ChronoUnit.NANOS.between(start, end);
    }

    // ==================== 获取日期时间部分 ====================

    /**
     * 将Instant转换为自1970年1月1日以来的毫秒数
     *
     * @param instant 要转换的时间点
     * @return 毫秒值，如果输入为null则返回0
     */
    /**
     * toEpochMilli方法。
     *      * @param instant Instant类型参数
     * @return static long类型返回值
     */
    /**
     * toEpochMilli方法。
     *      * @param instant Instant类型参数
     * @return static long类型返回值
     */
    public static long toEpochMilli(Instant instant) {
        if (instant == null) return 0;
        return instant.toEpochMilli();
    }

    /**
     * 将Instant转换为自1970年1月1日以来的秒数
     *
     * @param instant 要转换的时间点
     * @return 秒值，如果输入为null则返回0
     */
    /**
     * toEpochSecond方法。
     *      * @param instant Instant类型参数
     * @return static long类型返回值
     */
    /**
     * toEpochSecond方法。
     *      * @param instant Instant类型参数
     * @return static long类型返回值
     */
    public static long toEpochSecond(Instant instant) {
        if (instant == null) return 0;
        return instant.getEpochSecond();
    }

    /**
     * 获取Instant的纳秒部分
     *
     * @param instant 要获取的时间点，不能为null
     * @return 纳秒部分（0-999,999,999）
     * @throws NullPointerException if instant is null
     */
    /**
     * getNano方法。
     *      * @param instant Instant类型参数
     * @return static int类型返回值
     */
    /**
     * getNano方法。
     *      * @param instant Instant类型参数
     * @return static int类型返回值
     */
    public static int getNano(Instant instant) {
        return instant.getNano();
    }

    // ==================== 转换到其他类型 ====================

    /**
     * 将Instant转换为java.util.Date
     *
     * @param instant 要转换的时间点
     * @return 转换后的Date对象，如果输入为null则返回null
     */
    /**
     * toDate方法。
     *      * @param instant Instant类型参数
     * @return static Date类型返回值
     */
    /**
     * toDate方法。
     *      * @param instant Instant类型参数
     * @return static Date类型返回值
     */
    public static Date toDate(Instant instant) {
        return TimeConverter.toDate(instant);
    }

    /**
     * 将Instant转换为LocalDateTime（使用系统默认时区）
     *
     * @param instant 要转换的时间点
     * @return 转换后的LocalDateTime对象，如果输入为null则返回null
     */
    /**
     * toLocalDateTime方法。
     *      * @param instant Instant类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * toLocalDateTime方法。
     *      * @param instant Instant类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        return TimeConverter.toLocalDateTime(instant);
    }

    /**
     * 将Instant转换为LocalDateTime（使用指定时区）
     *
     * @param instant 要转换的时间点
     * @param zoneId  时区ID
     * @return 转换后的LocalDateTime对象，如果输入为null则返回null
     */
    /**
     * toLocalDateTime方法。
     *      * @param instant Instant类型参数
     * @param zoneId ZoneId类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * toLocalDateTime方法。
     *      * @param instant Instant类型参数
     * @param zoneId ZoneId类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zoneId) {
        return TimeConverter.toLocalDateTime(instant, zoneId);
    }

    /**
     * 将Instant转换为LocalDate（使用系统默认时区）
     *
     * @param instant 要转换的时间点
     * @return 转换后的LocalDate对象，如果输入为null则返回null
     */
    /**
     * toLocalDate方法。
     *      * @param instant Instant类型参数
     * @return static LocalDate类型返回值
     */
    /**
     * toLocalDate方法。
     *      * @param instant Instant类型参数
     * @return static LocalDate类型返回值
     */
    public static LocalDate toLocalDate(Instant instant) {
        return TimeConverter.toLocalDate(instant);
    }

    /**
     * 将Instant转换为LocalDate（使用指定时区）
     *
     * @param instant 要转换的时间点
     * @param zoneId  时区ID
     * @return 转换后的LocalDate对象，如果输入为null则返回null
     */
    /**
     * toLocalDate方法。
     *      * @param instant Instant类型参数
     * @param zoneId ZoneId类型参数
     * @return static LocalDate类型返回值
     */
    /**
     * toLocalDate方法。
     *      * @param instant Instant类型参数
     * @param zoneId ZoneId类型参数
     * @return static LocalDate类型返回值
     */
    public static LocalDate toLocalDate(Instant instant, ZoneId zoneId) {
        return TimeConverter.toLocalDate(instant, zoneId);
    }

    /**
     * 将Instant转换为LocalTime（使用系统默认时区）
     *
     * @param instant 要转换的时间点
     * @return 转换后的LocalTime对象，如果输入为null则返回null
     */
    /**
     * toLocalTime方法。
     *      * @param instant Instant类型参数
     * @return static LocalTime类型返回值
     */
    /**
     * toLocalTime方法。
     *      * @param instant Instant类型参数
     * @return static LocalTime类型返回值
     */
    public static LocalTime toLocalTime(Instant instant) {
        return TimeConverter.toLocalTime(instant);
    }

    /**
     * 将Instant转换为LocalTime（使用指定时区）
     *
     * @param instant 要转换的时间点
     * @param zoneId  时区ID
     * @return 转换后的LocalTime对象，如果输入为null则返回null
     */
    /**
     * toLocalTime方法。
     *      * @param instant Instant类型参数
     * @param zoneId ZoneId类型参数
     * @return static LocalTime类型返回值
     */
    /**
     * toLocalTime方法。
     *      * @param instant Instant类型参数
     * @param zoneId ZoneId类型参数
     * @return static LocalTime类型返回值
     */
    public static LocalTime toLocalTime(Instant instant, ZoneId zoneId) {
        return TimeConverter.toLocalTime(instant, zoneId);
    }

    /**
     * 将Instant转换为ZonedDateTime（使用系统默认时区）
     *
     * @param instant 要转换的时间点
     * @return 转换后的ZonedDateTime对象，如果输入为null则返回null
     */
    /**
     * toZonedDateTime方法。
     *      * @param instant Instant类型参数
     * @return static ZonedDateTime类型返回值
     */
    /**
     * toZonedDateTime方法。
     *      * @param instant Instant类型参数
     * @return static ZonedDateTime类型返回值
     */
    public static ZonedDateTime toZonedDateTime(Instant instant) {
        return instant.atZone(ZoneId.systemDefault());
    }

    /**
     * 将Instant转换为ZonedDateTime（使用指定时区）
     *
     * @param instant 要转换的时间点
     * @param zoneId  时区ID
     * @return 转换后的ZonedDateTime对象，如果输入为null则返回null
     */
    /**
     * toZonedDateTime方法。
     *      * @param instant Instant类型参数
     * @param zoneId ZoneId类型参数
     * @return static ZonedDateTime类型返回值
     */
    /**
     * toZonedDateTime方法。
     *      * @param instant Instant类型参数
     * @param zoneId ZoneId类型参数
     * @return static ZonedDateTime类型返回值
     */
    public static ZonedDateTime toZonedDateTime(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId);
    }

    // ==================== 从其他类型转换 ====================

    /**
     * 从java.util.Date转换为Instant
     *
     * @param date 要转换的Date对象
     * @return 转换后的Instant对象，如果输入为null则返回null
     */
    /**
     * fromDate方法。
     *      * @param date Date类型参数
     * @return static Instant类型返回值
     */
    /**
     * fromDate方法。
     *      * @param date Date类型参数
     * @return static Instant类型返回值
     */
    public static Instant fromDate(Date date) {
        return TimeConverter.toInstant(date);
    }

    /**
     * 从LocalDateTime转换为Instant（使用系统默认时区）
     *
     * @param localDateTime 要转换的LocalDateTime对象
     * @return 转换后的Instant对象，如果输入为null则返回null
     */
    /**
     * fromLocalDateTime方法。
     *      * @param localDateTime LocalDateTime类型参数
     * @return static Instant类型返回值
     */
    /**
     * fromLocalDateTime方法。
     *      * @param localDateTime LocalDateTime类型参数
     * @return static Instant类型返回值
     */
    public static Instant fromLocalDateTime(LocalDateTime localDateTime) {
        return TimeConverter.toInstant(localDateTime);
    }

    /**
     * 从LocalDateTime转换为Instant（使用指定时区）
     *
     * @param localDateTime 要转换的LocalDateTime对象
     * @param zoneId        时区ID
     * @return 转换后的Instant对象，如果输入为null则返回null
     */
    /**
     * fromLocalDateTime方法。
     *      * @param localDateTime LocalDateTime类型参数
     * @param zoneId ZoneId类型参数
     * @return static Instant类型返回值
     */
    /**
     * fromLocalDateTime方法。
     *      * @param localDateTime LocalDateTime类型参数
     * @param zoneId ZoneId类型参数
     * @return static Instant类型返回值
     */
    public static Instant fromLocalDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
        return TimeConverter.toInstant(localDateTime, zoneId);
    }

    /**
     * 从LocalDate转换为Instant（使用系统默认时区）
     * <p>
     * 转换后的时间为指定日期的凌晨00:00:00
     *
     * @param localDate 要转换的LocalDate对象
     * @return 转换后的Instant对象，如果输入为null则返回null
     */
    /**
     * fromLocalDate方法。
     *      * @param localDate LocalDate类型参数
     * @return static Instant类型返回值
     */
    /**
     * fromLocalDate方法。
     *      * @param localDate LocalDate类型参数
     * @return static Instant类型返回值
     */
    public static Instant fromLocalDate(LocalDate localDate) {
        return TimeConverter.toInstant(localDate);
    }

    /**
     * 从LocalDate转换为Instant（使用指定时区）
     * <p>
     * 转换后的时间为指定日期的凌晨00:00:00
     *
     * @param localDate 要转换的LocalDate对象
     * @param zoneId    时区ID
     * @return 转换后的Instant对象，如果输入为null则返回null
     */
    /**
     * fromLocalDate方法。
     *      * @param localDate LocalDate类型参数
     * @param zoneId ZoneId类型参数
     * @return static Instant类型返回值
     */
    /**
     * fromLocalDate方法。
     *      * @param localDate LocalDate类型参数
     * @param zoneId ZoneId类型参数
     * @return static Instant类型返回值
     */
    public static Instant fromLocalDate(LocalDate localDate, ZoneId zoneId) {
        return TimeConverter.toInstant(localDate, zoneId);
    }

    /**
     * 从LocalTime转换为Instant（使用系统默认时区）
     * <p>
     * 转换后的日期为Epoch（1970-01-01）
     *
     * @param localTime 要转换的LocalTime对象
     * @return 转换后的Instant对象，如果输入为null则返回null
     */
    /**
     * fromLocalTime方法。
     *      * @param localTime LocalTime类型参数
     * @return static Instant类型返回值
     */
    /**
     * fromLocalTime方法。
     *      * @param localTime LocalTime类型参数
     * @return static Instant类型返回值
     */
    public static Instant fromLocalTime(LocalTime localTime) {
        return TimeConverter.toInstant(localTime);
    }

    /**
     * 从LocalTime转换为Instant（使用指定时区）
     * <p>
     * 转换后的日期为Epoch（1970-01-01）
     *
     * @param localTime 要转换的LocalTime对象
     * @param zoneId    时区ID
     * @return 转换后的Instant对象，如果输入为null则返回null
     */
    /**
     * fromLocalTime方法。
     *      * @param localTime LocalTime类型参数
     * @param zoneId ZoneId类型参数
     * @return static Instant类型返回值
     */
    /**
     * fromLocalTime方法。
     *      * @param localTime LocalTime类型参数
     * @param zoneId ZoneId类型参数
     * @return static Instant类型返回值
     */
    public static Instant fromLocalTime(LocalTime localTime, ZoneId zoneId) {
        return TimeConverter.toInstant(localTime, zoneId);
    }

    /**
     * 从ZonedDateTime转换为Instant
     *
     * @param zonedDateTime 要转换的ZonedDateTime对象
     * @return 转换后的Instant对象，如果输入为null则返回null
     */
    /**
     * fromZonedDateTime方法。
     *      * @param zonedDateTime ZonedDateTime类型参数
     * @return static Instant类型返回值
     */
    /**
     * fromZonedDateTime方法。
     *      * @param zonedDateTime ZonedDateTime类型参数
     * @return static Instant类型返回值
     */
    public static Instant fromZonedDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant();
    }

    /**
     * 从毫秒值创建Instant
     *
     * @param epochMilli 自1970年1月1日以来的毫秒数
     * @return 对应的Instant对象
     */
    /**
     * fromEpochMilli方法。
     *      * @param epochMilli long类型参数
     * @return static Instant类型返回值
     */
    /**
     * fromEpochMilli方法。
     *      * @param epochMilli long类型参数
     * @return static Instant类型返回值
     */
    public static Instant fromEpochMilli(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli);
    }

    /**
     * 从秒值创建Instant
     *
     * @param epochSecond 自1970年1月1日以来的秒数
     * @return 对应的Instant对象
     */
    /**
     * fromEpochSecond方法。
     *      * @param epochSecond long类型参数
     * @return static Instant类型返回值
     */
    /**
     * fromEpochSecond方法。
     *      * @param epochSecond long类型参数
     * @return static Instant类型返回值
     */
    public static Instant fromEpochSecond(long epochSecond) {
        return Instant.ofEpochSecond(epochSecond);
    }
}
