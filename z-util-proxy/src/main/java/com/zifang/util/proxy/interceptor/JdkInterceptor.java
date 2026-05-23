package com.zifang.util.proxy.interceptor;

import com.zifang.util.proxy.aspects.Aspect;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * JDK 动态代理切面拦截器
 */
public class JdkInterceptor implements InvocationHandler, Serializable {
    private static final long serialVersionUID = 1L;

    private final Object target;
    private final Aspect aspect;

    public JdkInterceptor(Object target, Aspect aspect) {
        this.target = target;
        this.aspect = aspect;
    }

    public Object getTarget() {
        return this.target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // before 返回 false 时跳过方法执行，直接返回 null
        if (!aspect.before(target, method, args)) {
            return null;
        }

        method.setAccessible(true);
        Object result;
        try {
            // 静态方法没有 target
            Object actualTarget = Modifier.isStatic(method.getModifiers()) ? null : target;
            result = method.invoke(actualTarget, args);
        } catch (InvocationTargetException e) {
            // InvocationTargetException 由 method.invoke 抛出，内含业务异常
            Throwable targetException = e.getTargetException();
            boolean rethrow = !aspect.afterException(target, method, args, targetException);
            // afterException 返回 false：重新抛出原始异常
            // afterException 返回 true：消费异常，抛出包装异常
            if (rethrow) {
                throw targetException;
            }
            throw new RuntimeException(targetException);
        }

        // 正常返回后回调
        aspect.after(target, method, args, result);
        return result;
    }
}
