package com.zifang.util.http;

import okhttp3.MediaType;
import org.junit.Test;

import static org.junit.Assert.*;

public class OkHttpUtilTest {

    @Test
    public void testBasicAuth() {
        String auth = OkHttpUtil.basicAuth("testuser", "testpass");
        
        assertNotNull(auth);
        assertTrue(auth.startsWith("Basic "));
        assertFalse(auth.contains(":"));
        assertFalse(auth.contains("testuser"));
        assertFalse(auth.contains("testpass"));
    }

    @Test
    public void testBasicAuthWithSpecialCharacters() {
        String auth = OkHttpUtil.basicAuth("user:pass", "p@ss=word");
        
        assertNotNull(auth);
        assertTrue(auth.startsWith("Basic "));
    }

    @Test
    public void testMediaTypes() {
        assertNotNull(OkHttpUtil.JSON);
        assertEquals("application/json; charset=utf-8", OkHttpUtil.JSON.toString());
        
        assertNotNull(OkHttpUtil.FORM);
        assertEquals("application/x-www-form-urlencoded", OkHttpUtil.FORM.toString());
        
        assertNotNull(OkHttpUtil.FILE);
        assertEquals("application/octet-stream", OkHttpUtil.FILE.toString());
    }

    @Test
    public void testBasicAuthWithEmptyPassword() {
        String auth = OkHttpUtil.basicAuth("user", "");
        assertNotNull(auth);
        assertTrue(auth.startsWith("Basic "));
    }
}
