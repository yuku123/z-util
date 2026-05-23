package com.zifang.util.parser.proto;

/**
 * Proto 解析异常，当 .proto 文件格式非法时抛出。
 *
 * @author zifang
 */
public class ProtoException extends RuntimeException {

    public ProtoException(String message) {
        super(message);
    }

    public ProtoException(String message, Throwable cause) {
        super(message, cause);
    }
}
