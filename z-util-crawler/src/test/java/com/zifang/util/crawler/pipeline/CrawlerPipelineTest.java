package com.zifang.util.crawler.pipeline;

import com.zifang.util.crawler.http.CrawlerHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CrawlerPipelineTest {

    private MockWebServer mockWebServer;
    private CrawlerPipeline pipeline;

    @Before
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        pipeline = new CrawlerPipeline();
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    private String getBaseUrl() {
        return mockWebServer.url("/").toString().replaceAll("/$", "");
    }

    // --- Request static factory methods tests ---

    @Test
    public void testRequest_Get() {
        CrawlerPipeline.Request request = CrawlerPipeline.Request.get("https://example.com");
        assertEquals("https://example.com", request.getUrl());
        assertEquals("GET", request.getMethod());
    }

    @Test
    public void testRequest_Post() {
        CrawlerPipeline.Request request = CrawlerPipeline.Request.post(
                "https://example.com", "{\"name\":\"test\"}", "application/json");
        assertEquals("https://example.com", request.getUrl());
        assertEquals("POST", request.getMethod());
        assertEquals("{\"name\":\"test\"}", request.getBody());
        assertEquals("application/json", request.getContentType());
    }

    // --- Request getters and setters ---

    @Test
    public void testRequest_SettersAndGetters() {
        CrawlerPipeline.Request request = new CrawlerPipeline.Request();

        request.setUrl("https://test.com");
        request.setMethod("POST");
        request.setBody("body content");
        request.setContentType("text/plain");

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Custom", "value");
        request.setHeaders(headers);

        assertEquals("https://test.com", request.getUrl());
        assertEquals("POST", request.getMethod());
        assertEquals("body content", request.getBody());
        assertEquals("text/plain", request.getContentType());
        assertEquals("value", request.getHeaders().get("X-Custom"));
    }

    // --- Pipeline execution tests ---

    @Test
    public void testExecute_GetRequest() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("<html><head><title>Test</title></head><body>Hello</body></html>")
                .addHeader("Content-Type", "text/html"));

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(getBaseUrl());
        PipelineContext ctx = pipeline.execute(request);

        assertNotNull(ctx);
        assertEquals(getBaseUrl(), ctx.getUrl());
        assertNotNull(ctx.getHtml());
        assertTrue(ctx.getHtml().contains("Hello"));
        assertFalse(ctx.hasErrors());
    }

    @Test
    public void testExecute_PostRequest() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"result\":\"success\"}")
                .addHeader("Content-Type", "application/json"));

        CrawlerPipeline.Request request = CrawlerPipeline.Request.post(
                getBaseUrl(), "{}", "application/json");
        PipelineContext ctx = pipeline.execute(request);

        assertNotNull(ctx);
        assertFalse(ctx.hasErrors());
    }

    @Test
    public void testExecute_WithHeaders() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("OK")
                .setResponseCode(200));

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer token");
        headers.put("Accept", "application/json");

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(getBaseUrl());
        request.setHeaders(headers);

        PipelineContext ctx = pipeline.execute(request);

        assertNotNull(ctx);
        assertEquals("Bearer token", ctx.getHeaders().get("Authorization"));
    }

    @Test
    public void testExecute_With404Response() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("Not Found"));

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(getBaseUrl());
        PipelineContext ctx = pipeline.execute(request);

        assertNotNull(ctx);
        // HTTP errors don't add to context errors in current implementation
        assertNotNull(ctx.getHtml());
    }

    @Test
    public void testExecute_WithServerError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(getBaseUrl());
        PipelineContext ctx = pipeline.execute(request);

        assertNotNull(ctx);
        assertNotNull(ctx.getHtml());
    }

    @Test
    public void testExecute_WithConnectionFailure() {
        // Use an invalid URL to simulate connection failure
        CrawlerPipeline.Request request = CrawlerPipeline.Request.get("http://localhost:99999");
        PipelineContext ctx = pipeline.execute(request);

        assertNotNull(ctx);
        assertTrue(ctx.hasErrors());
        assertNotNull(ctx.getErrors().get("http"));
    }

    // --- Processor tests ---

    @Test
    public void testAddProcessor() {
        TestProcessor processor = new TestProcessor();
        pipeline.addProcessor(processor);

        mockWebServer.enqueue(new MockResponse().setBody("OK"));

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(getBaseUrl());
        PipelineContext ctx = pipeline.execute(request);

        assertTrue(processor.wasCalled());
        assertEquals(ctx, processor.getLastContext());
    }

    @Test
    public void testMultipleProcessors() {
        TestProcessor p1 = new TestProcessor();
        TestProcessor p2 = new TestProcessor();
        pipeline.addProcessor(p1);
        pipeline.addProcessor(p2);

        mockWebServer.enqueue(new MockResponse().setBody("OK"));

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(getBaseUrl());
        pipeline.execute(request);

        assertTrue(p1.wasCalled());
        assertTrue(p2.wasCalled());
    }

    @Test
    public void testProcessorException_DoesNotStopPipeline() {
        FailingProcessor failingProcessor = new FailingProcessor();
        TestProcessor successProcessor = new TestProcessor();
        pipeline.addProcessor(failingProcessor);
        pipeline.addProcessor(successProcessor);

        mockWebServer.enqueue(new MockResponse().setBody("OK"));

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(getBaseUrl());
        PipelineContext ctx = pipeline.execute(request);

        // Failing processor should add error but not stop pipeline
        assertTrue(ctx.hasErrors());
        assertTrue(ctx.getErrors().containsKey("processor.FailingProcessor"));
        // Next processor should still be called
        assertTrue(successProcessor.wasCalled());
    }

    // --- Parse and store tests ---

    @Test
    public void testExecute_ParsesHtmlTitle() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("<html><head><title>My Title</title></head><body>Content</body></html>"));

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(getBaseUrl());
        PipelineContext ctx = pipeline.execute(request);

        assertEquals("My Title", ctx.getData().get("_title"));
    }

    @Test
    public void testExecute_ParsesHtmlBodyText() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("<html><head><title>T</title></head><body><p>Some text</p></body></html>"));

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(getBaseUrl());
        PipelineContext ctx = pipeline.execute(request);

        assertNotNull(ctx.getData().get("_text"));
        assertTrue(ctx.getData().get("_text").toString().contains("Some text"));
    }

    @Test
    public void testExecute_WithEmptyBody() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("")
                .setResponseCode(200));

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(getBaseUrl());
        PipelineContext ctx = pipeline.execute(request);

        assertNotNull(ctx);
        // Should not throw exception with empty body
    }

    @Test
    public void testExecute_WithNullContentType() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("<html><head><title>T</title></head><body>Hi</body></html>"));

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(getBaseUrl());
        PipelineContext ctx = pipeline.execute(request);

        assertNotNull(ctx);
        assertFalse(ctx.hasErrors());
    }

    // --- Custom HTTP Client test ---

    @Test
    public void testExecute_WithCustomHttpClient() throws IOException {
        MockWebServer secondServer = new MockWebServer();
        secondServer.start();
        secondServer.enqueue(new MockResponse().setBody("Custom Client Response"));

        try {
            CrawlerHttpClient customClient = new CrawlerHttpClient();
            CrawlerPipeline customPipeline = new CrawlerPipeline(customClient);

            String customUrl = secondServer.url("/").toString().replaceAll("/$", "");
            CrawlerPipeline.Request request = CrawlerPipeline.Request.get(customUrl);
            PipelineContext ctx = customPipeline.execute(request);

            assertNotNull(ctx);
            assertTrue(ctx.getHtml().contains("Custom Client Response"));
        } finally {
            secondServer.shutdown();
        }
    }

    // --- Helper classes for testing ---

    private static class TestProcessor implements Processor {
        private PipelineContext lastContext;
        private boolean called = false;

        @Override
        public void process(PipelineContext ctx) {
            called = true;
            lastContext = ctx;
            ctx.put("processor_called", true);
        }

        public boolean wasCalled() {
            return called;
        }

        public PipelineContext getLastContext() {
            return lastContext;
        }
    }

    private static class FailingProcessor implements Processor {
        @Override
        public void process(PipelineContext ctx) {
            throw new RuntimeException("Intentional test failure");
        }
    }
}
