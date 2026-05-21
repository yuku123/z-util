package com.zifang.util.core.time;

import org.junit.Test;

import java.time.*;
import java.util.Date;

import static org.junit.Assert.*;

public class ZonedDateTimeUtilTest {

    @Test
    public void testFormat() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 15, 10, 30, 0, 0, ZoneId.of("Asia/Shanghai"));
        String result = ZonedDateTimeUtil.format(zdt);
        assertNotNull(result);
        assertTrue(result.contains("2024"));
    }

    @Test
    public void testFormatWithPattern() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 15, 10, 30, 0, 0, ZoneId.of("Asia/Shanghai"));
        String result = ZonedDateTimeUtil.format(zdt, "yyyy-MM-dd");
        assertEquals("2024-06-15", result);
    }

    @Test
    public void testFormatWithZone() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 15, 10, 30, 0, 0, ZoneId.of("Asia/Shanghai"));
        String result = ZonedDateTimeUtil.format(zdt, ZoneId.of("UTC"), "yyyy-MM-dd HH:mm:ss");
        assertNotNull(result);
    }

    @Test
    public void testParse() {
        ZonedDateTime zdt = ZonedDateTimeUtil.parse("2024-06-15 10:30:00", "yyyy-MM-dd HH:mm:ss");
        assertNotNull(zdt);
        assertEquals(2024, zdt.getYear());
        assertEquals(6, zdt.getMonthValue());
        assertEquals(15, zdt.getDayOfMonth());
    }

    @Test
    public void testParseNull() {
        assertNull(ZonedDateTimeUtil.parse(null));
        assertNull(ZonedDateTimeUtil.parse(""));
    }

    @Test
    public void testNow() {
        ZonedDateTime now = ZonedDateTimeUtil.now();
        assertNotNull(now);
        assertNotNull(now.getZone());
    }

    @Test
    public void testNowWithZone() {
        ZoneId zone = ZoneId.of("America/New_York");
        ZonedDateTime now = ZonedDateTimeUtil.now(zone);
        assertNotNull(now);
        assertEquals(zone, now.getZone());
    }

    @Test
    public void testOf() {
        ZonedDateTime zdt = ZonedDateTimeUtil.of(LocalDateTime.of(2024, 6, 15, 10, 30, 0), ZoneId.of("UTC"));
        assertNotNull(zdt);
        assertEquals(2024, zdt.getYear());
        assertEquals(ZoneId.of("UTC"), zdt.getZone());
    }

    @Test
    public void testPlusMinus() {
        ZonedDateTime base = ZonedDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime plus = ZonedDateTimeUtil.plusDays(base, 5);
        ZonedDateTime minus = ZonedDateTimeUtil.minusDays(base, 5);
        assertEquals(base.plusDays(5), plus);
        assertEquals(base.minusDays(5), minus);
    }

    @Test
    public void testFirstLastDayOfMonth() {
        ZonedDateTime mid = ZonedDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime first = ZonedDateTimeUtil.firstDayOfMonth(mid);
        ZonedDateTime last = ZonedDateTimeUtil.lastDayOfMonth(mid);
        assertEquals(1, first.getDayOfMonth());
        assertEquals(30, last.getDayOfMonth());
    }

    @Test
    public void testWithZoneSameInstant() {
        ZonedDateTime shanghai = ZonedDateTime.of(2024, 6, 15, 18, 0, 0, 0, ZoneId.of("Asia/Shanghai"));
        ZonedDateTime utc = ZonedDateTimeUtil.withZoneSameInstant(shanghai, ZoneId.of("UTC"));
        assertEquals(shanghai.toInstant(), utc.toInstant());
        assertEquals(ZoneId.of("UTC"), utc.getZone());
    }

    @Test
    public void testCompare() {
        ZonedDateTime earlier = ZonedDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime later = ZonedDateTime.of(2024, 6, 15, 11, 0, 0, 0, ZoneId.of("UTC"));
        assertTrue(ZonedDateTimeUtil.isBefore(earlier, later));
        assertTrue(ZonedDateTimeUtil.isAfter(later, earlier));
        assertTrue(ZonedDateTimeUtil.isEqual(earlier, earlier));
    }

    @Test
    public void testIsSameDay() {
        ZonedDateTime morning = ZonedDateTime.of(2024, 6, 15, 8, 0, 0, 0, ZoneId.of("Asia/Shanghai"));
        ZonedDateTime evening = ZonedDateTime.of(2024, 6, 15, 20, 0, 0, 0, ZoneId.of("Asia/Shanghai"));
        assertTrue(ZonedDateTimeUtil.isSameDay(morning, evening));
    }

    @Test
    public void testDaysBetween() {
        ZonedDateTime start = ZonedDateTime.of(2024, 6, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime end = ZonedDateTime.of(2024, 6, 15, 0, 0, 0, 0, ZoneId.of("UTC"));
        assertEquals(14, ZonedDateTimeUtil.daysBetween(start, end));
    }

    @Test
    public void testGetParts() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 15, 10, 30, 45, 0, ZoneId.of("Asia/Shanghai"));
        assertEquals(2024, ZonedDateTimeUtil.getYear(zdt));
        assertEquals(6, ZonedDateTimeUtil.getMonth(zdt));
        assertEquals(15, ZonedDateTimeUtil.getDayOfMonth(zdt));
        assertEquals(10, ZonedDateTimeUtil.getHour(zdt));
        assertEquals(30, ZonedDateTimeUtil.getMinute(zdt));
        assertEquals(45, ZonedDateTimeUtil.getSecond(zdt));
        assertEquals(DayOfWeek.SATURDAY, ZonedDateTimeUtil.getDayOfWeek(zdt));
    }

    @Test
    public void testToDate() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneId.of("UTC"));
        Date date = ZonedDateTimeUtil.toDate(zdt);
        assertNotNull(date);
        assertEquals(zdt.toInstant().toEpochMilli(), date.getTime());
    }

    @Test
    public void testToLocalDateTime() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 15, 10, 30, 0, 0, ZoneId.of("Asia/Shanghai"));
        LocalDateTime ldt = ZonedDateTimeUtil.toLocalDateTime(zdt);
        assertNotNull(ldt);
        assertEquals(2024, ldt.getYear());
        assertEquals(6, ldt.getMonthValue());
    }

    @Test
    public void testToLocalDate() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 15, 10, 30, 0, 0, ZoneId.of("Asia/Shanghai"));
        LocalDate ld = ZonedDateTimeUtil.toLocalDate(zdt);
        assertEquals(2024, ld.getYear());
        assertEquals(6, ld.getMonthValue());
        assertEquals(15, ld.getDayOfMonth());
    }

    @Test
    public void testToInstant() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneId.of("UTC"));
        Instant instant = ZonedDateTimeUtil.toInstant(zdt);
        assertEquals(zdt.toInstant(), instant);
    }

    @Test
    public void testFromDate() {
        Date date = new Date(1718455200000L);
        ZonedDateTime zdt = ZonedDateTimeUtil.fromDate(date);
        assertNotNull(zdt);
    }

    @Test
    public void testFromLocalDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
        ZonedDateTime zdt = ZonedDateTimeUtil.fromLocalDateTime(ldt);
        assertNotNull(zdt);
        assertEquals(ldt, zdt.toLocalDateTime());
    }

    @Test
    public void testFromInstant() {
        Instant instant = Instant.parse("2024-06-15T10:00:00Z");
        ZonedDateTime zdt = ZonedDateTimeUtil.fromInstant(instant);
        assertNotNull(zdt);
        assertEquals(instant, zdt.toInstant());
    }

    @Test
    public void testFromEpochMilli() {
        ZonedDateTime zdt = ZonedDateTimeUtil.fromEpochMilli(1718455200000L);
        assertNotNull(zdt);
        assertEquals(1718455200000L, zdt.toInstant().toEpochMilli());
    }

    @Test
    public void testNullHandling() {
        assertNull(ZonedDateTimeUtil.format(null));
        assertNull(ZonedDateTimeUtil.parse(null, "yyyy-MM-dd"));
    }
}
