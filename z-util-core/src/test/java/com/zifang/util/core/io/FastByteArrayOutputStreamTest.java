package com.zifang.util.core.io;

import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

public class FastByteArrayOutputStreamTest {

    @Test
    public void testWriteSingleByte() {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        out.write('a');
        out.write('b');
        out.write('c');
        assertEquals(3, out.size());
        assertEquals("abc", out.toString());
    }

    @Test
    public void testWriteByteArray() {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        byte[] data = "hello".getBytes();
        out.write(data, 0, data.length);
        assertEquals(5, out.size());
        assertEquals("hello", out.toString());
    }

    @Test
    public void testWritePartialByteArray() {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        byte[] data = "hello world".getBytes();
        out.write(data, 0, 5);
        assertEquals(5, out.size());
        assertEquals("hello", out.toString());
    }

    @Test
    public void testWriteToOutputStream() throws Exception {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        out.write("test".getBytes());
        ByteArrayOutputStream dest = new ByteArrayOutputStream();
        out.writeTo(dest);
        assertEquals("test", dest.toString());
    }

    @Test
    public void testToByteArray() {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        byte[] data = "byteArray".getBytes();
        out.write(data, 0, data.length);
        assertArrayEquals(data, out.toByteArray());
    }

    @Test
    public void testToStringWithCharset() throws Exception {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        byte[] data = "中文测试".getBytes("GBK");
        out.write(data, 0, data.length);
        assertEquals("中文测试", out.toString("GBK"));
    }

    @Test
    public void testToStringWithCharsetObject() throws Exception {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        out.write("hello".getBytes());
        assertEquals("hello", out.toString(java.nio.charset.StandardCharsets.UTF_8));
    }

    @Test
    public void testReset() throws Exception {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        out.write("hello".getBytes());
        assertEquals(5, out.size());
        out.reset();
        assertEquals(0, out.size());
        assertEquals("", out.toString());
    }

    @Test
    public void testCloseIsNoOp() throws Exception {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        out.write("before close".getBytes());
        out.close(); // no-op
        assertEquals("before close", out.toString());
        out.write(" after close".getBytes()); // still works
        assertEquals("before close after close", out.toString());
    }

    @Test
    public void testEmptyStream() {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        assertEquals(0, out.size());
        assertEquals("", out.toString());
        assertArrayEquals(new byte[0], out.toByteArray());
    }

    @Test
    public void testLargeWrite() {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        byte[] large = new byte[100_000];
        for (int i = 0; i < large.length; i++) {
            large[i] = (byte) (i % 256);
        }
        out.write(large, 0, large.length);
        assertEquals(large.length, out.size());
        assertArrayEquals(large, out.toByteArray());
    }

    @Test
    public void testWriteToMultipleTimes() throws Exception {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        out.write("first".getBytes());
        ByteArrayOutputStream dest1 = new ByteArrayOutputStream();
        out.writeTo(dest1);
        assertEquals("first", dest1.toString());
        // out still holds data
        assertEquals("first", out.toString());
    }

    @Test
    public void testConstructorWithInitialSize() throws Exception {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream(2048);
        byte[] data = "init".getBytes();
        out.write(data);
        assertEquals(4, out.size());
    }
}
