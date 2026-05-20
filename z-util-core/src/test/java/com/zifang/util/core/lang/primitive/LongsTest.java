package com.zifang.util.core.lang.primitive;

import org.junit.Test;

import static org.junit.Assert.*;

public class LongsTest {

    @Test
    public void of_singleByte() {
        byte[] bytes = new byte[]{1};
        assertEquals(1L, Longs.of(bytes));
    }

    @Test
    public void of_multipleBytes() {
        byte[] bytes = new byte[]{1, 2, 3, 4};
        long expected = 1 + (2 << 8) + (3 << 16) + (4 << 24);
        assertEquals(expected, Longs.of(bytes));
    }

    @Test
    public void of_allFF() {
        byte[] bytes = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        long expected = 0xffffffffL;
        assertEquals(expected, Longs.of(bytes));
    }

    @Test
    public void of_emptyBytes() {
        byte[] bytes = new byte[]{};
        assertEquals(0L, Longs.of(bytes));
    }
}
