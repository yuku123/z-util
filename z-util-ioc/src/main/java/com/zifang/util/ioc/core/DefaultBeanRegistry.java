package com.zifang.util.ioc.core;

import com.zifang.util.ioc.metadata.BeanDefinition;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于 ConcurrentHashMap 的 Bean 注册表实现。
 */
public class DefaultBeanRegistry implements BeanRegistry {

    private final Map<String, BeanDefinition> registry = new ConcurrentHashMap<>();
    private final Map<Class<?>, String> typeIndex = new ConcurrentHashMap<>();

    @Override
    public void register(BeanDefinition bd) {
        registry.put(bd.getName(), bd);
        typeIndex.put(bd.getBeanClass(), bd.getName());
    }

    @Override
    public BeanDefinition get(String name) {
        return registry.get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> BeanDefinition get(Class<T> type) {
        String name = typeIndex.get(type);
        return name != null ? registry.get(name) : null;
    }

    @Override
    public Collection<BeanDefinition> getAll() {
        return Collections.unmodifiableCollection(registry.values());
    }

    @Override
    public boolean contains(String name) {
        return registry.containsKey(name);
    }
}