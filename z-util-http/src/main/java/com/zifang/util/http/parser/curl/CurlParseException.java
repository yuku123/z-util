package com.zifang.util.http.parser.curl;

/**
 * cURL 解析异常
 */
public class CurlParseException extends RuntimeException {

    public CurlParseException(String message) {
        super(message);
    }

    public CurlParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
