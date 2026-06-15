package com.zifang.util.ioc.metadata;

import javax.inject.Named;
import java.lang.annotation.Annotation;

/**
 * Bean 作用域常量，对应 JSR 330 / 250 标准注解。
 * <ul>
 *   <li>singleton = {@link javax.inject.Singleton} 或默认作用域</li>
 *   <li>prototype = 每此请求新建实例（需要容器支持）</li>
 * </ul>
 */
public final class Scope {

    public static final Scope SINGLETON = new Scope();
    public static final Scope PROTOTYPE = new Scope();
    public static final Scope DEFAULT = SINGLETON;
    private Scope() {
    }

    /**
     * 从注解实例解析作用域
     */
    public static Scope fromAnnotation(Annotation annotation) {
        if (annotation instanceof javax.inject.Singleton) {
            return SINGLETON;
        }
        if (annotation instanceof Named) {
            // @Named 不声明 scope，默认 singleton
            return SINGLETON;
        }
        // 其他注解默认 singleton
        return DEFAULT;
    }

    @Override
    public String toString() {
        return this == SINGLETON ? "singleton" : "prototype";
    }
}