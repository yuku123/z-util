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

/**
 * OkHttp 工具类
 * <p>
 * 基于 OkHttp 库封装的HTTP客户端工具类，提供简洁的API用于发送各种HTTP请求。
 * 支持GET、POST请求，支持表单提交、JSON发送、文件上传、SSE等功能。
 * </p>
 *
 * @author zifang
 * @see HttpClientResult
 */
/**
 * OkHttpUtil类。
 */
public class OkHttpUtil {

    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 10;

    /**
     * MediaType.parse方法。
     *      * @param charset=utf-8" "application/json;类型参数
     * @return static final MediaType JSON =类型返回值
     */
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    /**
     * MediaType.parse方法。
     *      * @param "application/x-www-form-urlencoded" Object类型参数
     * @return static final MediaType FORM =类型返回值
     */
    public static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded");
    /**
     * MediaType.parse方法。
     *      * @param "application/octet-stream" Object类型参数
     * @return static final MediaType FILE =类型返回值
     */
    public static final MediaType FILE = MediaType.parse("application/octet-stream");

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
            .retryOnConnectionFailure(true)
            .build();

    // ==================== Basic Auth ====================

    /**
     * 生成 Basic Auth 认证字符串。
     *
     * @param user 用户名
     * @param pwd  密码
     * @return Base64 编码后的 Basic Auth 字符串，格式为 "Basic {credentials}"
     */
    /**
     * basicAuth方法。
     *      * @param user String类型参数
     * @param pwd String类型参数
     * @return static String类型返回值
     */
    public static String basicAuth(String user, String pwd) {
        String str = user + ":" + pwd;
        return "Basic " + java.util.Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    // ==================== GET ====================

    /**
     * 发送 GET 请求。
     *
     * @param url 请求 URL
     * @return HttpClientResult，包含响应状态码和响应体
     * @throws IOException 如果请求失败或网络异常
     */
    /**
     * get方法。
     *      * @param url String类型参数
     * @return static HttpClientResult类型返回值
     */
    public static HttpClientResult get(String url) throws IOException {
        return get(url, null, null);
    }

    /**
     * 发送 GET 请求（带查询参数）。
     *
     * @param url   请求 URL
     * @param params 查询参数 Map，会追加到 URL 后面
     * @return HttpClientResult，包含响应状态码和响应体
     * @throws IOException 如果请求失败或网络异常
     */
    /**
     * get方法。
     *      * @param url String类型参数
     * @param params MapString,类型参数
     * @return static HttpClientResult类型返回值
     */
    public static HttpClientResult get(String url, Map<String, String> params) throws IOException {
        return get(url, null, params);
    }

    /**
     * 发送 GET 请求（带头部和查询参数）。
     *
     * @param url     请求 URL
     * @param headers 请求头 Map，可为 null
     * @param params  查询参数 Map，会追加到 URL 后面，可为 null
     * @return HttpClientResult，包含响应状态码和响应体
     * @throws IOException 如果请求失败或网络异常
     */
    /**
     * get方法。
     *      * @param url String类型参数
     * @param headers MapString,类型参数
     * @param params MapString,类型参数
     * @return static HttpClientResult类型返回值
     */
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

    /**
     * 发送 POST JSON 请求。
     *
     * @param url  请求 URL
     * @param json JSON 格式的请求体
     * @return HttpClientResult，包含响应状态码和响应体
     * @throws IOException 如果请求失败或网络异常
     */
    /**
     * postJson方法。
     *      * @param url String类型参数
     * @param json String类型参数
     * @return static HttpClientResult类型返回值
     */
    public static HttpClientResult postJson(String url, String json) throws IOException {
        return postJson(url, null, json);
    }

    /**
     * 发送 POST JSON 请求（带请求头）。
     *
     * @param url     请求 URL
     * @param headers 请求头 Map，可为 null
     * @param json    JSON 格式的请求体
     * @return HttpClientResult，包含响应状态码和响应体
     * @throws IOException 如果请求失败或网络异常
     */
    /**
     * postJson方法。
     *      * @param url String类型参数
     * @param headers MapString,类型参数
     * @param json String类型参数
     * @return static HttpClientResult类型返回值
     */
    public static HttpClientResult postJson(String url, Map<String, String> headers, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder req = new Request.Builder().url(url).post(body);
        if (headers != null) headers.forEach(req::addHeader);
        try (Response res = CLIENT.newCall(req.build()).execute()) {
            return new HttpClientResult(res.code(), res.body() == null ? "" : res.body().string());
        }
    }

    // ==================== POST FORM ====================

    /**
     * 发送 POST 表单请求。
     *
     * @param url    请求 URL
     * @param params 表单参数 Map
     * @return HttpClientResult，包含响应状态码和响应体
     * @throws IOException 如果请求失败或网络异常
     */
    /**
     * postForm方法。
     *      * @param url String类型参数
     * @param params MapString,类型参数
     * @return static HttpClientResult类型返回值
     */
    public static HttpClientResult postForm(String url, Map<String, String> params) throws IOException {
        return postForm(url, null, params);
    }

    /**
     * 发送 POST 表单请求（带请求头）。
     *
     * @param url     请求 URL
     * @param headers 请求头 Map，可为 null
     * @param params  表单参数 Map
     * @return HttpClientResult，包含响应状态码和响应体
     * @throws IOException 如果请求失败或网络异常
     */
    /**
     * postForm方法。
     *      * @param url String类型参数
     * @param headers MapString,类型参数
     * @param params MapString,类型参数
     * @return static HttpClientResult类型返回值
     */
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

    /**
     * 发送异步请求。
     *
     * @param request  请求对象
     * @param callback 回调接口，用于处理响应或错误
     */
    /**
     * async方法。
     *      * @param request Request类型参数
     * @param callback Callback类型参数
     * @return static void类型返回值
     */
    public static void async(Request request, Callback callback) {
        CLIENT.newCall(request).enqueue(callback);
    }

    // ==================== 异步 POST JSON ====================

    /**
     * 发送异步 POST JSON 请求。
     *
     * @param url      请求 URL
     * @param json     JSON 格式的请求体
     * @param callback 回调接口，用于处理响应或错误
     */
    /**
     * asyncPostJson方法。
     *      * @param url String类型参数
     * @param json String类型参数
     * @param callback Callback类型参数
     * @return static void类型返回值
     */
    public static void asyncPostJson(String url, String json, Callback callback) {
        asyncPostJson(url, null, json, callback);
    }

    /**
     * 发送异步 POST JSON 请求（带请求头）。
     *
     * @param url      请求 URL
     * @param headers  请求头 Map，可为 null
     * @param json     JSON 格式的请求体
     * @param callback 回调接口，用于处理响应或错误
     */
    /**
     * asyncPostJson方法。
     *      * @param url String类型参数
     * @param headers MapString,类型参数
     * @param json String类型参数
     * @param callback Callback类型参数
     * @return static void类型返回值
     */
    public static void asyncPostJson(String url, Map<String, String> headers, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder req = new Request.Builder().url(url).post(body);
        if (headers != null) headers.forEach(req::addHeader);
        async(req.build(), callback);
    }

    // ==================== ✅ 正确 SSE（无 connect()）====================

    /**
     * 建立 Server-Sent Events（SSE）连接。
     *
     * @param url      SSE 服务端 URL
     * @param listener 事件监听器，用于处理 SSE 事件
     * @return EventSource 对象，可用于关闭连接
     */
    /**
     * sse方法。
     *      * @param url String类型参数
     * @param listener EventSourceListener类型参数
     * @return static EventSource类型返回值
     */
    public static EventSource sse(String url, EventSourceListener listener) {
        return sse(url, null, listener);
    }

    /**
     * 建立 Server-Sent Events（SSE）连接（带请求头）。
     *
     * @param url      SSE 服务端 URL
     * @param headers  请求头 Map，可为 null
     * @param listener 事件监听器，用于处理 SSE 事件
     * @return EventSource 对象，可用于关闭连接
     */
    /**
     * sse方法。
     *      * @param url String类型参数
     * @param headers MapString,类型参数
     * @param listener EventSourceListener类型参数
     * @return static EventSource类型返回值
     */
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

    /**
     * 上传文件（表单 multipart 方式）。
     *
     * @param url       请求 URL
     * @param fileField 表单中文件字段的名称
     * @param file      要上传的文件
     * @return HttpClientResult，包含响应状态码和响应体
     * @throws IOException 如果请求失败或网络异常
     */
    /**
     * upload方法。
     *      * @param url String类型参数
     * @param fileField String类型参数
     * @param file File类型参数
     * @return static HttpClientResult类型返回值
     */
    public static HttpClientResult upload(String url, String fileField, File file) throws IOException {
        return upload(url, null, fileField, file);
    }

    /**
     * 上传文件（表单 multipart 方式，带请求头）。
     *
     * @param url       请求 URL
     * @param headers   请求头 Map，可为 null
     * @param fileField 表单中文件字段的名称
     * @param file      要上传的文件
     * @return HttpClientResult，包含响应状态码和响应体
     * @throws IOException 如果请求失败或网络异常
     */
    /**
     * upload方法。
     *      * @param url String类型参数
     * @param headers MapString,类型参数
     * @param fileField String类型参数
     * @param file File类型参数
     * @return static HttpClientResult类型返回值
     */
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