package com.zifang.util.zex;

import java.io.Serializable;

/**
 * HTTP客户端响应结果封装类。
 * <p>
 * 此类封装了HTTP请求的响应结果，包括响应状态码和响应内容。
 * 用于统一处理各种HTTP请求的返回值。
 *
 * @author JourWon
 * @version 1.0
 * @since 1.0
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

    public HttpClientResult() {
    }

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
        return "HttpClientResult [code=" + code + ", content=" + content + "]";
    }

}
