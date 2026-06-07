package com.zifang.util.cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for MemoryCache implementation.
 */
/**
 * MemoryCacheTest类。
 */
public class MemoryCacheTest {

    private MemoryCache cache;

    @Before
    /**
     * setUp方法。
     */
    public void setUp() {
        cache = new MemoryCache("test-cache", 16);
    }

    @After
    /**
     * tearDown方法。
     */
    public void tearDown() {
        cache.clear();
        cache.shutdown();
    }

    @Test
    /**
     * testGetName方法。
     */
    public void testGetName() {
        assertEquals("test-cache", cache.getName());
    }

    @Test
    /**
     * testPutAndGet方法。
     */
    public void testPutAndGet() {
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    /**
     * testGetNonExistentKey方法。
     */
    public void testGetNonExistentKey() {
        assertNull(cache.get("non-existent"));
    }

    @Test
    /**
     * testPutWithTTL方法。
     */
    public void testPutWithTTL() throws InterruptedException {
        cache.put("ttl-key", "ttl-value", 1);
        assertEquals("ttl-value", cache.get("ttl-key"));
        
        // Wait for expiration
        Thread.sleep(1500);
        assertNull(cache.get("ttl-key"));
    }

    @Test
    /**
     * testContains方法。
     */
    public void testContains() {
        cache.put("key1", "value1");
        assertTrue(cache.contains("key1"));
        assertFalse(cache.contains("non-existent"));
    }

    @Test
    /**
     * testContainsExpired方法。
     */
    public void testContainsExpired() throws InterruptedException {
        cache.put("ttl-key", "ttl-value", 1);
        Thread.sleep(1500);
        assertFalse(cache.contains("ttl-key"));
    }

    @Test
    /**
     * testRemove方法。
     */
    public void testRemove() {
        cache.put("key1", "value1");
        assertTrue(cache.remove("key1"));
        assertNull(cache.get("key1"));
        
        // Remove non-existent key
        assertFalse(cache.remove("non-existent"));
    }

    @Test
    /**
     * testClear方法。
     */
    public void testClear() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.clear();
        assertNull(cache.get("key1"));
        assertNull(cache.get("key2"));
        assertEquals(0, cache.size());
    }

    @Test
    /**
     * testSize方法。
     */
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
    /**
     * testOverwriteValue方法。
     */
    public void testOverwriteValue() {
        cache.put("key1", "value1");
        cache.put("key1", "value2");
        assertEquals("value2", cache.get("key1"));
        assertEquals(1, cache.size());
    }

    @Test
    /**
     * testNullValue方法。
     */
    public void testNullValue() {
        cache.put("null-key", null);
        assertNull(cache.get("null-key"));
        assertTrue(cache.contains("null-key"));
    }

    @Test
    /**
     * testCacheWithDefaultTTL方法。
     */
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
    /**
     * testMultiplePuts方法。
     */
    public void testMultiplePuts() {
        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }
        assertEquals(100, cache.size());

        for (int i = 0; i < 100; i++) {
            assertEquals("value" + i, cache.get("key" + i));
        }
    }

    @Test
    /**
     * testExportImportRoundtrip方法。
     */
    public void testExportImportRoundtrip() throws java.io.IOException {
        cache.put("k1", "v1");
        cache.put("k2", 42);
        cache.put("k3", new java.util.HashMap<>());

        java.io.File tmp = java.io.File.createTempFile("cache-test", ".dat");
        tmp.deleteOnExit();

        cache.exportToFile(tmp.getPath());

        MemoryCache other = new MemoryCache("other");
        other.importFromFile(tmp.getPath());

        assertEquals("v1", other.get("k1"));
        assertEquals(42, other.get("k2"));
        assertNotNull(other.get("k3"));
        assertEquals(cache.size(), other.size());
    }

    @Test(expected = CacheException.class)
    /**
     * testImportFromFileNotFound方法。
     */
    public void testImportFromFileNotFound() {
        cache.importFromFile("/tmp/this_file_definitely_does_not_exist_12345.cache");
    }
}