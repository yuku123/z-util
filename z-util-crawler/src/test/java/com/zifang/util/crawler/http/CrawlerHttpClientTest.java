package com.zifang.util.crawler.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * CrawlerHttpClientTest类。
 */
public class CrawlerHttpClientTest {

    private Thread serverThread;
    private int serverPort;
    private volatile boolean serverStop = false;
    private final CountDownLatch serverReady = new CountDownLatch(1);
    private volatile IOException serverError;

    private CrawlerHttpClient httpClient;
    private String baseUrl;

    @Before
    /**
     * setUp方法。
     */
    public void setUp() throws Exception {
        httpClient = new CrawlerHttpClient();

        serverThread = new Thread(() -> {
            try (ServerSocket ss = new ServerSocket(0)) {
                serverPort = ss.getLocalPort();
                baseUrl = "http://localhost:" + serverPort;
                serverReady.countDown();
                ss.setSoTimeout(2000);
                while (!serverStop) {
                    try {
                        Socket socket = ss.accept();
                        socket.setSoTimeout(3000);
                        handleSocket(socket);
                    } catch (java.net.SocketTimeoutException ignored) {
                        // continue
                    }
                }
            } catch (IOException e) {
                if (!serverStop) {
                    serverError = e;
                }
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
        serverReady.await(5, TimeUnit.SECONDS);
    }

    @After
    /**
     * tearDown方法。
     */
    public void tearDown() throws Exception {
        serverStop = true;
        if (serverThread != null) {
            serverThread.interrupt();
            serverThread.join(3000);
        }
    }

    // Per-test response state (set by enqueue(), read by handleSocket())
    private final AtomicReference<TestResponse> nextResponse = new AtomicReference<>();

    private void enqueue(int code, String body, String contentType) {
        nextResponse.set(new TestResponse(code, body, contentType));
    }

    private static class TestResponse {
        final int code;
        final String body;
        final String contentType;
        TestResponse(int code, String body, String contentType) {
            this.code = code;
            this.body = body;
            this.contentType = contentType;
        }
    }

    private void handleSocket(Socket socket) {
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // Read and parse HTTP request (only need request line + headers)
            String requestLine = readLine(in);
            if (requestLine == null || requestLine.isEmpty()) {
                socket.close();
                return;
            }

            // Discard headers
            String line;
            while (!(line = readLine(in)).isEmpty()) {
                // skip headers
            }

            // Get response to send
            TestResponse resp = nextResponse.getAndSet(null);
            if (resp == null) {
                resp = new TestResponse(200, "Default", "text/plain");
            }

            // Send HTTP/1.1 response with Connection: close
            String statusText = resp.code == 200 ? "OK" : resp.code == 404 ? "Not Found"
                    : resp.code == 201 ? "Created" : resp.code == 204 ? "No Content"
                    : resp.code == 500 ? "Internal Server Error" : "Error";
            byte[] bodyBytes = resp.body.getBytes(StandardCharsets.UTF_8);
            String responseHeaders = String.format(
                    "HTTP/1.1 %d %s\r\nContent-Type: %s\r\nContent-Length: %d\r\nConnection: close\r\n\r\n",
                    resp.code, statusText, resp.contentType, bodyBytes.length);
            out.write(responseHeaders.getBytes(StandardCharsets.UTF_8));
            out.write(bodyBytes);
            out.flush();
            socket.close();
        } catch (Exception e) {
            try { socket.close(); } catch (Exception ignored) {}
        }
    }

    private String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int ch;
        while ((ch = in.read()) != -1) {
            if (ch == '\r') {
                int peek = in.read();
                if (peek == '\n') {
                    return sb.toString();
                } else {
                    if (peek != -1) sb.append((char) peek);
                }
            } else if (ch == '\n') {
                return sb.toString();
            }
            sb.append((char) ch);
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    // --- HttpResponse inner class tests ---

    @Test
    /**
     * testHttpResponse_GetCode方法。
     */
    public void testHttpResponse_GetCode() {
        CrawlerHttpClient.HttpResponse response = new CrawlerHttpClient.HttpResponse(200, "body", new HashMap<>());
        assertEquals(200, response.getCode());
    }

    @Test
    /**
     * testHttpResponse_GetBody方法。
     */
    public void testHttpResponse_GetBody() {
        CrawlerHttpClient.HttpResponse response = new CrawlerHttpClient.HttpResponse(200, "test body", new HashMap<>());
        assertEquals("test body", response.getBody());
    }

    @Test
    /**
     * testHttpResponse_GetHeaders方法。
     */
    public void testHttpResponse_GetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        CrawlerHttpClient.HttpResponse response = new CrawlerHttpClient.HttpResponse(200, "body", headers);
        assertEquals("application/json", response.getHeaders().get("Content-Type"));
    }

    @Test
    /**
     * testHttpResponse_WithNullBody方法。
     */
    public void testHttpResponse_WithNullBody() {
        CrawlerHttpClient.HttpResponse response = new CrawlerHttpClient.HttpResponse(200, null, new HashMap<>());
        assertNull(response.getBody());
    }

    @Test
    /**
     * testHttpResponse_WithNullHeaders方法。
     */
    public void testHttpResponse_WithNullHeaders() {
        CrawlerHttpClient.HttpResponse response = new CrawlerHttpClient.HttpResponse(200, "body", null);
        assertNotNull(response.getHeaders());
    }

    // --- GET request tests ---

    @Test
    /**
     * testGet_WithSuccessfulResponse方法。
     */
    public void testGet_WithSuccessfulResponse() throws Exception {
        enqueue(200, "Hello World", "text/plain");
        CrawlerHttpClient.HttpResponse response = httpClient.get(baseUrl, null);
        assertEquals(200, response.getCode());
        assertEquals("Hello World", response.getBody());
    }

    @Test
    /**
     * testGet_WithHeaders方法。
     */
    public void testGet_WithHeaders() throws Exception {
        enqueue(200, "OK", "text/plain");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer token");
        headers.put("Accept", "application/json");
        CrawlerHttpClient.HttpResponse response = httpClient.get(baseUrl, headers);
        assertEquals(200, response.getCode());
    }

    @Test
    /**
     * testGet_With404Response方法。
     */
    public void testGet_With404Response() throws Exception {
        enqueue(404, "Not Found", "text/plain");
        CrawlerHttpClient.HttpResponse response = httpClient.get(baseUrl, null);
        assertEquals(404, response.getCode());
        assertEquals("Not Found", response.getBody());
    }

    @Test
    /**
     * testGet_ReturnsResponseHeaders方法。
     */
    public void testGet_ReturnsResponseHeaders() throws Exception {
        enqueue(200, "OK", "text/plain");
        CrawlerHttpClient.HttpResponse response = httpClient.get(baseUrl, null);
        assertNotNull(response.getHeaders());
    }

    // --- POST request tests ---

    @Test
    /**
     * testPost_WithSuccessfulResponse方法。
     */
    public void testPost_WithSuccessfulResponse() throws Exception {
        enqueue(201, "Created", "text/plain");
        CrawlerHttpClient.HttpResponse response = httpClient.post(baseUrl, "{\"name\":\"test\"}", null);
        assertEquals(201, response.getCode());
        assertEquals("Created", response.getBody());
    }

    @Test
    /**
     * testPost_WithHeaders方法。
     */
    public void testPost_WithHeaders() throws Exception {
        enqueue(200, "OK", "text/plain");
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        CrawlerHttpClient.HttpResponse response = httpClient.post(baseUrl, "{}", headers);
        assertEquals(200, response.getCode());
    }

    @Test
    /**
     * testPost_WithEmptyBody方法。
     */
    public void testPost_WithEmptyBody() throws Exception {
        enqueue(204, "", "text/plain");
        CrawlerHttpClient.HttpResponse response = httpClient.post(baseUrl, "", null);
        assertEquals(204, response.getCode());
    }

    // --- Download tests ---

    @Test
    /**
     * testDownload_Success方法。
     */
    public void testDownload_Success() throws Exception {
        enqueue(200, "file content here", "text/plain");
        File tempFile = File.createTempFile("download_test", ".txt");
        tempFile.deleteOnExit();
        httpClient.download(baseUrl, tempFile.getAbsolutePath());
        assertTrue(tempFile.exists());
        String content = new String(java.nio.file.Files.readAllBytes(tempFile.toPath()));
        assertEquals("file content here", content);
    }

    @Test(expected = IOException.class)
    /**
     * testDownload_With404Response方法。
     */
    public void testDownload_With404Response() throws Exception {
        enqueue(404, "Not Found", "text/plain");
        File tempFile = File.createTempFile("download_test", ".txt");
        tempFile.deleteOnExit();
        httpClient.download(baseUrl, tempFile.getAbsolutePath());
    }

    @Test(expected = IOException.class)
    /**
     * testDownload_WithServerError方法。
     */
    public void testDownload_WithServerError() throws Exception {
        enqueue(500, "Internal Server Error", "text/plain");
        File tempFile = File.createTempFile("download_test", ".txt");
        tempFile.deleteOnExit();
        httpClient.download(baseUrl, tempFile.getAbsolutePath());
    }

    // --- setProxy test ---

    @Test
    /**
     * testSetProxy_DoesNotThrow方法。
     */
    public void testSetProxy_DoesNotThrow() {
        httpClient.setProxy("127.0.0.1", 8080);
    }

    // --- Multiple requests test ---

    @Test
    /**
     * testMultipleGetRequests方法。
     */
    public void testMultipleGetRequests() throws Exception {
        enqueue(200, "Response 1", "text/plain");
        CrawlerHttpClient.HttpResponse r1 = httpClient.get(baseUrl, null);
        enqueue(200, "Response 2", "text/plain");
        CrawlerHttpClient.HttpResponse r2 = httpClient.get(baseUrl, null);
        enqueue(200, "Response 3", "text/plain");
        CrawlerHttpClient.HttpResponse r3 = httpClient.get(baseUrl, null);
        assertEquals("Response 1", r1.getBody());
        assertEquals("Response 2", r2.getBody());
        assertEquals("Response 3", r3.getBody());
    }
}
