package com.zifang.util.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 指标注册表
 * 负责管理所有指标的注册和采集
 */
public class MetricsRegistry {

    private static final MetricsRegistry INSTANCE = new MetricsRegistry();

    public static MetricsRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * 单个指标的元数据和采集器
     */
    public static class MetricEntry {
        private final String name;
        private final MetricsSnapshot.Category category;
        private final Supplier<Object> provider;
        private final String description;
        private final String unit;

        public MetricEntry(String name, MetricsSnapshot.Category category, Supplier<Object> provider,
                          String description, String unit) {
            this.name = name;
            this.category = category;
            this.provider = provider;
            this.description = description;
            this.unit = unit;
        }

        public String getName() {
            return name;
        }

        public MetricsSnapshot.Category getCategory() {
            return category;
        }

        public Supplier<Object> getProvider() {
            return provider;
        }

        public String getDescription() {
            return description;
        }

        public String getUnit() {
            return unit;
        }
    }

    private final Map<String, MetricEntry> entries = new ConcurrentHashMap<>();
    private volatile boolean enabled = false;

    private MetricsRegistry() {
    }

    /**
     * 注册一个指标
     */
    public void register(String name, MetricsSnapshot.Category category, Supplier<Object> provider,
                        String description, String unit) {
        entries.put(name, new MetricEntry(name, category, provider, description, unit));
    }

    /**
     * 注册一个指标（简化版本）
     */
    public void register(String name, MetricsSnapshot.Category category, Supplier<Object> provider) {
        register(name, category, provider, null, null);
    }

    /**
     * 注册一个指标（带描述）
     */
    public void register(String name, MetricsSnapshot.Category category, Supplier<Object> provider, String description) {
        register(name, category, provider, description, null);
    }

    /**
     * 注销一个指标
     */
    public void unregister(String name) {
        entries.remove(name);
    }

    /**
     * 注销所有指标
     */
    public void clear() {
        entries.clear();
    }

    /**
     * 采集所有指标
     */
    public List<MetricsSnapshot> collect() {
        if (!enabled) {
            return Collections.emptyList();
        }

        List<MetricsSnapshot> snapshots = new ArrayList<>();
        for (MetricEntry entry : entries.values()) {
            try {
                Object value = entry.getProvider().get();
                snapshots.add(new MetricsSnapshot(
                        entry.getName(),
                        value,
                        entry.getDescription(),
                        entry.getUnit()
                ));
            } catch (Exception e) {
                snapshots.add(new MetricsSnapshot(entry.getName(), "ERROR: " + e.getMessage()));
            }
        }
        return snapshots;
    }

    /**
     * 按分类采集指标
     */
    public List<MetricsSnapshot> collect(MetricsSnapshot.Category category) {
        if (!enabled) {
            return Collections.emptyList();
        }

        List<MetricsSnapshot> snapshots = new ArrayList<>();
        for (MetricEntry entry : entries.values()) {
            if (entry.getCategory() == category) {
                try {
                    Object value = entry.getProvider().get();
                    snapshots.add(new MetricsSnapshot(
                            entry.getName(),
                            value,
                            entry.getDescription(),
                            entry.getUnit()
                    ));
                } catch (Exception e) {
                    snapshots.add(new MetricsSnapshot(entry.getName(), "ERROR: " + e.getMessage()));
                }
            }
        }
        return snapshots;
    }

    /**
     * 获取所有指标名称
     */
    public List<String> getMetricNames() {
        return new ArrayList<>(entries.keySet());
    }

    /**
     * 获取所有指标名称（按分类）
     */
    public List<String> getMetricNames(MetricsSnapshot.Category category) {
        List<String> names = new ArrayList<>();
        for (MetricEntry entry : entries.values()) {
            if (entry.getCategory() == category) {
                names.add(entry.getName());
            }
        }
        return names;
    }

    /**
     * 启用/禁用监控
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 是否启用
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 获取指标条目
     */
    public MetricEntry getEntry(String name) {
        return entries.get(name);
    }
}
