package com.zifang.util.validation.core;

/**
 * 校验异常
 */
public class ValidationException extends RuntimeException {

    private final ValidateResult result;

    public ValidationException(ValidateResult result) {
        super("Validation failed: " + result.getErrors());
        this.result = result;
    }

    public ValidateResult getResult() {
        return result;
    }
}