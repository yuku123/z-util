package com.zifang.util.http.base.pojo;

import com.zifang.util.http.base.define.RequestMethod;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestLineTest {

    @Test
    public void testDefaultConstructor() {
        HttpRequestLine httpRequestLine = new HttpRequestLine();
        assertNull(httpRequestLine.getRequestMethod());
        assertNull(httpRequestLine.getUrl());
    }

    @Test
    public void testSettersAndGetters() {
        HttpRequestLine httpRequestLine = new HttpRequestLine();
        httpRequestLine.setRequestMethod(RequestMethod.GET);
        httpRequestLine.setUrl("http://example.com/api");
        
        assertEquals(RequestMethod.GET, httpRequestLine.getRequestMethod());
        assertEquals("http://example.com/api", httpRequestLine.getUrl());
    }

    @Test
    public void testToString() {
        HttpRequestLine httpRequestLine = new HttpRequestLine();
        httpRequestLine.setRequestMethod(RequestMethod.POST);
        httpRequestLine.setUrl("http://example.com/api");
        
        String str = httpRequestLine.toString();
        assertTrue(str.contains("POST"));
        assertTrue(str.contains("http://example.com/api"));
    }

    @Test
    public void testEquals() {
        HttpRequestLine line1 = new HttpRequestLine();
        line1.setRequestMethod(RequestMethod.GET);
        line1.setUrl("http://example.com");
        
        HttpRequestLine line2 = new HttpRequestLine();
        line2.setRequestMethod(RequestMethod.GET);
        line2.setUrl("http://example.com");
        
        assertEquals(line1, line2);
    }

    @Test
    public void testNotEquals() {
        HttpRequestLine line1 = new HttpRequestLine();
        line1.setRequestMethod(RequestMethod.GET);
        line1.setUrl("http://example.com");
        
        HttpRequestLine line2 = new HttpRequestLine();
        line2.setRequestMethod(RequestMethod.POST);
        line2.setUrl("http://example.com");
        
        assertNotEquals(line1, line2);
    }

    @Test
    public void testHashCode() {
        HttpRequestLine line1 = new HttpRequestLine();
        line1.setRequestMethod(RequestMethod.GET);
        line1.setUrl("http://example.com");
        
        HttpRequestLine line2 = new HttpRequestLine();
        line2.setRequestMethod(RequestMethod.GET);
        line2.setUrl("http://example.com");
        
        assertEquals(line1.hashCode(), line2.hashCode());
    }
}
