package com.zifang.util.office.word;

/**
 * 键值对泛型类
 * 用于存储一一对应的键值数据
 *
 * @param <K> 键的类型
 * @param <V> 值的类型
 */
/**
 * Tuple类。
 */
/**
 * Tuple类。
 */
public class Tuple<K, V> {
    private K key;
    private V value;

    /**
     * 获取键
     *
     * @return 键
     */
    /**
     * getKey方法。
     * @return K类型返回值
     */
    /**
     * getKey方法。
     * @return K类型返回值
     */
    public K getKey() {
        return key;
    }

    /**
     * 设置键
     *
     * @param key 键
     */
    /**
     * setKey方法。
     *      * @param key K类型参数
     */
    /**
     * setKey方法。
     *      * @param key K类型参数
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * 获取值
     *
     * @return 值
     */
    /**
     * getValue方法。
     * @return V类型返回值
     */
    /**
     * getValue方法。
     * @return V类型返回值
     */
    public V getValue() {
        return value;
    }

    /**
     * 设置值
     *
     * @param value 值
     */
    /**
     * setValue方法。
     *      * @param value V类型参数
     */
    /**
     * setValue方法。
     *      * @param value V类型参数
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * 构造方法
     *
     * @param key 键
     * @param value 值
     */
    /**
     * Tuple方法。
     *      * @param key K类型参数
     * @param value V类型参数
     */
    /**
     * Tuple方法。
     *      * @param key K类型参数
     * @param value V类型参数
     */
    public Tuple(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
