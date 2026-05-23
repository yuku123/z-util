package com.zifang.util.json.model;

import com.zifang.util.json.exception.JsonTypeException;

import java.util.*;

/**
 * JSON数组模型，表示一个JSON数组（有序元素列表）。
 * <p>
 * 内部使用ArrayList存储元素，支持链式调用和类型安全的取值操作。
 * <p>
 * 兼容 Gson's JsonArray API：
 * <ul>
 *   <li>{@link #get(int)} - 获取指定索引的元素</li>
 *   <li>{@link #getAsJsonObject(int)} - 获取指定索引的 JsonObject</li>
 *   <li>{@link #getAsJsonArray(int)} - 获取指定索引的 JsonArray</li>
 *   <li>{@link #getAsString(int)} - 获取指定索引的字符串值</li>
 *   <li>{@link #getAsInt(int)} - 获取指定索引的整数值</li>
 *   <li>{@link #getAsLong(int)} - 获取指定索引的长整型值</li>
 *   <li>{@link #getAsDouble(int)} - 获取指定索引的双精度值</li>
 *   <li>{@link #getAsBoolean(int)} - 获取指定索引的布尔值</li>
 *   <li>{@link #size()} - 元素数量</li>
 *   <li>{@link #isEmpty()} - 是否为空</li>
 *   <li>{@link #iterator()} - 迭代器</li>
 *   <li>{@link #add(Object)} - 添加元素</li>
 * </ul>
 *
 * @author zifang
 * @see JsonObject
 */
public class JsonArray implements Iterable<Object> {

    private final List<Object> list = new ArrayList<>();

    /**
     * 添加一个元素到数组末尾。
     *
     * @param value 元素值
     * @return this，支持链式调用
     */
    public JsonArray add(Object value) {
        list.add(value);
        return this;
    }

    /**
     * 获取指定索引的元素。
     *
     * @param index 索引
     * @return 元素值
     * @throws IndexOutOfBoundsException 如果索引越界
     */
    public Object get(int index) {
        return list.get(index);
    }

    /**
     * 获取指定索引的元素（安全版本，索引越界返回 null）。
     *
     * @param index 索引
     * @return 元素值或 null
     */
    public Object opt(int index) {
        if (index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    /**
     * 获取指定索引的 JsonObject 元素。
     *
     * @param index 索引
     * @return JsonObject
     * @throws IndexOutOfBoundsException 如果索引越界
     * @throws JsonTypeException        如果元素不是 JsonObject 类型
     */
    public JsonObject getAsJsonObject(int index) {
        Object obj = list.get(index);
        if (obj == null) {
            return null;
        }
        if (obj instanceof JsonObject) {
            return (JsonObject) obj;
        }
        throw new JsonTypeException("Element at index " + index + " is not a JsonObject: " + obj.getClass().getSimpleName());
    }

    /**
     * 获取指定索引的 JsonArray 元素。
     *
     * @param index 索引
     * @return JsonArray
     * @throws IndexOutOfBoundsException 如果索引越界
     * @throws JsonTypeException       如果元素不是 JsonArray 类型
     */
    public JsonArray getAsJsonArray(int index) {
        Object obj = list.get(index);
        if (obj == null) {
            return null;
        }
        if (obj instanceof JsonArray) {
            return (JsonArray) obj;
        }
        throw new JsonTypeException("Element at index " + index + " is not a JsonArray: " + obj.getClass().getSimpleName());
    }

    /**
     * getJsonObject 的别名，兼容旧代码。
     */
    public JsonObject getJsonObject(int index) {
        return getAsJsonObject(index);
    }

    /**
     * getJsonArray 的别名，兼容旧代码。
     */
    public JsonArray getJsonArray(int index) {
        return getAsJsonArray(index);
    }

    /**
     * 获取指定索引的字符串值。
     */
    public String getAsString(int index) {
        Object value = list.get(index);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return String.valueOf(value);
    }

    /**
     * 获取指定索引的整数值。
     */
    public int getAsInt(int index) {
        Object value = list.get(index);
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 获取指定索引的长整型值。
     */
    public long getAsLong(int index) {
        Object value = list.get(index);
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString().trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    /**
     * 获取指定索引的双精度值。
     */
    public double getAsDouble(int index) {
        Object value = list.get(index);
        if (value == null) {
            return 0.0;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString().trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * 获取指定索引的布尔值。
     */
    public boolean getAsBoolean(int index) {
        Object value = list.get(index);
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        String s = value.toString().trim().toLowerCase();
        return "true".equals(s) || "1".equals(s);
    }

    /**
     * 获取数组元素数量。
     */
    public int size() {
        return list.size();
    }

    /**
     * 检查数组是否为空。
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * 返回迭代器。
     */
    @Override
    public Iterator<Object> iterator() {
        return list.iterator();
    }

    @Override
    public String toString() {
        return toJsonString();
    }

    /**
     * 返回 JSON 字符串。
     */
    public String toJsonString() {
        if (list.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object item : list) {
            if (!first) sb.append(",");
            first = false;
            sb.append(serializeValue(item));
        }
        sb.append("]");
        return sb.toString();
    }

    private String serializeValue(Object value) {
        if (value == null) return "null";
        if (value instanceof String) return escapeString((String) value);
        if (value instanceof Number) return value.toString();
        if (value instanceof Boolean) return value.toString();
        if (value instanceof JsonObject) return ((JsonObject) value).toJsonString();
        if (value instanceof JsonArray) return ((JsonArray) value).toJsonString();
        return escapeString(value.toString());
    }

    private String escapeString(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < ' ') sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }
        sb.append("\"");
        return sb.toString();
    }
}
