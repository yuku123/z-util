package com.zifang.util.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache manager for creating and managing cache instances.
 * Supports different cache types: memory, redis, etc.
 */
/**
 * CacheManager类。
 */
public class CacheManager {

    private static volatile CacheManager instance;
    private final Map<String, Cache> caches;

    private CacheManager() {
        this.caches = new ConcurrentHashMap<>();
    }

    /**
     * Get the singleton instance of CacheManager.
     *
     * @return the CacheManager instance
     */
    /**
     * getInstance方法。
     * @return static CacheManager类型返回值
     */
    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    /**
     * Get or create a memory cache with the given name.
     *
     * @param name cache name
     * @return the memory cache instance
     */
    /**
     * getMemoryCache方法。
     *      * @param name String类型参数
     * @return MemoryCache类型返回值
     */
    public MemoryCache getMemoryCache(String name) {
        return getMemoryCache(name, 16);
    }

    /**
     * Get or create a memory cache with the given name and initial capacity.
     *
     * @param name           cache name
     * @param initialCapacity initial capacity
     * @return the memory cache instance
     */
    /**
     * getMemoryCache方法。
     *      * @param name String类型参数
     * @param initialCapacity int类型参数
     * @return MemoryCache类型返回值
     */
    public MemoryCache getMemoryCache(String name, int initialCapacity) {
        return getOrCreate(name, () -> new MemoryCache(name, initialCapacity));
    }

    /**
     * Get or create a memory cache with the given name, initial capacity, and default TTL.
     *
     * @param name           cache name
     * @param initialCapacity initial capacity
     * @param defaultTtlSeconds default TTL in seconds
     * @return the memory cache instance
     */
    /**
     * getMemoryCache方法。
     *      * @param name String类型参数
     * @param initialCapacity int类型参数
     * @param defaultTtlSeconds long类型参数
     * @return MemoryCache类型返回值
     */
    public MemoryCache getMemoryCache(String name, int initialCapacity, long defaultTtlSeconds) {
        return getOrCreate(name, () -> new MemoryCache(name, initialCapacity, defaultTtlSeconds));
    }

    /**
     * Get or create a cache with the given name and type.
     *
     * @param name cache name
     * @param type cache type ("memory", "redis")
     * @return the cache instance
     */
    /**
     * getCache方法。
     *      * @param name String类型参数
     * @param type String类型参数
     * @return Cache类型返回值
     */
    public Cache getCache(String name, String type) {
        if ("memory".equalsIgnoreCase(type)) {
            return getMemoryCache(name);
        }
        throw new IllegalArgumentException("Unsupported cache type: " + type);
    }

    /**
     * Register a cache instance with the given name.
     *
     * @param name  cache name
     * @param cache the cache instance
     * @return the previous cache with this name, or null if none
     */
    /**
     * registerCache方法。
     *      * @param name String类型参数
     * @param cache Cache类型参数
     * @return Cache类型返回值
     */
    public Cache registerCache(String name, Cache cache) {
        return caches.put(name, cache);
    }

    /**
     * Get a cache by name.
     *
     * @param name cache name
     * @return the cache instance, or null if not found
     */
    /**
     * getCache方法。
     *      * @param name String类型参数
     * @return Cache类型返回值
     */
    public Cache getCache(String name) {
        return caches.get(name);
    }

    /**
     * Remove a cache by name.
     *
     * @param name cache name
     * @return the removed cache, or null if not found
     */
    /**
     * removeCache方法。
     *      * @param name String类型参数
     * @return Cache类型返回值
     */
    public Cache removeCache(String name) {
        Cache cache = caches.remove(name);
        if (cache instanceof MemoryCache) {
            ((MemoryCache) cache).shutdown();
        }
        return cache;
    }

    /**
     * Clear all caches.
     */
    /**
     * clearAll方法。
     */
    public void clearAll() {
        caches.values().forEach(Cache::clear);
    }

    @SuppressWarnings("unchecked")
    private <T extends Cache> T getOrCreate(String name, CacheFactory<T> factory) {
        Cache cache = caches.get(name);
        if (cache == null) {
            synchronized (this) {
                cache = caches.get(name);
                if (cache == null) {
                    cache = factory.create();
                    caches.put(name, cache);
                }
            }
        }
        return (T) cache;
    }

    @FunctionalInterface
    private interface CacheFactory<T extends Cache> {
        T create();
    }
}