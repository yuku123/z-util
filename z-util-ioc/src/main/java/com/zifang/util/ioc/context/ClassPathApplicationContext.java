package com.zifang.util.ioc.context;

import com.zifang.util.ioc.annotation.Bean;
import com.zifang.util.ioc.annotation.Component;
import com.zifang.util.ioc.annotation.Configuration;
import com.zifang.util.ioc.core.BeanRegistry;
import com.zifang.util.ioc.core.DefaultBeanRegistry;
import com.zifang.util.ioc.exception.NoSuchBeanException;
import com.zifang.util.ioc.inject.InjectorContext;
import com.zifang.util.ioc.inject.Instantiator;
import com.zifang.util.ioc.lifecycle.LifecycleManager;
import com.zifang.util.ioc.metadata.BeanDefinition;
import com.zifang.util.ioc.metadata.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * IOC 容器上下文。
 * 支持：
 * <ul>
 *   <li>扫描指定包下的所有 {@link Component} 类并注册</li>
 *   <li>注册配置类并扫描其中的 {@link Bean} 方法</li>
 *   <li>延迟创建 + singleton 缓存 + prototype 新建</li>
 *   <li>字段注入（{@link Inject}）+ {@link PostConstruct} + {@link PreDestroy}</li>
 * </ul>
 *
 * <p>使用标准 JSR 330 / JSR 250 注解：
 * <ul>
 *   <li>{@link Named} / {@link Component} → Bean 名称</li>
 *   <li>{@link Singleton} → singleton 作用域</li>
 *   <li>{@link Inject} → 依赖注入</li>
 *   <li>{@link PostConstruct} / {@link PreDestroy} → 生命周期</li>
 * </ul>
 */
public class ClassPathApplicationContext {

    private final String basePackage;
    private final BeanRegistry registry = new DefaultBeanRegistry();
    private final Instantiator instantiator = new Instantiator(registry);
    private final LifecycleManager lifecycleManager = new LifecycleManager(instantiator);

    private InjectorContext injectorContext;
    private volatile boolean initialized = false;

    public ClassPathApplicationContext(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * 触发容器初始化：扫描注册 → 实例化所有 singleton → 字段注入 → PostConstruct
     */
    public void init() {
        if (initialized) return;
        injectorContext = new InjectorContext(registry, instantiator);
        scanAndRegister(basePackage);
        instantiateSingletons();
        initialized = true;
    }

    /**
     * 扫描包，注册所有 @Component 类和 @Configuration 类
     */
    private void scanAndRegister(String pkg) {
        // 简化实现：依赖手动 registerBeanClass
        // 完整包扫描需要 ASM/BCEL，可后续引入 z-util-source 或 reflections
    }

    /**
     * 手动注册一个类。
     * 检测标准注解：
     * <ul>
     *   <li>{@link Named} → Bean 名称</li>
     *   <li>{@link Component} → 自定义简称注解</li>
     *   <li>{@link Singleton} → singleton 作用域</li>
     *   <li>{@link Configuration} → 配置类（扫描 @Bean 方法）</li>
     * </ul>
     */
    public ClassPathApplicationContext registerBeanClass(Class<?> clazz) {
        String name = resolveBeanName(clazz);
        Scope scope = detectScope(clazz);
        boolean isConfig = clazz.isAnnotationPresent(Configuration.class);
        BeanDefinition bd = new BeanDefinition(name, clazz, scope, isConfig, null);
        registry.register(bd);

        // 若是 @Configuration，扫描 @Bean 方法
        if (isConfig) {
            registerConfigBeanMethods(clazz);
        }
        return this;
    }

    private void registerConfigBeanMethods(Class<?> configClass) {
        for (Method m : configClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Bean.class) && !Modifier.isStatic(m.getModifiers())) {
                String beanName = resolveBeanMethodName(m);
                Class<?> returnType = m.getReturnType();
                BeanDefinition bd = new BeanDefinition(beanName, returnType, Scope.DEFAULT, false, m);
                // 注册配置类本身（若尚未注册）
                BeanDefinition configBd = registry.get(configClass);
                if (configBd == null) {
                    registry.register(new BeanDefinition(resolveBeanName(configClass), configClass, Scope.DEFAULT, true, null));
                }
                registry.register(bd);
            }
        }
    }

    /**
     * 解析 Bean 名称，优先级：
     * 1. {@link Named#value()}（JSR 330 标准）
     * 2. {@link Component#value()}（自定义简称注解）
     * 3. 类名首字母小写
     */
    String resolveBeanName(Class<?> clazz) {
        Named named = clazz.getAnnotation(Named.class);
        if (named != null && !named.value().isEmpty()) {
            return named.value();
        }
        Component comp = clazz.getAnnotation(Component.class);
        if (comp != null && !comp.value().isEmpty()) {
            return comp.value();
        }
        return toLowerFirst(clazz.getSimpleName());
    }

    String resolveBeanMethodName(Method m) {
        Named named = m.getAnnotation(Named.class);
        if (named != null && !named.value().isEmpty()) {
            return named.value();
        }
        Bean bean = m.getAnnotation(Bean.class);
        if (bean != null && !bean.value().isEmpty()) {
            return bean.value();
        }
        return m.getName();
    }

    /**
     * 检测作用域，优先级：
     * 1. {@link Singleton} 存在 → singleton
     * 2. {@link Component#scope()} = "prototype" → prototype
     * 3. 默认 → singleton
     */
    Scope detectScope(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Singleton.class)) {
            return Scope.SINGLETON;
        }
        Component comp = clazz.getAnnotation(Component.class);
        if (comp != null && "prototype".equals(comp.scope())) {
            return Scope.PROTOTYPE;
        }
        return Scope.DEFAULT;
    }

    private String toLowerFirst(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * 实例化所有 singleton 并注入
     */
    private void instantiateSingletons() {
        for (BeanDefinition bd : registry.getAll()) {
            if (bd.isSingleton() && bd.getInstance() == null) {
                Object instance = instantiator.instantiate(bd, injectorContext);
                instantiator.injectFields(instance, injectorContext);
                instantiator.invokePostConstruct(instance);
                bd.setInstance(instance);
                bd.setCreationTime(System.currentTimeMillis());
            }
        }
    }

    /**
     * 获取 Bean 实例
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        checkInit();
        BeanDefinition bd = registry.get(type);
        if (bd == null) {
            throw new NoSuchBeanException("No bean of type: " + type.getName());
        }
        return (T) injectorContext.resolve(type);
    }

    public <T> T getBean(String name) {
        checkInit();
        BeanDefinition bd = registry.get(name);
        if (bd == null) {
            throw new NoSuchBeanException("No bean named: " + name);
        }
        return (T) injectorContext.resolve(bd.getBeanClass());
    }

    public boolean containsBean(String name) {
        return registry.contains(name);
    }

    public Collection<BeanDefinition> getAllBeanDefinitions() {
        return registry.getAll();
    }

    /**
     * 关闭容器：调用所有 PreDestroy（逆序）
     */
    public void close() {
        List<Object> singletons = lifecycleManager.getInitializedSingletons(new ArrayList<>(registry.getAll()));
        lifecycleManager.destroySingletons(singletons);
        initialized = false;
    }

    private void checkInit() {
        if (!initialized) {
            throw new IllegalStateException("Container not initialized. Call init() first.");
        }
    }
}