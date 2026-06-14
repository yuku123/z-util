package com.zifang.util.jwt;

/** JWT 解析/验签异常。 */
public class JwtException extends RuntimeException {
    public JwtException(String message) { super(message); }
    public JwtException(String message, Throwable cause) { super(message, cause); }
}
