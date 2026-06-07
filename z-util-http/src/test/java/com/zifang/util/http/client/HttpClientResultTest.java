package com.zifang.util.http.client;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * HttpClientResultTest类。
 */
public class HttpClientResultTest {

    @Test
    /**
     * testDefaultConstructor方法。
     */
    public void testDefaultConstructor() {
        HttpClientResult result = new HttpClientResult();
        assertEquals(0, result.getCode());
        assertNull(result.getContent());
    }

    @Test
    /**
     * testConstructorWithCodeOnly方法。
     */
    public void testConstructorWithCodeOnly() {
        HttpClientResult result = new HttpClientResult(200);
        assertEquals(200, result.getCode());
        assertNull(result.getContent());
    }

    @Test
    /**
     * testConstructorWithContentOnly方法。
     */
    public void testConstructorWithContentOnly() {
        HttpClientResult result = new HttpClientResult("response body");
        assertEquals(0, result.getCode());
        assertEquals("response body", result.getContent());
    }

    @Test
    /**
     * testConstructorWithCodeAndContent方法。
     */
    public void testConstructorWithCodeAndContent() {
        HttpClientResult result = new HttpClientResult(200, "response body");
        assertEquals(200, result.getCode());
        assertEquals("response body", result.getContent());
    }

    @Test
    /**
     * testSettersAndGetters方法。
     */
    public void testSettersAndGetters() {
        HttpClientResult result = new HttpClientResult();
        result.setCode(404);
        result.setContent("Not Found");
        
        assertEquals(404, result.getCode());
        assertEquals("Not Found", result.getContent());
    }

    @Test
    /**
     * testToString方法。
     */
    public void testToString() {
        HttpClientResult result = new HttpClientResult(200, "OK");
        String str = result.toString();
        
        assertTrue(str.contains("200"));
        assertTrue(str.contains("OK"));
    }

    @Test
    /**
     * testEqualsWithSameContent方法。
     */
    public void testEqualsWithSameContent() {
        HttpClientResult result1 = new HttpClientResult(200, "OK");
        HttpClientResult result2 = new HttpClientResult(200, "OK");
        
        assertEquals(result1, result2);
        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    /**
     * testEqualsWithDifferentCode方法。
     */
    public void testEqualsWithDifferentCode() {
        HttpClientResult result1 = new HttpClientResult(200, "OK");
        HttpClientResult result2 = new HttpClientResult(404, "OK");
        
        assertNotEquals(result1, result2);
    }

    @Test
    /**
     * testEqualsWithDifferentContent方法。
     */
    public void testEqualsWithDifferentContent() {
        HttpClientResult result1 = new HttpClientResult(200, "OK");
        HttpClientResult result2 = new HttpClientResult(200, "Not OK");
        
        assertNotEquals(result1, result2);
    }

    @Test
    /**
     * testEqualsWithNullContent方法。
     */
    public void testEqualsWithNullContent() {
        HttpClientResult result1 = new HttpClientResult(200);
        HttpClientResult result2 = new HttpClientResult(200);
        
        assertEquals(result1, result2);
    }

    @Test
    /**
     * testEqualsWithSelf方法。
     */
    public void testEqualsWithSelf() {
        HttpClientResult result = new HttpClientResult(200, "OK");
        assertEquals(result, result);
    }

    @Test
    /**
     * testEqualsWithNull方法。
     */
    public void testEqualsWithNull() {
        HttpClientResult result = new HttpClientResult(200, "OK");
        assertNotEquals(null, result);
    }

    @Test
    /**
     * testEqualsWithDifferentClass方法。
     */
    public void testEqualsWithDifferentClass() {
        HttpClientResult result = new HttpClientResult(200, "OK");
        assertNotEquals("string", result);
    }
}
