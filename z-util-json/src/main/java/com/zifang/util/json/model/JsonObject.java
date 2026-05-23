package com.zifang.util.json.model;

import com.zifang.util.json.BeautifyJsonUtils;
import com.zifang.util.json.exception.JsonParseException;
import com.zifang.util.json.exception.JsonTypeException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * JSON对象模型，表示一个JSON对象（键值对集合）。
 * <p>
 * 内部使用HashMap存储键值对，支持链式调用和类型安全的取值操作。
 * <p>
 * 兼容 Gson's JsonObject API：
 * <ul>
 *   <li>{@link #has(String)} - 检查是否包含指定键</li>
 *   <li>{@link #get(String)} - 获取值（Object）</li>
 *   <li>{@link #getAsJsonObject(String)} - 获取嵌套对象</li>
 *   <li>{@link #getAsJsonArray(String)} - 获取数组</li>
 *   <li>{@link #getAsString(String)} - 获取字符串值</li>
 *   <li>{@link #getAsInt(String)} - 获取整数值</li>
 *   <li>{@link #getAsLong(String)} - 获取长整型值</li>
 *   <li>{@link #getAsDouble(String)} - 获取双精度值</li>
 *   <li>{@link #getAsBoolean(String)} - 获取布尔值</li>
 *   <li>{@link #size()} - 键值对数量</li>
 *   <li>{@link #isEmpty()} - 是否为空</li>
 *   <li>{@link #keySet()} - 所有键集合</li>
 *   <li>{@link #entrySet()} - 所有键值对</li>
 * </ul>
 *
 * @author zifang
 * @see JsonArray
 */
public class JsonObject {

    private final Map<String, Object> map = new LinkedHashMap<>();

    /**
     * 向JSON对象中添加一个键值对。
     *
     * @param key   键名
     * @param value 值，可以是基本类型、String、JsonObject、JsonArray或null
     * @return this，支持链式调用
     */
    public JsonObject put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    /**
     * 根据键名获取值。
     *
     * @param key 键名
     * @return 对应的值，如果键不存在则返回null
     */
    public Object get(String key) {
        return map.get(key);
    }

    /**
     * 检查是否包含指定键。
     *
     * @param key 键名
     * @return 是否存在
     */
    public boolean has(String key) {
        return map.containsKey(key);
    }

    /**
     * 获取所有键的集合。
     *
     * @return 键集合
     */
    public Set<String> keySet() {
        return map.keySet();
    }

    /**
     * 获取所有键值对。
     *
     * @return 键值对集合
     */
    public Set<Map.Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    /**
     * 获取所有键值对的列表。
     *
     * @return 键值对列表
     */
    public List<Map.Entry<String, Object>> getAllKeyValue() {
        return new ArrayList<>(map.entrySet());
    }

    /**
     * 获取指定键对应的JsonObject值。
     *
     * @param key 键名
     * @return JsonObject值
     * @throws IllegalArgumentException 如果键不存在
     * @throws JsonTypeException        如果值不是JsonObject类型
     */
    public JsonObject getAsJsonObject(String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        Object obj = map.get(key);
        if (obj == null) {
            return null;
        }
        if (obj instanceof JsonObject) {
            return (JsonObject) obj;
        }
        if (obj instanceof Map) {
            JsonObject jo = new JsonObject();
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) obj).entrySet()) {
                jo.put(entry.getKey(), entry.getValue());
            }
            return jo;
        }
        throw new JsonTypeException("Value at key '" + key + "' is not a JsonObject: " + obj.getClass().getSimpleName());
    }

    /**
     * 获取指定键对应的JsonArray值。
     *
     * @param key 键名
     * @return JsonArray值
     * @throws IllegalArgumentException 如果键不存在
     * @throws JsonTypeException      如果值不是JsonArray类型
     */
    public JsonArray getAsJsonArray(String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        Object obj = map.get(key);
        if (obj == null) {
            return null;
        }
        if (obj instanceof JsonArray) {
            return (JsonArray) obj;
        }
        if (obj instanceof List) {
            JsonArray ja = new JsonArray();
            for (Object item : (List<?>) obj) {
                ja.add(item);
            }
            return ja;
        }
        throw new JsonTypeException("Value at key '" + key + "' is not a JsonArray: " + obj.getClass().getSimpleName());
    }

    /**
     * getJsonObject 的别名，兼容旧代码。
     */
    public JsonObject getJsonObject(String key) {
        return getAsJsonObject(key);
    }

    /**
     * getJsonArray 的别名，兼容旧代码。
     */
    public JsonArray getJsonArray(String key) {
        return getAsJsonArray(key);
    }

    /**
     * getAsString 的别名，兼容旧代码。
     */
    public String getString(String key) {
        return getAsString(key);
    }

    /**
     * 获取指定键对应的字符串值。
     *
     * @param key 键名
     * @return 字符串值，如果不存在或值为null返回null
     */
    public String getAsString(String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return String.valueOf(value);
    }

    /**
     * 获取指定键对应的整数值。
     *
     * @param key 键名
     * @return 整数值，如果不存在或类型不兼容返回0
     */
    public int getAsInt(String key) {
        Object value = map.get(key);
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
     * 获取指定键对应的长整型值。
     *
     * @param key 键名
     * @return 长整型值，如果不存在或类型不兼容返回0L
     */
    public long getAsLong(String key) {
        Object value = map.get(key);
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
     * 获取指定键对应的双精度值。
     *
     * @param key 键名
     * @return 双精度值，如果不存在或类型不兼容返回0.0
     */
    public double getAsDouble(String key) {
        Object value = map.get(key);
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
     * 获取指定键对应的布尔值。
     *
     * @param key 键名
     * @return 布尔值，如果不存在或类型不兼容返回false
     */
    public boolean getAsBoolean(String key) {
        Object value = map.get(key);
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
     * 获取键值对数量。
     */
    public int size() {
        return map.size();
    }

    /**
     * 是否为空。
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * 检查指定键的值是否为 null。
     *
     * @param key 键名
     * @return 值是否为 null
     */
    public boolean isNull(String key) {
        return !map.containsKey(key) || map.get(key) == null;
    }

    /**
     * 获取指定键对应的 JsonObject 值（不抛异常，键不存在返回 null）。
     */
    public JsonObject optAsJsonObject(String key) {
        if (!map.containsKey(key)) {
            return null;
        }
        Object obj = map.get(key);
        if (obj instanceof JsonObject) {
            return (JsonObject) obj;
        }
        return null;
    }

    /**
     * 获取指定键对应的 JsonArray 值（不抛异常，键不存在返回 null）。
     */
    public JsonArray optAsJsonArray(String key) {
        if (!map.containsKey(key)) {
            return null;
        }
        Object obj = map.get(key);
        if (obj instanceof JsonArray) {
            return (JsonArray) obj;
        }
        return null;
    }

    /**
     * 获取指定键对应的字符串值（不抛异常，键不存在返回空字符串）。
     */
    public String optAsString(String key) {
        Object value = map.get(key);
        if (value == null) {
            return "";
        }
        return value instanceof String ? (String) value : value.toString();
    }

    /**
     * 检查是否包含指定键。
     */
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    /**
     * 移除指定键。
     */
    public Object remove(String key) {
        return map.remove(key);
    }

    /**
     * 添加所有键值对。
     */
    public JsonObject addAll(Map<String, Object> values) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public String toString() {
        return BeautifyJsonUtils.beautify(this);
    }

    /**
     * 返回 JSON 字符串（非格式化）。
     */
    public String toJsonString() {
        return serializeToString(this);
    }

    private String serializeToString(JsonObject obj) {
        if (obj.isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append(escapeString(entry.getKey())).append(":");
            sb.append(serializeValue(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
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

    private String serializeValue(Object value) {
        if (value == null) return "null";
        if (value instanceof String) return escapeString((String) value);
        if (value instanceof Number) return value.toString();
        if (value instanceof Boolean) return value.toString();
        if (value instanceof JsonObject) return serializeToString((JsonObject) value);
        if (value instanceof JsonArray) return serializeArrayToString((JsonArray) value);
        return escapeString(value.toString());
    }

    private String serializeArrayToString(JsonArray arr) {
        if (arr.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object item : arr) {
            if (!first) sb.append(",");
            first = false;
            sb.append(serializeValue(item));
        }
        sb.append("]");
        return sb.toString();
    }
}
