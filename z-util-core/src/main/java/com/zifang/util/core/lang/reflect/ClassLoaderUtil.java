package com.zifang.util.core.lang.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * ClassLoaderUtil
 */
public class ClassLoaderUtil {

    private static final Logger log = LoggerFactory.getLogger(ClassLoaderUtil.class);

    public static ClassLoader overrideClassLoader;

    public static ClassLoader getContextClassLoader() {
        return overrideClassLoader != null ? overrideClassLoader : Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载指定的类
     */
    public static Class<?> loadClass(String className) {
        Class<?> theClass = null;
        try {
            theClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error:" + e.getMessage());
            e.printStackTrace();
        }
        return theClass;
    }

    public static List<Class> getLoaderClass(ClassLoader classLoader) throws NoSuchFieldException, IllegalAccessException {
        Class cla = classLoader.getClass();
        while (cla != ClassLoader.class) {
            cla = cla.getSuperclass();
        }
        Field field = cla.getDeclaredField("classes");
        field.setAccessible(true);
        Vector v = (Vector) field.get(classLoader);
        List<Class> result = new ArrayList<>();
        for (Object o : v) {
            result.add((Class) o);
        }
        return result;
    }
}