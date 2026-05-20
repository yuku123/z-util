package com.zifang.util.zex.source;

import java.util.AbstractMap;
import java.util.Set;

/**
 * 临时Map实现类。
 * <p>
 * 此类继承自AbstractMap，是Map接口的部分实现。
 * 主要用于演示AbstractMap的用法。
 *
 * @author zifang
 * @version 1.0
 */
public class TempMap<K, V> extends AbstractMap<K, V> {

    /**
     * 返回Map的条目集合。
     *
     * @return 条目集合
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    /**
     * 向Map中添加键值对。
     *
     * @param key   键
     * @param value 值
     * @return 旧值
     */
    @Override
    public V put(K key, V value) {
        return super.put(key, value);
    }
}
