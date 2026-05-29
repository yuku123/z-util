package com.zifang.util.core.net;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

public class UrlUtilTest {

    // --- encode ---

    @Test
    public void testEncode_WithNormalString() {
        String result = UrlUtil.encode("hello world");
        assertEquals("hello%20world", result);
    }

    @Test
    public void testEncode_WithSpecialChars() {
        String result = UrlUtil.encode("a=b&c=d");
        assertTrue(result.contains("%3D"));
        assertTrue(result.contains("%26"));
    }

    @Test
    public void testEncode_WithChinese() {
        String result = UrlUtil.encode("测试");
        assertTrue(result.contains("%E"));
    }

    @Test
    public void testEncode_WithCustomEncoding() {
        String result = UrlUtil.encode("hello world", "UTF-8");
        assertEquals("hello%20world", result);
    }

    // --- decode ---

    @Test
    public void testDecode_WithEncodedString() {
        String result = UrlUtil.decode("hello%20world");
        assertEquals("hello world", result);
    }

    @Test
    public void testDecode_WithCustomEncoding() {
        String result = UrlUtil.decode("hello%20world", "UTF-8");
        assertEquals("hello world", result);
    }

    // --- setParam ---

    @Test
    public void testSetParam_WithoutExistingParams() {
        String result = UrlUtil.setParam("http://example.com", "name", "value");
        assertTrue(result.contains("name=value"));
    }

    @Test
    public void testSetParam_WithExistingParams() {
        String result = UrlUtil.setParam("http://example.com?a=1", "b", "2");
        assertTrue(result.contains("a=1"));
        assertTrue(result.contains("b=2"));
    }

    @Test
    public void testSetParam_ReplaceExistingParam() {
        String result = UrlUtil.setParam("http://example.com?name=old", "name", "new");
        assertTrue(result.contains("name=new"));
        assertFalse(result.contains("name=old"));
    }

    // --- getParamValue ---

    @Test
    public void testGetParamValue_WithExistingParam() {
        String result = UrlUtil.getParamValue("http://example.com?name=test", "name");
        assertEquals("test", result);
    }

    @Test
    public void testGetParamValue_WithMultipleParams() {
        String result = UrlUtil.getParamValue("http://example.com?a=1&b=2&c=3", "b");
        assertEquals("2", result);
    }

    @Test
    public void testGetParamValue_WithNonExistentParam() {
        String result = UrlUtil.getParamValue("http://example.com?name=test", "nonExistent");
        assertNull(result);
    }

    @Test
    public void testGetParamValue_WithoutParams() {
        String result = UrlUtil.getParamValue("http://example.com", "name");
        assertNull(result);
    }

    // --- removeParam ---

    @Test
    public void testRemoveParam_WithSingleParam() {
        String result = UrlUtil.removeParam("http://example.com?name=test", "name");
        assertFalse(result.contains("name=test"));
    }

    @Test
    public void testRemoveParam_WithMultipleParams() {
        String result = UrlUtil.removeParam("http://example.com?a=1&b=2&c=3", "b");
        assertTrue(result.contains("a=1"));
        assertFalse(result.contains("b=2"));
        assertTrue(result.contains("c=3"));
    }

    @Test
    public void testRemoveParam_WithMultipleParamNames() {
        String result = UrlUtil.removeParam("http://example.com?a=1&b=2&c=3", "a", "c");
        assertFalse(result.contains("a=1"));
        assertTrue(result.contains("b=2"));
        assertFalse(result.contains("c=3"));
    }

    @Test
    public void testRemoveParam_WithNonExistentParam() {
        String result = UrlUtil.removeParam("http://example.com?name=test", "nonExistent");
        assertEquals("http://example.com?name=test", result);
    }

    // --- urlJoin ---

    @Test
    public void testUrlJoin_WithAbsoluteLocation() throws MalformedURLException {
        URL url = new URL("http://example.com/path");
        String result = UrlUtil.urlJoin(url, "http://other.com/new");
        assertEquals("http://other.com/new", result);
    }

    @Test
    public void testUrlJoin_WithRelativeLocation() throws MalformedURLException {
        URL url = new URL("http://example.com/path");
        String result = UrlUtil.urlJoin(url, "/new");
        assertTrue(result.contains("/new"));
    }

