package com.zifang.util.http;

import com.zifang.util.http.client.HttpClientResult;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp3 封装 HTTP 工具类
 * 与原 Apache HttpUtil 方法名、参数完全一致，可无缝替换
 */
public class OkHttpUtil {

    // 编码格式
    private static final String ENCODING = StandardCharsets.UTF_8.name();

    // 超时时间
    private static final int CONNECT_TIMEOUT = 6;
    private static final int READ_TIMEOUT = 6;
    private static final int WRITE_TIMEOUT = 6;

    // 单例 OkHttpClient（OkHttp 官方强制推荐）
    private static final OkHttpClient OK_HTTP_CLIENT;

    static {
        OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                // 连接池复用，避免频繁创建关闭
                .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                .build();
    }

    /**
     * 生成 BasicAuth 请求头
     */
    public static String toBasicAuthValue(String username, String password) {
        return "Basic " + java.util.Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    // ====================== GET ======================
    public static HttpClientResult doGet(String url) throws IOException {
        return doGet(url, null, null);
    }

    public static HttpClientResult doGet(String url, Map<String, String> params) throws IOException {
        return doGet(url, null, params);
    }

    public static HttpClientResult doGet(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            params.forEach(urlBuilder::addQueryParameter);
        }

        Request.Builder requestBuilder = new Request.Builder().url(urlBuilder.build());
        setHeaders(requestBuilder, headers);

        Request request = requestBuilder.get().build();
        return execute(request);
    }

    // ====================== POST ======================
    public static HttpClientResult doPost(String url) throws IOException {
        return doPost(url, null, null);
    }

    public static HttpClientResult doPost(String url, Map<String, String> params) throws IOException {
        return doPost(url, null, params);
    }

    public static HttpClientResult doPost(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder(StandardCharsets.UTF_8);
        if (params != null) {
            params.forEach(formBuilder::add);
        }

        Request.Builder requestBuilder = new Request.Builder().url(url).post(formBuilder.build());
        setHeaders(requestBuilder, headers);

        return execute(requestBuilder.build());
    }

    // ====================== PUT ======================
    public static HttpClientResult doPut(String url) throws IOException {
        return doPut(url, null, null);
    }

    public static HttpClientResult doPut(String url, Map<String, String> params) throws IOException {
        return doPut(url, null, params);
    }

    public static HttpClientResult doPut(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder(StandardCharsets.UTF_8);
        if (params != null) {
            params.forEach(formBuilder::add);
        }

        Request.Builder requestBuilder = new Request.Builder().url(url).put(formBuilder.build());
        setHeaders(requestBuilder, headers);

        return execute(requestBuilder.build());
    }

    // ====================== DELETE ======================
    public static HttpClientResult doDelete(String url) throws IOException {
        return doDelete(url, null, null);
    }

    public static HttpClientResult doDelete(String url, Map<String, String> params) throws IOException {
        return doDelete(url, null, params);
    }

    public static HttpClientResult doDelete(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            params.forEach(urlBuilder::addQueryParameter);
        }

        Request.Builder requestBuilder = new Request.Builder().url(urlBuilder.build()).delete();
        setHeaders(requestBuilder, headers);

        return execute(requestBuilder.build());
    }

    // ====================== 工具方法 ======================

    /**
     * 设置请求头
     */
    private static void setHeaders(Request.Builder builder, Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return;
        }
        headers.forEach(builder::addHeader);
    }

    /**
     * 统一执行请求
     */
    private static HttpClientResult execute(Request request) throws IOException {
        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()) {
            int code = response.code();
            String body = response.body() == null ? "" : response.body().string();
            return new HttpClientResult(code, body);
        }
    }
}