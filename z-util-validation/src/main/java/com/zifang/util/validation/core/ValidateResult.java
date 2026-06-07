package com.zifang.util.validation.core;

import java.util.*;

/**
 * 校验结果收集器
 */
/**
 * ValidateResult类。
 */
/**
 * ValidateResult类。
 */
public class ValidateResult {

    private final Map<String, List<String>> errors = new HashMap<>();

    /**
     * 添加错误信息
     */
    /**
     * addError方法。
     *      * @param fieldName String类型参数
     * @param message String类型参数
     */
    /**
     * addError方法。
     *      * @param fieldName String类型参数
     * @param message String类型参数
     */
    public void addError(String fieldName, String message) {
        errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(message);
    }

    /**
     * 判断是否有错误
     */
    /**
     * hasErrors方法。
     * @return boolean类型返回值
     */
    /**
     * hasErrors方法。
     * @return boolean类型返回值
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * 获取所有错误信息
     */
    /**
     * getErrors方法。
     * @return Map<String, List<String>>类型返回值
     */
    /**
     * getErrors方法。
     * @return Map<String, List<String>>类型返回值
     */
    public Map<String, List<String>> getErrors() {
        return Collections.unmodifiableMap(errors);
    }

    /**
     * 获取第一个错误信息
     */
    /**
     * getFirstError方法。
     * @return Optional<String>类型返回值
     */
    /**
     * getFirstError方法。
     * @return Optional<String>类型返回值
     */
    public Optional<String> getFirstError() {
        if (errors.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(errors.values().iterator().next().get(0));
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        if (errors.isEmpty()) {
            return "ValidateResult{valid=true}";
        }
        StringBuilder sb = new StringBuilder("ValidateResult{valid=false, errors=");
        sb.append(errors);
        sb.append("}");
        return sb.toString();
    }
}