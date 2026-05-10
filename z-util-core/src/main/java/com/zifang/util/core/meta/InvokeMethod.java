package com.zifang.util.core.meta;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

/**
 * InvokeMethod
 */
public class InvokeMethod {

    /***
     * An instance of the class in which the method is located
     * 方法所在的类的实例
     */
    private Object target;

    private MethodHandle methodHandle;

    private Method method;

    public InvokeMethod() {
    }

    public InvokeMethod(Object target, MethodHandle methodHandle, Method method) {
        this.target = target;
        this.methodHandle = methodHandle;
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public MethodHandle getMethodHandle() {
        return methodHandle;
    }

    public void setMethodHandle(MethodHandle methodHandle) {
        this.methodHandle = methodHandle;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "InvokeMethod{target=" + target + ", methodHandle=" + methodHandle + ", method=" + method + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvokeMethod that = (InvokeMethod) o;
        return java.util.Objects.equals(target, that.target) &&
                java.util.Objects.equals(methodHandle, that.methodHandle) &&
                java.util.Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(target, methodHandle, method);
    }
}