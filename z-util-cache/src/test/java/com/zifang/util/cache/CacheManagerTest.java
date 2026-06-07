package com.zifang.util.cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for CacheManager.
 */
/**
 * CacheManagerTest类。
 */
public class CacheManagerTest {

    private CacheManager cacheManager;

    @Before
    /**
     * setUp方法。
     */
    public void setUp() {
        cacheManager = CacheManager.getInstance();
    }

    @After
    /**
     * tearDown方法。
     */
    public void tearDown() {
        cacheManager.clearAll();
        // Clear all registered caches
        java.util.Map<String, Cache> caches = getCachesField();
        if (caches != null) {
            caches.clear();
        }
    }

    @SuppressWarnings("unchecked")
    private java.util.Map<String, Cache> getCachesField() {
        try {
            java.lang.reflect.Field field = CacheManager.class.getDeclaredField("caches");
            field.setAccessible(true);
            return (java.util.Map<String, Cache>) field.get(cacheManager);
        } catch (Exception e) {
            return null;
        }
    }

    @Test
    /**
     * testGetInstance方法。
     */
    public void testGetInstance() {
        CacheManager instance1 = CacheManager.getInstance();
        CacheManager instance2 = CacheManager.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    /**
     * testGetMemoryCache方法。
     */
    public void testGetMemoryCache() {
        MemoryCache cache = cacheManager.getMemoryCache("test-memory");
        assertNotNull(cache);
        assertEquals("test-memory", cache.getName());
    }

    @Test
    /**
     * testGetMemoryCacheWithCapacity方法。
     */
    public void testGetMemoryCacheWithCapacity() {
        MemoryCache cache = cacheManager.getMemoryCache("test-capacity", 32);
        assertNotNull(cache);
        
        // Add entries and verify
        for (int i = 0; i < 20; i++) {
            cache.put("key" + i, "value" + i);
        }
        assertEquals(20, cache.size());
    }

    @Test
    /**
     * testGetMemoryCacheWithTTL方法。
     */
    public void testGetMemoryCacheWithTTL() {
        MemoryCache cache = cacheManager.getMemoryCache("test-ttl", 16, 60);
        assertNotNull(cache);
        
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    /**
     * testGetCacheReturnsSameInstance方法。
     */
    public void testGetCacheReturnsSameInstance() {
        MemoryCache cache1 = cacheManager.getMemoryCache("singleton-test");
        MemoryCache cache2 = cacheManager.getMemoryCache("singleton-test");
        assertSame(cache1, cache2);
    }

    @Test
    /**
     * testRegisterCache方法。
     */
    public void testRegisterCache() {
        MemoryCache customCache = new MemoryCache("custom-cache");
        Cache previous = cacheManager.registerCache("custom-cache", customCache);
        assertNull(previous);
        
        Cache retrieved = cacheManager.getCache("custom-cache");
        assertSame(customCache, retrieved);
    }

    @Test
    /**
     * testRegisterCacheOverwrite方法。
     */
    public void testRegisterCacheOverwrite() {
        MemoryCache cache1 = new MemoryCache("overwrite-test");
        MemoryCache cache2 = new MemoryCache("overwrite-test-2");
        
        cacheManager.registerCache("overwrite-key", cache1);
        Cache previous = cacheManager.registerCache("overwrite-key", cache2);
        
        assertSame(cache1, previous);
        assertSame(cache2, cacheManager.getCache("overwrite-key"));
    }

    @Test
    /**
     * testGetCacheByName方法。
     */
    public void testGetCacheByName() {
        MemoryCache memoryCache = cacheManager.getMemoryCache("by-name-test");
        Cache cache = cacheManager.getCache("by-name-test");
        assertNotNull(cache);
        assertSame(memoryCache, cache);
    }

    @Test
    /**
     * testGetNonExistentCache方法。
     */
    public void testGetNonExistentCache() {
        Cache cache = cacheManager.getCache("non-existent");
        assertNull(cache);
    }

    @Test
    /**
     * testRemoveCache方法。
     */
    public void testRemoveCache() {
        MemoryCache cache = cacheManager.getMemoryCache("remove-test");
        Cache removed = cacheManager.removeCache("remove-test");
        assertNotNull(removed);
        assertSame(cache, removed);
        assertNull(cacheManager.getCache("remove-test"));
    }

    @Test
    /**
     * testRemoveNonExistentCache方法。
     */
    public void testRemoveNonExistentCache() {
        Cache removed = cacheManager.removeCache("non-existent");
        assertNull(removed);
    }

    @Test
    /**
     * testClearAll方法。
     */
    public void testClearAll() {
        MemoryCache cache1 = cacheManager.getMemoryCache("clear-1");
        MemoryCache cache2 = cacheManager.getMemoryCache("clear-2");
        
        cache1.put("key1", "value1");
        cache2.put("key2", "value2");
        
        cacheManager.clearAll();
        
        // clearAll should clear the content, not remove the cache references
        assertNotNull(cacheManager.getCache("clear-1"));
        assertNotNull(cacheManager.getCache("clear-2"));
        assertNull(cacheManager.getCache("clear-1").get("key1"));
        assertNull(cacheManager.getCache("clear-2").get("key2"));
    }

    @Test
    /**
     * testGetCacheWithType方法。
     */
    public void testGetCacheWithType() {
        Cache cache = cacheManager.getCache("type-test", "memory");
        assertNotNull(cache);
        assertTrue(cache instanceof MemoryCache);
    }

    @Test(expected = IllegalArgumentException.class)
    /**
     * testGetCacheWithUnsupportedType方法。
     */
    public void testGetCacheWithUnsupportedType() {
        cacheManager.getCache("invalid-type", "redis");
    }
}