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
