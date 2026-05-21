package com.zifang.util.core.time;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

import static org.junit.Assert.*;

public class DurationUtilTest {

    // ===== Duration 创建 =====

    @Test
    public void testOfMillis() {
        Duration d = DurationUtil.ofMillis(1500);
        assertEquals(1500, d.toMillis());
    }

    @Test
    public void testOfSeconds() {
        Duration d = DurationUtil.ofSeconds(65);
        assertEquals(65, d.getSeconds());
    }

    @Test
    public void testBetween() {
        java.time.LocalDateTime start = java.time.LocalDateTime.of(2024, 6, 15, 10, 0, 0);
        java.time.LocalDateTime end = java.time.LocalDateTime.of(2024, 6, 15, 10, 1, 0);
        Duration d = DurationUtil.between(start, end);
        assertEquals(60, d.getSeconds());
    }

    // ===== Duration 格式化 =====

    @Test
    public void testFormat() {
        Duration d = Duration.ofSeconds(125);
        String result = DurationUtil.format(d);
        assertNotNull(result);
        assertTrue(result.contains("秒"));
    }

    @Test
    public void testFormatHMS() {
        Duration d = Duration.ofHours(1).plusMinutes(5).plusSeconds(30);
        String result = DurationUtil.formatHMS(d);
        assertNotNull(result);
        assertTrue(result.contains("01:05:30") || result.equals("01:05:30"));
    }

    @Test
    public void testFormatDHMS() {
        Duration d = Duration.ofDays(1).plusHours(2).plusMinutes(30).plusSeconds(15);
        String result = DurationUtil.formatDHMS(d);
        assertNotNull(result);
        assertTrue(result.contains("天"));
    }

    @Test
    public void testFormatNull() {
        assertNull(DurationUtil.format((Duration) null));
        assertNull(DurationUtil.formatHMS(null));
    }

    // ===== Duration 计算 =====

    @Test
    public void testPlusMillis() {
        Duration base = Duration.ofSeconds(10);
        Duration result = DurationUtil.plusMillis(base, 500);
        assertEquals(10500, result.toMillis());
    }

    @Test
    public void testMinusMillis() {
        Duration base = Duration.ofSeconds(10);
        Duration result = DurationUtil.minusMillis(base, 500);
        assertEquals(9500, result.toMillis());
    }

    @Test
    public void testMultipliedBy() {
        Duration base = Duration.ofSeconds(10);
        Duration result = DurationUtil.multipliedBy(base, 3);
        assertEquals(30, result.getSeconds());
    }

    @Test
    public void testDividedBy() {
        Duration base = Duration.ofSeconds(30);
        Duration result = DurationUtil.dividedBy(base, 3);
        assertEquals(10, result.getSeconds());
    }

    @Test
    public void testAbs() {
        Duration negative = Duration.ofSeconds(-10);
        Duration result = DurationUtil.abs(negative);
        assertEquals(10, result.getSeconds());
        assertFalse(result.isNegative());
    }

    // ===== Duration 比较 =====

    @Test
    public void testIsBefore() {
        Duration shorter = Duration.ofSeconds(10);
        Duration longer = Duration.ofSeconds(20);
        assertTrue(DurationUtil.isBefore(shorter, longer));
        assertFalse(DurationUtil.isBefore(longer, shorter));
    }

    @Test
    public void testIsAfter() {
        Duration shorter = Duration.ofSeconds(10);
        Duration longer = Duration.ofSeconds(20);
        assertTrue(DurationUtil.isAfter(longer, shorter));
        assertFalse(DurationUtil.isAfter(shorter, longer));
    }

    @Test
    public void testIsNegative() {
        assertTrue(DurationUtil.isNegative(Duration.ofSeconds(-1)));
        assertFalse(DurationUtil.isNegative(Duration.ofSeconds(1)));
        assertFalse(DurationUtil.isNegative(Duration.ZERO));
    }

    @Test
    public void testIsZero() {
        assertTrue(DurationUtil.isZero(Duration.ZERO));
        assertFalse(DurationUtil.isZero(Duration.ofSeconds(1)));
    }

    // ===== Duration 转换 =====

