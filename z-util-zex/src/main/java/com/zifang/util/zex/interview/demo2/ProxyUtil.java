package com.zifang.util.zex.interview.demo2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 动态代理工具类
 */
public class ProxyUtil {

    @SuppressWarnings("unchecked")
    public static <T> T newProxyInstance(java.lang.reflect.InvocationHandler handler, Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                handler
        );
    }
}
