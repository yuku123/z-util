package com.zifang.util.http;

import okhttp3.MediaType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * OkHttpUtilTest类。
 */
public class OkHttpUtilTest {

    @Test
    /**
     * testBasicAuth方法。
     */
    public void testBasicAuth() {
        String auth = OkHttpUtil.basicAuth("testuser", "testpass");
        
        assertNotNull(auth);
        assertTrue(auth.startsWith("Basic "));
        assertFalse(auth.contains(":"));
        assertFalse(auth.contains("testuser"));
        assertFalse(auth.contains("testpass"));
    }

    @Test
    /**
     * testBasicAuthWithSpecialCharacters方法。
     */
    public void testBasicAuthWithSpecialCharacters() {
        String auth = OkHttpUtil.basicAuth("user:pass", "p@ss=word");
        
        assertNotNull(auth);
        assertTrue(auth.startsWith("Basic "));
    }

    @Test
    /**
     * testMediaTypes方法。
     */
    public void testMediaTypes() {
        assertNotNull(OkHttpUtil.JSON);
        assertEquals("application/json; charset=utf-8", OkHttpUtil.JSON.toString());
        
        assertNotNull(OkHttpUtil.FORM);
        assertEquals("application/x-www-form-urlencoded", OkHttpUtil.FORM.toString());
        
        assertNotNull(OkHttpUtil.FILE);
        assertEquals("application/octet-stream", OkHttpUtil.FILE.toString());
    }

    @Test
    /**
     * testBasicAuthWithEmptyPassword方法。
     */
    public void testBasicAuthWithEmptyPassword() {
        String auth = OkHttpUtil.basicAuth("user", "");
        assertNotNull(auth);
        assertTrue(auth.startsWith("Basic "));
    }
}
