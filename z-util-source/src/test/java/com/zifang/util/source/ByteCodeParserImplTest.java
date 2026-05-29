package com.zifang.util.source;

import com.zifang.util.source.parser.ByteCodeParserImpl;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ByteCodeParserImpl 测试
 */
public class ByteCodeParserImplTest {

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBytecode() {
        ByteCodeParserImpl parser = new ByteCodeParserImpl();
        parser.parse((byte[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyBytecode() {
        ByteCodeParserImpl parser = new ByteCodeParserImpl();
        parser.parse(new byte[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullClass() {
        ByteCodeParserImpl parser = new ByteCodeParserImpl();
        parser.parse((Class<?>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseBytecodeThrowsUnsupported() {
        ByteCodeParserImpl parser = new ByteCodeParserImpl();
        parser.parse(new byte[]{0x00});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseClassThrowsUnsupported() {
        ByteCodeParserImpl parser = new ByteCodeParserImpl();
        parser.parse(String.class);
    }
}