package com.zifang.util.core.time;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class DateFormatUtilTest {

    @Test
    public void testFormatString14ToDateTime() {
        String input = "20240615183045";
        String result = DateFormatUtil.formatString(input);
        assertEquals("2024-06-15 18:30:45", result);
    }

    @Test
    public void testFormatString19ToCompact() {
        String input = "2024-06-15 18:30:45";
        String result = DateFormatUtil.formatString(input);
        assertEquals("20240615183045", result);
    }

    @Test
    public void testFormatString10ToCompact() {
        String input = "2024-06-15";
        String result = DateFormatUtil.formatString(input);
        assertEquals("20240615", result);
    }

    @Test
    public void testFormatString8ToDate() {
        String input = "20240615";
        String result = DateFormatUtil.formatString(input);
        assertEquals("2024-06-15", result);
    }

    @Test
    public void testFormatStringUnknown() {
        assertEquals("abc", DateFormatUtil.formatString("abc"));
        assertEquals("12345", DateFormatUtil.formatString("12345"));
    }

    @Test
    public void testFormatStringNull() {
        assertEquals("", DateFormatUtil.formatString(null));
        assertEquals("", DateFormatUtil.formatString(""));
    }

    @Test
    public void testParseDateCompact() {
        Date result = DateFormatUtil.parse("20240615");
        assertNotNull(result);
    }

    @Test
    public void testParseDateFull() {
        Date result = DateFormatUtil.parse("20240615183045");
        assertNotNull(result);
    }

    @Test
    public void testParseDateNull() {
        assertNull(DateFormatUtil.parse(null));
        assertNull(DateFormatUtil.parse(""));
    }

    @Test
    public void testParseWithOutPattern() {
        Date result = DateFormatUtil.parse("20240615", "yyyy-MM-dd");
        assertNotNull(result);
    }

    @Test
    public void testParseInvalid() {
        assertNull(DateFormatUtil.parse("invalid"));
        assertNull(DateFormatUtil.parse("2024-06-15 10:30:00")); // wrong length
    }

    @Test
    public void testFormatDateWithPattern() {
        Date date = new Date(1718455200000L);
        String result = DateFormatUtil.format(date, "yyyy-MM-dd");
        assertNotNull(result);
        assertTrue(result.startsWith("2024"));
    }

    @Test
    public void testFormatDateNull() {
        assertEquals("", DateFormatUtil.format(null, "yyyy-MM-dd"));
        assertEquals("", DateFormatUtil.format(null, null));
    }
}
