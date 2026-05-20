package com.zifang.util.http.base.helper;

import com.zifang.util.http.base.define.RequestMethod;
import com.zifang.util.http.base.pojo.HttpRequestDefinition;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 根据 请求定义而产生请求
 */
public class HttpRequestProducer {

    private static CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    public Object produceRequest(HttpRequestDefinition httpRequestDefination) {
        String url = httpRequestDefination.getHttpRequestLine().getUrl();
        System.out.println("[DEBUG] HttpRequestProducer.produceRequest() URL = " + url + " method = " + httpRequestDefination.getHttpRequestLine().getRequestMethod());

        try {
            if (RequestMethod.GET == httpRequestDefination.getHttpRequestLine().getRequestMethod()) {
                return handleGetRequest(httpRequestDefination);
            } else if (RequestMethod.POST == httpRequestDefination.getHttpRequestLine().getRequestMethod()) {
                return handlePostRequest(httpRequestDefination);
            } else if (RequestMethod.PUT == httpRequestDefination.getHttpRequestLine().getRequestMethod()) {
                return handlePutRequest(httpRequestDefination);
            } else if (RequestMethod.DELETE == httpRequestDefination.getHttpRequestLine().getRequestMethod()) {
                return handleDeleteRequest(httpRequestDefination);
            }
        } catch (IOException e) {
            System.out.println("[DEBUG] HttpRequestProducer IOException: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private Object handlePostRequest(HttpRequestDefinition httpRequestDefinition) throws IOException {
        HttpPost httpPost = new HttpPost(httpRequestDefinition.getHttpRequestLine().getUrl());
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        httpPost.setEntity(new StringEntity(new String(httpRequestDefinition.getHttpRequestBody().getBody())));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        return EntityUtils.toString(response.getEntity());
    }

    private Object handleGetRequest(HttpRequestDefinition httpRequestDefination) throws IOException {
        HttpGet httpGet = new HttpGet(httpRequestDefination.getHttpRequestLine().getUrl());
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return EntityUtils.toString(response.getEntity());
    }

    private Object handlePutRequest(HttpRequestDefinition httpRequestDefinition) throws IOException {
        HttpPut httpPut = new HttpPut(httpRequestDefinition.getHttpRequestLine().getUrl());
        httpPut.setHeader("Content-Type", "application/json;charset=utf8");
        if (httpRequestDefinition.getHttpRequestBody() != null
                && httpRequestDefinition.getHttpRequestBody().getBody() != null) {
            httpPut.setEntity(new StringEntity(new String(httpRequestDefinition.getHttpRequestBody().getBody())));
        }
        CloseableHttpResponse response = httpClient.execute(httpPut);
        return EntityUtils.toString(response.getEntity());
    }

    private Object handleDeleteRequest(HttpRequestDefinition httpRequestDefinition) throws IOException {
        HttpDelete httpDelete = new HttpDelete(httpRequestDefinition.getHttpRequestLine().getUrl());
        CloseableHttpResponse response = httpClient.execute(httpDelete);
        return EntityUtils.toString(response.getEntity());
    }
}
