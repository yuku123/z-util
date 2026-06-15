package com.zifang.util.ioc.inject;

import com.zifang.util.ioc.core.BeanRegistry;
import com.zifang.util.ioc.metadata.BeanDefinition;

/**
 * 依赖解析上下文，负责根据类型从容器中获取 Bean 实例。
 * 每次解析都可能触发 Bean 的创建（延迟初始化）。
 */
public class InjectorContext {

    private final BeanRegistry registry;
    private final Instantiator instantiator;

    public InjectorContext(BeanRegistry registry, Instantiator instantiator) {
        this.registry = registry;
        this.instantiator = instantiator;
    }

    /**
     * 根据类型解析（优先按类型精确匹配，fallback 到接口/父类匹配）。
     *
     * @param type 所需类型
     * @param <T>  类型参数
     * @return Bean 实例，若找不到返回 null
     */
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        // 精确类型查找
        BeanDefinition bd = registry.get(type);
        if (bd != null) {
            return (T) resolveInstance(bd);
        }
        // 接口/父类匹配
        for (BeanDefinition existing : registry.getAll()) {
            if (type.isAssignableFrom(existing.getBeanClass())) {
                return (T) resolveInstance(existing);
            }
        }
        return null;
    }

    private Object resolveInstance(BeanDefinition bd) {
        if (bd.isSingleton()) {
            if (bd.getInstance() == null) {
                synchronized (bd) {
                    if (bd.getInstance() == null) {
                        Object instance = instantiator.instantiate(bd, this);
                        bd.setInstance(instance);
                        bd.setCreationTime(System.currentTimeMillis());
                    }
                }
            }
            return bd.getInstance();
        } else {
            // prototype：每次创建新实例
            return instantiator.instantiate(bd, this);
        }
    }
}