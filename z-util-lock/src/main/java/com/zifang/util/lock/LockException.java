package com.zifang.util.lock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/** 锁异常。 */
public class LockException extends RuntimeException {
    public LockException(String message) { super(message); }
    public LockException(String message, Throwable cause) { super(message, cause); }
}
