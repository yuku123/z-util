package com.zifang.util.core.net;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * UrlUtil 单元测试类
 */
public class UrlUtilTest {

    // ========== encode ==========

    @Test
    public void testEncode_Null() {
        assertNull(UrlUtil.encode(null));
    }

    @Test
    public void testEncode_NormalString() {
        assertEquals("hello%20world", UrlUtil.encode("hello world"));
    }

    @Test
    public void testEncode_SpecialChars() {
        String result = UrlUtil.encode("a=b&c=d");
        assertTrue(result.contains("%3D"));
        assertTrue(result.contains("%26"));
    }

    @Test
    public void testEncode_Chinese() {
        String result = UrlUtil.encode("测试");
        assertTrue(result.contains("%E"));
    }

    @Test
    public void testEncode_CustomEncoding() {
        assertEquals("hello%20world", UrlUtil.encode("hello world", "UTF-8"));
    }

    @Test
    public void testEncode_Empty() {
        assertEquals("", UrlUtil.encode(""));
    }

    // ========== decode ==========

    @Test
    public void testDecode_Null() {
        assertNull(UrlUtil.decode(null));
    }

    @Test
    public void testDecode_EncodedString() {
        assertEquals("hello world", UrlUtil.decode("hello%20world"));
    }

    @Test
    public void testDecode_CustomEncoding() {
        assertEquals("hello world", UrlUtil.decode("hello%20world", "UTF-8"));
    }

    @Test
    public void testDecode_Chinese() {
        assertEquals("测试", UrlUtil.decode("%E6%B5%8B%E8%AF%95"));
    }

    // ========== setParam ==========

    @Test
    public void testSetParam_NullUrl() {
        assertNull(UrlUtil.setParam(null, "name", "value"));
    }

    @Test
    public void testSetParam_NullParamName() {
        assertNull(UrlUtil.setParam("http://example.com", null, "value"));
    }

    @Test
    public void testSetParam_WithoutExistingParams() {
        String result = UrlUtil.setParam("http://example.com", "name", "value");
        assertEquals("http://example.com?name=value", result);
    }

    @Test
    public void testSetParam_WithExistingParams() {
        String result = UrlUtil.setParam("http://example.com?a=1", "b", "2");
        assertEquals("http://example.com?a=1&b=2", result);
    }

    @Test
    public void testSetParam_ReplaceExistingParam() {
        String result = UrlUtil.setParam("http://example.com?name=old", "name", "new");
        assertEquals("http://example.com?name=new", result);
    }

    @Test
    public void testSetParam_ReplaceMiddleParam() {
        String result = UrlUtil.setParam("http://example.com?a=1&b=2&c=3", "b", "new");
        assertEquals("http://example.com?a=1&b=new&c=3", result);
    }

    @Test
    public void testSetParam_ValueAutoEncoded() {
        String result = UrlUtil.setParam("http://example.com", "name", "hello world");
        assertEquals("http://example.com?name=hello%20world", result);
    }

    // ========== getParamValue ==========

    @Test
    public void testGetParamValue_NullUrl() {
        assertNull(UrlUtil.getParamValue(null, "name"));
    }

    @Test
    public void testGetParamValue_NullParamName() {
        assertNull(UrlUtil.getParamValue("http://example.com?name=test", null));
    }

    @Test
    public void testGetParamValue_WithExistingParam() {
        assertEquals("test", UrlUtil.getParamValue("http://example.com?name=test", "name"));
    }

    @Test
    public void testGetParamValue_WithMultipleParams() {
        assertEquals("2", UrlUtil.getParamValue("http://example.com?a=1&b=2&c=3", "b"));
    }

    @Test
    public void testGetParamValue_FirstParam() {
        assertEquals("1", UrlUtil.getParamValue("http://example.com?a=1&b=2", "a"));
    }

    @Test
    public void testGetParamValue_LastParam() {
        assertEquals("3", UrlUtil.getParamValue("http://example.com?a=1&b=2&c=3", "c"));
    }

    @Test
    public void testGetParamValue_WithNonExistentParam() {
        assertNull(UrlUtil.getParamValue("http://example.com?name=test", "nonExistent"));
    }

