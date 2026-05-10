package com.zifang.util.http.base.pojo;

/**
 * 所有注解获得到的信息都会在这个地方存储起来
 */
public class HttpRequestDefinition {

    /**
     * 请求行
     */
    private HttpRequestLine httpRequestLine;

    /**
     * 请求头
     */
    private HttpRequestHeader httpRequestHeader;

    /**
     * 请求体
     */
    private HttpRequestBody httpRequestBody;

    public HttpRequestLine getHttpRequestLine() {
        return httpRequestLine;
    }

    public void setHttpRequestLine(HttpRequestLine httpRequestLine) {
        this.httpRequestLine = httpRequestLine;
    }

    public HttpRequestHeader getHttpRequestHeader() {
        return httpRequestHeader;
    }

    public void setHttpRequestHeader(HttpRequestHeader httpRequestHeader) {
        this.httpRequestHeader = httpRequestHeader;
    }

    public HttpRequestBody getHttpRequestBody() {
        return httpRequestBody;
    }

    public void setHttpRequestBody(HttpRequestBody httpRequestBody) {
        this.httpRequestBody = httpRequestBody;
    }

    @Override
    public String toString() {
        return "HttpRequestDefinition{httpRequestLine=" + httpRequestLine + ", httpRequestHeader=" + httpRequestHeader + ", httpRequestBody=" + httpRequestBody + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestDefinition that = (HttpRequestDefinition) o;
        if (httpRequestLine != null ? !httpRequestLine.equals(that.httpRequestLine) : that.httpRequestLine != null)
            return false;
        if (httpRequestHeader != null ? !httpRequestHeader.equals(that.httpRequestHeader) : that.httpRequestHeader != null)
            return false;
        return httpRequestBody != null ? httpRequestBody.equals(that.httpRequestBody) : that.httpRequestBody == null;
    }

    @Override
    public int hashCode() {
        int result = httpRequestLine != null ? httpRequestLine.hashCode() : 0;
        result = 31 * result + (httpRequestHeader != null ? httpRequestHeader.hashCode() : 0);
        result = 31 * result + (httpRequestBody != null ? httpRequestBody.hashCode() : 0);
        return result;
    }
}
