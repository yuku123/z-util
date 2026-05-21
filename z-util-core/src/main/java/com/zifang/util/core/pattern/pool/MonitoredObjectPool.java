package com.zifang.util.core.pattern.pool;

import com.zifang.util.core.pattern.pool.monitor.PoolListener;
import com.zifang.util.core.pattern.pool.monitor.PoolMonitor;
import com.zifang.util.core.pattern.pool.monitor.PoolStats;

import java.util.NoSuchElementException;

/**
 * 带监控的对象池包装器
 * <p>
 * 包装现有的ObjectPool实现，添加监控能力
 *
 * @param <T> 对象类型
 */
public class MonitoredObjectPool<T> implements ObjectPool<T> {

    private final ObjectPool<T> pool;
    private final PoolMonitor<T> monitor;

    public MonitoredObjectPool(ObjectPool<T> pool, PoolConfig config, PoolListener<T> listener) {
        this.pool = pool;
        this.monitor = new PoolMonitor<>(listener);
    }

    public MonitoredObjectPool(PooledObjectFactory<T> factory, PoolConfig config, PoolListener<T> listener) {
        this.pool = new StackObjectPool<>(factory, config);
        this.monitor = new PoolMonitor<>(listener);
    }

    public PoolMonitor<T> getMonitor() {
        return monitor;
    }

    public PoolStats getStats() {
        return monitor.getStats();
    }

    @Override
    public T borrowObject() throws Exception {
        long start = System.currentTimeMillis();
        try {
            T obj = pool.borrowObject();
            monitor.recordBorrow(obj, System.currentTimeMillis() - start);
            return obj;
        } catch (NoSuchElementException e) {
            monitor.recordBorrow(null, System.currentTimeMillis() - start);
            throw e;
        }
    }

    @Override
    public void returnObject(T obj) throws Exception {
        long start = System.currentTimeMillis();
        try {
            pool.returnObject(obj);
            monitor.recordReturn(obj, System.currentTimeMillis() - start);
        } catch (Exception e) {
            monitor.recordReturn(obj, System.currentTimeMillis() - start);
            throw e;
        }
    }

    @Override
    public void invalidateObject(T obj) throws Exception {
        pool.invalidateObject(obj);
    }

    @Override
    public void clear() throws Exception {
        pool.clear();
    }

    @Override
    public void close() {
        try {
            pool.close();
        } finally {
            monitor.recordClose();
        }
    }

    @Override
    public int getNumActive() {
        return pool.getNumActive();
    }

    @Override
    public int getNumIdle() {
        return pool.getNumIdle();
    }

    @Override
    public PoolConfig getConfig() {
        return pool.getConfig();
    }
}