    @Test
    public void testGetParamValue_WithoutParams() {
        assertNull(UrlUtil.getParamValue("http://example.com", "name"));
    }

    @Test
    public void testGetParamValue_ValueAutoDecoded() {
        assertEquals("hello world", UrlUtil.getParamValue("http://example.com?name=hello%20world", "name"));
    }

    // ========== removeParam ==========

    @Test
    public void testRemoveParam_NullUrl() {
        assertNull(UrlUtil.removeParam(null, "name"));
    }

    @Test
    public void testRemoveParam_NullParamNames() {
        assertEquals("http://example.com?name=test", UrlUtil.removeParam("http://example.com?name=test", (String[]) null));
    }

    @Test
    public void testRemoveParam_SingleParam() {
        String result = UrlUtil.removeParam("http://example.com?name=test", "name");
        assertEquals("http://example.com", result);
    }

    @Test
    public void testRemoveParam_MultipleParams() {
        String result = UrlUtil.removeParam("http://example.com?a=1&b=2&c=3", "b");
        assertTrue(result.contains("a=1"));
        assertFalse(result.contains("b=2"));
        assertTrue(result.contains("c=3"));
    }

    @Test
    public void testRemoveParam_MultipleParamNames() {
        String result = UrlUtil.removeParam("http://example.com?a=1&b=2&c=3", "a", "c");
        assertFalse(result.contains("a=1"));
        assertTrue(result.contains("b=2"));
        assertFalse(result.contains("c=3"));
    }

    @Test
    public void testRemoveParam_NonExistentParam() {
        assertEquals("http://example.com?name=test", UrlUtil.removeParam("http://example.com?name=test", "nonExistent"));
    }

    @Test
    public void testRemoveParam_LastParam() {
        assertEquals("http://example.com?a=1&b=2", UrlUtil.removeParam("http://example.com?a=1&b=2&c=3", "c"));
    }

    @Test
    public void testRemoveParam_OnlyParam() {
        assertEquals("http://example.com", UrlUtil.removeParam("http://example.com?name=test", "name"));
    }

    // ========== urlJoin ==========

    @Test
    public void testUrlJoin_NullBaseUrl() {
        assertEquals("http://other.com/new", UrlUtil.urlJoin(null, "http://other.com/new"));
    }

    @Test
    public void testUrlJoin_NullLocation() {
        assertEquals("http://example.com/path", UrlUtil.urlJoin(new URL("http://example.com/path"), null));
    }

    @Test
    public void testUrlJoin_AbsoluteLocation() throws MalformedURLException {
        URL url = new URL("http://example.com/path");
        assertEquals("http://other.com/new", UrlUtil.urlJoin(url, "http://other.com/new"));
    }

    @Test
    public void testUrlJoin_RelativeLocation() throws MalformedURLException {
        URL url = new URL("http://example.com/path");
        String result = UrlUtil.urlJoin(url, "/new");
        assertEquals("http://example.com/new", result);
    }

    @Test
    public void testUrlJoin_RelativeLocationWithSlash() throws MalformedURLException {
        URL url = new URL("http://example.com/moved_perm");
        String result = UrlUtil.urlJoin(url, "/");
        assertEquals("http://example.com/", result);
    }

    @Test
    public void testUrlJoin_RelativeLocationWithPath() throws MalformedURLException {
        URL url = new URL("http://example.com/path");
        String result = UrlUtil.urlJoin(url, "subpath");
        assertEquals("http://example.com/subpath", result);
    }

    // ========== getRequestParams ==========

