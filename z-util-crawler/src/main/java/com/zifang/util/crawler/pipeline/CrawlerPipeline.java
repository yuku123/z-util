package com.zifang.util.crawler.pipeline;

import com.zifang.util.crawler.http.CrawlerHttpClient;
import com.zifang.util.crawler.parse.HtmlParser;
import com.zifang.util.crawler.parse.JsonExtractor;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 爬虫管道编排器，协调 HTTP 请求、HTML/JSON 解析和处理器执行。
 * <p>
 * 负责调度整个爬虫流程：发送 HTTP 请求、解析响应内容、
 * 按顺序执行注册的处理器，最终返回包含提取结果的上下文对象。
 *
 * @author zifang
 * @version 1.0.0
 */
public class CrawlerPipeline {

    private final List<Processor> processors = new ArrayList<>();
    private final CrawlerHttpClient httpClient;

    /**
     * 构造 CrawlerPipeline，使用默认的 CrawlerHttpClient。
     */
    public CrawlerPipeline() {
        this.httpClient = new CrawlerHttpClient();
    }

    /**
     * 构造 CrawlerPipeline，使用指定的 HTTP 客户端。
     * @param httpClient HTTP 客户端
     */
    public CrawlerPipeline(CrawlerHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * 添加处理器到管道。
     * @param p 处理器实例
     */
    public void addProcessor(Processor p) {
        processors.add(p);
    }

    /**
     * Execute the pipeline for the given request.
     * Steps: HTTP fetch -> HTML/JSON parse -> processors run in order -> return context
     */
    public PipelineContext execute(Request request) {
        PipelineContext ctx = new PipelineContext();
        ctx.setUrl(request.getUrl());
        if (request.getHeaders() != null) {
            ctx.setHeaders(request.getHeaders());
        }

        // HTTP fetch
        try {
            CrawlerHttpClient.HttpResponse response;
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                response = httpClient.post(request.getUrl(), request.getBody(), request.getHeaders());
            } else {
                response = httpClient.get(request.getUrl(), request.getHeaders());
            }
            ctx.setHtml(response.getBody());
            ctx.setJson(response.getBody()); // same content can be treated as JSON too

            // Pre-parse HTML/JSON if content type suggests
            parseAndStore(ctx, response.getBody(), request.getContentType());
        } catch (IOException e) {
            ctx.addError("http", e.getMessage());
        }

        // Run processors in order
        for (Processor processor : processors) {
            try {
                processor.process(ctx);
            } catch (Exception e) {
                ctx.addError("processor." + processor.getClass().getSimpleName(), e.getMessage());
            }
        }

        return ctx;
    }

    private void parseAndStore(PipelineContext ctx, String body, String contentType) {
        if (body == null || body.isEmpty()) {
            return;
        }
        if (contentType != null && contentType.contains("json")) {
            ctx.setJson(body);
        } else if (contentType == null || contentType.contains("html")) {
            try {
                Document doc = HtmlParser.parse(body);
                ctx.getData().put("_title", doc.title());
                ctx.getData().put("_text", doc.body().text());
            } catch (Exception e) {
                // ignore parsing errors
            }
        }
    }

    /**
     * 简单的请求对象。
     */
    public static class Request {
        private String url;
        private String method = "GET";
        private String body;
        private String contentType;
        private java.util.Map<String, String> headers;

    /**
     * getUrl方法。
     * @return String类型返回值
     */
        public String getUrl() {
            return url;
        }

    /**
     * setUrl方法。
     *      * @param url String类型参数
     */
        public void setUrl(String url) {
            this.url = url;
        }

    /**
     * getMethod方法。
     * @return String类型返回值
     */
        public String getMethod() {
            return method;
        }

    /**
     * setMethod方法。
     *      * @param method String类型参数
     */
        public void setMethod(String method) {
            this.method = method;
        }

    /**
     * getBody方法。
     * @return String类型返回值
     */
        public String getBody() {
            return body;
        }

    /**
     * setBody方法。
     *      * @param body String类型参数
     */
        public void setBody(String body) {
            this.body = body;
        }

    /**
     * getContentType方法。
     * @return String类型返回值
     */
        public String getContentType() {
            return contentType;
        }

    /**
     * setContentType方法。
     *      * @param contentType String类型参数
     */
        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

    /**
     * getHeaders方法。
     * @return java.util.Map<String, String>类型返回值
     */
        public java.util.Map<String, String> getHeaders() {
            return headers;
        }

    /**
     * setHeaders方法。
     *      * @param headers java.util.MapString,类型参数
     */
        public void setHeaders(java.util.Map<String, String> headers) {
            this.headers = headers;
        }

    /**
     * get方法。
     *      * @param url String类型参数
     * @return static Request类型返回值
     */
        public static Request get(String url) {
            Request req = new Request();
            req.setUrl(url);
            return req;
        }

    /**
     * post方法。
     *      * @param url String类型参数
     * @param body String类型参数
     * @param contentType String类型参数
     * @return static Request类型返回值
     */
        public static Request post(String url, String body, String contentType) {
            Request req = new Request();
            req.setUrl(url);
            req.setMethod("POST");
            req.setBody(body);
            req.setContentType(contentType);
            return req;
        }
    }
}
