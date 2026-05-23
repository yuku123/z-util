package com.zifang.util.db.sequence;

/**
 * 序列生成异常
 */
public class SequenceException extends RuntimeException {
    public SequenceException(String message) {
        super(message);
    }

    public SequenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
