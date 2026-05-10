package com.zifang.util.core.lang.tuples;


import com.zifang.util.core.lang.BeanUtil;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zifang
 */
public class Unit<A> {

    protected A a;

    public Unit(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public Map<String, Object> toMap() {
        try {
            return BeanUtil.beanToMap(this);
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return new LinkedHashMap<>();
    }

    @Override
    public String toString() {
        return "Unit{a=" + a + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit<?> unit = (Unit<?>) o;
        return java.util.Objects.equals(a, unit.a);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(a);
    }
}