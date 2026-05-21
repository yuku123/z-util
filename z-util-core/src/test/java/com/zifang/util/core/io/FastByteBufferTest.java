package com.zifang.util.core.io;

import org.junit.Test;

import static org.junit.Assert.*;

public class FastByteBufferTest {

    @Test
    public void testAppendByte() {
        FastByteBuffer buf = new FastByteBuffer();
        buf.append((byte) 'a');
        buf.append((byte) 'b');
        buf.append((byte) 'c');
        assertEquals(3, buf.size());
        assertArrayEquals(new byte[]{'a', 'b', 'c'}, buf.toArray());
    }

    @Test
    public void testAppendByteArray() {
        FastByteBuffer buf = new FastByteBuffer();
        buf.append("hello".getBytes());
        assertEquals(5, buf.size());
        assertArrayEquals("hello".getBytes(), buf.toArray());
    }

    @Test
    public void testAppendByteArrayPartial() {
        FastByteBuffer buf = new FastByteBuffer();
        byte[] data = "hello world".getBytes();
        buf.append(data, 0, 5);
        assertEquals(5, buf.size());
        assertArrayEquals("hello".getBytes(), buf.toArray());
    }

    @Test
    public void testAppendByteArrayWithOffset() {
        FastByteBuffer buf = new FastByteBuffer();
        byte[] data = "hello world".getBytes();
        buf.append(data, 6, 5);
        assertEquals(5, buf.size());
        assertArrayEquals("world".getBytes(), buf.toArray());
    }

    @Test
    public void testAppendAnotherBuffer() {
        FastByteBuffer buf1 = new FastByteBuffer();
        buf1.append("hello".getBytes());
        FastByteBuffer buf2 = new FastByteBuffer();
        buf2.append(" world".getBytes());
        buf1.append(buf2);
        assertEquals(11, buf1.size());
        assertEquals("hello world", buf1.toArrayString());
    }

    @Test
    public void testSize() {
        FastByteBuffer buf = new FastByteBuffer();
        assertEquals(0, buf.size());
        buf.append((byte) 'x');
        assertEquals(1, buf.size());
    }

    @Test
    public void testIsEmpty() {
        FastByteBuffer buf = new FastByteBuffer();
        assertTrue(buf.isEmpty());
        buf.append((byte) 'x');
        assertFalse(buf.isEmpty());
    }

    @Test
    public void testReset() {
        FastByteBuffer buf = new FastByteBuffer();
        buf.append("hello".getBytes());
        assertFalse(buf.isEmpty());
        buf.reset();
        assertTrue(buf.isEmpty());
        assertEquals(0, buf.size());
    }

    @Test
    public void testGetByte() {
        FastByteBuffer buf = new FastByteBuffer();
        buf.append("abc".getBytes());
        assertEquals((byte) 'a', buf.get(0));
        assertEquals((byte) 'b', buf.get(1));
        assertEquals((byte) 'c', buf.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetByteOutOfBounds() {
        FastByteBuffer buf = new FastByteBuffer();
        buf.append((byte) 'a');
        buf.get(5);
    }

    @Test
    public void testToArrayPartial() {
        FastByteBuffer buf = new FastByteBuffer();
        buf.append("hello world".getBytes());
        byte[] partial = buf.toArray(0, 5);
        assertEquals(5, partial.length);
        assertEquals("hello", new String(partial));
    }

    @Test
    public void testIndexAndOffset() {
        FastByteBuffer buf = new FastByteBuffer();
        buf.append("abc".getBytes());
        assertEquals(0, buf.index());
        assertEquals(3, buf.offset());
    }

    @Test
    public void testLargeData() {
        FastByteBuffer buf = new FastByteBuffer();
        byte[] large = new byte[100_000];
        for (int i = 0; i < large.length; i++) {
            large[i] = (byte) (i % 256);
        }
        buf.append(large);
        assertEquals(large.length, buf.size());
        assertArrayEquals(large, buf.toArray());
    }

    @Test
    public void testGrowBeyondInitialCapacity() {
        FastByteBuffer buf = new FastByteBuffer(1);
        // Append beyond single chunk size
        byte[] data = new byte[3000];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        buf.append(data);
        assertEquals(data.length, buf.size());
        assertArrayEquals(data, buf.toArray());
    }

    @Test
    public void testAppendEmptyByteArray() {
        FastByteBuffer buf = new FastByteBuffer();
        buf.append("hello".getBytes());
        buf.append(new byte[0]);
        assertEquals(5, buf.size());
    }

    @Test
    public void testAppendBufferEmpty() {
        FastByteBuffer buf1 = new FastByteBuffer();
        buf1.append("hello".getBytes());
        FastByteBuffer buf2 = new FastByteBuffer();
        buf1.append(buf2);
        assertEquals(5, buf1.size());
    }

    private String bufToString(FastByteBuffer buf) {
        return new String(buf.toArray());
    }
}
