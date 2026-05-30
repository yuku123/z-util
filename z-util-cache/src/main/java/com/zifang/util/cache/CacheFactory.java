package com.zifang.util.cache;

/**
 * Factory interface for creating Cache instances.
 * Implementations provide different cache types.
 */
/**
 * CacheFactory接口。
 */
public interface CacheFactory {

    /**
     * Create a new Cache instance.
     *
     * @param name cache name
     * @return the cache instance
     */
    Cache createCache(String name);
}