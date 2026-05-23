package com.zifang.util.proxy.interceptor;

import com.zifang.util.proxy.aspects.Aspect;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Cglib 动态代理切面拦截器
 */
public class CglibInterceptor implements MethodInterceptor, Serializable {
    private static final long serialVersionUID = 1L;

    private final Object target;
    private final Aspect aspect;

    public CglibInterceptor(Object target, Aspect aspect) {
        this.target = target;
        this.aspect = aspect;
    }

    public Object getTarget() {
        return this.target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        // before 返回 false 时跳过方法执行，直接返回 null
        if (!aspect.before(target, method, args)) {
            return null;
        }

        Object result = null;
        boolean methodInvoked = false;
        try {
            method.setAccessible(true);
            result = method.invoke(target, args);
            methodInvoked = true;
        } catch (InvocationTargetException e) {
            // method.invoke 包装了业务异常
            Throwable targetException = e.getTargetException();
            methodInvoked = true;
            if (!aspect.afterException(target, method, args, targetException)) {
                // afterException 返回 false：重新抛出原始异常
                throw targetException;
            }
            // afterException 返回 true：消费异常，抛出包装异常
            throw new RuntimeException(targetException);
        } catch (IllegalArgumentException e) {
            // 非 InvocationTargetException 的反射异常（如静态方法用错 target），直接抛出
            throw e;
        }

        // 正常返回后回调
        aspect.after(target, method, args, result);
        return result;
    }
}
