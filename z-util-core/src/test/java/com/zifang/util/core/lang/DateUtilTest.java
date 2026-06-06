package com.zifang.util.core.lang;

import com.zifang.util.core.time.DateUtil;
import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * DateUtil 单元测试类（基于实际 API 编写）。
 * 测试覆盖：format / parse / getDayStart / getDayEnd / 转换 / 时间戳 / 微秒。
 */
public class DateUtilTest {

    // --- format and parse ---

    @Test
    public void testFormat_WithValidDate() {
        Date date = new Date(0);
        String result = DateUtil.format(date, DateUtil.PATTERN_DEFAULT);
        // 北京时间 1970-01-01 08:00:00
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    public void testFormat_WithDayFormat() {
        Date date = new Date(0);
        String result = DateUtil.format(date, DateUtil.PATTERN_DATE);
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testParse_WithValidString() throws ParseException {
        Date date = DateUtil.parse("2023-01-01 12:00:00", DateUtil.PATTERN_DEFAULT);
        assertNotNull(date);
    }

    @Test(expected = ParseException.class)
    public void testParse_WithInvalidString() throws ParseException {
        DateUtil.parse("invalid-date", DateUtil.PATTERN_DEFAULT);
    }

    // --- getDayStart / getDayEnd (offset 形式) ---

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

    // --- Date <-> LocalDateTime 转换 ---

    @Test
    public void testToLocalDateTime_WithValidDate() {
        Date date = new Date(0);
        LocalDateTime result = DateUtil.toLocalDateTime(date);
        assertNotNull(result);
    }

    @Test
    public void testToLocalDateTime_WithNull() {
        assertNull(DateUtil.toLocalDateTime(null));
    }

    @Test
    public void testToLocalDateTime_WithZoneId() {
        Date date = new Date(0);
        LocalDateTime result = DateUtil.toLocalDateTime(date, ZoneId.of("UTC"));
        assertNotNull(result);
        assertEquals(1970, result.getYear());
        assertEquals(1, result.getMonthValue());
        assertEquals(1, result.getDayOfMonth());
    }

    @Test
    public void testFromLocalDateTime_WithValidLocalDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        Date result = DateUtil.fromLocalDateTime(ldt);
        assertNotNull(result);
    }

    @Test
    public void testFromLocalDateTime_WithNull() {
        assertNull(DateUtil.fromLocalDateTime(null));
    }

    @Test
    public void testFromLocalDateTime_WithZoneId() {
        LocalDateTime ldt = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
        Date result = DateUtil.fromLocalDateTime(ldt, ZoneId.of("UTC"));
        assertNotNull(result);
    }

    // --- microsecond ---

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

    // --- parseLocalDateTime / parseLocalTime ---

    @Test
    public void testParseLocalDateTime_WithValidString() {
        LocalDateTime result = DateUtil.parseLocalDateTime("2023-01-01 12:00:00");
        assertNotNull(result);
        assertEquals(2023, result.getYear());
        assertEquals(1, result.getMonthValue());
        assertEquals(1, result.getDayOfMonth());
        assertEquals(12, result.getHour());
        assertEquals(0, result.getMinute());
        assertEquals(0, result.getSecond());
    }

    @Test
    public void testParseLocalDateTime_WithMicroseconds() {
        LocalDateTime result = DateUtil.parseLocalDateTime("2023-01-01 12:00:00.123456");
        assertNotNull(result);
    }

    @Test
    public void testParseLocalTime_WithValidString() {
        LocalTime result = DateUtil.parseLocalTime("12:30:45");
        assertNotNull(result);
        assertEquals(12, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(45, result.getSecond());
    }

    @Test
    public void testParseLocalTime_WithMicroseconds() {
        LocalTime result = DateUtil.parseLocalTime("12:30:45.123456");
        assertNotNull(result);
    }

    // --- timestamp 转换 ---

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

    @Test
    public void testLocalDateTimeToTimestamp() {
        LocalDateTime ldt = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        long result = DateUtil.localDateTimeToTimestamp(ldt);
        assertTrue(result > 0);
    }

    @Test
    public void testLocalDateTimeToMilliTimestamp() {
        LocalDateTime ldt = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        long result = DateUtil.localDateTimeToMilliTimestamp(ldt);
        assertTrue(result > 0);
    }
}
