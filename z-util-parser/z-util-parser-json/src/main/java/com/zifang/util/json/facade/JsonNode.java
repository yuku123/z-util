package com.zifang.util.json.facade;

/**
 * JSON DOM 节点抽象。
 * 对应 JSON object 或 array 的内存结构。
 */
public interface JsonNode {

    // ==================== 取值 ====================

    String getString(String path);

    Integer getInt(String path);

    Long getLong(String path);

    Double getDouble(String path);

    Boolean getBoolean(String path);

    JsonNode getNode(String path);

    // ==================== 判断 ====================

    boolean isObject();

    boolean isArray();

    boolean isNull();

    // ==================== 大小 ====================

    int size();

    // ==================== 转换 ====================

    String toJsonString();

    Object toJavaObject();
}