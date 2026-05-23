package com.zifang.util.parser.ini;

/**
 * INI 文件解析异常
 */
public class IniException extends RuntimeException {

    public IniException(String message) {
        super(message);
    }

    public IniException(String message, Throwable cause) {
        super(message, cause);
    }
}
