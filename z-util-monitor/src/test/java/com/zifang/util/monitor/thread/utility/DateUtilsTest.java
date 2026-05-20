package com.zifang.util.monitor.thread.utility;

import org.junit.Test;
import java.text.ParseException;
import java.util.Date;
import static org.junit.Assert.*;

public class DateUtilsTest {

    @Test
    public void testConstants() {
        assertEquals("yyyy-MM-dd", DateUtils.DEFAULT_DATE_FORMAT);
        assertEquals("yyyy-MM-dd HH:mm:ss", DateUtils.DEFAULT_DATE_TIME_FORMAT);
        assertEquals(86400000L, DateUtils.dayTotalMilliseconds);
        assertEquals(86400, DateUtils.dayTotalSeconds);
    }

    @Test
    public void testGetDateString() {
        String dateStr = DateUtils.getDateString(System.currentTimeMillis());
        assertNotNull(dateStr);
        assertTrue(dateStr.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testGetDateStringNoArg() {
        String dateStr = DateUtils.getDateString();
        assertNotNull(dateStr);
        assertTrue(dateStr.matches("\\d{4}-\\d{2}-\\d{2}"));
    }

    @Test
    public void testGetDateAndTimeString() {
        String dateTimeStr = DateUtils.getDateAndTimeString();
        assertNotNull(dateTimeStr);
        assertTrue(dateTimeStr.contains("-"));
    }

    @Test
    public void testGetNextDateString() {
        String today = DateUtils.getDateString();
        String nextDay = DateUtils.getNextDateString(System.currentTimeMillis());
        assertNotNull(nextDay);
        assertNotEquals(today, nextDay);
    }

    @Test
    public void testGetDateTimeString() {
        long timestamp = System.currentTimeMillis();
        String result = DateUtils.getDateTimeString(timestamp, "yyyy-MM-dd");
        assertNotNull(result);
    }

    @Test
    public void testDateToStr() {
        Date date = new Date();
        String result = DateUtils.dateToStr(date, "yyyy-MM-dd");
        assertNotNull(result);
    }

    @Test
    public void testStrToDate() {
        Date date = DateUtils.strToDate("2023-01-01", "yyyy-MM-dd");
        assertNotNull(date);
    }

    @Test
    public void testStrToDateMillisTimestamp() {
        long millis = DateUtils.strToDateMillisTimestamp("2023-01-01", "yyyy-MM-dd");
        assertTrue(millis > 0);
    }

    @Test
    public void testStrToDateTimestamp() {
        int timestamp = DateUtils.strToDateTimestamp("2023-01-01", "yyyy-MM-dd");
        assertTrue(timestamp > 0);
    }

    @Test
    public void testCalcDay() throws Exception {
        long days = DateUtils.calcDay("2023-01-02", "2023-01-01", "yyyy-MM-dd");
        assertEquals(1, days);
    }

    @Test
    public void testCompareDate() {
        Date date1 = new Date(1000);
        Date date2 = new Date(2000);
        assertEquals(-1, DateUtils.compareDate(date1, date2));
        assertEquals(1, DateUtils.compareDate(date2, date1));
        assertEquals(0, DateUtils.compareDate(date1, date1));
    }

    @Test
    public void testCompareDateString() {
        int result = DateUtils.compareDate("2023-01-01", "2023-01-02", "yyyy-MM-dd");
        assertTrue(result < 0);
    }
}
