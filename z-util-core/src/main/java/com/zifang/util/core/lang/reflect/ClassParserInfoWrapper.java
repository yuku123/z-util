package com.zifang.util.core.lang.reflect;

import java.lang.reflect.Type;

/**
 * ClassParserInfoWrapper
 */
public class ClassParserInfoWrapper {

    private Class<?> clazz;
    private Type type;

    public ClassParserInfoWrapper() {
    }

    public ClassParserInfoWrapper(Class<?> clazz, Type type) {
        this.clazz = clazz;
        this.type = type;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ClassParserInfoWrapper{clazz=" + clazz + ", type=" + type + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassParserInfoWrapper that = (ClassParserInfoWrapper) o;
        return java.util.Objects.equals(clazz, that.clazz) &&
                java.util.Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(clazz, type);
    }
}