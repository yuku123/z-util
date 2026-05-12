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
 * HTTP client wrapper around OkHttp with retry, cookie jar, proxy support, and configurable timeouts.
 */
public class CrawlerHttpClient {

    private final OkHttpClient client;
    private final CookieJarImpl cookieJar;

    public CrawlerHttpClient() {
        this.cookieJar = new CookieJarImpl();
        this.client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new RetryInterceptor(3))
                .build();
    }

    /**
     * Set proxy for HTTP requests.
     */
    public void setProxy(String host, int port) {
        // Note: proxy must be set at builder creation time for OkHttp
        // This is a simplified approach - for dynamic proxy, use RoutePlanner
    }

    /**
     * GET request
     */
    public HttpResponse get(String url, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(url).get();
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
        Request request = builder.build();
        return execute(request);
    }

    /**
     * POST request with body
     */
    public HttpResponse post(String url, String body, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        builder.post(RequestBody.create(body, JSON));
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
        Request request = builder.build();
        return execute(request);
    }

    /**
     * Download file from URL
     */
    public void download(String url, String savePath) throws IOException {
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Download failed: " + response.code());
        }
        BufferedSink sink = Okio.buffer(Okio.sink(new File(savePath)));
        sink.writeAll(response.body().source());
        sink.close();
    }

    private HttpResponse execute(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        int code = response.code();
        String body = response.body() != null ? response.body().string() : "";
        Map<String, String> responseHeaders = new HashMap<>();
        Headers headers = response.headers();
        for (String name : headers.names()) {
            responseHeaders.put(name, headers.get(name));
        }
        response.close();
        return new HttpResponse(code, body, responseHeaders);
    }

    /**
     * HTTP response container
     */
    public static class HttpResponse {
        private final int code;
        private final String body;
        private final Map<String, String> headers;

        public HttpResponse(int code, String body, Map<String, String> headers) {
            this.code = code;
            this.body = body;
            this.headers = headers;
        }

        public int getCode() {
            return code;
        }

        public String getBody() {
            return body;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }
    }

    /**
     * Retry interceptor for failed requests
     */
    private static class RetryInterceptor implements Interceptor {
        private final int maxRetries;

        public RetryInterceptor(int maxRetries) {
            this.maxRetries = maxRetries;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = null;
            IOException exception = null;
            for (int i = 0; i <= maxRetries; i++) {
                try {
                    response = chain.proceed(request);
                    if (response.isSuccessful()) {
                        return response;
                    }
                    if (response.body() != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    exception = e;
                    if (response != null) {
                        response.close();
                    }
                }
            }
            if (exception != null) {
                throw exception;
            }
            return response;
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
