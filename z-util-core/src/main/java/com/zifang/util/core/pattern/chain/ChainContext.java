package com.zifang.util.core.pattern.chain;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 链执行上下文
 * <p>
 * 携带数据在链中传递，支持任意类型的key-value数据存储
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author zifang
 */
public class ChainContext<K, V> implements Map<K, V> {

    private final Map<K, V> data;
    private final List<ChainContext<K, V>> history;
    private boolean executed;

    public ChainContext() {
        this.data = new HashMap<>();
        this.history = new ArrayList<>();
        this.executed = false;
    }

    public ChainContext(Map<K, V> initialData) {
        this.data = new HashMap<>(initialData);
        this.history = new ArrayList<>();
        this.executed = false;
    }

    /**
     * 创建并初始化上下文的工厂方法
     */
    public static <K, V> ChainContext<K, V> create() {
        return new ChainContext<>();
    }

    public static <K, V> ChainContext<K, V> create(Map<K, V> initialData) {
        return new ChainContext<>(initialData);
    }

    /**
     * 获取值并自动类型转换
     */
    @SuppressWarnings("unchecked")
    public <T extends V> T get(K key, Class<T> type) {
        V value = data.get(key);
        if (value != null && !type.isInstance(value)) {
            throw new ClassCastException("Value for key " + key + " is not of type " + type);
        }
        return (T) value;
    }

    /**
     * 获取值，如果不存在则使用默认值
     */
    public V getOrDefaultValue(K key, V defaultValue) {
        V value = data.get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 如果不存在则插入
     */
    public V putIfAbsent(K key, V value) {
        return data.putIfAbsent(key, value);
    }

    /**
     * 放入值并返回this，支持链式调用
     */
    public ChainContext<K, V> putChain(K key, V value) {
        data.put(key, value);
        return this;
    }

    /**
     * 获取执行历史
     */
    public List<ChainContext<K, V>> getHistory() {
        return Collections.unmodifiableList(history);
    }

    /**
     * 标记为已执行
     */
    public void markExecuted() {
        this.executed = true;
    }

    /**
     * 是否已执行
     */
    public boolean isExecuted() {
        return executed;
    }

    /**
     * 复制上下文
     */
    public ChainContext<K, V> copy() {
        ChainContext<K, V> copy = new ChainContext<>(this.data);
        copy.history.addAll(this.history);
        copy.executed = this.executed;
        return copy;
    }

    // Map接口实现

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return data.get(key);
    }

    @Override
    public V put(K key, V value) {
        return data.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return data.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        data.putAll(m);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public Set<K> keySet() {
        return data.keySet();
    }

    @Override
    public Collection<V> values() {
        return data.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return data.entrySet();
    }

    @Override
    public String toString() {
        return "ChainContext{" +
                "data=" + data +
                ", executed=" + executed +
                '}';
    }
}