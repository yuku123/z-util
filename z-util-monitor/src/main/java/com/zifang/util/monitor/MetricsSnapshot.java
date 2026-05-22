package com.zifang.util.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 指标快照数据模型
 * 用于存储单个时间点的指标数据
 */
public class MetricsSnapshot {

    private final long timestamp;
    private final String name;
    private final Object value;
    private final String description;
    private final String unit;
    private final Category category;

    public MetricsSnapshot(String name, Object value) {
        this(name, value, null, null, null);
    }

    public MetricsSnapshot(String name, Object value, String description, String unit) {
        this(name, value, description, unit, null);
    }

    public MetricsSnapshot(String name, Object value, String description, String unit, Category category) {
        this.timestamp = System.currentTimeMillis();
        this.name = name;
        this.value = value;
        this.description = description;
        this.unit = unit;
        this.category = category;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getUnit() {
        return unit;
    }

    public MetricsSnapshot.Category getCategory() {
        return category;
    }

    /**
     * 指标分类
     */
    public enum Category {
        JVM("JVM 指标"),
        THREAD("线程指标"),
        OS("操作系统指标"),
        CUSTOM("自定义指标");

        private final String label;

        Category(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
