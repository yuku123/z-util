package com.zifang.util.core.pattern.pool;

import com.zifang.util.core.pattern.pool.monitor.PoolListener;
import com.zifang.util.core.pattern.pool.monitor.PoolMonitor;
import com.zifang.util.core.pattern.pool.monitor.PoolStats;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * 对象池监控测试
 */
public class PoolMonitorTest {

    private MonitoredObjectPool<StringBuffer> pool;

    @After
    public void tearDown() {
        if (pool != null) {
            pool.close();
        }
    }

    @Test
    public void testMonitorStats() throws Exception {
        PoolListener<StringBuffer> listener = new PoolListener<StringBuffer>() {
            @Override
            public void onBorrow(StringBuffer object, long waitTime) {
            }

            @Override
            public void onReturn(StringBuffer object, long waitTime) {
            }

            @Override
            public void onCreate(StringBuffer object) {
            }

            @Override
            public void onDestroy(StringBuffer object) {
            }

            @Override
            public void onValidate(StringBuffer object, boolean valid) {
            }

            @Override
            public void onClose() {
            }

            @Override
            public void onEvict(StringBuffer object) {
            }
        };

        pool = PoolUtils.createMonitoredPool(
                StringBuffer::new,
                sb -> {},
                listener
        );

        StringBuffer sb1 = pool.borrowObject();
        assertNotNull(sb1);

        PoolStats stats = pool.getStats();
        assertTrue(stats.getBorrowCount() >= 1);

        pool.returnObject(sb1);

        assertTrue(stats.getReturnCount() >= 1);
    }

    @Test
    public void testMonitorListener() throws Exception {
        AtomicInteger borrowCount = new AtomicInteger(0);
        AtomicInteger returnCount = new AtomicInteger(0);

        PoolListener<StringBuffer> listener = new PoolListener<StringBuffer>() {
            @Override
            public void onBorrow(StringBuffer object, long waitTime) {
                borrowCount.incrementAndGet();
            }

            @Override
            public void onReturn(StringBuffer object, long waitTime) {
                returnCount.incrementAndGet();
            }

            @Override
            public void onCreate(StringBuffer object) {
            }

            @Override
            public void onDestroy(StringBuffer object) {
            }

            @Override
            public void onValidate(StringBuffer object, boolean valid) {
            }

            @Override
            public void onClose() {
            }

            @Override
            public void onEvict(StringBuffer object) {
            }
        };

        pool = PoolUtils.createMonitoredPool(
                StringBuffer::new,
                sb -> {},
                listener
        );

        StringBuffer sb1 = pool.borrowObject();
        assertEquals(1, borrowCount.get());

        pool.returnObject(sb1);
        assertEquals(1, returnCount.get());

        StringBuffer sb2 = pool.borrowObject();
        assertEquals(2, borrowCount.get());
    }

    @Test
    public void testMonitorDisable() throws Exception {
        AtomicInteger count = new AtomicInteger(0);

        PoolListener<StringBuffer> listener = new PoolListener<StringBuffer>() {
            @Override
            public void onBorrow(StringBuffer object, long waitTime) {
                count.incrementAndGet();
            }

            @Override
            public void onReturn(StringBuffer object, long waitTime) {
            }

            @Override
            public void onCreate(StringBuffer object) {
            }

            @Override
            public void onDestroy(StringBuffer object) {
            }

            @Override
            public void onValidate(StringBuffer object, boolean valid) {
            }

            @Override
            public void onClose() {
            }

            @Override
            public void onEvict(StringBuffer object) {
            }
        };

        pool = PoolUtils.createMonitoredPool(
                StringBuffer::new,
                sb -> {},
                listener
        );

        pool.getMonitor().setEnabled(false);

        StringBuffer sb1 = pool.borrowObject();
        assertEquals(0, count.get()); // 监控被禁用，不计数

        pool.getMonitor().setEnabled(true);

        StringBuffer sb2 = pool.borrowObject();
        assertEquals(1, count.get());
    }

    @Test
    public void testMonitorStatsConsumer() throws Exception {
        AtomicInteger updateCount = new AtomicInteger(0);

        pool = PoolUtils.createMonitoredPool(
                StringBuffer::new,
                sb -> {}
        );

        pool.getMonitor().addStatsConsumer(stats -> {
            if (stats.getBorrowCount() > 0) {
                updateCount.incrementAndGet();
            }
        });

        StringBuffer sb1 = pool.borrowObject();
        assertTrue(updateCount.get() > 0);
    }

    @Test
    public void testMonitorReset() throws Exception {
        pool = PoolUtils.createMonitoredPool(
                StringBuffer::new,
                sb -> {}
        );

        StringBuffer sb1 = pool.borrowObject();
        pool.returnObject(sb1);

        PoolStats stats = pool.getStats();
        assertTrue(stats.getBorrowCount() >= 1);

        pool.getMonitor().reset();

        assertEquals(0, stats.getBorrowCount());
        assertEquals(0, stats.getReturnCount());
    }
}