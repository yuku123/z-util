package com.zifang.util.ioc.metadata;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Bean 定义元数据，对应一个待创建 Bean 的完整描述。
 */
public class BeanDefinition {

    private final String name;
    private final Class<?> beanClass;
    private final Scope scope;
    private final boolean isConfiguration;
    private final Method factoryMethod; // 仅 @Bean 场景
    private Object instance; // 已实例化的单例对象
    private long creationTime;

    public BeanDefinition(String name, Class<?> beanClass) {
        this(name, beanClass, Scope.DEFAULT, false, null);
    }

    public BeanDefinition(String name, Class<?> beanClass, Scope scope,
                          boolean isConfiguration, Method factoryMethod) {
        this.name = name;
        this.beanClass = beanClass;
        this.scope = scope != null ? scope : Scope.DEFAULT;
        this.isConfiguration = isConfiguration;
        this.factoryMethod = factoryMethod;
    }

    public String getName() {
        return name;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public Scope getScope() {
        return scope;
    }

    public boolean isSingleton() {
        return scope == Scope.SINGLETON;
    }

    public boolean isPrototype() {
        return scope == Scope.PROTOTYPE;
    }

    public boolean isConfiguration() {
        return isConfiguration;
    }

    public Method getFactoryMethod() {
        return factoryMethod;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanDefinition that = (BeanDefinition) o;
        return name.equals(that.name) && beanClass == that.beanClass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, beanClass);
    }

    @Override
    public String toString() {
        return "BeanDefinition{" + "name='" + name + '\'' + ", beanClass=" + beanClass.getName() + ", scope=" + scope + '}';
    }
}