package com.zifang.util.cache;

/**
 * Factory interface for creating Cache instances.
 * Implementations provide different cache types.
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