package com.zifang.util.zex.disrupt;

/**
 * LongEvent类，代表Disruptor中的数据事件。
 * <p>
 * 此类用于在Disruptor框架中封装要传递的数据。
 *
 * @author zifang
 * @version 1.0
 */
/**
 * LongEvent类。
 */
public class LongEvent {
    private long value;

    /**
     * 设置事件的值。
     *
     * @param value 事件值
     */
    /**
     * set方法。
     *      * @param value long类型参数
     */
    public void set(long value) {
        this.value = value;
    }
}