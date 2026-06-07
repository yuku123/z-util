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
/**
 * LocalDateTimeUtil类。
 */
/**
 * LocalDateTimeUtil类。
 */
public class LocalDateTimeUtil {

    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_DATETIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_DATETIME_COMPACT = "yyyyMMddHHmmss";
    public static final String PATTERN_ISO = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String PATTERN_ISO_MS = "yyyy-MM-dd'T'HH:mm:ss.SSS";

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
    /**
     * DateTimeFormatter.ofPattern方法。
     *      * @param PATTERN_TIME Object类型参数
     * @return static final DateTimeFormatter FMT_TIME =类型返回值
     */
    /**
     * DateTimeFormatter.ofPattern方法。
     *      * @param PATTERN_TIME Object类型参数
     * @return static final DateTimeFormatter FMT_TIME =类型返回值
     */
    public static final DateTimeFormatter FMT_TIME = DateTimeFormatter.ofPattern(PATTERN_TIME);
    /**
     * DateTimeFormatter.ofPattern方法。
     *      * @param PATTERN_DATETIME_MS Object类型参数
     * @return static final DateTimeFormatter FMT_DATETIME_MS =类型返回值
     */
    /**
     * DateTimeFormatter.ofPattern方法。
     *      * @param PATTERN_DATETIME_MS Object类型参数
     * @return static final DateTimeFormatter FMT_DATETIME_MS =类型返回值
     */
    public static final DateTimeFormatter FMT_DATETIME_MS = DateTimeFormatter.ofPattern(PATTERN_DATETIME_MS);

    // ==================== 格式化 ====================

