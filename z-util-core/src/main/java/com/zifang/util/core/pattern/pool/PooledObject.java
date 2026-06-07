package com.zifang.util.core.pattern.pool;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 池化对象包装器
 * <p>
 * 封装被池化的对象及其元数据
 *
 * @param <T> 对象类型
 */
/**
 * PooledObject类。
 */
/**
 * PooledObject类。
 */
public class PooledObject<T> {

    private final T object;
    private final AtomicInteger state;
    private final long createTime;
    private final AtomicLong lastBorrowTime;
    private final AtomicLong lastReturnTime;
    private final AtomicLong borrowCount;
    private final AtomicLong useCount;

    /**
     * PooledObject方法。
     *      * @param object T类型参数
     */
    /**
     * PooledObject方法。
     *      * @param object T类型参数
     */
    public PooledObject(T object) {
        this.object = object;
        this.state = new AtomicInteger(PooledObjectState.IDLE.ordinal());
        this.createTime = System.currentTimeMillis();
        this.lastBorrowTime = new AtomicLong(createTime);
        this.lastReturnTime = new AtomicLong(createTime);
        this.borrowCount = new AtomicLong(0);
        this.useCount = new AtomicLong(0);
    }

    /**
     * 获取原始对象
     */
    /**
     * getObject方法。
     * @return T类型返回值
     */
    /**
     * getObject方法。
     * @return T类型返回值
     */
    public T getObject() {
        return object;
    }

    /**
     * 获取创建时间
     */
    /**
     * getCreateTime方法。
     * @return long类型返回值
     */
    /**
     * getCreateTime方法。
     * @return long类型返回值
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * 获取最后借用时间
     */
    /**
     * getLastBorrowTime方法。
     * @return long类型返回值
     */
    /**
     * getLastBorrowTime方法。
     * @return long类型返回值
     */
    public long getLastBorrowTime() {
        return lastBorrowTime.get();
    }

    /**
     * 获取最后归还时间
     */
    /**
     * getLastReturnTime方法。
     * @return long类型返回值
     */
    /**
     * getLastReturnTime方法。
     * @return long类型返回值
     */
    public long getLastReturnTime() {
        return lastReturnTime.get();
    }

    /**
     * 获取借用次数
     */
    /**
     * getBorrowCount方法。
     * @return long类型返回值
     */
    /**
     * getBorrowCount方法。
     * @return long类型返回值
     */
    public long getBorrowCount() {
        return borrowCount.get();
    }

    /**
     * 获取使用次数
     */
    /**
     * getUseCount方法。
     * @return long类型返回值
     */
    /**
     * getUseCount方法。
     * @return long类型返回值
     */
    public long getUseCount() {
        return useCount.get();
    }

    /**
     * 获取空闲时间（毫秒）
     */
    /**
     * getIdleTime方法。
     * @return long类型返回值
     */
    /**
     * getIdleTime方法。
     * @return long类型返回值
     */
    public long getIdleTime() {
        return System.currentTimeMillis() - lastReturnTime.get();
    }

    /**
     * 获取使用时间（毫秒）
     */
    /**
     * getActiveTime方法。
     * @return long类型返回值
     */
    /**
     * getActiveTime方法。
     * @return long类型返回值
     */
    public long getActiveTime() {
        return System.currentTimeMillis() - lastBorrowTime.get();
    }

    /**
     * 是否空闲
     */
    /**
     * isIdle方法。
     * @return boolean类型返回值
     */
    /**
     * isIdle方法。
     * @return boolean类型返回值
     */
    public boolean isIdle() {
        return state.get() == PooledObjectState.IDLE.ordinal();
    }

    /**
     * 分配（借用）
     */
    /**
     * allocate方法。
     * @return boolean类型返回值
     */
    /**
     * allocate方法。
     * @return boolean类型返回值
     */
    public boolean allocate() {
        return state.compareAndSet(PooledObjectState.IDLE.ordinal(), PooledObjectState.ALLOCATED.ordinal());
    }

    /**
     * 归还
     */
    /**
     * returnObject方法。
     */
    /**
     * returnObject方法。
     */
    public void returnObject() {
        if (state.compareAndSet(PooledObjectState.ALLOCATED.ordinal(), PooledObjectState.IDLE.ordinal())) {
            lastReturnTime.set(System.currentTimeMillis());
            useCount.incrementAndGet();
        }
    }

    /**
     * 标记为废弃
     */
    /**
     * invalidate方法。
     */
    /**
     * invalidate方法。
     */
    public void invalidate() {
        state.set(PooledObjectState.INVALID.ordinal());
    }

    /**
     * 标记为正在验证
     */
    /**
     * startEvictionTest方法。
     * @return boolean类型返回值
     */
    /**
     * startEvictionTest方法。
     * @return boolean类型返回值
     */
    public boolean startEvictionTest() {
        return state.compareAndSet(PooledObjectState.IDLE.ordinal(), PooledObjectState.EVICTION.ordinal());
    }

    /**
     * 结束验证
     */
    /**
     * endEvictionTest方法。
     *      * @param evictable boolean类型参数
     */
    /**
     * endEvictionTest方法。
     *      * @param evictable boolean类型参数
     */
    public void endEvictionTest(boolean evictable) {
        if (evictable) {
            state.set(PooledObjectState.IDLE.ordinal());
        } else {
            state.set(PooledObjectState.INVALID.ordinal());
        }
    }

    /**
     * 获取状态
     */
    /**
     * getState方法。
     * @return PooledObjectState类型返回值
     */
    /**
     * getState方法。
     * @return PooledObjectState类型返回值
     */
    public PooledObjectState getState() {
        return PooledObjectState.values()[state.get()];
    }

    /**
     * 记录借用
     */
    /**
     * recordBorrow方法。
     */
    /**
     * recordBorrow方法。
     */
    public void recordBorrow() {
        lastBorrowTime.set(System.currentTimeMillis());
        borrowCount.incrementAndGet();
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "PooledObject{" +
                "state=" + getState() +
                ", createTime=" + createTime +
                ", borrowCount=" + borrowCount +
                ", useCount=" + useCount +
                '}';
    }
}
