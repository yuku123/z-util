package com.zifang.util.http.parser.curl;

import com.zifang.util.http.base.define.RequestMethod;
import com.zifang.util.http.base.pojo.HttpRequestBody;
import com.zifang.util.http.base.pojo.HttpRequestDefinition;
import com.zifang.util.http.base.pojo.HttpRequestHeader;
import com.zifang.util.http.base.pojo.HttpRequestLine;
import org.junit.Test;

import static org.junit.Assert.*;

public class CurlBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWithNullDefinition() {
        CurlBuilder.build(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildPrettyWithNullDefinition() {
        CurlBuilder.buildPretty(null);
    }

    @Test
    public void testBuildSimpleGetRequest() {
        HttpRequestDefinition definition = new HttpRequestDefinition();
        HttpRequestLine requestLine = new HttpRequestLine();
        requestLine.setRequestMethod(RequestMethod.GET);
        requestLine.setUrl("http://example.com/api");
        definition.setHttpRequestLine(requestLine);
        
        String curl = CurlBuilder.build(definition);
        assertNotNull(curl);
        assertTrue(curl.contains("curl"));
        assertTrue(curl.contains("http://example.com/api"));
    }

    @Test
    public void testBuildPostRequestWithHeadersAndBody() {
        HttpRequestDefinition definition = new HttpRequestDefinition();
        
        HttpRequestLine requestLine = new HttpRequestLine();
        requestLine.setRequestMethod(RequestMethod.POST);
        requestLine.setUrl("http://example.com/api");
        definition.setHttpRequestLine(requestLine);
        
        HttpRequestHeader headers = new HttpRequestHeader();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        definition.setHttpRequestHeader(headers);
        
        HttpRequestBody body = new HttpRequestBody();
        body.setBody("{\"key\":\"value\"}".getBytes());
        definition.setHttpRequestBody(body);
        
        String curl = CurlBuilder.build(definition);
        assertNotNull(curl);
        assertTrue(curl.contains("-X"));
        assertTrue(curl.contains("POST"));
        assertTrue(curl.contains("-H"));
        assertTrue(curl.contains("-d"));
        assertTrue(curl.contains("http://example.com/api"));
    }

    @Test
    public void testBuildPrettyFormat() {
        HttpRequestDefinition definition = new HttpRequestDefinition();
        HttpRequestLine requestLine = new HttpRequestLine();
        requestLine.setRequestMethod(RequestMethod.GET);
        requestLine.setUrl("http://example.com/api");
        definition.setHttpRequestLine(requestLine);
        
        String curl = CurlBuilder.buildPretty(definition);
        assertNotNull(curl);
        assertTrue(curl.contains("curl"));
        assertTrue(curl.contains("\\\n"));
    }

    @Test
    public void testEscapeForShellWithSpecialCharacters() {
        HttpRequestDefinition definition = new HttpRequestDefinition();
        
        HttpRequestLine requestLine = new HttpRequestLine();
        requestLine.setRequestMethod(RequestMethod.POST);
        requestLine.setUrl("http://example.com/api");
        definition.setHttpRequestLine(requestLine);
        
        HttpRequestBody body = new HttpRequestBody();
        body.setBody("test with spaces".getBytes());
        definition.setHttpRequestBody(body);
        
        String curl = CurlBuilder.build(definition);
        assertNotNull(curl);
    }

    @Test
    public void testBuildWithNullRequestLine() {
        HttpRequestDefinition definition = new HttpRequestDefinition();
        
        String curl = CurlBuilder.build(definition);
        assertNotNull(curl);
        assertEquals("curl", curl.trim());
    }

    @Test
    public void testBuildWithNullHeaders() {
        HttpRequestDefinition definition = new HttpRequestDefinition();
        HttpRequestLine requestLine = new HttpRequestLine();
        requestLine.setRequestMethod(RequestMethod.GET);
        requestLine.setUrl("http://example.com/api");
        definition.setHttpRequestLine(requestLine);
        
        String curl = CurlBuilder.build(definition);
        assertNotNull(curl);
        assertTrue(curl.contains("curl"));
        assertTrue(curl.contains("http://example.com/api"));
    }

    @Test
    public void testBuildWithNullBody() {
        HttpRequestDefinition definition = new HttpRequestDefinition();
        HttpRequestLine requestLine = new HttpRequestLine();
        requestLine.setRequestMethod(RequestMethod.GET);
        requestLine.setUrl("http://example.com/api");
        definition.setHttpRequestLine(requestLine);
        
        HttpRequestHeader headers = new HttpRequestHeader();
        headers.put("Content-Type", "application/json");
        definition.setHttpRequestHeader(headers);
        
        String curl = CurlBuilder.build(definition);
        assertNotNull(curl);
        assertTrue(curl.contains("-H"));
        assertFalse(curl.contains("-d"));
    }
}
