package com.zifang.util.crawler.pipeline;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * PipelineContextTest类。
 */
public class PipelineContextTest {

    private PipelineContext context;

    @Before
    /**
     * setUp方法。
     */
    public void setUp() {
        context = new PipelineContext();
    }

    // --- Constructor tests ---

    @Test
    /**
     * testConstructor_InitializesMaps方法。
     */
    public void testConstructor_InitializesMaps() {
        assertNotNull(context.getHeaders());
        assertNotNull(context.getCookies());
        assertNotNull(context.getData());
        assertNotNull(context.getErrors());
        assertNotNull(context.getScreenshots());
        assertNotNull(context.getMetadata());
    }

    // --- URL tests ---

    @Test
    /**
     * testSetAndGetUrl方法。
     */
    public void testSetAndGetUrl() {
        context.setUrl("https://example.com");
        assertEquals("https://example.com", context.getUrl());
    }

    @Test
    /**
     * testGetUrl_WhenNotSet_ReturnsNull方法。
     */
    public void testGetUrl_WhenNotSet_ReturnsNull() {
        assertNull(context.getUrl());
    }

    // --- Headers tests ---

    @Test
    /**
     * testSetAndGetHeaders方法。
     */
    public void testSetAndGetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer token");

        context.setHeaders(headers);

