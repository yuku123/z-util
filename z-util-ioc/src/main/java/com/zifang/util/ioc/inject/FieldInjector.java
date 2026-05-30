package com.zifang.util.ioc.inject;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * 字段级别的依赖注入器。
 * 遍历所有被 {@link Inject} 标记的字段（JSR 330 标准），从容器中查找依赖并写入。
 */
public class FieldInjector {

    private final InjectorContext context;

    public FieldInjector(InjectorContext context) {
        this.context = context;
    }

    /**
     * 向 target 实例注入所有被 {@link Inject} 标记的字段。
     *
     * @param target 待注入的对象实例
     * @throws IllegalStateException 若找不到所需依赖
     */
    public void inject(Object target) {
        Class<?> clazz = target.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                injectField(target, field);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void injectField(Object target, Field field) {
        Class<?> type = field.getType();
        Object value = context.resolve(type);
        if (value == null) {
            throw new IllegalStateException(
                "Cannot resolve dependency for field '" + field.getName() +
                "' of type " + type.getName() + " in " + target.getClass().getName());
        }
        boolean accessible = field.canAccess(target);
        field.setAccessible(true);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to inject field: " + field, e);
        } finally {
            field.setAccessible(accessible);
        }
    }
}