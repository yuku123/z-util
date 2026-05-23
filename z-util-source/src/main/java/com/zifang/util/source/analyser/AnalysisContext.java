package com.zifang.util.source.analyser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 分析上下文，存储源码/字节码解析过程中的中间结果
 */
public class AnalysisContext {

    private static final Logger log = LoggerFactory.getLogger(AnalysisContext.class);

    private final Map<String, Object> attributes = new HashMap<>();

    public AnalysisContext() {
    }

    /**
     * 设置属性
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * 获取属性
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * 获取属性（带默认值）
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key, T defaultValue) {
        Object value = attributes.get(key);
        return value != null ? (T) value : defaultValue;
    }

    /**
     * 清除所有属性
     */
    public void clear() {
        attributes.clear();
    }

    @Override
    public String toString() {
        return "AnalysisContext{attributes=" + attributes + "}";
    }
}
