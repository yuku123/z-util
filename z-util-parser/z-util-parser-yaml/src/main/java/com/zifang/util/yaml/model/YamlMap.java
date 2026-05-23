package com.zifang.util.yaml.model;

import com.zifang.util.yaml.define.YamlNodeType;

import java.util.*;

/**
 * YAML 映射节点，对应 YAML 中的映射（mapping）结构，
 * 即键值对集合，类似 JSON 中的对象。
 *
 * @author zifang
 */
public class YamlMap implements Map<String, Object> {

    private final Map<String, Object> delegate = new LinkedHashMap<>();

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return delegate.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return delegate.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return delegate.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        delegate.putAll(m);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<String> keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<Object> values() {
        return delegate.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return delegate.entrySet();
    }

    public YamlNodeType getNodeType() {
        return YamlNodeType.MAP;
    }

    /**
     * 获取字符串值，辅助方法。
     *
     * @param key 键
     * @return 字符串值，若不存在或类型不匹配返回 null
     */
    public String getString(String key) {
        Object val = get(key);
        return val instanceof String ? (String) val : null;
    }

    /**
     * 获取整数值，辅助方法。
     *
     * @param key 键
     * @return 整数值，若不存在或类型不匹配返回 null
     */
    public Integer getInt(String key) {
        Object val = get(key);
        if (val instanceof Number) {
            return ((Number) val).intValue();
        }
        return null;
    }

    /**
     * 获取嵌套映射，辅助方法。
     *
     * @param key 键
     * @return 嵌套的 YamlMap，若不存在或类型不匹配返回 null
     */
    public YamlMap getMap(String key) {
        Object val = get(key);
        return val instanceof YamlMap ? (YamlMap) val : null;
    }

    /**
     * 获取嵌套列表，辅助方法。
     *
     * @param key 键
     * @return 嵌套的 YamlArray，若不存在或类型不匹配返回 null
     */
    public YamlArray getArray(String key) {
        Object val = get(key);
        return val instanceof YamlArray ? (YamlArray) val : null;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YamlMap)) return false;
        return delegate.equals(((YamlMap) o).delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}
