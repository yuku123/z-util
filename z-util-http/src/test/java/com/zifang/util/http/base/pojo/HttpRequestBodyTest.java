package com.zifang.util.http.base.pojo;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestBodyTest {

    @Test
    public void testDefaultConstructor() {
        HttpRequestBody httpRequestBody = new HttpRequestBody();
        assertNull(httpRequestBody.getBody());
    }

    @Test
    public void testSettersAndGetters() {
        HttpRequestBody httpRequestBody = new HttpRequestBody();
        byte[] body = "test content".getBytes();
        httpRequestBody.setBody(body);
        
        assertArrayEquals(body, httpRequestBody.getBody());
    }

    @Test
    public void testToString() {
        HttpRequestBody httpRequestBody = new HttpRequestBody();
        byte[] body = "test".getBytes();
        httpRequestBody.setBody(body);
        
        String str = httpRequestBody.toString();
        assertTrue(str.contains("byte[4]"));
    }

    @Test
    public void testToStringWithNullBody() {
        HttpRequestBody httpRequestBody = new HttpRequestBody();
        String str = httpRequestBody.toString();
        assertTrue(str.contains("null"));
    }

    @Test
    public void testEqualsWithSameBody() {
        HttpRequestBody body1 = new HttpRequestBody();
        body1.setBody("test".getBytes());
        
        HttpRequestBody body2 = new HttpRequestBody();
        body2.setBody("test".getBytes());
        
        assertEquals(body1, body2);
    }

    @Test
    public void testEqualsWithDifferentBody() {
        HttpRequestBody body1 = new HttpRequestBody();
        body1.setBody("test1".getBytes());
        
        HttpRequestBody body2 = new HttpRequestBody();
        body2.setBody("test2".getBytes());
        
        assertNotEquals(body1, body2);
    }

    @Test
    public void testEqualsWithNullBodies() {
        HttpRequestBody body1 = new HttpRequestBody();
        HttpRequestBody body2 = new HttpRequestBody();
        
        assertEquals(body1, body2);
    }

    @Test
    public void testEqualsWithOneNullBody() {
        HttpRequestBody body1 = new HttpRequestBody();
        body1.setBody("test".getBytes());
        
        HttpRequestBody body2 = new HttpRequestBody();
        
        assertNotEquals(body1, body2);
    }

    @Test
    public void testHashCode() {
        HttpRequestBody body1 = new HttpRequestBody();
        body1.setBody("test".getBytes());
        
        HttpRequestBody body2 = new HttpRequestBody();
        body2.setBody("test".getBytes());
        
        assertEquals(body1.hashCode(), body2.hashCode());
    }

    @Test
    public void testHashCodeWithNullBody() {
        HttpRequestBody body = new HttpRequestBody();
        int hash = body.hashCode();
        assertEquals(1, hash);
    }
}
