package com.zifang.util.core.pattern.pool.monitor;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 池统计数据实现
 */
public class PoolStatsImpl implements PoolStats {

    private final AtomicLong borrowCount = new AtomicLong(0);
    private final AtomicLong returnCount = new AtomicLong(0);
    private final AtomicLong createCount = new AtomicLong(0);
    private final AtomicLong destroyCount = new AtomicLong(0);
    private final AtomicLong validateCount = new AtomicLong(0);

    private final AtomicLong totalBorrowTime = new AtomicLong(0);
    private final AtomicLong maxBorrowWaitTime = new AtomicLong(0);
    private final AtomicLong totalReturnTime = new AtomicLong(0);
    private final AtomicLong maxReturnWaitTime = new AtomicLong(0);

    private final AtomicLong borrowRateCount = new AtomicLong(0);
    private final AtomicLong returnRateCount = new AtomicLong(0);
    private volatile long lastBorrowRateTime = System.currentTimeMillis();
    private volatile long lastReturnRateTime = System.currentTimeMillis();

    @Override
    public int getActiveCount() {
        return 0; // 由池提供
    }

    @Override
    public int getIdleCount() {
        return 0; // 由池提供
    }

    @Override
    public int getTotalCount() {
        return 0; // 由池提供
    }

    @Override
    public int getWaitingThreads() {
        return 0; // 由池提供
    }

    @Override
    public long getBorrowCount() {
        return borrowCount.get();
    }

    @Override
    public long getReturnCount() {
        return returnCount.get();
    }

    @Override
    public long getCreateCount() {
        return createCount.get();
    }

    @Override
    public long getDestroyCount() {
        return destroyCount.get();
    }

    @Override
    public long getValidateCount() {
        return validateCount.get();
    }

    @Override
    public long getMaxBorrowWaitTime() {
        return maxBorrowWaitTime.get();
    }

    @Override
    public long getAvgBorrowWaitTime() {
        long borrow = borrowCount.get();
        if (borrow == 0) return 0;
        return totalBorrowTime.get() / borrow;
    }

    @Override
    public long getMaxReturnWaitTime() {
        return maxReturnWaitTime.get();
    }

    @Override
    public double getUtilization() {
        return 0; // 由池提供
    }

    @Override
    public double getBorrowRate() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastBorrowRateTime;
        if (elapsed <= 0) return 0;
        long count = borrowRateCount.getAndSet(0);
        lastBorrowRateTime = now;
        return count * 1000.0 / elapsed;
    }

    @Override
    public double getReturnRate() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastReturnRateTime;
        if (elapsed <= 0) return 0;
        long count = returnRateCount.getAndSet(0);
        lastReturnRateTime = now;
        return count * 1000.0 / elapsed;
    }

    // 供池内部调用的方法

    public void recordBorrow(long waitTime) {
        borrowCount.incrementAndGet();
        borrowRateCount.incrementAndGet();
        totalBorrowTime.addAndGet(waitTime);
        updateMax(maxBorrowWaitTime, waitTime);
    }

    public void recordReturn(long waitTime) {
        returnCount.incrementAndGet();
        returnRateCount.incrementAndGet();
        totalReturnTime.addAndGet(waitTime);
        updateMax(maxReturnWaitTime, waitTime);
    }

    public void recordCreate() {
        createCount.incrementAndGet();
    }

    public void recordDestroy() {
        destroyCount.incrementAndGet();
    }

    public void recordValidate() {
        validateCount.incrementAndGet();
    }

    private void updateMax(AtomicLong max, long value) {
        long current;
        do {
            current = max.get();
            if (value <= current) break;
        } while (!max.compareAndSet(current, value));
    }

    public void reset() {
        borrowCount.set(0);
        returnCount.set(0);
        createCount.set(0);
        destroyCount.set(0);
        validateCount.set(0);
        totalBorrowTime.set(0);
        maxBorrowWaitTime.set(0);
        totalReturnTime.set(0);
        maxReturnWaitTime.set(0);
    }
}