    @Test
    public void testToMillis() {
        Duration d = Duration.ofSeconds(5);
        assertEquals(5000, DurationUtil.toMillis(d));
    }

    @Test
    public void testToSeconds() {
        Duration d = Duration.ofMillis(5500);
        assertEquals(5, DurationUtil.toSeconds(d));
    }

    @Test
    public void testToMinutes() {
        Duration d = Duration.ofMinutes(5);
        assertEquals(5, DurationUtil.toMinutes(d));
    }

    @Test
    public void testToHours() {
        Duration d = Duration.ofHours(3);
        assertEquals(3, DurationUtil.toHours(d));
    }

    @Test
    public void testToDays() {
        Duration d = Duration.ofDays(2);
        assertEquals(2, DurationUtil.toDays(d));
    }

    @Test
    public void testToNanos() {
        Duration d = Duration.ofMillis(1);
        assertTrue(DurationUtil.toNanos(d) > 0);
    }

    @Test
    public void testNullHandling() {
        assertEquals(0, DurationUtil.toMillis(null));
        assertEquals(0, DurationUtil.toSeconds(null));
        assertEquals(0, DurationUtil.toMinutes(null));
    }

    // ===== Period 创建 =====

    @Test
    public void testPeriodOfDays() {
        Period p = DurationUtil.ofDays(10);
        assertEquals(10, p.getDays());
    }

    @Test
    public void testPeriodOfMonths() {
        Period p = DurationUtil.ofMonths(3);
        assertEquals(3, p.getMonths());
    }

    @Test
    public void testPeriodOfYears() {
        Period p = DurationUtil.ofYears(2);
        assertEquals(2, p.getYears());
    }

    @Test
    public void testPeriodBetween() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 6, 15);
        Period p = DurationUtil.between(start, end);
        assertNotNull(p);
        assertTrue(p.getDays() >= 0 || p.getMonths() > 0);
    }

    // ===== Period 格式化 =====

    @Test
    public void testPeriodFormat() {
        Period p = Period.of(1, 2, 15);
        String result = DurationUtil.format(p);
        assertNotNull(result);
        assertTrue(result.contains("年") || result.contains("月") || result.contains("日"));
    }

    @Test
    public void testPeriodFormatYMD() {
        Period p = Period.of(1, 2, 15);
        String result = DurationUtil.formatYMD(p);
        assertNotNull(result);
        assertTrue(result.contains("1年"));
        assertTrue(result.contains("2月"));
        assertTrue(result.contains("15日"));
    }

    @Test
    public void testPeriodFormatNull() {
        assertNull(DurationUtil.format((Period) null));
        assertNull(DurationUtil.formatYMD(null));
    }

    // ===== Period 计算 =====

    @Test
    public void testPeriodPlusDays() {
        Period p = Period.of(0, 1, 10);
        Period result = DurationUtil.plusDays(p, 5);
        assertEquals(15, result.getDays());
    }

    @Test
    public void testPeriodMinusDays() {
        Period p = Period.of(0, 1, 10);
        Period result = DurationUtil.minusDays(p, 3);
        assertEquals(7, result.getDays());
    }

    @Test
    public void testPeriodMultipliedBy() {
        Period p = Period.of(1, 2, 10);
        Period result = DurationUtil.multipliedBy(p, 2);
        assertEquals(2, result.getYears());
        assertEquals(4, result.getMonths());
        assertEquals(20, result.getDays());
    }

    @Test
    public void testPeriodNormalized() {
        Period p = Period.of(1, 15, 0);
        Period result = DurationUtil.normalized(p);
        assertEquals(2, result.getYears());
        assertEquals(3, result.getMonths());
    }

    // ===== Period 转换 =====

    @Test
    public void testPeriodToDays() {
        Period p = Period.of(1, 2, 15);
        long days = DurationUtil.toDays(p);
        assertEquals(365 + 60 + 15, days);
    }

    @Test
    public void testGetPeriodParts() {
        Period p = Period.of(1, 2, 15);
        assertEquals(1, DurationUtil.getYears(p));
        assertEquals(2, DurationUtil.getMonths(p));
        assertEquals(15, DurationUtil.getDays(p));
    }
}
