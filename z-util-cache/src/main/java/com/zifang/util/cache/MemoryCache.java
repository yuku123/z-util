package com.zifang.util.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * In-memory cache implementation with optional TTL support.
 * Uses ConcurrentHashMap for thread-safety and ScheduledExecutorService for expiration.
 */
public class MemoryCache implements Cache {

    private final String name;
    private final Map<String, CacheEntry> cache;
    private final ScheduledExecutorService scheduler;
    private final long defaultTtlSeconds;

    /**
     * Create a new in-memory cache with the given name.
     *
     * @param name cache name
     */
    public MemoryCache(String name) {
        this(name, 16, 0);
    }

    /**
     * Create a new in-memory cache with specified initial capacity and no expiration.
     *
     * @param name           cache name
     * @param initialCapacity initial capacity
     */
    public MemoryCache(String name, int initialCapacity) {
        this(name, initialCapacity, 0);
    }

    /**
     * Create a new in-memory cache with specified capacity and default TTL.
     *
     * @param name           cache name
     * @param initialCapacity initial capacity
     * @param defaultTtlSeconds default TTL in seconds, 0 means no expiration
     */
    public MemoryCache(String name, int initialCapacity, long defaultTtlSeconds) {
        this.name = name;
        this.defaultTtlSeconds = defaultTtlSeconds;
        this.cache = new ConcurrentHashMap<>(initialCapacity);
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "MemoryCache-" + name + "-cleanup");
            t.setDaemon(true);
            return t;
        });

        if (defaultTtlSeconds > 0) {
            scheduleCleanup(defaultTtlSeconds);
        }
    }

    @Override
    public Object get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return entry.getValue();
    }

    @Override
    public void put(String key, Object value) {
        put(key, value, defaultTtlSeconds);
    }

    @Override
    public void put(String key, Object value, long ttl) {
        long effectiveTtl = ttl > 0 ? ttl : defaultTtlSeconds;
        if (effectiveTtl > 0) {
            cache.put(key, new CacheEntry(value, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(effectiveTtl)));
        } else {
            cache.put(key, new CacheEntry(value, Long.MAX_VALUE));
        }
    }

    @Override
    public boolean remove(String key) {
        return cache.remove(key) != null;
    }

    @Override
    public boolean contains(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return false;
        }
        if (entry.isExpired()) {
            cache.remove(key);
            return false;
        }
        return true;
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the current size of the cache.
     *
     * @return number of entries (including expired but not yet removed)
     */
    public int size() {
        return cache.size();
    }

    /**
     * Shutdown the cleanup scheduler.
     */
    public void shutdown() {
        scheduler.shutdown();
    }

    private void scheduleCleanup(long ttlSeconds) {
        scheduler.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        }, ttlSeconds, ttlSeconds, TimeUnit.MILLISECONDS);
    }

    /**
     * Cache entry with value and expiration time.
     */
    private static class CacheEntry {
        private final Object value;
        private final long expirationTime; // 0 means no expiration

        CacheEntry(Object value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }

        Object getValue() {
            return value;
        }

        boolean isExpired() {
            return expirationTime > 0 && System.currentTimeMillis() > expirationTime;
        }
    }
}