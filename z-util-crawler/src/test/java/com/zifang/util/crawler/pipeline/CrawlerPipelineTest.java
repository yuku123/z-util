package com.zifang.util.crawler.pipeline;

import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

/**
 * CrawlerPipelineTest类。
 */
public class CrawlerPipelineTest {

    private HttpServer httpServer;
    private String baseUrl;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Before
    /**
     * setUp方法。
     */
    public void setUp() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(0), 0);
        httpServer.setExecutor(executor);
        httpServer.start();
        baseUrl = "http://localhost:" + httpServer.getAddress().getPort();
    }

    @After
    /**
     * tearDown方法。
     */
    public void tearDown() {
        if (httpServer != null) {
            httpServer.stop(0);
        }
    }

    private void enqueue(int code, String body, String contentType) {
        httpServer.createContext("/", exchange -> {
            if (contentType != null) {
                exchange.getResponseHeaders().set("Content-Type", contentType);
            }
            byte[] bytes = body.getBytes();
            exchange.sendResponseHeaders(code, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        });
    }

    // --- Request static factory methods tests ---

    @Test
    /**
     * testRequest_Get方法。
     */
    public void testRequest_Get() {
        CrawlerPipeline.Request request = CrawlerPipeline.Request.get("https://example.com");
        assertEquals("https://example.com", request.getUrl());
        assertEquals("GET", request.getMethod());
    }

    @Test
    /**
     * testRequest_Post方法。
     */
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
    /**
     * testRequest_SettersAndGetters方法。
     */
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
    /**
     * testExecute_GetRequest方法。
     */
    public void testExecute_GetRequest() {
        enqueue(200, "<html><head><title>Test</title></head><body>Hello</body></html>", "text/html");

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(baseUrl);
        PipelineContext ctx = new CrawlerPipeline().execute(request);

        assertNotNull(ctx);
        assertEquals(baseUrl, ctx.getUrl());
        assertNotNull(ctx.getHtml());
        assertTrue(ctx.getHtml().contains("Hello"));
        assertFalse(ctx.hasErrors());
    }

    @Test
    /**
     * testExecute_PostRequest方法。
     */
    public void testExecute_PostRequest() {
        enqueue(200, "{\"result\":\"success\"}", "application/json");

        CrawlerPipeline.Request request = CrawlerPipeline.Request.post(
                baseUrl, "{}", "application/json");
        PipelineContext ctx = new CrawlerPipeline().execute(request);

        assertNotNull(ctx);
        assertFalse(ctx.hasErrors());
    }

    @Test
    /**
     * testExecute_WithHeaders方法。
     */
    public void testExecute_WithHeaders() {
        enqueue(200, "OK", "text/plain");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer token");
        headers.put("Accept", "application/json");

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(baseUrl);
        request.setHeaders(headers);
        PipelineContext ctx = new CrawlerPipeline().execute(request);

        assertNotNull(ctx);
    }

    @Test
    /**
     * testExecute_With404Response方法。
     */
    public void testExecute_With404Response() {
        enqueue(404, "Not Found", "text/plain");

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(baseUrl);
        PipelineContext ctx = new CrawlerPipeline().execute(request);

        assertNotNull(ctx);
        assertNotNull(ctx.getHtml());
    }

    @Test
    /**
     * testExecute_WithServerError方法。
     */
    public void testExecute_WithServerError() {
        enqueue(500, "Internal Server Error", "text/plain");

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(baseUrl);
        PipelineContext ctx = new CrawlerPipeline().execute(request);

        assertNotNull(ctx);
        assertNotNull(ctx.getHtml());
    }

    @Test
    /**
     * testExecute_WithConnectionFailure方法。
     */
    public void testExecute_WithConnectionFailure() {
        CrawlerPipeline.Request request = CrawlerPipeline.Request.get("http://localhost:99999");
        PipelineContext ctx = new CrawlerPipeline().execute(request);

        assertNotNull(ctx);
        assertTrue(ctx.hasErrors());
        assertNotNull(ctx.getErrors().get("http"));
    }

    // --- Processor tests ---

    @Test
    /**
     * testAddProcessor方法。
     */
    public void testAddProcessor() {
        TestProcessor processor = new TestProcessor();
        CrawlerPipeline pipeline = new CrawlerPipeline();
        pipeline.addProcessor(processor);

        enqueue(200, "OK", "text/plain");

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(baseUrl);
        PipelineContext ctx = pipeline.execute(request);

        assertTrue(processor.wasCalled());
        assertEquals(ctx, processor.getLastContext());
    }

    @Test
    /**
     * testMultipleProcessors方法。
     */
    public void testMultipleProcessors() {
        TestProcessor p1 = new TestProcessor();
        TestProcessor p2 = new TestProcessor();
        CrawlerPipeline pipeline = new CrawlerPipeline();
        pipeline.addProcessor(p1);
        pipeline.addProcessor(p2);

        enqueue(200, "OK", "text/plain");

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(baseUrl);
        pipeline.execute(request);

        assertTrue(p1.wasCalled());
        assertTrue(p2.wasCalled());
    }

    @Test
    /**
     * testProcessorException_DoesNotStopPipeline方法。
     */
    public void testProcessorException_DoesNotStopPipeline() {
        FailingProcessor failingProcessor = new FailingProcessor();
        TestProcessor successProcessor = new TestProcessor();
        CrawlerPipeline pipeline = new CrawlerPipeline();
        pipeline.addProcessor(failingProcessor);
        pipeline.addProcessor(successProcessor);

        enqueue(200, "OK", "text/plain");

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(baseUrl);
        PipelineContext ctx = pipeline.execute(request);

        assertTrue(ctx.hasErrors());
        assertTrue(ctx.getErrors().containsKey("processor.FailingProcessor"));
        assertTrue(successProcessor.wasCalled());
    }

    // --- Parse and store tests ---

    @Test
    /**
     * testExecute_ParsesHtmlTitle方法。
     */
    public void testExecute_ParsesHtmlTitle() {
        enqueue(200, "<html><head><title>My Title</title></head><body>Content</body></html>", "text/html");

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(baseUrl);
        PipelineContext ctx = new CrawlerPipeline().execute(request);

        assertEquals("My Title", ctx.getData().get("_title"));
    }

    @Test
    /**
     * testExecute_ParsesHtmlBodyText方法。
     */
    public void testExecute_ParsesHtmlBodyText() {
        enqueue(200, "<html><head><title>T</title></head><body><p>Some text</p></body></html>", "text/html");

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(baseUrl);
        PipelineContext ctx = new CrawlerPipeline().execute(request);

        assertNotNull(ctx.getData().get("_text"));
        assertTrue(ctx.getData().get("_text").toString().contains("Some text"));
    }

    @Test
    /**
     * testExecute_WithEmptyBody方法。
     */
    public void testExecute_WithEmptyBody() {
        enqueue(200, "", "text/plain");

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(baseUrl);
        PipelineContext ctx = new CrawlerPipeline().execute(request);

        assertNotNull(ctx);
    }

    @Test
    /**
     * testExecute_WithNullContentType方法。
     */
    public void testExecute_WithNullContentType() {
        enqueue(200, "<html><head><title>T</title></head><body>Hi</body></html>", null);

        CrawlerPipeline.Request request = CrawlerPipeline.Request.get(baseUrl);
        PipelineContext ctx = new CrawlerPipeline().execute(request);

        assertNotNull(ctx);
        assertFalse(ctx.hasErrors());
    }

    // --- Helper classes for testing ---

    private static class TestProcessor implements Processor {
        private PipelineContext lastContext;
        private boolean called = false;

        @Override
        /**
         * process方法。
         *      * @param ctx PipelineContext类型参数
         */
        public void process(PipelineContext ctx) {
            called = true;
            lastContext = ctx;
            ctx.put("processor_called", true);
        }

        /**
         * wasCalled方法。
         *
         * @return boolean类型返回值
         */
        public boolean wasCalled() {
            return called;
        }

        /**
         * getLastContext方法。
         *
         * @return PipelineContext类型返回值
         */
        public PipelineContext getLastContext() {
            return lastContext;
        }
    }

    private static class FailingProcessor implements Processor {
        @Override
        /**
         * process方法。
         *      * @param ctx PipelineContext类型参数
         */
        public void process(PipelineContext ctx) {
            throw new RuntimeException("Intentional test failure");
        }
    }
}
