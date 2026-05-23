package com.zifang.util.parser.toml;

/**
 * TOML 解析异常
 */
public class TomlException extends RuntimeException {

    public TomlException(String message) {
        super(message);
    }

    public TomlException(String message, Throwable cause) {
        super(message, cause);
    }
}
