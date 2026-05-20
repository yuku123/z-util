package com.zifang.util.http.base.pojo;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestHeaderTest {

    @Test
    public void testDefaultConstructor() {
        HttpRequestHeader header = new HttpRequestHeader();
        assertTrue(header.isEmpty());
    }

    @Test
    public void testPutAndGet() {
        HttpRequestHeader header = new HttpRequestHeader();
        header.put("Content-Type", "application/json");
        
        assertEquals("application/json", header.get("Content-Type"));
    }

    @Test
    public void testContainsKey() {
        HttpRequestHeader header = new HttpRequestHeader();
        header.put("Accept", "text/html");
        
        assertTrue(header.containsKey("Accept"));
        assertFalse(header.containsKey("X-Custom-Header"));
    }

    @Test
    public void testRemove() {
        HttpRequestHeader header = new HttpRequestHeader();
        header.put("Accept", "text/html");
        header.remove("Accept");
        
        assertFalse(header.containsKey("Accept"));
    }

    @Test
    public void testClear() {
        HttpRequestHeader header = new HttpRequestHeader();
        header.put("Accept", "text/html");
        header.put("Content-Type", "application/json");
        header.clear();
        
        assertTrue(header.isEmpty());
    }

    @Test
    public void testSize() {
        HttpRequestHeader header = new HttpRequestHeader();
        header.put("Accept", "text/html");
        header.put("Content-Type", "application/json");
        
        assertEquals(2, header.size());
    }

    @Test
    public void testKeySet() {
        HttpRequestHeader header = new HttpRequestHeader();
        header.put("Accept", "text/html");
        header.put("Content-Type", "application/json");
        
        assertEquals(2, header.keySet().size());
        assertTrue(header.keySet().contains("Accept"));
        assertTrue(header.keySet().contains("Content-Type"));
    }

    @Test
    public void testValues() {
        HttpRequestHeader header = new HttpRequestHeader();
        header.put("Accept", "text/html");
        header.put("Content-Type", "application/json");
        
        assertEquals(2, header.values().size());
        assertTrue(header.values().contains("text/html"));
        assertTrue(header.values().contains("application/json"));
    }

    @Test
    public void testEntrySet() {
        HttpRequestHeader header = new HttpRequestHeader();
        header.put("Accept", "text/html");
        
        assertEquals(1, header.entrySet().size());
    }

    @Test
    public void testPutAll() {
        HttpRequestHeader header1 = new HttpRequestHeader();
        header1.put("Accept", "text/html");
        
        HttpRequestHeader header2 = new HttpRequestHeader();
        header2.putAll(header1);
        
        assertEquals("text/html", header2.get("Accept"));
    }
}
