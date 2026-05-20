package com.zifang.util.core.lang.primitive;

import org.junit.Test;

import static org.junit.Assert.*;

public class BooleanUtilTest {

    @Test
    public void parseBoolean_withNull() {
        assertNull(BooleanUtil.parseBoolean(null));
    }

    @Test
    public void parseBoolean_withTrueString() {
        assertEquals(Boolean.TRUE, BooleanUtil.parseBoolean("true"));
        assertEquals(Boolean.TRUE, BooleanUtil.parseBoolean("TRUE"));
        assertEquals(Boolean.TRUE, BooleanUtil.parseBoolean("True"));
    }

    @Test
    public void parseBoolean_withFalseString() {
        assertEquals(Boolean.FALSE, BooleanUtil.parseBoolean("false"));
        assertEquals(Boolean.FALSE, BooleanUtil.parseBoolean("FALSE"));
        assertEquals(Boolean.FALSE, BooleanUtil.parseBoolean("False"));
    }

    @Test
    public void parseBoolean_withNonBooleanString() {
        // Result depends on implementation
        Boolean result = BooleanUtil.parseBoolean("hello");
        assertNotNull(result);
    }

    @Test
    public void parseBoolean_withEmptyString() {
        Boolean result = BooleanUtil.parseBoolean("");
        assertNotNull(result);
    }

    @Test
    public void parseBooleanOrDefault_withNull() {
        assertEquals(Boolean.TRUE, BooleanUtil.parseBooleanOrDefault(null, Boolean.TRUE));
        assertEquals(Boolean.FALSE, BooleanUtil.parseBooleanOrDefault(null, Boolean.FALSE));
        assertNull(BooleanUtil.parseBooleanOrDefault(null, null));
    }

    @Test
    public void parseBooleanOrDefault_withValidValue() {
        assertEquals(Boolean.TRUE, BooleanUtil.parseBooleanOrDefault("true", Boolean.FALSE));
        assertEquals(Boolean.FALSE, BooleanUtil.parseBooleanOrDefault("false", Boolean.TRUE));
    }

    @Test
    public void compare_bothTrue() {
        assertEquals(0, BooleanUtil.compare(true, true));
    }

    @Test
    public void compare_bothFalse() {
        assertEquals(0, BooleanUtil.compare(false, false));
    }

    @Test
    public void compare_xTrue_yFalse() {
        assertEquals(1, BooleanUtil.compare(true, false));
    }

    @Test
    public void compare_xFalse_yTrue() {
        assertEquals(-1, BooleanUtil.compare(false, true));
    }
}
