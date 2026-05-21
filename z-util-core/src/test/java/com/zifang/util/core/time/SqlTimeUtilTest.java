package com.zifang.util.core.time;

import org.junit.Test;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.Assert.*;

public class SqlTimeUtilTest {

    @Test
    public void testFormat() {
        Time time = Time.valueOf("10:30:45");
        String result = SqlTimeUtil.format(time);
        assertEquals("10:30:45", result);
    }

    @Test
    public void testFormatWithPattern() {
        Time time = Time.valueOf("10:30:45");
        String result = SqlTimeUtil.format(time, "HHmmss");
        assertEquals("103045", result);
    }

    @Test
    public void testFormatNull() {
        assertNull(SqlTimeUtil.format(null));
        assertNull(SqlTimeUtil.format(null, "HH:mm:ss"));
    }

    @Test
    public void testParse() {
        Time time = SqlTimeUtil.parse("10:30:45");
        assertNotNull(time);
        assertEquals(10, time.toLocalTime().getHour());
        assertEquals(30, time.toLocalTime().getMinute());
        assertEquals(45, time.toLocalTime().getSecond());
    }

    @Test
    public void testParseShort() {
        Time time = SqlTimeUtil.parse("10:30", "HH:mm");
        assertNotNull(time);
        assertEquals(10, time.toLocalTime().getHour());
        assertEquals(30, time.toLocalTime().getMinute());
    }

    @Test
    public void testParseNull() {
        assertNull(SqlTimeUtil.parse(null));
        assertNull(SqlTimeUtil.parse(""));
    }

    @Test
    public void testNow() {
        Time now = SqlTimeUtil.now();
        assertNotNull(now);
        assertTrue(now.getTime() > 0);
    }

    @Test
    public void testPlusHours() {
        Time base = Time.valueOf("10:00:00");
        Time plus = SqlTimeUtil.plusHours(base, 2);
        Time minus = SqlTimeUtil.minusHours(base, 2);
        assertEquals(Time.valueOf("12:00:00"), plus);
        assertEquals(Time.valueOf("08:00:00"), minus);
    }

    @Test
    public void testPlusMinutes() {
        Time base = Time.valueOf("10:30:00");
        Time plus = SqlTimeUtil.plusMinutes(base, 15);
        Time minus = SqlTimeUtil.minusMinutes(base, 15);
        assertEquals(Time.valueOf("10:45:00"), plus);
        assertEquals(Time.valueOf("10:15:00"), minus);
    }

    @Test
    public void testPlusSeconds() {
        Time base = Time.valueOf("10:30:45");
        Time plus = SqlTimeUtil.plusSeconds(base, 15);
        Time minus = SqlTimeUtil.minusSeconds(base, 15);
        assertEquals(Time.valueOf("10:31:00"), plus);
        assertEquals(Time.valueOf("10:30:30"), minus);
    }

    @Test
    public void testCompare() {
        Time earlier = Time.valueOf("10:00:00");
        Time later = Time.valueOf("15:00:00");
        assertTrue(SqlTimeUtil.isBefore(earlier, later));
        assertTrue(SqlTimeUtil.isAfter(later, earlier));
    }

    @Test
    public void testMillisBetween() {
        Time start = Time.valueOf("10:00:00");
        Time end = Time.valueOf("10:01:00");
        assertEquals(60000, SqlTimeUtil.millisBetween(start, end));
    }

    @Test
    public void testSecondsBetween() {
        Time start = Time.valueOf("10:00:00");
        Time end = Time.valueOf("10:01:00");
        assertEquals(60, SqlTimeUtil.secondsBetween(start, end));
    }

    @Test
    public void testMinutesBetween() {
        Time start = Time.valueOf("10:00:00");
        Time end = Time.valueOf("11:00:00");
        assertEquals(60, SqlTimeUtil.minutesBetween(start, end));
    }

    @Test
    public void testHoursBetween() {
        Time start = Time.valueOf("10:00:00");
        Time end = Time.valueOf("13:00:00");
        assertEquals(3, SqlTimeUtil.hoursBetween(start, end));
    }

    @Test
    public void testGetParts() {
        Time time = Time.valueOf("10:30:45");
        assertEquals(10, SqlTimeUtil.getHour(time));
        assertEquals(30, SqlTimeUtil.getMinute(time));
        assertEquals(45, SqlTimeUtil.getSecond(time));
    }

    @Test
    public void testToLocalTime() {
        Time time = Time.valueOf("10:30:45");
        LocalTime lt = SqlTimeUtil.toLocalTime(time);
        assertNotNull(lt);
        assertEquals(10, lt.getHour());
        assertEquals(30, lt.getMinute());
        assertEquals(45, lt.getSecond());
    }

    @Test
    public void testToLocalDate() {
        Time time = Time.valueOf("10:30:45");
        LocalDate ld = SqlTimeUtil.toLocalDate(time);
        assertNotNull(ld);
    }

    @Test
    public void testToLocalDateTime() {
        Time time = Time.valueOf("10:30:45");
        LocalDateTime ldt = SqlTimeUtil.toLocalDateTime(time);
        assertNotNull(ldt);
        assertEquals(10, ldt.getHour());
        assertEquals(30, ldt.getMinute());
        assertEquals(45, ldt.getSecond());
    }

    @Test
    public void testToDate() {
        Time time = Time.valueOf("10:30:45");
        Date date = SqlTimeUtil.toDate(time);
        assertNotNull(date);
        assertEquals(time.getTime(), date.getTime());
    }

    @Test
    public void testToInstant() {
        Time time = Time.valueOf("10:30:45");
        Instant instant = SqlTimeUtil.toInstant(time);
        assertNotNull(instant);
        assertEquals(Instant.ofEpochMilli(time.getTime()), instant);
    }

    @Test
    public void testFromLocalTime() {
        LocalTime lt = LocalTime.of(10, 30, 45);
        Time time = SqlTimeUtil.fromLocalTime(lt);
        assertNotNull(time);
        assertEquals(lt, time.toLocalTime());
    }

    @Test
    public void testFromLocalDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2024, 6, 15, 10, 30, 45);
        Time time = SqlTimeUtil.fromLocalDateTime(ldt);
        assertNotNull(time);
        assertEquals(LocalTime.of(10, 30, 45), time.toLocalTime());
    }

    @Test
    public void testFromDate() {
        Date date = new Date(1718455200000L);
        Time time = SqlTimeUtil.fromDate(date);
        assertNotNull(time);
        assertEquals(date.getTime(), time.getTime());
    }

    @Test
    public void testFromInstant() {
        Instant instant = Instant.parse("2024-06-15T10:00:00Z");
        Time time = SqlTimeUtil.fromInstant(instant);
        assertNotNull(time);
    }

    @Test
    public void testFromEpochMilli() {
        Time time = SqlTimeUtil.fromEpochMilli(1718455200000L);
        assertNotNull(time);
        assertEquals(1718455200000L, time.getTime());
    }

    @Test
    public void testNullHandling() {
        assertNull(SqlTimeUtil.toLocalTime(null));
        assertNull(SqlTimeUtil.toLocalDate(null));
        assertNull(SqlTimeUtil.toLocalDateTime(null));
        assertNull(SqlTimeUtil.toDate(null));
        assertNull(SqlTimeUtil.toInstant(null));
        assertNull(SqlTimeUtil.fromLocalTime(null));
        assertNull(SqlTimeUtil.fromLocalDateTime(null));
        assertNull(SqlTimeUtil.fromDate(null));
        assertNull(SqlTimeUtil.fromInstant(null));
        assertEquals(0, SqlTimeUtil.toEpochMilli(null));
    }
}
