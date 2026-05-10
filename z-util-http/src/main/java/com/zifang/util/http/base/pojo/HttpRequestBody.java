package com.zifang.util.http.base.pojo;

/**
 * 请求体
 */
public class HttpRequestBody {

    private byte[] body;

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpRequestBody{body=" + (body != null ? "byte[" + body.length + "]" : "null") + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestBody that = (HttpRequestBody) o;
        if (body == null && that.body == null) return true;
        if (body == null || that.body == null) return false;
        if (body.length != that.body.length) return false;
        for (int i = 0; i < body.length; i++) {
            if (body[i] != that.body[i]) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        if (body != null) {
            for (byte b : body) {
                result = 31 * result + b;
            }
        }
        return result;
    }
}