    @Test
    public void testGetRequestParams_Null() {
        Map<String, String> result = UrlUtil.getRequestParams(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ========== parseQuery ==========

    @Test
    public void testParseQuery_Null() {
        Map<String, String> result = UrlUtil.parseQuery(null, '&', '=', null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testParseQuery_Empty() {
        Map<String, String> result = UrlUtil.parseQuery("", '&', '=', null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testParseQuery_Simple() {
        Map<String, String> result = UrlUtil.parseQuery("a=1&b=2", '&', '=', null);
        assertEquals("1", result.get("a"));
        assertEquals("2", result.get("b"));
    }

    @Test
    public void testParseQuery_DuplicateParams() {
        Map<String, String> result = UrlUtil.parseQuery("a=1&a=2", '&', '=', null);
        assertEquals("2", result.get("a"));
    }

    @Test
    public void testParseQuery_DuplicateParamsWithDupLink() {
        Map<String, String> result = UrlUtil.parseQuery("a=1&a=2", '&', '=', ",");
        assertEquals("2,1", result.get("a"));
    }

    @Test
    public void testParseQuery_NoSeparator() {
        Map<String, String> result = UrlUtil.parseQuery("a", '&', '=', null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ========== httpParseQuery ==========

    @Test
    public void testHttpParseQuery_Valid() {
        Map<String, String> result = UrlUtil.httpParseQuery("name=test&value=123");
        assertEquals("test", result.get("name"));
        assertEquals("123", result.get("value"));
    }

    @Test
    public void testHttpParseQuery_Null() {
        Map<String, String> result = UrlUtil.httpParseQuery(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testHttpParseQuery_Empty() {
        Map<String, String> result = UrlUtil.httpParseQuery("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ========== decodeQuery ==========

    @Test
    public void testDecodeQuery_Null() {
        assertNull(UrlUtil.decodeQuery(null));
    }

    @Test
    public void testDecodeQuery_EncodedString() {
        assertEquals("hello world", UrlUtil.decodeQuery("hello%20world"));
    }

    @Test
    public void testDecodeQuery_PlusAsSpace() {
        assertEquals("hello world", UrlUtil.decodeQuery("hello+world"));
    }

    @Test
    public void testDecodeQuery_Chinese() {
        assertEquals("测试", UrlUtil.decodeQuery("%E6%B5%8B%E8%AF%95"));
    }

    @Test
    public void testDecodeQuery_InvalidSequence() {
        // 无效序列不抛异常，返回原字符串
        String result = UrlUtil.decodeQuery("%ZZ");
        assertEquals("%ZZ", result);
    }

    @Test
    public void testDecodeQuery_IncompleteSequence() {
        // 不完整的序列不抛异常，返回原字符串
        String result = UrlUtil.decodeQuery("%ZZ");
        assertNotNull(result);
    }

    // ========== Builder ==========

    @Test
    public void testBuilder_Basic() {
        String result = new UrlUtil.Builder("/api/user").toString();
        assertEquals("/api/user", result);
    }

    @Test
    public void testBuilder_WithQueryParams() {
        String result = new UrlUtil.Builder("/api/user")
                .queryParam("name", "张三")
                .queryParam("age", "20")
                .toString();
        assertTrue(result.startsWith("/api/user"));
        assertTrue(result.contains("name="));
        assertTrue(result.contains("age="));
    }

    @Test
    public void testBuilder_QueryParamWithNullName() {
        String result = new UrlUtil.Builder("/api/user")
                .queryParam(null, "value")
                .toString();
        assertEquals("/api/user", result);
    }

    @Test
    public void testBuilder_ChainMultipleQueryParams() {
        String result = new UrlUtil.Builder("/search")
                .queryParam("q", "test")
                .queryParam("page", "1")
                .queryParam("size", "10")
                .toString();
        assertTrue(result.contains("q="));
        assertTrue(result.contains("page="));
        assertTrue(result.contains("size="));
    }

    @Test
    public void testBuilder_PathSegment() {
        String result = new UrlUtil.Builder("/api")
                .pathSegment("user")
                .pathSegment("123")
                .toString();
        assertEquals("/api/user/123", result);
    }

    @Test
    public void testBuilder_QueryParamsMap() {
        Map<String, String> params = new java.util.HashMap<>();
        params.put("name", "test");
        params.put("value", "123");
        String result = new UrlUtil.Builder("/api/user")
                .queryParams(params)
                .toString();
        assertTrue(result.contains("name="));
        assertTrue(result.contains("value="));
    }

    @Test
    public void testBuilder_Build() {
        String result = new UrlUtil.Builder("/api/user")
                .queryParam("name", "test")
                .build();
        assertTrue(result.contains("name="));
    }

    @Test
    public void testBuilder_ToString() {
        UrlUtil.Builder builder = new UrlUtil.Builder("/api/user");
        assertEquals(builder.toString(), builder.build());
    }
}
