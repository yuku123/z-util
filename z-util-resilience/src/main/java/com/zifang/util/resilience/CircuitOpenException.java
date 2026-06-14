package com.zifang.util.resilience;

/** 熔断器开启。 */
public class CircuitOpenException extends ResilienceException {
    public CircuitOpenException(String message) { super(message); }
}
