package com.zifang.util.core.lang.primitive;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * IntegerUtilTest类。
 */
public class IntegerUtilTest {

    @Test
    /**
     * parseInteger_withNull方法。
     */
    public void parseInteger_withNull() {
        assertNull(IntegerUtil.parseInteger(null));
    }

    @Test
    /**
     * parseInteger_withValidString方法。
     */
    public void parseInteger_withValidString() {
        assertEquals(Integer.valueOf(0), IntegerUtil.parseInteger("0"));
        assertEquals(Integer.valueOf(123), IntegerUtil.parseInteger("123"));
        assertEquals(Integer.valueOf(-456), IntegerUtil.parseInteger("-456"));
        assertEquals(Integer.valueOf(Integer.MAX_VALUE), IntegerUtil.parseInteger(String.valueOf(Integer.MAX_VALUE)));
        assertEquals(Integer.valueOf(Integer.MIN_VALUE), IntegerUtil.parseInteger(String.valueOf(Integer.MIN_VALUE)));
    }

    @Test(expected = NumberFormatException.class)
    /**
     * parseInteger_withInvalidString方法。
     */
    public void parseInteger_withInvalidString() {
        IntegerUtil.parseInteger("not a number");
    }

    @Test
    /**
     * parseInteger_withObject方法。
     */
    public void parseInteger_withObject() {
        assertEquals(Integer.valueOf(100), IntegerUtil.parseInteger(Long.valueOf(100)));
        assertEquals(Integer.valueOf(0), IntegerUtil.parseInteger("0"));
    }

    @Test
    /**
     * parseIntegerOrDefault_withNull方法。
     */
    public void parseIntegerOrDefault_withNull() {
        assertEquals(Integer.valueOf(999), IntegerUtil.parseIntegerOrDefault(null, Integer.valueOf(999)));
        assertNull(IntegerUtil.parseIntegerOrDefault(null, null));
    }

    @Test
    /**
     * parseIntegerOrDefault_withValidValue方法。
     */
    public void parseIntegerOrDefault_withValidValue() {
        assertEquals(Integer.valueOf(42), IntegerUtil.parseIntegerOrDefault("42", Integer.valueOf(999)));
    }

    @Test
    /**
     * saturatedCast_withinRange方法。
     */
    public void saturatedCast_withinRange() {
        assertEquals(0, IntegerUtil.saturatedCast(0));
        assertEquals(100, IntegerUtil.saturatedCast(100));
        assertEquals(-100, IntegerUtil.saturatedCast(-100));
    }

    @Test
    /**
     * saturatedCast_atMaxValue方法。
     */
    public void saturatedCast_atMaxValue() {
        assertEquals(Integer.MAX_VALUE, IntegerUtil.saturatedCast(Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, IntegerUtil.saturatedCast((long) Integer.MAX_VALUE + 1));
        assertEquals(Integer.MAX_VALUE, IntegerUtil.saturatedCast(Long.MAX_VALUE));
    }

    @Test
    /**
     * saturatedCast_atMinValue方法。
     */
    public void saturatedCast_atMinValue() {
        assertEquals(Integer.MIN_VALUE, IntegerUtil.saturatedCast(Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, IntegerUtil.saturatedCast((long) Integer.MIN_VALUE - 1));
        assertEquals(Integer.MIN_VALUE, IntegerUtil.saturatedCast(Long.MIN_VALUE));
    }

    @Test
    /**
     * saturatedCast_justBeyondBounds方法。
     */
    public void saturatedCast_justBeyondBounds() {
        assertEquals(Integer.MAX_VALUE - 1, IntegerUtil.saturatedCast((long) Integer.MAX_VALUE - 1));
        assertEquals(Integer.MIN_VALUE + 1, IntegerUtil.saturatedCast((long) Integer.MIN_VALUE + 1));
    }
}