    @Test
    public void testUrlJoin_WithRelativeLocationStartingWithSlash() throws MalformedURLException {
        URL url = new URL("http://example.com/moved_perm");
        String result = UrlUtil.urlJoin(url, "/");
        assertTrue(result.endsWith("/"));
        assertTrue(result.contains("example.com"));
    }

    // --- parseQuery ---

    @Test
    public void testParseQuery_WithSimpleQuery() {
        java.util.Map<String, String> result = UrlUtil.parseQuery("a=1&b=2", '&', '=', null);
        assertEquals("1", result.get("a"));
        assertEquals("2", result.get("b"));
    }

    @Test
    public void testParseQuery_WithDuplicateParams() {
        java.util.Map<String, String> result = UrlUtil.parseQuery("a=1&a=2", '&', '=', null);
        assertEquals("2", result.get("a"));
    }

    @Test
    public void testParseQuery_WithDuplicateParamsAndDupLink() {
        java.util.Map<String, String> result = UrlUtil.parseQuery("a=1&a=2", '&', '=', ",");
        assertEquals("2,1", result.get("a"));
    }

    @Test
    public void testParseQuery_WithNullQuery() {
        java.util.Map<String, String> result = UrlUtil.parseQuery(null, '&', '=', null);
        assertNull(result);
    }

    @Test
    public void testParseQuery_WithEmptyQuery() {
        java.util.Map<String, String> result = UrlUtil.parseQuery("", '&', '=', null);
        assertNull(result);
    }

    // --- httpParseQuery ---

    @Test
    public void testHttpParseQuery_WithValidQuery() {
        java.util.Map<String, String> result = UrlUtil.httpParseQuery("name=test&value=123");
        assertEquals("test", result.get("name"));
        assertEquals("123", result.get("value"));
    }

    @Test
    public void testHttpParseQuery_WithNull() {
        java.util.Map<String, String> result = UrlUtil.httpParseQuery(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- decodeQuery ---

    @Test
    public void testDecodeQuery_WithEncodedString() {
        String result = UrlUtil.decodeQuery("hello%20world");
        assertEquals("hello world", result);
    }

    @Test
    public void testDecodeQuery_WithPlusAsSpace() {
        String result = UrlUtil.decodeQuery("hello+world");
        assertEquals("hello world", result);
    }

    @Test
    public void testDecodeQuery_WithNull() {
        String result = UrlUtil.decodeQuery(null);
        assertNull(result);
    }

    @Test
    public void testDecodeQuery_WithChinese() {
        String result = UrlUtil.decodeQuery("%E6%B5%8B%E8%AF%95");
        assertEquals("测试", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeQuery_WithInvalidSequence() {
        UrlUtil.decodeQuery("%ZZ");
    }

    // --- Builder ---

    @Test
    public void testBuilder_WithBasicPath() {
        String result = UrlUtil.Builder.build("/api/user").toString();
        // Path is URL encoded, so / becomes %2F
        assertTrue("Result should contain encoded path", result.contains("%2Fapi%2Fuser"));
    }

    @Test
    public void testBuilder_WithQueryParams() {
        String result = UrlUtil.Builder.build("/api/user")
                .queryParam("name", "张三")
                .queryParam("age", "20")
                .toString();
        assertTrue(result.contains("%2Fapi%2Fuser"));
        assertTrue(result.contains("name="));
        assertTrue(result.contains("age="));
    }

    @Test
    public void testBuilder_WithNullValue() {
        // null值不会添加name=，因为代码判断 value != null && !value.isEmpty()
        String result = UrlUtil.Builder.build("/api/user")
                .queryParam("name", null)
                .toString();
        assertTrue(result.contains("%2Fapi%2Fuser"));
        // null值不产生任何输出
        assertFalse(result.contains("name="));
    }

    @Test
    public void testBuilder_WithEmptyValue() {
        // 空字符串也不会添加name=
        String result = UrlUtil.Builder.build("/api/user")
                .queryParam("name", "")
                .toString();
        assertTrue(result.contains("%2Fapi%2Fuser"));
        assertFalse(result.contains("name="));
    }

    @Test
    public void testBuilder_ChainMultipleQueryParams() {
        String result = UrlUtil.Builder.build("/search")
                .queryParam("q", "test")
                .queryParam("page", "1")
                .queryParam("size", "10")
                .toString();
        assertTrue(result.contains("%2Fsearch"));
        assertTrue(result.contains("q="));
        assertTrue(result.contains("page="));
        assertTrue(result.contains("size="));
    }
}