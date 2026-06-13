package com.zifang.util.cache;

import java.time.Duration;

/**
 * 自定义过期策略（对标 Caffeine 的 {@code Expiry}）。
 * <p>
 * 与 {@code expireAfterWrite}/{@code expireAfterAccess} 互斥；同时设置时优先 Expiry。
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
@FunctionalInterface
public interface Expiry<K, V> {

    /**
     * 条目创建后多久过期。返回 {@link Duration#ZERO} 或负值表示永不过期。
     */
    default Duration expireAfterCreate(K key, V value) {
        return Duration.ofNanos(Long.MAX_VALUE);
    }

    /**
     * 条目最近一次读/写后多久过期。
     */
    default Duration expireAfterUpdate(K key, V value, long currentTimeNanos) {
        return expireAfterCreate(key, value);
    }

    /**
     * 条目最近一次读后多久过期。
     */
    default Duration expireAfterRead(K key, V value, long currentTimeNanos) {
        return currentTimeNanos == 0 ? Duration.ofNanos(Long.MAX_VALUE) : Duration.ofNanos(Long.MAX_VALUE);
    }
}
