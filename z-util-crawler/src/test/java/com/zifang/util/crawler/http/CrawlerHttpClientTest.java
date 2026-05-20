package com.zifang.util.crawler.http;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CrawlerHttpClientTest {

    private MockWebServer mockWebServer;
    private CrawlerHttpClient httpClient;

    @Before
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        httpClient = new CrawlerHttpClient();
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    private String getBaseUrl() {
        return mockWebServer.url("/").toString().replaceAll("/$", "");
    }

    // --- HttpResponse inner class tests ---

    @Test
    public void testHttpResponse_GetCode() {
        CrawlerHttpClient.HttpResponse response = new CrawlerHttpClient.HttpResponse(
                200, "body", new HashMap<>());
        assertEquals(200, response.getCode());
    }

    @Test
    public void testHttpResponse_GetBody() {
        CrawlerHttpClient.HttpResponse response = new CrawlerHttpClient.HttpResponse(
                200, "test body", new HashMap<>());
        assertEquals("test body", response.getBody());
    }

    @Test
    public void testHttpResponse_GetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        CrawlerHttpClient.HttpResponse response = new CrawlerHttpClient.HttpResponse(
                200, "body", headers);
        assertEquals("application/json", response.getHeaders().get("Content-Type"));
    }

    @Test
    public void testHttpResponse_WithNullBody() {
        CrawlerHttpClient.HttpResponse response = new CrawlerHttpClient.HttpResponse(
                200, null, new HashMap<>());
        assertNull(response.getBody());
    }

    @Test
    public void testHttpResponse_WithNullHeaders() {
        CrawlerHttpClient.HttpResponse response = new CrawlerHttpClient.HttpResponse(
                200, "body", null);
        assertNotNull(response.getHeaders());
    }

    // --- GET request tests ---

    @Test
    public void testGet_WithSuccessfulResponse() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setBody("Hello World")
                .addHeader("Content-Type", "text/plain"));

        CrawlerHttpClient.HttpResponse response = httpClient.get(getBaseUrl(), null);

        assertEquals(200, response.getCode());
        assertEquals("Hello World", response.getBody());
    }

    @Test
    public void testGet_WithHeaders() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setBody("OK")
                .setResponseCode(200));

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer token");
        headers.put("Accept", "application/json");

        CrawlerHttpClient.HttpResponse response = httpClient.get(getBaseUrl(), headers);

        assertEquals(200, response.getCode());
    }

    @Test
    public void testGet_With404Response() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("Not Found"));

        CrawlerHttpClient.HttpResponse response = httpClient.get(getBaseUrl(), null);

        assertEquals(404, response.getCode());
        assertEquals("Not Found", response.getBody());
    }

    @Test
    public void testGet_ReturnsResponseHeaders() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setBody("OK")
                .addHeader("X-Custom-Header", "custom-value"));

        CrawlerHttpClient.HttpResponse response = httpClient.get(getBaseUrl(), null);

        assertEquals("custom-value", response.getHeaders().get("X-Custom-Header"));
    }

    // --- POST request tests ---

    @Test
    public void testPost_WithSuccessfulResponse() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setBody("Created")
                .setResponseCode(201));

        CrawlerHttpClient.HttpResponse response = httpClient.post(
                getBaseUrl(), "{\"name\":\"test\"}", null);

        assertEquals(201, response.getCode());
        assertEquals("Created", response.getBody());
    }

    @Test
    public void testPost_WithHeaders() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("OK"));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        CrawlerHttpClient.HttpResponse response = httpClient.post(
                getBaseUrl(), "{}", headers);

        assertEquals(200, response.getCode());
    }

    @Test
    public void testPost_WithEmptyBody() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(204));

        CrawlerHttpClient.HttpResponse response = httpClient.post(getBaseUrl(), "", null);

        assertEquals(204, response.getCode());
    }

    // --- Download tests ---

    @Test
    public void testDownload_Success() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setBody("file content here")
                .setResponseCode(200));

        File tempFile = File.createTempFile("download_test", ".txt");
        tempFile.deleteOnExit();

        httpClient.download(getBaseUrl(), tempFile.getAbsolutePath());

        assertTrue(tempFile.exists());
        String content = new String(java.nio.file.Files.readAllBytes(tempFile.toPath()));
        assertEquals("file content here", content);
    }

    @Test(expected = IOException.class)
    public void testDownload_With404Response() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404));

        File tempFile = File.createTempFile("download_test", ".txt");
        tempFile.deleteOnExit();

        httpClient.download(getBaseUrl(), tempFile.getAbsolutePath());
    }

    @Test(expected = IOException.class)
    public void testDownload_WithServerError() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        File tempFile = File.createTempFile("download_test", ".txt");
        tempFile.deleteOnExit();

        httpClient.download(getBaseUrl(), tempFile.getAbsolutePath());
    }

    // --- setProxy test ---

    @Test
    public void testSetProxy_DoesNotThrow() {
        // setProxy is a no-op in current implementation
        // but we test it doesn't throw
        httpClient.setProxy("127.0.0.1", 8080);
        // no exception means success
    }

    // --- Multiple requests test ---

    @Test
    public void testMultipleGetRequests() throws IOException {
        mockWebServer.enqueue(new MockResponse().setBody("Response 1"));
        mockWebServer.enqueue(new MockResponse().setBody("Response 2"));
        mockWebServer.enqueue(new MockResponse().setBody("Response 3"));

        CrawlerHttpClient.HttpResponse r1 = httpClient.get(getBaseUrl(), null);
        CrawlerHttpClient.HttpResponse r2 = httpClient.get(getBaseUrl(), null);
        CrawlerHttpClient.HttpResponse r3 = httpClient.get(getBaseUrl(), null);

        assertEquals("Response 1", r1.getBody());
        assertEquals("Response 2", r2.getBody());
        assertEquals("Response 3", r3.getBody());
    }
}
