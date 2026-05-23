package com.zifang.util.parser.properties;

/**
 * Properties 解析异常，当 Properties 字符串格式非法时抛出。
 *
 * @author zifang
 */
public class PropertiesException extends RuntimeException {

    public PropertiesException(String message) {
        super(message);
    }

    public PropertiesException(String message, Throwable cause) {
        super(message, cause);
    }
}
