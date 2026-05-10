package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.PrimitiveUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 执行转换的句柄包装
 */
public class ConvertCaller<F, T> implements IConverter<F,T>{

    private Method method;
    private Object caller;

    private Class<?> from;
    private Class<?> target;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getCaller() {
        return caller;
    }

    public void setCaller(Object caller) {
        this.caller = caller;
    }

    public Class<?> getFrom() {
        return from;
    }

    public void setFrom(Class<?> from) {
        this.from = from;
    }

    public Class<?> getTarget() {
        return target;
    }

    public void setTarget(Class<?> target) {
        this.target = target;
    }

    public T to(F o) {
        Object defaultValue = null;
        try {
            if(PrimitiveUtil.isGeneralType(target)){
                defaultValue = target.newInstance();
            } else {
                defaultValue = PrimitiveUtil.defaultValue(target);
            }
            method.setAccessible(true);
            return (T) method.invoke(caller, o, defaultValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public T to(Object o, Object defaultValue) {
        if (from == target) {
            return (T) o;
        }
        try {
            return (T) method.invoke(caller, o, defaultValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ConvertCaller<F, T> copy() {
        ConvertCaller<F, T> convertCaller = new ConvertCaller<>();
        convertCaller.setMethod(method);
        convertCaller.setCaller(caller);
        convertCaller.setFrom(from);
        convertCaller.setTarget(target);
        return convertCaller;
    }

    @Override
    public String toString() {
        return "ConvertCaller{method=" + method + ", caller=" + caller +
                ", from=" + from + ", target=" + target + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConvertCaller<?, ?> that = (ConvertCaller<?, ?>) o;
        return java.util.Objects.equals(method, that.method) &&
                java.util.Objects.equals(caller, that.caller) &&
                java.util.Objects.equals(from, that.from) &&
                java.util.Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(method, caller, from, target);
    }
}