package com.zifang.util.http;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.Assert.*;

public class HttpUtilTest {

    @Test
    public void testToBasicAuthValue() {
        String authValue = HttpUtil.toBasicAuthValue("testuser", "testpass");
        
        assertNotNull(authValue);
        assertTrue(authValue.startsWith("Basic "));
        
        // Decode and verify
        String base64Part = authValue.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64Part), StandardCharsets.UTF_8);
        assertEquals("testuser:testpass", decoded);
    }

    @Test
    public void testToBasicAuthValueWithSpecialCharacters() {
        String authValue = HttpUtil.toBasicAuthValue("user@domain", "pass:word");
        
        assertNotNull(authValue);
        assertTrue(authValue.startsWith("Basic "));
        
        String base64Part = authValue.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64Part), StandardCharsets.UTF_8);
        assertEquals("user@domain:pass:word", decoded);
    }

    @Test
    public void testToBasicAuthValueWithEmptyPassword() {
        String authValue = HttpUtil.toBasicAuthValue("testuser", "");
        
        assertNotNull(authValue);
        String base64Part = authValue.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64Part), StandardCharsets.UTF_8);
        assertEquals("testuser:", decoded);
    }
}
