package com.zifang.util.http.client;

import java.io.Serializable;

/**
 * HTTP客户端响应结果封装类。
 * <p>
 * 该类封装了HTTP响应的状态码和响应内容，用于统一处理HTTP请求的返回结果。
 * </p>
 *
 * @author zifang
 * @see Serializable
 */
/**
 * HttpClientResult类。
 */
/**
 * HttpClientResult类。
 */
public class HttpClientResult implements Serializable {

    private static final long serialVersionUID = 2168152194164783950L;

    /**
     * 响应状态码，如200、404、500等HTTP状态码。
     */
    private int code;

    /**
     * 响应数据内容，通常为JSON字符串格式。
     */
    private String content;

    /**
     * 构造一个空的HTTP客户端结果。
     */
    /**
     * HttpClientResult方法。
     */
    /**
     * HttpClientResult方法。
     */
    public HttpClientResult() {}

    /**
     * 构造一个仅包含状态码的HTTP客户端结果。
     *
     * @param code HTTP响应状态码
     */
    /**
     * HttpClientResult方法。
     *      * @param code int类型参数
     */
    /**
     * HttpClientResult方法。
     *      * @param code int类型参数
     */
    public HttpClientResult(int code) {
        this.code = code;
    }

    /**
     * 构造一个仅包含响应内容的HTTP客户端结果。
     *
     * @param content HTTP响应内容
     */
    /**
     * HttpClientResult方法。
     *      * @param content String类型参数
     */
    /**
     * HttpClientResult方法。
     *      * @param content String类型参数
     */
    public HttpClientResult(String content) {
        this.content = content;
    }

    /**
     * 构造一个包含状态码和响应内容的HTTP客户端结果。
     *
     * @param code    HTTP响应状态码
     * @param content HTTP响应内容
     */
    /**
     * HttpClientResult方法。
     *      * @param code int类型参数
     * @param content String类型参数
     */
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
     * 获取HTTP响应状态码。
     *
     * @return HTTP响应状态码
     */
    /**
     * getCode方法。
     * @return int类型返回值
     */
    /**
     * getCode方法。
     * @return int类型返回值
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置HTTP响应状态码。
     *
     * @param code HTTP响应状态码
     */
    /**
     * setCode方法。
     *      * @param code int类型参数
     */
    /**
     * setCode方法。
     *      * @param code int类型参数
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取HTTP响应内容。
     *
     * @return HTTP响应内容字符串
     */
    /**
     * getContent方法。
     * @return String类型返回值
     */
    /**
     * getContent方法。
     * @return String类型返回值
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置HTTP响应内容。
     *
     * @param content HTTP响应内容字符串
     */
    /**
     * setContent方法。
     *      * @param content String类型参数
     */
    /**
     * setContent方法。
     *      * @param content String类型参数
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 返回该结果的字符串表示。
     *
     * @return 包含 code 和 content 的字符串表示
     */
    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "HttpClientResult{code=" + code + ", content=" + content + "}";
    }

    /**
     * 比较两个HTTP客户端结果是否相等。
     * <p>
     * 两个结果相等当且仅当它们的 code 和 content 都相等。
     * </p>
     *
     * @param o 待比较的对象
     * @return 如果相等则返回 true，否则返回 false
     */
    @Override
    /**
     * equals方法。
     *      * @param o Object类型参数
     * @return boolean类型返回值
     */
    /**
     * equals方法。
     *      * @param o Object类型参数
     * @return boolean类型返回值
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpClientResult that = (HttpClientResult) o;
        if (code != that.code) return false;
        return content != null ? content.equals(that.content) : that.content == null;
    }

    /**
     * 返回该结果的哈希码。
     *
     * @return 哈希码值
     */
    @Override
    /**
     * hashCode方法。
     * @return int类型返回值
     */
    /**
     * hashCode方法。
     * @return int类型返回值
     */
    public int hashCode() {
        int result = code;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
