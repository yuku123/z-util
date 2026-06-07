package com.zifang.util.cache;

/**
 * Cache interface defining basic cache operations.
 * Provides abstraction for different cache implementations (memory, redis, etc.)
 */
/**
 * Cache接口。
 */
/**
 * Cache接口。
 */
public interface Cache {

    /**
     * Get value from cache by key.
     *
     * @param key the cache key
     * @return the cached value, or null if not found or expired
     */
    Object get(String key);

    /**
     * Put value into cache with default expiration (no expiration).
     *
     * @param key   the cache key
     * @param value the value to cache
     */
    void put(String key, Object value);

    /**
     * Put value into cache with specified time-to-live in seconds.
     *
     * @param key   the cache key
     * @param value the value to cache
     * @param ttl   time-to-live in seconds
     */
    void put(String key, Object value, long ttl);

    /**
     * Remove a key from cache.
     *
     * @param key the cache key
     * @return true if the key was removed, false if it didn't exist
     */
    boolean remove(String key);

    /**
     * Check if key exists in cache.
     *
     * @param key the cache key
     * @return true if key exists
     */
    boolean contains(String key);

    /**
     * Clear all entries from cache.
     */
    void clear();

    /**
     * Get the name of this cache instance.
     *
     * @return cache name
     */
    String getName();

    /**
     * Export cache contents to a file for persistence.
     *
     * @param filePath path to the export file
     */
    void exportToFile(String filePath);

    /**
     * Import cache contents from a file previously exported.
     *
     * @param filePath path to the import file
     */
    void importFromFile(String filePath);
}