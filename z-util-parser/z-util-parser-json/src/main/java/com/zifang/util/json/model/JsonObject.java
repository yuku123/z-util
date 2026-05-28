package com.zifang.util.json.model;

import com.zifang.util.json.BeautifyJsonUtils;
import com.zifang.util.json.exception.JsonTypeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON对象模型，表示一个JSON对象（键值对集合）。
 * <p>
 * 内部使用HashMap存储键值对，支持链式调用和类型安全的取值操作。
 *
 * @author zifang
 * @see JsonArray
 */
public class JsonObject {

    private Map<String, Object> map = new HashMap<String, Object>();

    /**
     * 向JSON对象中添加一个键值对。
     *
     * @param key   键名
     * @param value 值，可以是基本类型、String、JsonObject、JsonArray或null
     */
    public void put(String key, Object value) {
        map.put(key, value);
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
     * 获取 JSON 对象中键值对的数量。
     *
     * @return 键值对数量
     */
    public int size() {
        return map.size();
    }

    /**
     * 判断 JSON 对象是否包含指定键。
     *
     * @param key 键名
     * @return 是否包含
     */
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    /**
     * 移除指定键的键值对。
     *
     * @param key 键名
     * @return 被移除的值，如果键不存在则返回null
     */
    public Object remove(String key) {
        return map.remove(key);
    }

    /**
     * 获取指定键的值，如果键不存在则返回默认值。
     *
     * @param key          键名
     * @param defaultValue 默认值
     * @return 键值或默认值
     */
    public Object getOrDefault(String key, Object defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    /**
     * 获取指定键对应的 String 值。
     *
     * @param key 键名
     * @return String 值，键不存在或类型不匹配返回 null
     */
    public String getString(String key) {
        Object v = map.get(key);
        return v instanceof String ? (String) v : null;
    }

    /**
     * 获取指定键对应的 Integer 值。
     *
     * @param key 键名
     * @return Integer 值，键不存在或类型不匹配返回 null
     */
    public Integer getInt(String key) {
        Object v = map.get(key);
        if (v instanceof Number) return ((Number) v).intValue();
        return null;
    }

    /**
     * 获取指定键对应的 Long 值。
     *
     * @param key 键名
     * @return Long 值，键不存在或类型不匹配返回 null
     */
    public Long getLong(String key) {
        Object v = map.get(key);
        if (v instanceof Number) return ((Number) v).longValue();
        return null;
    }

    /**
     * 获取指定键对应的 Double 值。
     *
     * @param key 键名
     * @return Double 值，键不存在或类型不匹配返回 null
     */
    public Double getDouble(String key) {
        Object v = map.get(key);
        if (v instanceof Number) return ((Number) v).doubleValue();
        return null;
    }

    /**
     * 获取指定键对应的 Boolean 值。
     *
     * @param key 键名
     * @return Boolean 值，键不存在或类型不匹配返回 null
     */
    public Boolean getBoolean(String key) {
        Object v = map.get(key);
        if (v instanceof Boolean) return (Boolean) v;
        return null;
    }

    /**
     * 判断 JSON 对象是否为空（不包含任何键值对）。
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return map.isEmpty();
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
     * @throws JsonTypeException       如果值不是JsonObject类型
     */
    public JsonObject getJsonObject(String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }

        Object obj = map.get(key);
        if (!(obj instanceof JsonObject)) {
            throw new JsonTypeException("Type of value is not JsonObject");
        }

        return (JsonObject) obj;
    }

    /**
     * 获取指定键对应的JsonArray值。
     *
     * @param key 键名
     * @return JsonArray值
     * @throws IllegalArgumentException 如果键不存在
     * @throws JsonTypeException       如果值不是JsonArray类型
     */
    public JsonArray getJsonArray(String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }

        Object obj = map.get(key);
        if (!(obj instanceof JsonArray)) {
            throw new JsonTypeException("Type of value is not JsonArray");
        }

        return (JsonArray) obj;
    }

    @Override
    public String toString() {
        return BeautifyJsonUtils.beautify(this);
    }
}
