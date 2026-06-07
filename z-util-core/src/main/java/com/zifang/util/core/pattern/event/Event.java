package com.zifang.util.core.pattern.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件。
 * <p>
 * 用于事件驱动模式中的事件对象。
 *
 * @author zifang
 */
/**
 * Event类。
 */
/**
 * Event类。
 */
public class Event {

    private final Object source;
    private final long timestamp;
    private final String type;
    private final Map<String, Object> data;

    /**
     * 构造事件。
     *
     * @param source 事件源
     * @param type   事件类型
     */
    /**
     * Event方法。
     *      * @param source Object类型参数
     * @param type String类型参数
     */
    /**
     * Event方法。
     *      * @param source Object类型参数
     * @param type String类型参数
     */
    public Event(Object source, String type) {
        this(source, type, new HashMap<>());
    }

    /**
     * 构造事件。
     *
     * @param source 事件源
     * @param type   事件类型
     * @param data   事件数据
     */
    /**
     * Event方法。
     *      * @param source Object类型参数
     * @param type String类型参数
     * @param data MapString,类型参数
     */
    /**
     * Event方法。
     *      * @param source Object类型参数
     * @param type String类型参数
     * @param data MapString,类型参数
     */
    public Event(Object source, String type, Map<String, Object> data) {
        this.source = source;
        this.timestamp = System.currentTimeMillis();
        this.type = type;
        this.data = data != null ? new HashMap<>(data) : new HashMap<>();
    }

    /**
     * 获取事件源。
     *
     * @return 事件源
     */
    /**
     * getSource方法。
     * @return Object类型返回值
     */
    /**
     * getSource方法。
     * @return Object类型返回值
     */
    public Object getSource() {
        return source;
    }

    /**
     * 获取事件时间戳。
     *
     * @return 时间戳
     */
    /**
     * getTimestamp方法。
     * @return long类型返回值
     */
    /**
     * getTimestamp方法。
     * @return long类型返回值
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 获取事件类型。
     *
     * @return 事件类型
     */
    /**
     * getType方法。
     * @return String类型返回值
     */
    /**
     * getType方法。
     * @return String类型返回值
     */
    public String getType() {
        return type;
    }

    /**
     * 获取事件数据。
     *
     * @return 不可修改的事件数据
     */
    /**
     * getData方法。
     * @return Map<String, Object>类型返回值
     */
    /**
     * getData方法。
     * @return Map<String, Object>类型返回值
     */
    public Map<String, Object> getData() {
        return Collections.unmodifiableMap(data);
    }

    /**
     * 添加数据项。
     *
     * @param key   键
     * @param value 值
     * @return 当前事件
     */
    /**
     * addData方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     * @return Event类型返回值
     */
    /**
     * addData方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     * @return Event类型返回值
     */
    public Event addData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    /**
     * 获取数据项。
     *
     * @param key 键
     * @return 值
     */
    /**
     * getData方法。
     *      * @param key String类型参数
     * @return Object类型返回值
     */
    /**
     * getData方法。
     *      * @param key String类型参数
     * @return Object类型返回值
     */
    public Object getData(String key) {
        return data.get(key);
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
        return "Event{" +
                "source=" + source +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}