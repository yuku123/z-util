package com.zifang.util.core.lang;

import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.*;

public class DateUtilTest {

    // --- format and parse ---

    @Test
    public void testFormat_WithValidDate() {
        Date date = new Date(0);
        String result = DateUtil.format(date, DateUtil.DATE_FORMAT_WHIFFLETREE_SECOND);
        assertEquals("1970-01-01 08:00:00", result);
    }

    @Test
    public void testFormat_WithDayFormat() {
        Date date = new Date(0);
        String result = DateUtil.format(date, DateUtil.DATE_FORMAT_WHIFFLETREE_DAY);
        assertEquals("1970-01-01", result);
    }

    @Test
    public void testParse_WithValidString() throws ParseException {
        Date date = DateUtil.parse("2023-01-01 12:00:00", DateUtil.DATE_FORMAT_WHIFFLETREE_SECOND);
        assertNotNull(date);
        assertEquals(2023 - 1900, date.getYear());
        assertEquals(0, date.getMonth());
        assertEquals(1, date.getDate());
    }

    @Test(expected = ParseException.class)
    public void testParse_WithInvalidString() throws ParseException {
        DateUtil.parse("invalid-date", DateUtil.DATE_FORMAT_WHIFFLETREE_SECOND);
    }

    // --- getNextDayStart ---

    @Test
    public void testGetNextDayStart() {
        String result = DateUtil.getNextDayStart();
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2} 00:00:00"));
    }

    // --- getTodayEnd ---

    @Test
    public void testGetTodayEnd() {
        String result = DateUtil.getTodayEnd();
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    // --- getTodayStart ---

    @Test
    public void testGetTodayStart() {
        String result = DateUtil.getTodayStart();
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2} 00:00:00"));
    }

    // --- getDayStart ---

    @Test
    public void testGetDayStart_Today() {
        LocalDateTime result = DateUtil.getDayStart(0);
        assertNotNull(result);
        assertEquals(LocalTime.of(0, 0, 0), result.toLocalTime());
    }

    @Test
    public void testGetDayStart_Tomorrow() {
        LocalDateTime result = DateUtil.getDayStart(1);
        assertNotNull(result);
        assertEquals(LocalTime.of(0, 0, 0), result.toLocalTime());
    }

    @Test
    public void testGetDayStart_Yesterday() {
        LocalDateTime result = DateUtil.getDayStart(-1);
        assertNotNull(result);
        assertEquals(LocalTime.of(0, 0, 0), result.toLocalTime());
    }

    // --- getDayEnd ---

    @Test
    public void testGetDayEnd_Today() {
        LocalDateTime result = DateUtil.getDayEnd(0);
        assertNotNull(result);
        assertEquals(LocalTime.of(23, 59, 59), result.toLocalTime());
    }

    @Test
    public void testGetDayEnd_Tomorrow() {
        LocalDateTime result = DateUtil.getDayEnd(1);
        assertNotNull(result);
        assertEquals(LocalTime.of(23, 59, 59), result.toLocalTime());
    }

    // --- dateToLocalDateTime ---

    @Test
    public void testDateToLocalDateTime_WithValidDate() {
        Date date = new Date(0);
        LocalDateTime result = DateUtil.dateToLocalDateTime(date);
        assertNotNull(result);
    }

    @Test
    public void testDateToLocalDateTime_WithNull() {
        assertNull(DateUtil.dateToLocalDateTime(null));
    }

    @Test
    public void testDateToLocalDateTime_WithZoneId() {
        Date date = new Date(0);
        LocalDateTime result = DateUtil.dateToLocalDateTime(date, ZoneId.of("UTC"));
        assertNotNull(result);
    }

    // --- localDateTimeToDate ---

    @Test
    public void testLocalDateTimeToDate_WithValidLocalDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        Date result = DateUtil.localDateTimeToDate(ldt);
        assertNotNull(result);
    }

    @Test
    public void testLocalDateTimeToDate_WithNull() {
        assertNull(DateUtil.localDateTimeToDate(null));
    }

    @Test
    public void testLocalDateTimeToDate_WithZoneId() {
        LocalDateTime ldt = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        Date result = DateUtil.localDateTimeToDate(ldt, ZoneId.of("UTC"));
        assertNotNull(result);
    }

    // --- getMicrosecond ---

    @Test
    public void testGetMicrosecond_WithValidDateTime() {
        long result = DateUtil.getMicrosecond("2023-01-01 12:00:00");
        assertTrue(result > 0);
    }

    @Test
    public void testGetMicrosecond_WithMicroseconds() {
        long result = DateUtil.getMicrosecond("2023-01-01 12:00:00.123456");
        assertTrue(result > 0);
    }

    // --- getDefaultLocalDateTime ---

    @Test
    public void testGetDefaultLocalDateTime_WithValidString() {
        LocalDateTime result = DateUtil.getDefaultLocalDateTime("2023-01-01 12:00:00");
        assertNotNull(result);
        assertEquals(2023, result.getYear());
        assertEquals(1, result.getMonthValue());
        assertEquals(1, result.getDayOfMonth());
        assertEquals(12, result.getHour());
        assertEquals(0, result.getMinute());
        assertEquals(0, result.getSecond());
    }

    @Test
    public void testGetDefaultLocalDateTime_WithMicroseconds() {
        LocalDateTime result = DateUtil.getDefaultLocalDateTime("2023-01-01 12:00:00.123456");
        assertNotNull(result);
    }

    // --- getDefaultLocalTime ---

    @Test
    public void testGetDefaultLocalTime_WithValidString() {
        LocalTime result = DateUtil.getDefaultLocalTime("12:30:45");
        assertNotNull(result);
        assertEquals(12, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(45, result.getSecond());
    }

    @Test
    public void testGetDefaultLocalTime_WithMicroseconds() {
        LocalTime result = DateUtil.getDefaultLocalTime("12:30:45.123456");
        assertNotNull(result);
    }

    // --- timestampToLocalDateTime ---

    @Test
    public void testTimestampToLocalDateTime_WithValidTimestamp() {
        LocalDateTime result = DateUtil.timestampToLocalDateTime(0L);
        assertNotNull(result);
    }

    @Test
    public void testTimestampToLocalDateTime_WithOffset() {
        LocalDateTime result = DateUtil.timestampToLocalDateTime(0L, 8);
        assertNotNull(result);
    }

    // --- localDateTimeToTimestamp ---

    @Test
    public void testLocalDateTimeToTimestamp() {
        LocalDateTime ldt = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        Long result = DateUtil.localDateTimeToTimestamp(ldt);
        assertNotNull(result);
        assertTrue(result > 0);
    }

    // --- localDateTimeToMilliTimestamp ---

    @Test
    public void testLocalDateTimeToMilliTimestamp() {
        LocalDateTime ldt = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        Long result = DateUtil.localDateTimeToMilliTimestamp(ldt);
        assertNotNull(result);
        assertTrue(result > 0);
    }

    // --- getFormat ---

    @Test
    public void testGetFormat_WithValidPattern() {
        assertNotNull(DateUtil.getFormat(DateUtil.DATE_FORMAT_WHIFFLETREE_SECOND));
    }

    @Test
    public void testGetFormat_WithEmptyPattern() {
        assertNotNull(DateUtil.getFormat(""));
    }

    @Test
    public void testGetFormat_WithNullPattern() {
        assertNotNull(DateUtil.getFormat(null));
    }
}
