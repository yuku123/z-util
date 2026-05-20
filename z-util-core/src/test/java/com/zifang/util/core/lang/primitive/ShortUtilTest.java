package com.zifang.util.core.lang.primitive;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShortUtilTest {

    @Test
    public void parseShort_withNull() {
        assertNull(ShortUtil.parseShort(null));
    }

    @Test
    public void parseShort_withValidString() {
        assertEquals(Short.valueOf("0"), ShortUtil.parseShort("0"));
        assertEquals(Short.valueOf("123"), ShortUtil.parseShort("123"));
        assertEquals(Short.valueOf("-456"), ShortUtil.parseShort("-456"));
        assertEquals(Short.valueOf("32767"), ShortUtil.parseShort("32767"));
        assertEquals(Short.valueOf("-32768"), ShortUtil.parseShort("-32768"));
    }

    @Test(expected = NumberFormatException.class)
    public void parseShort_withInvalidString() {
        ShortUtil.parseShort("not a number");
    }

    @Test
    public void parseShort_withObject() {
        assertEquals(Short.valueOf("100"), ShortUtil.parseShort(Integer.valueOf(100)));
    }

    @Test
    public void parseShortOrDefault_withNull() {
        assertEquals(Short.valueOf("99"), ShortUtil.parseShortOrDefault(null, Short.valueOf("99")));
        assertNull(ShortUtil.parseShortOrDefault(null, null));
    }

    @Test
    public void parseShortOrDefault_withValidValue() {
        assertEquals(Short.valueOf("42"), ShortUtil.parseShortOrDefault("42", Short.valueOf("99")));
    }

    @Test(expected = NumberFormatException.class)
    public void parseShortOrDefault_withInvalidValue() {
        ShortUtil.parseShortOrDefault("notanumber", Short.valueOf("0"));
    }
}
