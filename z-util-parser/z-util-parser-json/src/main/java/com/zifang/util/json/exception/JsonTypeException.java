package com.zifang.util.json.exception;

/**
 * JSON类型异常，当JSON数据类型不匹配时抛出。
 *
 * @author zifang
 */
public class JsonTypeException extends RuntimeException {

    /**
     * 构造一个JSON类型异常。
     *
     * @param message 异常消息
     */
    public JsonTypeException(String message) {
        super(message);
    }
}
