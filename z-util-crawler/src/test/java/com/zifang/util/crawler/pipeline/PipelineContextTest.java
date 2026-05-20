package com.zifang.util.crawler.pipeline;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PipelineContextTest {

    private PipelineContext context;

    @Before
    public void setUp() {
        context = new PipelineContext();
    }

    // --- Constructor tests ---

    @Test
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
    public void testSetAndGetUrl() {
        context.setUrl("https://example.com");
        assertEquals("https://example.com", context.getUrl());
    }

    @Test
    public void testGetUrl_WhenNotSet_ReturnsNull() {
        assertNull(context.getUrl());
    }

    // --- Headers tests ---

    @Test
    public void testSetAndGetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer token");

        context.setHeaders(headers);

        assertEquals("application/json", context.getHeaders().get("Content-Type"));
        assertEquals("Bearer token", context.getHeaders().get("Authorization"));
    }

    @Test
    public void testGetHeaders_WhenNotSet_ReturnsEmptyMap() {
        assertTrue(context.getHeaders().isEmpty());
    }

    // --- Cookies tests ---

    @Test
    public void testSetAndGetCookies() {
        Map<String, String> cookies = new HashMap<>();
        cookies.put("session", "abc123");
        cookies.put("theme", "dark");

        context.setCookies(cookies);

        assertEquals("abc123", context.getCookies().get("session"));
        assertEquals("dark", context.getCookies().get("theme"));
    }

    @Test
    public void testGetCookies_WhenNotSet_ReturnsEmptyMap() {
        assertTrue(context.getCookies().isEmpty());
    }

    // --- Data tests ---

    @Test
    public void testSetAndGetData() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Test Page");
        data.put("count", 42);

        context.setData(data);

        assertEquals("Test Page", context.getData().get("title"));
        assertEquals(42, context.getData().get("count"));
    }

    @Test
    public void testPut() {
        context.put("key1", "value1");
        context.put("key2", 123);

        assertEquals("value1", context.get("key1"));
        assertEquals(123, context.get("key2"));
    }

    @Test
    public void testPutData() {
        context.putData("dataKey", "dataValue");
        assertEquals("dataValue", context.getData("dataKey"));
    }

    @Test
    public void testGetData_WithKey() {
        context.put("testKey", "testValue");
        assertEquals("testValue", context.getData("testKey"));
    }

    @Test
    public void testGetData_WhenNotSet_ReturnsNull() {
        assertNull(context.getData("nonexistent"));
    }

    @Test
    public void testGetAlias() {
        context.put("aliasKey", "aliasValue");
        assertEquals("aliasValue", context.get("aliasKey"));
    }

    // --- Parameter tests (aliases) ---

    @Test
    public void testGetParameter() {
        context.putParameter("param1", "value1");
        assertEquals("value1", context.getParameter("param1"));
    }

    @Test
    public void testPutParameter() {
        context.putParameter("paramKey", "paramValue");
        assertEquals("paramValue", context.getParameter("paramKey"));
    }

    // --- HTML tests ---

    @Test
    public void testSetAndGetHtml() {
        String html = "<html><body>Content</body></html>";
        context.setHtml(html);
        assertEquals(html, context.getHtml());
    }

    @Test
    public void testGetHtml_WhenNotSet_ReturnsNull() {
        assertNull(context.getHtml());
    }

    // --- JSON tests ---

    @Test
    public void testSetAndGetJson() {
        String json = "{\"name\":\"test\"}";
        context.setJson(json);
        assertEquals(json, context.getJson());
    }

    @Test
    public void testGetJson_WhenNotSet_ReturnsNull() {
        assertNull(context.getJson());
    }

    // --- Errors tests ---

    @Test
    public void testAddError() {
        context.addError("http", "Connection timeout");
        assertFalse(context.getErrors().isEmpty());
        assertEquals("Connection timeout", context.getErrors().get("http"));
    }

    @Test
    public void testHasErrors_WhenErrorsExist_ReturnsTrue() {
        context.addError("stage1", "error message");
        assertTrue(context.hasErrors());
    }

    @Test
    public void testHasErrors_WhenNoErrors_ReturnsFalse() {
        assertFalse(context.hasErrors());
    }

    @Test
    public void testHasErrors_AfterMultipleErrors_ReturnsTrue() {
        context.addError("stage1", "error1");
        context.addError("stage2", "error2");
        assertTrue(context.hasErrors());
        assertEquals(2, context.getErrors().size());
    }

    // --- Screenshots tests ---

    @Test
    public void testAddScreenshot() {
        File screenshot = new File("/tmp/test_screenshot.png");
        context.addScreenshot("homepage", screenshot);

        assertEquals(1, context.getScreenshots().size());
        assertEquals(screenshot, context.getScreenshots().get("homepage"));
    }

    @Test
    public void testGetScreenshots_WhenNotSet_ReturnsEmptyMap() {
        assertTrue(context.getScreenshots().isEmpty());
    }

    // --- Metadata tests ---

    @Test
    public void testSetMetadata() {
        context.setMetadata("customKey", "customValue");
        assertEquals("customValue", context.getMetadata("customKey"));
    }

    @Test
    public void testGetMetadata_WhenNotSet_ReturnsNull() {
        assertNull(context.getMetadata("nonexistent"));
    }

    @Test
    public void testGetMetadata_Map() {
        Map<String, Object> metadata = context.getMetadata();
        metadata.put("key1", "value1");

        // getMetadata returns the actual map, so changes are reflected
        assertEquals("value1", context.getMetadata("key1"));
    }

    // --- Multiple data types test ---

    @Test
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
