package com.zifang.util.http.client;

import java.io.Serializable;

/**
 * Description: 封装httpClient响应结果
 */
public class HttpClientResult implements Serializable {

    private static final long serialVersionUID = 2168152194164783950L;

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private String content;

    public HttpClientResult() {}

    public HttpClientResult(int code) {
        this.code = code;
    }

    public HttpClientResult(String content) {
        this.content = content;
    }

    public HttpClientResult(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "HttpClientResult{code=" + code + ", content=" + content + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpClientResult that = (HttpClientResult) o;
        if (code != that.code) return false;
        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