        assertEquals("application/json", context.getHeaders().get("Content-Type"));
        assertEquals("Bearer token", context.getHeaders().get("Authorization"));
    }

    @Test
    /**
     * testGetHeaders_WhenNotSet_ReturnsEmptyMap方法。
     */
    public void testGetHeaders_WhenNotSet_ReturnsEmptyMap() {
        assertTrue(context.getHeaders().isEmpty());
    }

    // --- Cookies tests ---

    @Test
    /**
     * testSetAndGetCookies方法。
     */
    public void testSetAndGetCookies() {
        Map<String, String> cookies = new HashMap<>();
        cookies.put("session", "abc123");
        cookies.put("theme", "dark");

        context.setCookies(cookies);

        assertEquals("abc123", context.getCookies().get("session"));
        assertEquals("dark", context.getCookies().get("theme"));
    }

    @Test
    /**
     * testGetCookies_WhenNotSet_ReturnsEmptyMap方法。
     */
    public void testGetCookies_WhenNotSet_ReturnsEmptyMap() {
        assertTrue(context.getCookies().isEmpty());
    }

    // --- Data tests ---

    @Test
    /**
     * testSetAndGetData方法。
     */
    public void testSetAndGetData() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Test Page");
        data.put("count", 42);

        context.setData(data);

        assertEquals("Test Page", context.getData().get("title"));
        assertEquals(42, context.getData().get("count"));
    }

    @Test
    /**
     * testPut方法。
     */
    public void testPut() {
        context.put("key1", "value1");
        context.put("key2", 123);

        assertEquals("value1", context.get("key1"));
        assertEquals(123, context.get("key2"));
    }

    @Test
    /**
     * testPutData方法。
     */
    public void testPutData() {
        context.putData("dataKey", "dataValue");
        assertEquals("dataValue", context.getData("dataKey"));
    }

    @Test
    /**
     * testGetData_WithKey方法。
     */
    public void testGetData_WithKey() {
        context.put("testKey", "testValue");
        assertEquals("testValue", context.getData("testKey"));
    }

    @Test
    /**
     * testGetData_WhenNotSet_ReturnsNull方法。
     */
    public void testGetData_WhenNotSet_ReturnsNull() {
        assertNull(context.getData("nonexistent"));
    }

    @Test
    /**
     * testGetAlias方法。
     */
    public void testGetAlias() {
        context.put("aliasKey", "aliasValue");
        assertEquals("aliasValue", context.get("aliasKey"));
    }

    // --- Parameter tests (aliases) ---

    @Test
    /**
     * testGetParameter方法。
     */
    public void testGetParameter() {
        context.putParameter("param1", "value1");
        assertEquals("value1", context.getParameter("param1"));
    }

    @Test
    /**
     * testPutParameter方法。
     */
    public void testPutParameter() {
        context.putParameter("paramKey", "paramValue");
        assertEquals("paramValue", context.getParameter("paramKey"));
    }

    // --- HTML tests ---

    @Test
    /**
     * testSetAndGetHtml方法。
     */
    public void testSetAndGetHtml() {
        String html = "<html><body>Content</body></html>";
        context.setHtml(html);
        assertEquals(html, context.getHtml());
    }

    @Test
    /**
     * testGetHtml_WhenNotSet_ReturnsNull方法。
     */
    public void testGetHtml_WhenNotSet_ReturnsNull() {
        assertNull(context.getHtml());
    }

    // --- JSON tests ---

    @Test
    /**
     * testSetAndGetJson方法。
     */
    public void testSetAndGetJson() {
        String json = "{\"name\":\"test\"}";
        context.setJson(json);
        assertEquals(json, context.getJson());
    }

    @Test
    /**
     * testGetJson_WhenNotSet_ReturnsNull方法。
     */
    public void testGetJson_WhenNotSet_ReturnsNull() {
        assertNull(context.getJson());
    }

    // --- Errors tests ---

    @Test
    /**
     * testAddError方法。
     */
    public void testAddError() {
        context.addError("http", "Connection timeout");
        assertFalse(context.getErrors().isEmpty());
        assertEquals("Connection timeout", context.getErrors().get("http"));
    }

    @Test
    /**
     * testHasErrors_WhenErrorsExist_ReturnsTrue方法。
     */
    public void testHasErrors_WhenErrorsExist_ReturnsTrue() {
        context.addError("stage1", "error message");
        assertTrue(context.hasErrors());
    }

    @Test
    /**
     * testHasErrors_WhenNoErrors_ReturnsFalse方法。
     */
    public void testHasErrors_WhenNoErrors_ReturnsFalse() {
        assertFalse(context.hasErrors());
    }

    @Test
    /**
     * testHasErrors_AfterMultipleErrors_ReturnsTrue方法。
     */
    public void testHasErrors_AfterMultipleErrors_ReturnsTrue() {
        context.addError("stage1", "error1");
        context.addError("stage2", "error2");
        assertTrue(context.hasErrors());
        assertEquals(2, context.getErrors().size());
    }

    // --- Screenshots tests ---

    @Test
    /**
     * testAddScreenshot方法。
     */
    public void testAddScreenshot() {
        File screenshot = new File("/tmp/test_screenshot.png");
        context.addScreenshot("homepage", screenshot);

        assertEquals(1, context.getScreenshots().size());
        assertEquals(screenshot, context.getScreenshots().get("homepage"));
    }

    @Test
    /**
     * testGetScreenshots_WhenNotSet_ReturnsEmptyMap方法。
     */
    public void testGetScreenshots_WhenNotSet_ReturnsEmptyMap() {
        assertTrue(context.getScreenshots().isEmpty());
    }

    // --- Metadata tests ---

    @Test
    /**
     * testSetMetadata方法。
     */
    public void testSetMetadata() {
        context.setMetadata("customKey", "customValue");
        assertEquals("customValue", context.getMetadata("customKey"));
    }

    @Test
    /**
     * testGetMetadata_WhenNotSet_ReturnsNull方法。
     */
    public void testGetMetadata_WhenNotSet_ReturnsNull() {
        assertNull(context.getMetadata("nonexistent"));
    }

    @Test
    /**
     * testGetMetadata_Map方法。
     */
    public void testGetMetadata_Map() {
        Map<String, Object> metadata = context.getMetadata();
        metadata.put("key1", "value1");

        // getMetadata returns the actual map, so changes are reflected
        assertEquals("value1", context.getMetadata("key1"));
    }

    // --- Multiple data types test ---

    @Test
    /**
     * testPutVariousTypes方法。
     */
    public void testPutVariousTypes() {
        context.put("string", "text");
        context.put("integer", 100);
        context.put("double", 99.5);
        context.put("boolean", true);
        context.put("map", new HashMap<>());
        context.put("list", java.util.Arrays.asList("a", "b", "c"));

        assertEquals("text", context.get("string"));
        assertEquals(100, context.get("integer"));
        assertEquals(99.5, context.get("double"));
        assertEquals(true, context.get("boolean"));
        assertNotNull(context.get("map"));
        assertNotNull(context.get("list"));
    }
}
