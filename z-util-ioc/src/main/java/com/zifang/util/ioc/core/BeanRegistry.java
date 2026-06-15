package com.zifang.util.ioc.core;

import com.zifang.util.ioc.metadata.BeanDefinition;

import java.util.Collection;

/**
 * Bean 注册表，提供 BeanDefinition 的登记与查询。
 */
public interface BeanRegistry {

    /**
     * 注册 Bean 定义（尚未实例化）
     */
    void register(BeanDefinition bd);

    /**
     * 按名称查找 Bean 定义
     */
    BeanDefinition get(String name);

    /**
     * 按类型查找唯一 Bean 定义
     */
    <T> BeanDefinition get(Class<T> type);

    /**
     * 获取所有已注册的 Bean 定义
     */
    Collection<BeanDefinition> getAll();

    /**
     * 是否已注册
     */
    boolean contains(String name);
}