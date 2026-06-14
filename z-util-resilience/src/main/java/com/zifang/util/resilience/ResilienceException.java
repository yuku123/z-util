package com.zifang.util.resilience;

/** 弹性能力基类异常。 */
public class ResilienceException extends RuntimeException {
    public ResilienceException(String message) { super(message); }
    public ResilienceException(String message, Throwable cause) { super(message, cause); }
}

/** 重试耗尽。 */
class RetryExhaustedException extends ResilienceException {
    public RetryExhaustedException(String message) { super(message); }
    public RetryExhaustedException(String message, Throwable cause) { super(message, cause); }
}

/** 熔断器开启。 */
class CircuitOpenException extends ResilienceException {
    public CircuitOpenException(String message) { super(message); }
}
