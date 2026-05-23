package com.zifang.util.proxy.interceptor;

import com.zifang.util.proxy.aspects.Aspect;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Cglib实现的动态代理切面
 */
public class CglibInterceptor implements MethodInterceptor, Serializable {
    private static final long serialVersionUID = 1L;

    private final Object target;
    private final Aspect aspect;

    /**
     * 构造
     *
     * @param target 被代理对象
     * @param aspect 切面实现
     */
    public CglibInterceptor(Object target, Aspect aspect) {
        this.target = target;
        this.aspect = aspect;
    }

    public Object getTarget() {
        return this.target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        final Object target = this.target;
        final Aspect aspect = this.aspect;

        // 开始前回调，返回 false 则跳过目标方法，也不调用 after
        if (!aspect.before(target, method, args)) {
            return null;
        }

        Object result = null;
        try {
            result = proxy.invokeSuper(obj, args);
        } catch (InvocationTargetException e) {
            // 异常回调（只捕获业务代码导致的异常，而非反射导致的异常）
            if (aspect.afterException(target, method, args, e.getTargetException())) {
                throw e;
            }
            return null;
        }

        // 结束执行回调
        if (aspect.after(target, method, args, result)) {
            return result;
        }
        return null;
    }
}
