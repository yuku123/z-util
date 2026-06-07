package com.zifang.util.cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * CacheManager 单元测试（基于新泛型 API）。
 */
public class CacheManagerTest {

    private CacheManager mgr;

    @Before
    public void setUp() {
        mgr = CacheManager.getInstance();
    }

    @After
    public void tearDown() {
        mgr.clearAll();
    }

    @Test
    public void testGetInstance_singleton() {
        assertSame(CacheManager.getInstance(), CacheManager.getInstance());
    }

    @Test
    public void testRegister_andGet() {
        Cache<String, Integer> c = CacheBuilder.<String, Integer>newBuilder()
                .name("reg-test")
                .maximumSize(10)
                .build();
        mgr.register(c);
        assertSame(c, mgr.get("reg-test"));
    }

    @Test
    public void testRegister_doesNotOverwrite() {
        Cache<String, Integer> c1 = CacheBuilder.<String, Integer>newBuilder().name("a").build();
        Cache<String, Integer> c2 = CacheBuilder.<String, Integer>newBuilder().name("a").build();
        mgr.register(c1);
        // 第二次 register 同名不会覆盖，返回旧的
        assertSame(c1, mgr.register(c2));
        assertSame(c1, mgr.get("a"));
    }

    @Test
    public void testGetOrCreate_lazilyBuilds() {
        Cache<String, Integer> c = mgr.getOrCreate("lazy", name ->
                CacheBuilder.<String, Integer>newBuilder().name(name).maximumSize(5).build());
        assertNotNull(c);
        // 再调一次返回同一实例
        assertSame(c, mgr.getOrCreate("lazy", name ->
                CacheBuilder.<String, Integer>newBuilder().name(name).build()));
    }

    @Test
    public void testContains_andRemove() {
        mgr.register(CacheBuilder.<String, Integer>newBuilder().name("rm").build());
        assertTrue(mgr.contains("rm"));
        assertNotNull(mgr.remove("rm"));
        assertFalse(mgr.contains("rm"));
    }

    @Test
    public void testClearAll_shutsDownAllCaches() {
        Cache<String, Integer> c1 = CacheBuilder.<String, Integer>newBuilder().name("c1").build();
        mgr.register(c1);
        mgr.clearAll();
        assertEquals(0, mgr.size());
    }
}
