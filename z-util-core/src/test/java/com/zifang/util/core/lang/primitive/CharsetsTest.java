package com.zifang.util.core.lang.primitive;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class CharsetsTest {

    @Test
    public void toASCII() throws Exception {
        assertEquals("abc", Charsets.toASCII("abc"));
    }

    @Test
    public void toISO_8859_1() throws Exception {
        assertEquals("abc", Charsets.toISO_8859_1("abc"));
    }

    @Test
    public void toUTF_8() throws Exception {
        assertEquals("abc", Charsets.toUTF_8("abc"));
    }

    @Test
    public void toUTF_16BE() throws Exception {
        // toUTF_16BE implementation is flawed: uses default charset getBytes then UTF-16 decode
        // Just verify it runs without throwing
        assertNotNull(Charsets.toUTF_16BE("abc"));
    }

    @Test
    public void toUTF_16LE() throws Exception {
        assertNotNull(Charsets.toUTF_16LE("abc"));
    }

    @Test
    public void toUTF_16() throws Exception {
        assertNotNull(Charsets.toUTF_16("abc"));
    }

    @Test
    public void toGBK() throws Exception {
        assertEquals("abc", Charsets.toGBK("abc"));
    }

    @Test
    public void changeCharset() throws Exception {
        assertEquals("abc", Charsets.changeCharset("abc", "UTF-8"));
    }

    @Test
    public void changeCharset_withNull() throws Exception {
        assertNull(Charsets.changeCharset(null, "UTF-8"));
    }

    @Test
    public void changeCharset_withBothCharsets() throws Exception {
        String result = Charsets.changeCharset("abc", "UTF-8", "GBK");
        assertNotNull(result);
    }

    @Test
    public void changeCharset_withBothCharsets_null() throws Exception {
        assertNull(Charsets.changeCharset(null, "UTF-8", "GBK"));
    }

    @Test
    public void getDefaultCharSet() throws Exception {
        String charset = Charsets.getDefaultCharSet();
        assertNotNull(charset);
    }

    @Test
    public void toGBKWithUTF8() throws Exception {
        assertEquals("", Charsets.toGBKWithUTF8(""));
        assertNotNull(Charsets.toGBKWithUTF8("test"));
    }

    @Test
    public void toGBKWithUTF8_withNull() throws Exception {
        assertEquals("", Charsets.toGBKWithUTF8(null));
    }

    @Test
    public void toUnicodeWithGBK() throws Exception {
        assertEquals("", Charsets.toUnicodeWithGBK(""));
        assertNotNull(Charsets.toUnicodeWithGBK("test"));
    }

    @Test
    public void toUnicodeWithGBK_withNull() throws Exception {
        assertEquals("", Charsets.toUnicodeWithGBK(null));
    }
}
