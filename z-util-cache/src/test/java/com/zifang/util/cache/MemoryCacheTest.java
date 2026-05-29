package com.zifang.util.cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for MemoryCache implementation.
 */
public class MemoryCacheTest {

    private MemoryCache cache;

    @Before
    public void setUp() {
        cache = new MemoryCache("test-cache", 16);
    }

    @After
    public void tearDown() {
        cache.clear();
        cache.shutdown();
    }

    @Test
    public void testGetName() {
        assertEquals("test-cache", cache.getName());
    }

    @Test
    public void testPutAndGet() {
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    public void testGetNonExistentKey() {
        assertNull(cache.get("non-existent"));
    }

    @Test
    public void testPutWithTTL() throws InterruptedException {
        cache.put("ttl-key", "ttl-value", 1);
        assertEquals("ttl-value", cache.get("ttl-key"));
        
        // Wait for expiration
        Thread.sleep(1500);
        assertNull(cache.get("ttl-key"));
    }

    @Test
    public void testContains() {
        cache.put("key1", "value1");
        assertTrue(cache.contains("key1"));
        assertFalse(cache.contains("non-existent"));
    }

    @Test
    public void testContainsExpired() throws InterruptedException {
        cache.put("ttl-key", "ttl-value", 1);
        Thread.sleep(1500);
        assertFalse(cache.contains("ttl-key"));
    }

    @Test
    public void testRemove() {
        cache.put("key1", "value1");
        assertTrue(cache.remove("key1"));
        assertNull(cache.get("key1"));
        
        // Remove non-existent key
        assertFalse(cache.remove("non-existent"));
    }

    @Test
    public void testClear() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.clear();
        assertNull(cache.get("key1"));
        assertNull(cache.get("key2"));
        assertEquals(0, cache.size());
    }

    @Test
    public void testSize() {
        assertEquals(0, cache.size());
        cache.put("key1", "value1");
        assertEquals(1, cache.size());
        cache.put("key2", "value2");
        assertEquals(2, cache.size());
        cache.remove("key1");
        assertEquals(1, cache.size());
    }

    @Test
    public void testOverwriteValue() {
        cache.put("key1", "value1");
        cache.put("key1", "value2");
        assertEquals("value2", cache.get("key1"));
        assertEquals(1, cache.size());
    }

    @Test
    public void testNullValue() {
        cache.put("null-key", null);
        assertNull(cache.get("null-key"));
        assertTrue(cache.contains("null-key"));
    }

    @Test
    public void testCacheWithDefaultTTL() throws InterruptedException {
        MemoryCache ttlCache = new MemoryCache("ttl-cache", 16, 1);
        ttlCache.put("key1", "value1");
        assertEquals("value1", ttlCache.get("key1"));
        
        // Wait long enough for cleanup to run (cleanup runs every 1s)
        Thread.sleep(2500);
        assertNull("Entry should be expired and removed", ttlCache.get("key1"));
        ttlCache.shutdown();
    }

    @Test
    public void testMultiplePuts() {
        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }
        assertEquals(100, cache.size());
        
        for (int i = 0; i < 100; i++) {
            assertEquals("value" + i, cache.get("key" + i));
        }
    }
}