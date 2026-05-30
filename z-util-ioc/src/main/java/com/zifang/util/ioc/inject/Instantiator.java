package com.zifang.util.ioc.inject;

import com.zifang.util.ioc.annotation.Component;
import com.zifang.util.ioc.annotation.Configuration;
import com.zifang.util.ioc.annotation.Bean;
import com.zifang.util.ioc.core.BeanRegistry;
import com.zifang.util.ioc.exception.BeanCreationException;
import com.zifang.util.ioc.metadata.BeanDefinition;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Method;

/**
 * 根据 BeanDefinition 实例化对象。
 * 支持：
 * <ul>
 *   <li>普通类：{@link Component} 标记类 → 构造函数实例化</li>
 *   <li>配置类：{@link Configuration} 标记类 → 直接返回类本身</li>
 *   <li>{@link Bean} 方法 → 调用工厂方法获取实例</li>
 * </ul>
 * 实例化完成后注入字段（singleton 字段注入由容器统一管理）。
 */
public class Instantiator {

    private final BeanRegistry registry;

    public Instantiator(BeanRegistry registry) {
        this.registry = registry;
    }

    /**
     * 创建（或获取 @Bean）Bean 实例。
     * 不负责字段注入，由调用方使用 {@link FieldInjector} 完成。
     */
    public Object instantiate(BeanDefinition bd, InjectorContext context) {
        try {
            if (bd.isConfiguration()) {
                // 配置类本身作为单例 Bean
                return bd.getBeanClass().getDeclaredConstructor().newInstance();
            }
            if (bd.getFactoryMethod() != null) {
                // @Bean 方法：直接实例化配置类（绕过容器 resolve 以避免循环）
                Class<?> configClass = bd.getBeanClass().getDeclaringClass();
                Object configInstance;
                try {
                    configInstance = configClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new BeanCreationException("Cannot instantiate config class: " + configClass.getName(), e);
                }
                injectFields(configInstance, context);
                Method m = bd.getFactoryMethod();
                m.setAccessible(true);
                return m.invoke(configInstance);
            }
            // 普通 @Component 类：默认构造函数
            Object instance = bd.getBeanClass().getDeclaredConstructor().newInstance();
            return instance;
        } catch (Exception e) {
            throw new BeanCreationException("Failed to create bean: " + bd.getName(), e);
        }
    }

    /** 注入实例字段（供 resolveInstance 后调用） */
    public void injectFields(Object instance, InjectorContext context) {
        FieldInjector injector = new FieldInjector(context);
        injector.inject(instance);
    }

    /** 处理 @PostConstruct 回调（JSR 250 标准） */
    public void invokePostConstruct(Object instance) {
        invokeLifecycleCallback(instance, PostConstruct.class);
    }

    /** 处理 @PreDestroy 回调（JSR 250 标准） */
    public void invokePreDestroy(Object instance) {
        invokeLifecycleCallback(instance, PreDestroy.class);
    }

    private void invokeLifecycleCallback(Object instance, Class<? extends java.lang.annotation.Annotation> annoClass) {
        for (Method m : instance.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(annoClass) && m.getParameterCount() == 0) {
                try {
                    m.setAccessible(true);
                    m.invoke(instance);
                } catch (Exception e) {
                    throw new BeanCreationException("Lifecycle callback failed: " + m.getName(), e);
                }
            }
        }
    }
}