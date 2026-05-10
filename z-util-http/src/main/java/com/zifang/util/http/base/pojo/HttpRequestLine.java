package com.zifang.util.http.base.pojo;

import com.zifang.util.http.base.define.RequestMethod;

/**
 * 请求行
 */
public class HttpRequestLine {

    /**
     * 请求种类
     */
    private RequestMethod requestMethod;

    /**
     * 请求地址
     */
    private String url;

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "HttpRequestLine{requestMethod=" + requestMethod + ", url=" + url + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestLine that = (HttpRequestLine) o;
        if (requestMethod != that.requestMethod) return false;
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = requestMethod != null ? requestMethod.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
