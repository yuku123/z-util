package com.zifang.util.http.base.pojo;

import com.zifang.util.http.base.define.RequestMethod;

/**
 * HTTP请求行
 * <p>
 * 用于存储HTTP请求的请求行信息，包括请求方法和URL。
 * </p>
 *
 * @author zifang
 * @see RequestMethod
 */
/**
 * HttpRequestLine类。
 */
public class HttpRequestLine {

    /**
     * HTTP请求方法。
     */
    private RequestMethod requestMethod;

    /**
     * 请求URL地址。
     */
    private String url;

    /**
     * 获取请求方法。
     *
     * @return HTTP请求方法
     */
    /**
     * getRequestMethod方法。
     * @return RequestMethod类型返回值
     */
    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    /**
     * 设置请求方法。
     *
     * @param requestMethod HTTP请求方法
     */
    /**
     * setRequestMethod方法。
     *      * @param requestMethod RequestMethod类型参数
     */
    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    /**
     * 获取请求URL。
     *
     * @return 请求URL
     */
    /**
     * getUrl方法。
     * @return String类型返回值
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置请求URL。
     *
     * @param url 请求URL
     */
    /**
     * setUrl方法。
     *      * @param url String类型参数
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "HttpRequestLine{requestMethod=" + requestMethod + ", url=" + url + "}";
    }

    @Override
    /**
     * equals方法。
     *      * @param o Object类型参数
     * @return boolean类型返回值
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestLine that = (HttpRequestLine) o;
        if (requestMethod != that.requestMethod) return false;
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    /**
     * hashCode方法。
     * @return int类型返回值
     */
    public int hashCode() {
        int result = requestMethod != null ? requestMethod.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
