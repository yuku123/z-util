package com.zifang.util.http;

import com.zifang.util.http.client.HttpClientResult;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 10;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded");
    public static final MediaType FILE = MediaType.parse("application/octet-stream");

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
            .retryOnConnectionFailure(true)
            .build();

    // ==================== Basic Auth ====================
    public static String basicAuth(String user, String pwd) {
        String str = user + ":" + pwd;
        return "Basic " + java.util.Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    // ==================== GET ====================
    public static HttpClientResult get(String url) throws IOException {
        return get(url, null, null);
    }

    public static HttpClientResult get(String url, Map<String, String> params) throws IOException {
        return get(url, null, params);
    }

    public static HttpClientResult get(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) params.forEach(urlBuilder::addQueryParameter);
        Request.Builder req = new Request.Builder().url(urlBuilder.build());
        if (headers != null) headers.forEach(req::addHeader);
        try (Response res = CLIENT.newCall(req.build()).execute()) {
            return new HttpClientResult(res.code(), res.body() == null ? "" : res.body().string());
        }
    }

    // ==================== POST JSON ====================
    public static HttpClientResult postJson(String url, String json) throws IOException {
        return postJson(url, null, json);
    }

    public static HttpClientResult postJson(String url, Map<String, String> headers, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder req = new Request.Builder().url(url).post(body);
        if (headers != null) headers.forEach(req::addHeader);
        try (Response res = CLIENT.newCall(req.build()).execute()) {
            return new HttpClientResult(res.code(), res.body() == null ? "" : res.body().string());
        }
    }

    // ==================== POST FORM ====================
    public static HttpClientResult postForm(String url, Map<String, String> params) throws IOException {
        return postForm(url, null, params);
    }

    public static HttpClientResult postForm(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        FormBody.Builder form = new FormBody.Builder(StandardCharsets.UTF_8);
        if (params != null) params.forEach(form::add);
        Request.Builder req = new Request.Builder().url(url).post(form.build());
        if (headers != null) headers.forEach(req::addHeader);
        try (Response res = CLIENT.newCall(req.build()).execute()) {
            return new HttpClientResult(res.code(), res.body() == null ? "" : res.body().string());
        }
    }

    // ==================== 异步请求 ====================
    public static void async(Request request, Callback callback) {
        CLIENT.newCall(request).enqueue(callback);
    }

    // ==================== 异步 POST JSON ====================
    public static void asyncPostJson(String url, String json, Callback callback) {
        asyncPostJson(url, null, json, callback);
    }

    public static void asyncPostJson(String url, Map<String, String> headers, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder req = new Request.Builder().url(url).post(body);
        if (headers != null) headers.forEach(req::addHeader);
        async(req.build(), callback);
    }

    // ==================== ✅ 正确 SSE（无 connect()）====================
    public static EventSource sse(String url, EventSourceListener listener) {
        return sse(url, null, listener);
    }

    public static EventSource sse(String url, Map<String, String> headers, EventSourceListener listener) {
        Request.Builder req = new Request.Builder().url(url)
                .addHeader("Accept", "text/event-stream")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Connection", "keep-alive");

        if (headers != null) {
            headers.forEach(req::addHeader);
        }

        // 官方正确写法 → 自动连接，不需要 .connect()
        EventSource.Factory factory = EventSources.createFactory(CLIENT);
        return factory.newEventSource(req.build(), listener);
    }

    // ==================== 文件上传 ====================
    public static HttpClientResult upload(String url, String fileField, File file) throws IOException {
        return upload(url, null, fileField, file);
    }

    public static HttpClientResult upload(String url, Map<String, String> headers, String fileField, File file) throws IOException {
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileField, file.getName(), RequestBody.create(file, FILE))
                .build();
        Request.Builder req = new Request.Builder().url(url).post(body);
        if (headers != null) headers.forEach(req::addHeader);
        try (Response res = CLIENT.newCall(req.build()).execute()) {
            return new HttpClientResult(res.code(), res.body() == null ? "" : res.body().string());
        }
    }
}