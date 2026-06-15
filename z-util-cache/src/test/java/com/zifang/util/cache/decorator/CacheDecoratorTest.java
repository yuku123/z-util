package com.zifang.util.cache.decorator;

import com.zifang.util.cache.Cache;
import com.zifang.util.cache.CacheBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * cache 装饰器测试。
 */
public class CacheDecoratorTest {

    private Cache<String, String> base() {
        return CacheBuilder.<String, String>newBuilder()
                .name("test")
                .maximumSize(100)
                .recordStats()
                .build();
    }

    @Test
    public void testMeteredCache() {
        Cache<String, String> c = new MeteredCache<>(base());
        c.put("a", "1");
        c.get("a");     // hit
        c.get("z");     // miss
        MeteredCache<String, String> m = (MeteredCache<String, String>) c;
        assertEquals(1, m.meter().hitCount());
        assertEquals(1, m.meter().missCount());
        assertEquals(1, m.meter().putCount());
        assertTrue("hitRate should be ~0.5, got " + m.meter().hitRate(), Math.abs(m.meter().hitRate() - 0.5) < 0.01);
    }

    @Test
    public void testBoundedCache_dropsWhenFull() {
        BoundedCache<String, String> bc = new BoundedCache<>(base(), 3);
        bc.put("a", "1");
        bc.put("b", "2");
        bc.put("c", "3");
        bc.put("d", "4");   // 满了
        assertEquals(3, bc.currentSize());
        assertNull(bc.get("d"));
    }

    @Test
    public void testBoundedCache_tryPutReturnsBool() {
        BoundedCache<String, String> bc = new BoundedCache<>(base(), 2);
        assertTrue(bc.tryPut("a", "1"));
        assertTrue(bc.tryPut("b", "2"));
        assertFalse(bc.tryPut("c", "3"));
    }

    @Test
    public void testTransactionalCache_commit() {
        TransactionalCache<String, String> tx = new TransactionalCache<>(base());
        try (TransactionalCache<String, String>.Transaction t = tx.beginTransaction()) {
            t.put("a", "1");
            t.put("b", "2");
            t.remove("x");
            t.commit();
        }
        assertEquals("1", tx.get("a"));
        assertEquals("2", tx.get("b"));
    }

    @Test
    public void testTransactionalCache_rollback() {
        TransactionalCache<String, String> tx = new TransactionalCache<>(base());
        try (TransactionalCache<String, String>.Transaction t = tx.beginTransaction()) {
            t.put("a", "1");
            t.put("b", "2");
            t.rollback();
        }
        assertNull(tx.get("a"));
        assertNull(tx.get("b"));
    }

    @Test
    public void testNullCache_alwaysReturnsNull() {
        Cache<String, String> c = new NullCache<>();
        c.put("a", "1");
        assertNull(c.get("a"));
        assertFalse(c.contains("a"));
        assertEquals(0L, c.size());
        assertEquals("null-cache", c.getName());
    }
}
