package com.zifang.util.lock;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/** 自研分布式锁测试（用 H2 in-memory 测 DB 锁）。 */
public class DistributedLockTest {

    private static org.h2.jdbcx.JdbcDataSource DS;

    @BeforeClass
    public static void setupDb() throws Exception {
        DS = new org.h2.jdbcx.JdbcDataSource();
        DS.setURL("jdbc:h2:mem:locktest;DB_CLOSE_DELAY=-1");
        DS.setUser("sa");
        DS.setPassword("");
        try (java.sql.Connection c = DS.getConnection();
             java.sql.Statement s = c.createStatement()) {
            s.execute("CREATE TABLE distributed_lock (" +
                    "lock_key VARCHAR(128) PRIMARY KEY, " +
                    "token VARCHAR(64) NOT NULL, " +
                    "expire_at BIGINT NOT NULL)");
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (DS != null) {
            try (java.sql.Connection c = DS.getConnection();
                 java.sql.Statement s = c.createStatement()) {
                s.execute("DROP ALL OBJECTS");
            }
        }
    }

    // ===== FileDistributedLock =====

    @Test
    public void testFileLock_mutualExclusion() throws Exception {
        Path tmp = Files.createTempFile("zutil-lock-", ".lock");
        tmp.toFile().deleteOnExit();
        FileDistributedLock lock = new FileDistributedLock(tmp.toString());

        String token1 = lock.tryLock();
        assertNotNull(token1);
        assertNull(lock.tryLock());
        assertTrue(lock.unlock(token1));
        String token2 = lock.tryLock();
        assertNotNull(token2);
        assertTrue(lock.unlock(token2));
    }

    @Test
    public void testFileLock_unlockWrongToken() throws Exception {
        Path tmp = Files.createTempFile("zutil-lock-", ".lock");
        FileDistributedLock lock = new FileDistributedLock(tmp.toString());
        String token = lock.tryLock();
        assertNotNull(token);
        assertFalse(lock.unlock("bogus"));
        assertTrue(lock.unlock(token));
    }

    @Test
    public void testFileLock_concurrent() throws Exception {
        Path tmp = Files.createTempFile("zutil-lock-", ".lock");
        final FileDistributedLock lock = new FileDistributedLock(tmp.toString());
        final AtomicInteger acquired = new AtomicInteger();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    String t = lock.tryLock(200_000_000L);
                    if (t != null) {
                        acquired.incrementAndGet();
                        try { Thread.sleep(2); } catch (InterruptedException ignored) {}
                        lock.unlock(t);
                    }
                }
            });
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
        assertTrue("expected at least 1 acquire, got " + acquired.get(), acquired.get() >= 1);
    }

    // ===== DbDistributedLock =====

    @Test
    public void testDbLock_acquireAndRelease() {
        DbDistributedLock lock = new DbDistributedLock(DS, "order:create", 5000, DbDistributedLock.STANDARD);
        String t1 = lock.tryLock();
        assertNotNull(t1);
        // 立即再抢（不等待）→ 应失败
        String t2 = lock.tryLock();
        assertNull("second acquire without wait should fail", t2);
        // CAS 释放
        assertTrue(lock.unlock(t1));
        // 释放后能再拿
        String t3 = lock.tryLock();
        assertNotNull(t3);
        assertTrue(lock.unlock(t3));
    }

    @Test
    public void testDbLock_wait() throws Exception {
        DbDistributedLock lock = new DbDistributedLock(DS, "order:pay", 200, DbDistributedLock.STANDARD);   // 200ms 过期
        String t1 = lock.tryLock();
        assertNotNull(t1);
        // 等 300ms 让锁自动过期
        Thread.sleep(300);
        // 第二次应能拿到（基于 expire_at 判定过期）
        String t2 = lock.tryLock();
        assertNotNull("after expiration should re-acquire", t2);
        lock.unlock(t2);
    }

    @Test
    public void testDbLock_wrongTokenNotReleased() {
        DbDistributedLock lock = new DbDistributedLock(DS, "any:key", 5000, DbDistributedLock.STANDARD);
        String t1 = lock.tryLock();
        assertNotNull(t1);
        // 别人用错的 token 释放不了
        assertFalse(lock.unlock("not-mine"));
        // 自己还能正常释放
        assertTrue(lock.unlock(t1));
    }
}
