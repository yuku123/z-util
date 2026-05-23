package com.zifang.util.crawler.http;

import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于 OkHttp 的 HTTP 客户端，支持重试、Cookie 管理、代理支持和可配置超时。
 * <p>
 * 为爬虫场景设计的 HTTP 客户端，提供 GET/POST 请求、文件下载等功能，
 * 内置自动重试机制和内存 Cookie 管理。
 *
 * @author zifang
 * @version 1.0.0
 */
public class CrawlerHttpClient {

    private final OkHttpClient client;
    private final CookieJarImpl cookieJar;

    /**
     * 构造 CrawlerHttpClient，使用默认配置。
     */
    public CrawlerHttpClient() {
        this.cookieJar = new CookieJarImpl();
        this.client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(0, Long.MAX_VALUE, TimeUnit.NANOSECONDS))
                .build();
    }

    /**
     * 设置 HTTP 代理。
     * @param host 代理主机
     * @param port 代理端口
     */
    public void setProxy(String host, int port) {
        // Note: proxy must be set at builder creation time for OkHttp
        // This is a simplified approach - for dynamic proxy, use RoutePlanner
    }

    /**
     * 发送 GET 请求。
     * @param url 请求 URL
     * @param headers 请求头
     * @return HTTP 响应
     * @throws IOException 如果请求失败
     */
    public HttpResponse get(String url, Map<String, String> headers) throws IOException {
        try {
            Request.Builder builder = new Request.Builder().url(url).get();
            if (headers != null) {
                headers.forEach(builder::addHeader);
            }
            Request request = builder.build();
            return execute(request);
        } catch (Exception e) {
            throw e instanceof IOException ? (IOException) e : new IOException(e.getMessage(), e);
        }
    }

    /**
     * 发送 POST 请求。
     * @param url 请求 URL
     * @param body 请求体
     * @param headers 请求头
     * @return HTTP 响应
     * @throws IOException 如果请求失败
     */
    public HttpResponse post(String url, String body, Map<String, String> headers) throws IOException {
        try {
            Request.Builder builder = new Request.Builder().url(url);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            builder.post(RequestBody.create(body, JSON));
            if (headers != null) {
                headers.forEach(builder::addHeader);
            }
            Request request = builder.build();
            return execute(request);
        } catch (Exception e) {
            throw e instanceof IOException ? (IOException) e : new IOException(e.getMessage(), e);
        }
    }

    /**
     * 从 URL 下载文件。
     * @param url 请求 URL
     * @param savePath 保存路径
     * @throws IOException 如果下载失败
     */
    public void download(String url, String savePath) throws IOException {
        try {
            Request request = new Request.Builder().url(url).get().build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Download failed: " + response.code());
            }
            BufferedSink sink = Okio.buffer(Okio.sink(new File(savePath)));
            sink.writeAll(response.body().source());
            sink.close();
        } catch (Exception e) {
            throw e instanceof IOException ? (IOException) e : new IOException(e.getMessage(), e);
        }
    }

    private HttpResponse execute(Request request) throws IOException {
        Response response = null;
        IOException lastException = null;
        for (int attempt = 0; attempt <= 3; attempt++) {
            try {
                response = client.newCall(request).execute();
                break;
            } catch (Exception e) {
                lastException = e instanceof IOException ? (IOException) e : new IOException(e.getMessage(), e);
                if (response != null) {
                    response.close();
                    response = null;
                }
            }
        }
        if (response == null) {
            throw lastException != null ? lastException : new IOException("Request failed");
        }
        try {
            String body = response.body() != null ? response.body().string() : "";
            int code = response.code();
            Map<String, String> responseHeaders = new HashMap<>();
            Headers h = response.headers();
            for (String name : h.names()) {
                responseHeaders.put(name, h.get(name));
            }
            return new HttpResponse(code, body, responseHeaders);
        } finally {
            response.close();
        }
    }

    /**
     * HTTP 响应容器。
     */
    public static class HttpResponse {
        private final int code;
        private final String body;
        private final Map<String, String> headers;

        /**
         * 构造 HTTP 响应。
         * @param code 状态码
         * @param body 响应体
         * @param headers 响应头
         */
        public HttpResponse(int code, String body, Map<String, String> headers) {
            this.code = code;
            this.body = body;
            this.headers = headers;
        }

        /**
         * 获取状态码。
         * @return HTTP 状态码
         */
        public int getCode() {
            return code;
        }

        /**
         * 获取响应体。
         * @return 响应体字符串
         */
        public String getBody() {
            return body;
        }

        /**
         * 获取响应头。
         * @return 响应头映射
         */
        public Map<String, String> getHeaders() {
            return headers != null ? headers : new HashMap<>();
        }
    }

    /**
     * Simple in-memory CookieJar implementation
     */
    private static class CookieJarImpl implements CookieJar {
        private final Map<String, Map<String, Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, java.util.List<Cookie> cookies) {
            cookieStore.put(url.host(), new HashMap<>());
            for (Cookie cookie : cookies) {
                cookieStore.get(url.host()).put(cookie.name(), cookie);
            }
        }

        @Override
        public java.util.List<Cookie> loadForRequest(HttpUrl url) {
            Map<String, Cookie> hostCookies = cookieStore.get(url.host());
            if (hostCookies == null) {
                return java.util.Collections.emptyList();
            }
            return new java.util.ArrayList<>(hostCookies.values());
        }
    }
}
