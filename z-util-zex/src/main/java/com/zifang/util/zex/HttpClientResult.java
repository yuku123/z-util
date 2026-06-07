package com.zifang.util.zex;

import java.io.Serializable;

/**
 * HTTP客户端响应结果封装类。
 * <p>
 * 此类封装了HTTP请求的响应结果，包括响应状态码和响应内容。
 * 用于统一处理各种HTTP请求的返回值。
 *
 * @author zifang
 * @version 1.0
 */
/**
 * HttpClientResult类。
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

    /**
     * HttpClientResult方法。
     */
    public HttpClientResult() {
    }

    /**
     * HttpClientResult方法。
     *      * @param code int类型参数
     */
    public HttpClientResult(int code) {
        this.code = code;
    }

    /**
     * HttpClientResult方法。
     *      * @param content String类型参数
     */
    public HttpClientResult(String content) {
        this.content = content;
    }

    /**
     * HttpClientResult方法。
     *      * @param code int类型参数
     * @param content String类型参数
     */
    public HttpClientResult(int code, String content) {
        this.code = code;
        this.content = content;
    }

    /**
     * getCode方法。
     * @return int类型返回值
     */
    public int getCode() {
        return code;
    }

    /**
     * setCode方法。
     *      * @param code int类型参数
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * getContent方法。
     * @return String类型返回值
     */
    public String getContent() {
        return content;
    }

    /**
     * setContent方法。
     *      * @param content String类型参数
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "HttpClientResult [code=" + code + ", content=" + content + "]";
    }

}