    /**
     * format方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static String类型返回值
     */
    /**
     * format方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static String类型返回值
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, FMT_DEFAULT);
    }

    /**
     * format方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param pattern String类型参数
     * @return static String类型返回值
     */
    /**
     * format方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param pattern String类型参数
     * @return static String类型返回值
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * format方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param formatter DateTimeFormatter类型参数
     * @return static String类型返回值
     */
    /**
     * format方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param formatter DateTimeFormatter类型参数
     * @return static String类型返回值
     */
    public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
        if (dateTime == null) return null;
        return dateTime.format(formatter);
    }

    // ==================== 解析 ====================

    /**
     * parse方法。
     *      * @param dateTimeStr String类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * parse方法。
     *      * @param dateTimeStr String类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime parse(String dateTimeStr) {
        return parse(dateTimeStr, PATTERN_DEFAULT);
    }

    /**
     * parse方法。
     *      * @param dateTimeStr String类型参数
     * @param pattern String类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * parse方法。
     *      * @param dateTimeStr String类型参数
     * @param pattern String类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime parse(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) return null;
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * parseStrict方法。
     *      * @param dateTimeStr String类型参数
     * @param patterns String...类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * parseStrict方法。
     *      * @param dateTimeStr String类型参数
     * @param patterns String...类型参数
     * @return static LocalDateTime类型返回值
     */
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

    /**
     * now方法。
     * @return static LocalDateTime类型返回值
     */
    /**
     * now方法。
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * todayStart方法。
     * @return static LocalDateTime类型返回值
     */
    /**
     * todayStart方法。
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime todayStart() {
        return LocalDate.now().atStartOfDay();
    }

    /**
     * todayEnd方法。
     * @return static LocalDateTime类型返回值
     */
    /**
     * todayEnd方法。
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime todayEnd() {
        return LocalDate.now().atTime(LocalTime.MAX);
    }

    /**
     * of方法。
     *      * @param year int类型参数
     * @param month int类型参数
     * @param dayOfMonth int类型参数
     * @param hour int类型参数
     * @param minute int类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * of方法。
     *      * @param year int类型参数
     * @param month int类型参数
     * @param dayOfMonth int类型参数
     * @param hour int类型参数
     * @param minute int类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute);
    }

    /**
     * of方法。
     *      * @param year int类型参数
     * @param month int类型参数
     * @param dayOfMonth int类型参数
     * @param hour int类型参数
     * @param minute int类型参数
     * @param second int类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * of方法。
     *      * @param year int类型参数
     * @param month int类型参数
     * @param dayOfMonth int类型参数
     * @param hour int类型参数
     * @param minute int类型参数
     * @param second int类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
    }

    /**
     * of方法。
     *      * @param year int类型参数
     * @param month int类型参数
     * @param dayOfMonth int类型参数
     * @param hour int类型参数
     * @param minute int类型参数
     * @param second int类型参数
     * @param nanoOfSecond int类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * of方法。
     *      * @param year int类型参数
     * @param month int类型参数
     * @param dayOfMonth int类型参数
     * @param hour int类型参数
     * @param minute int类型参数
     * @param second int类型参数
     * @param nanoOfSecond int类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond);
    }

    /**
     * of方法。
     *      * @param date LocalDate类型参数
     * @param time LocalTime类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * of方法。
     *      * @param date LocalDate类型参数
     * @param time LocalTime类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime of(LocalDate date, LocalTime time) {
        return date.atTime(time);
    }

    // ==================== 日期时间计算 ====================

    /**
     * plusYears方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param years long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * plusYears方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param years long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime plusYears(LocalDateTime dateTime, long years) {
        return dateTime.plusYears(years);
    }

    /**
     * minusYears方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param years long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * minusYears方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param years long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime minusYears(LocalDateTime dateTime, long years) {
        return dateTime.minusYears(years);
    }

    /**
     * plusMonths方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param months long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * plusMonths方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param months long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime plusMonths(LocalDateTime dateTime, long months) {
        return dateTime.plusMonths(months);
    }

    /**
     * minusMonths方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param months long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * minusMonths方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param months long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime minusMonths(LocalDateTime dateTime, long months) {
        return dateTime.minusMonths(months);
    }

    /**
     * plusDays方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param days long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * plusDays方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param days long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        return dateTime.plusDays(days);
    }

    /**
     * minusDays方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param days long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * minusDays方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param days long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime minusDays(LocalDateTime dateTime, long days) {
        return dateTime.minusDays(days);
    }

    /**
     * plusHours方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param hours long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * plusHours方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param hours long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        return dateTime.plusHours(hours);
    }

    /**
     * minusHours方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param hours long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * minusHours方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param hours long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime minusHours(LocalDateTime dateTime, long hours) {
        return dateTime.minusHours(hours);
    }

    /**
     * plusMinutes方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param minutes long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * plusMinutes方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param minutes long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime.plusMinutes(minutes);
    }

    /**
     * minusMinutes方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param minutes long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * minusMinutes方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param minutes long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime minusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime.minusMinutes(minutes);
    }

    /**
     * plusSeconds方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param seconds long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * plusSeconds方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param seconds long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime plusSeconds(LocalDateTime dateTime, long seconds) {
        return dateTime.plusSeconds(seconds);
    }

    /**
     * minusSeconds方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param seconds long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * minusSeconds方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param seconds long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime minusSeconds(LocalDateTime dateTime, long seconds) {
        return dateTime.minusSeconds(seconds);
    }

    // ==================== 日期调整 ====================

    /**
     * firstDayOfMonth方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * firstDayOfMonth方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime firstDayOfMonth(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * lastDayOfMonth方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * lastDayOfMonth方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime lastDayOfMonth(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * firstDayOfYear方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * firstDayOfYear方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime firstDayOfYear(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * lastDayOfYear方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * lastDayOfYear方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime lastDayOfYear(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * firstDayOfWeek方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * firstDayOfWeek方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime firstDayOfWeek(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /**
     * lastDayOfWeek方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * lastDayOfWeek方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime lastDayOfWeek(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }

    /**
     * withHour方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param hour int类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * withHour方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param hour int类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime withHour(LocalDateTime dateTime, int hour) {
        return dateTime.withHour(hour);
    }

    /**
     * withMinute方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param minute int类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * withMinute方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param minute int类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime withMinute(LocalDateTime dateTime, int minute) {
        return dateTime.withMinute(minute);
    }

    /**
     * withSecond方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param second int类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * withSecond方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param second int类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime withSecond(LocalDateTime dateTime, int second) {
        return dateTime.withSecond(second);
    }

    // ==================== 日期时间比较 ====================

    /**
     * isBefore方法。
     *      * @param dateTime1 LocalDateTime类型参数
     * @param dateTime2 LocalDateTime类型参数
     * @return static boolean类型返回值
     */
    /**
     * isBefore方法。
     *      * @param dateTime1 LocalDateTime类型参数
     * @param dateTime2 LocalDateTime类型参数
     * @return static boolean类型返回值
     */
    public static boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isBefore(dateTime2);
    }

    /**
     * isAfter方法。
     *      * @param dateTime1 LocalDateTime类型参数
     * @param dateTime2 LocalDateTime类型参数
     * @return static boolean类型返回值
     */
    /**
     * isAfter方法。
     *      * @param dateTime1 LocalDateTime类型参数
     * @param dateTime2 LocalDateTime类型参数
     * @return static boolean类型返回值
     */
    public static boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2);
    }

    /**
     * isEqual方法。
     *      * @param dateTime1 LocalDateTime类型参数
     * @param dateTime2 LocalDateTime类型参数
     * @return static boolean类型返回值
     */
    /**
     * isEqual方法。
     *      * @param dateTime1 LocalDateTime类型参数
     * @param dateTime2 LocalDateTime类型参数
     * @return static boolean类型返回值
     */
    public static boolean isEqual(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isEqual(dateTime2);
    }

    /**
     * isSameDay方法。
     *      * @param dateTime1 LocalDateTime类型参数
     * @param dateTime2 LocalDateTime类型参数
     * @return static boolean类型返回值
     */
    /**
     * isSameDay方法。
     *      * @param dateTime1 LocalDateTime类型参数
     * @param dateTime2 LocalDateTime类型参数
     * @return static boolean类型返回值
     */
    public static boolean isSameDay(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate());
    }

    // ==================== 日期时间差 ====================

    /**
     * daysBetween方法。
     *      * @param start LocalDateTime类型参数
     * @param end LocalDateTime类型参数
     * @return static long类型返回值
     */
    /**
     * daysBetween方法。
     *      * @param start LocalDateTime类型参数
     * @param end LocalDateTime类型参数
     * @return static long类型返回值
     */
    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }

    /**
     * hoursBetween方法。
     *      * @param start LocalDateTime类型参数
     * @param end LocalDateTime类型参数
     * @return static long类型返回值
     */
    /**
     * hoursBetween方法。
     *      * @param start LocalDateTime类型参数
     * @param end LocalDateTime类型参数
     * @return static long类型返回值
     */
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.HOURS.between(start, end);
    }

    /**
     * minutesBetween方法。
     *      * @param start LocalDateTime类型参数
     * @param end LocalDateTime类型参数
     * @return static long类型返回值
     */
    /**
     * minutesBetween方法。
     *      * @param start LocalDateTime类型参数
     * @param end LocalDateTime类型参数
     * @return static long类型返回值
     */
    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.MINUTES.between(start, end);
    }

    /**
     * secondsBetween方法。
     *      * @param start LocalDateTime类型参数
     * @param end LocalDateTime类型参数
     * @return static long类型返回值
     */
    /**
     * secondsBetween方法。
     *      * @param start LocalDateTime类型参数
     * @param end LocalDateTime类型参数
     * @return static long类型返回值
     */
    public static long secondsBetween(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.SECONDS.between(start, end);
    }

    /**
     * millisBetween方法。
     *      * @param start LocalDateTime类型参数
     * @param end LocalDateTime类型参数
     * @return static long类型返回值
     */
    /**
     * millisBetween方法。
     *      * @param start LocalDateTime类型参数
     * @param end LocalDateTime类型参数
     * @return static long类型返回值
     */
    public static long millisBetween(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.MILLIS.between(start, end);
    }

    // ==================== 获取日期时间部分 ====================

    /**
     * getYear方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static int类型返回值
     */
    /**
     * getYear方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static int类型返回值
     */
    public static int getYear(LocalDateTime dateTime) {
        return dateTime.getYear();
    }

    /**
     * getMonth方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static int类型返回值
     */
    /**
     * getMonth方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static int类型返回值
     */
    public static int getMonth(LocalDateTime dateTime) {
        return dateTime.getMonthValue();
    }

    /**
     * getDayOfMonth方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static int类型返回值
     */
    /**
     * getDayOfMonth方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static int类型返回值
     */
    public static int getDayOfMonth(LocalDateTime dateTime) {
        return dateTime.getDayOfMonth();
    }

    /**
     * getHour方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static int类型返回值
     */
    /**
     * getHour方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static int类型返回值
     */
    public static int getHour(LocalDateTime dateTime) {
        return dateTime.getHour();
    }

    /**
     * getMinute方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static int类型返回值
     */
    /**
     * getMinute方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static int类型返回值
     */
    public static int getMinute(LocalDateTime dateTime) {
        return dateTime.getMinute();
    }

    /**
     * getSecond方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static int类型返回值
     */
    /**
     * getSecond方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static int类型返回值
     */
    public static int getSecond(LocalDateTime dateTime) {
        return dateTime.getSecond();
    }

    /**
     * getDayOfWeek方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static DayOfWeek类型返回值
     */
    /**
     * getDayOfWeek方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static DayOfWeek类型返回值
     */
    public static DayOfWeek getDayOfWeek(LocalDateTime dateTime) {
        return dateTime.getDayOfWeek();
    }

    // ==================== 转换到其他类型 ====================

    /**
     * toDate方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static Date类型返回值
     */
    /**
     * toDate方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static Date类型返回值
     */
    public static Date toDate(LocalDateTime dateTime) {
        return TimeConverter.toDate(dateTime);
    }

    /**
     * toDate方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param zoneId ZoneId类型参数
     * @return static Date类型返回值
     */
    /**
     * toDate方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param zoneId ZoneId类型参数
     * @return static Date类型返回值
     */
    public static Date toDate(LocalDateTime dateTime, ZoneId zoneId) {
        return TimeConverter.toDate(dateTime, zoneId);
    }

    /**
     * toLocalDate方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDate类型返回值
     */
    /**
     * toLocalDate方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalDate类型返回值
     */
    public static LocalDate toLocalDate(LocalDateTime dateTime) {
        return TimeConverter.toLocalDate(dateTime);
    }

    /**
     * toLocalTime方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalTime类型返回值
     */
    /**
     * toLocalTime方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static LocalTime类型返回值
     */
    public static LocalTime toLocalTime(LocalDateTime dateTime) {
        return TimeConverter.toLocalTime(dateTime);
    }

    /**
     * toInstant方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static Instant类型返回值
     */
    /**
     * toInstant方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static Instant类型返回值
     */
    public static Instant toInstant(LocalDateTime dateTime) {
        return TimeConverter.toInstant(dateTime);
    }

    /**
     * toInstant方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param zoneId ZoneId类型参数
     * @return static Instant类型返回值
     */
    /**
     * toInstant方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param zoneId ZoneId类型参数
     * @return static Instant类型返回值
     */
    public static Instant toInstant(LocalDateTime dateTime, ZoneId zoneId) {
        return TimeConverter.toInstant(dateTime, zoneId);
    }

    /**
     * toEpochMilli方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static long类型返回值
     */
    /**
     * toEpochMilli方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static long类型返回值
     */
    public static long toEpochMilli(LocalDateTime dateTime) {
        return TimeConverter.toEpochMilli(dateTime);
    }

    /**
     * toEpochSecond方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static long类型返回值
     */
    /**
     * toEpochSecond方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static long类型返回值
     */
    public static long toEpochSecond(LocalDateTime dateTime) {
        return TimeConverter.toEpochSecond(dateTime);
    }

    /**
     * toZonedDateTime方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static ZonedDateTime类型返回值
     */
    /**
     * toZonedDateTime方法。
     *      * @param dateTime LocalDateTime类型参数
     * @return static ZonedDateTime类型返回值
     */
    public static ZonedDateTime toZonedDateTime(LocalDateTime dateTime) {
        return TimeConverter.toZonedDateTime(dateTime);
    }

    /**
     * toZonedDateTime方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param zoneId ZoneId类型参数
     * @return static ZonedDateTime类型返回值
     */
    /**
     * toZonedDateTime方法。
     *      * @param dateTime LocalDateTime类型参数
     * @param zoneId ZoneId类型参数
     * @return static ZonedDateTime类型返回值
     */
    public static ZonedDateTime toZonedDateTime(LocalDateTime dateTime, ZoneId zoneId) {
        return TimeConverter.toZonedDateTime(dateTime, zoneId);
    }

    // ==================== 从其他类型转换 ====================

    /**
     * fromDate方法。
     *      * @param date Date类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * fromDate方法。
     *      * @param date Date类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime fromDate(Date date) {
        return TimeConverter.toLocalDateTime(date);
    }

    /**
     * fromDate方法。
     *      * @param date Date类型参数
     * @param zoneId ZoneId类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * fromDate方法。
     *      * @param date Date类型参数
     * @param zoneId ZoneId类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime fromDate(Date date, ZoneId zoneId) {
        return TimeConverter.toLocalDateTime(date, zoneId);
    }

    /**
     * fromLocalDate方法。
     *      * @param date LocalDate类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * fromLocalDate方法。
     *      * @param date LocalDate类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime fromLocalDate(LocalDate date) {
        return TimeConverter.toLocalDateTime(date);
    }

    /**
     * fromLocalDate方法。
     *      * @param date LocalDate类型参数
     * @param localTime LocalTime类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * fromLocalDate方法。
     *      * @param date LocalDate类型参数
     * @param localTime LocalTime类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime fromLocalDate(LocalDate date, LocalTime localTime) {
        return TimeConverter.toLocalDateTime(date, localTime);
    }

    /**
     * fromLocalTime方法。
     *      * @param localTime LocalTime类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * fromLocalTime方法。
     *      * @param localTime LocalTime类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime fromLocalTime(LocalTime localTime) {
        return TimeConverter.toLocalDateTime(localTime);
    }

    /**
     * fromInstant方法。
     *      * @param instant Instant类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * fromInstant方法。
     *      * @param instant Instant类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime fromInstant(Instant instant) {
        return TimeConverter.toLocalDateTime(instant);
    }

    /**
     * fromInstant方法。
     *      * @param instant Instant类型参数
     * @param zoneId ZoneId类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * fromInstant方法。
     *      * @param instant Instant类型参数
     * @param zoneId ZoneId类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime fromInstant(Instant instant, ZoneId zoneId) {
        return TimeConverter.toLocalDateTime(instant, zoneId);
    }

    /**
     * fromEpochMilli方法。
     *      * @param epochMilli long类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * fromEpochMilli方法。
     *      * @param epochMilli long类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime fromEpochMilli(long epochMilli) {
        return TimeConverter.fromEpochMilli(epochMilli);
    }

    /**
     * fromZonedDateTime方法。
     *      * @param zonedDateTime ZonedDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    /**
     * fromZonedDateTime方法。
     *      * @param zonedDateTime ZonedDateTime类型参数
     * @return static LocalDateTime类型返回值
     */
    public static LocalDateTime fromZonedDateTime(ZonedDateTime zonedDateTime) {
        return TimeConverter.toLocalDateTime(zonedDateTime);
    }
}