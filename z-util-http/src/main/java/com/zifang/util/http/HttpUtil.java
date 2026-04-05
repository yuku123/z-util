package com.zifang.util.http;

import com.zifang.util.http.client.HttpClientResult;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * HTTP 工具类（基于 Apache HttpClient4）
 * 支持 GET/POST/PUT/DELETE + 请求头 + 参数 + 超时 + BasicAuth
 *
 * @author zifang util
 */
public class HttpUtil {

    // 编码格式
    private static final String ENCODING = StandardCharsets.UTF_8.name();

    // 连接超时
    private static final int CONNECT_TIMEOUT = 6000;

    // 读取超时
    private static final int SOCKET_TIMEOUT = 6000;

    // 从连接池获取连接超时
    private static final int CONNECTION_REQUEST_TIMEOUT = 6000;

    /**
     * 生成 BasicAuth 请求头值
     */
    public static String toBasicAuthValue(String username, String password) {
        return "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    // ====================== GET ======================
    public static HttpClientResult doGet(String url) throws Exception {
        return doGet(url, null, null);
    }

    public static HttpClientResult doGet(String url, Map<String, String> params) throws Exception {
        return doGet(url, null, params);
    }

    public static HttpClientResult doGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                params.forEach(uriBuilder::setParameter);
            }

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setConfig(buildRequestConfig());
            packageHeader(headers, httpGet);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                return getResult(response);
            }
        }
    }

    // ====================== POST ======================
    public static HttpClientResult doPost(String url) throws Exception {
        return doPost(url, null, null);
    }

    public static HttpClientResult doPost(String url, Map<String, String> params) throws Exception {
        return doPost(url, null, params);
    }

    public static HttpClientResult doPost(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(buildRequestConfig());
            packageHeader(headers, httpPost);
            packageParam(params, httpPost);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return getResult(response);
            }
        }
    }

    // ====================== PUT ======================
    public static HttpClientResult doPut(String url) throws Exception {
        return doPut(url, null, null);
    }

    public static HttpClientResult doPut(String url, Map<String, String> params) throws Exception {
        return doPut(url, null, params);
    }

    public static HttpClientResult doPut(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(url);
            httpPut.setConfig(buildRequestConfig());
            packageHeader(headers, httpPut);
            packageParam(params, httpPut);

            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                return getResult(response);
            }
        }
    }

    // ====================== DELETE ======================
    public static HttpClientResult doDelete(String url) throws Exception {
        return doDelete(url, null, null);
    }

    public static HttpClientResult doDelete(String url, Map<String, String> params) throws Exception {
        return doDelete(url, null, params);
    }

    public static HttpClientResult doDelete(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                params.forEach(uriBuilder::setParameter);
            }

            HttpDelete httpDelete = new HttpDelete(uriBuilder.build());
            httpDelete.setConfig(buildRequestConfig());
            packageHeader(headers, httpDelete);

            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
                return getResult(response);
            }
        }
    }

    /**
     * 封装请求头
     */
    private static void packageHeader(Map<String, String> headers, HttpRequestBase httpMethod) {
        if (headers == null || headers.isEmpty()) {
            return;
        }
        headers.forEach(httpMethod::setHeader);
    }

    /**
     * 封装 form 表单参数
     */
    private static void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        if (params == null || params.isEmpty()) {
            return;
        }

        List<NameValuePair> pairs = new ArrayList<>();
        params.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));
        httpMethod.setEntity(new UrlEncodedFormEntity(pairs, ENCODING));
    }

    /**
     * 统一获取响应结果
     */
    private static HttpClientResult getResult(CloseableHttpResponse response) throws Exception {
        if (response == null || response.getStatusLine() == null) {
            return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

        int code = response.getStatusLine().getStatusCode();
        String content = response.getEntity() == null ? "" : EntityUtils.toString(response.getEntity(), ENCODING);
        return new HttpClientResult(code, content);
    }

    /**
     * 统一构建超时配置
     */
    private static RequestConfig buildRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .build();
    }
}