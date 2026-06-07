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
/**
 * MemoryCache类。
 */
/**
 * MemoryCache类。
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
    /**
     * MemoryCache方法。
     *      * @param name String类型参数
     */
    /**
     * MemoryCache方法。
     *      * @param name String类型参数
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
    /**
     * MemoryCache方法。
     *      * @param name String类型参数
     * @param initialCapacity int类型参数
     */
    /**
     * MemoryCache方法。
     *      * @param name String类型参数
     * @param initialCapacity int类型参数
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
    /**
     * MemoryCache方法。
     *      * @param name String类型参数
     * @param initialCapacity int类型参数
     * @param defaultTtlSeconds long类型参数
     */
    /**
     * MemoryCache方法。
     *      * @param name String类型参数
     * @param initialCapacity int类型参数
     * @param defaultTtlSeconds long类型参数
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
    /**
     * get方法。
     *      * @param key String类型参数
     * @return Object类型返回值
     */
    /**
     * get方法。
     *      * @param key String类型参数
     * @return Object类型返回值
     */
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
    /**
     * put方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     */
    /**
     * put方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     */
    public void put(String key, Object value) {
        put(key, value, defaultTtlSeconds);
    }

    @Override
    /**
     * put方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     * @param ttl long类型参数
     */
    /**
     * put方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     * @param ttl long类型参数
     */
    public void put(String key, Object value, long ttl) {
        long effectiveTtl = ttl > 0 ? ttl : defaultTtlSeconds;
        if (effectiveTtl > 0) {
            cache.put(key, new CacheEntry(value, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(effectiveTtl)));
        } else {
            cache.put(key, new CacheEntry(value, Long.MAX_VALUE));
        }
    }

    @Override
    /**
     * remove方法。
     *      * @param key String类型参数
     * @return boolean类型返回值
     */
    /**
     * remove方法。
     *      * @param key String类型参数
     * @return boolean类型返回值
     */
    public boolean remove(String key) {
        return cache.remove(key) != null;
    }

    @Override
    /**
     * contains方法。
     *      * @param key String类型参数
     * @return boolean类型返回值
     */
    /**
     * contains方法。
     *      * @param key String类型参数
     * @return boolean类型返回值
     */
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
    /**
     * clear方法。
     */
    /**
     * clear方法。
     */
    public void clear() {
        cache.clear();
    }

    @Override
    /**
     * getName方法。
     * @return String类型返回值
     */
    /**
     * getName方法。
     * @return String类型返回值
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current size of the cache.
     *
     * @return number of entries (including expired but not yet removed)
     */
    /**
     * size方法。
     * @return int类型返回值
     */
    /**
     * size方法。
     * @return int类型返回值
     */
    public int size() {
        return cache.size();
    }

    /**
     * Shutdown the cleanup scheduler.
     */
    /**
     * shutdown方法。
     */
    /**
     * shutdown方法。
     */
    public void shutdown() {
        scheduler.shutdown();
    }

    // ==================== 持久化 ====================

    @Override
    /**
     * exportToFile方法。
     *      * @param filePath String类型参数
     */
    /**
     * exportToFile方法。
     *      * @param filePath String类型参数
     */
    public void exportToFile(String filePath) {
        try (java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(
                new java.io.BufferedOutputStream(
                        new java.io.FileOutputStream(filePath)))) {
            synchronized (cache) {
                out.writeObject(name);
                out.writeInt(cache.size());
                for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
                    if (!entry.getValue().isExpired()) {
                        out.writeObject(entry.getKey());
                        out.writeObject(entry.getValue());
                    }
                }
            }
        } catch (java.io.IOException e) {
            throw new CacheException("Failed to export cache to file: " + filePath, e);
        }
    }

    @Override
    /**
     * importFromFile方法。
     *      * @param filePath String类型参数
     */
    /**
     * importFromFile方法。
     *      * @param filePath String类型参数
     */
    public void importFromFile(String filePath) {
        try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(
                new java.io.BufferedInputStream(
                        new java.io.FileInputStream(filePath)))) {
            String exportedName = (String) in.readObject();
            int size = in.readInt();
            synchronized (cache) {
                cache.clear();
                for (int i = 0; i < size; i++) {
                    String key = (String) in.readObject();
                    CacheEntry entry = (CacheEntry) in.readObject();
                    if (!entry.isExpired()) {
                        cache.put(key, entry);
                    }
                }
            }
        } catch (java.io.IOException | java.lang.ClassNotFoundException e) {
            throw new CacheException("Failed to import cache from file: " + filePath, e);
        }
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
    private static class CacheEntry implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
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