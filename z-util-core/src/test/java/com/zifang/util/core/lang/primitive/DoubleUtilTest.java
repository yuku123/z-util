package com.zifang.util.core.lang.primitive;

import org.junit.Test;

import static org.junit.Assert.*;

public class DoubleUtilTest {

    @Test
    public void parseDouble_withNull() {
        assertNull(DoubleUtil.parseDouble(null));
    }

    @Test
    public void parseDouble_withValidString() {
        assertEquals(Double.valueOf("0.0"), DoubleUtil.parseDouble("0"));
        assertEquals(Double.valueOf("3.1415926"), DoubleUtil.parseDouble("3.1415926"));
        assertEquals(Double.valueOf("-2.5"), DoubleUtil.parseDouble("-2.5"));
        assertEquals(Double.valueOf(Double.MAX_VALUE), DoubleUtil.parseDouble(String.valueOf(Double.MAX_VALUE)));
        assertEquals(Double.valueOf(Double.MIN_VALUE), DoubleUtil.parseDouble(String.valueOf(Double.MIN_VALUE)));
    }

    @Test(expected = NumberFormatException.class)
    public void parseDouble_withInvalidString() {
        DoubleUtil.parseDouble("not a number");
    }

    @Test
    public void parseDouble_withObject() {
        assertEquals(Double.valueOf("100.5"), DoubleUtil.parseDouble(Float.valueOf("100.5")));
    }

    @Test
    public void parseDouble_withSpecialValues() {
        assertEquals(Double.valueOf(Double.POSITIVE_INFINITY), DoubleUtil.parseDouble("Infinity"));
        assertEquals(Double.valueOf(Double.NEGATIVE_INFINITY), DoubleUtil.parseDouble("-Infinity"));
        assertEquals(Double.valueOf(Double.NaN), DoubleUtil.parseDouble("NaN"));
    }

    @Test
    public void parseDoubleOrDefault_withNull() {
        assertEquals(Double.valueOf("99.9"), DoubleUtil.parseDoubleOrDefault(null, Double.valueOf("99.9")));
        assertNull(DoubleUtil.parseDoubleOrDefault(null, null));
    }

    @Test
    public void parseDoubleOrDefault_withValidValue() {
        assertEquals(Double.valueOf("3.14"), DoubleUtil.parseDoubleOrDefault("3.14", Double.valueOf("99.9")));
    }

    @Test(expected = NumberFormatException.class)
    public void parseDoubleOrDefault_withInvalidValue() {
        DoubleUtil.parseDoubleOrDefault("notanumber", Double.valueOf("0"));
    }
}
