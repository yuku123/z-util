package com.zifang.util.core.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理基本类型与包装类型的工具类
 *
 * @author zifang
 */
/**
 * PrimitiveUtil类。
 */
/**
 * PrimitiveUtil类。
 */
public class PrimitiveUtil {

    private static final Logger log = LoggerFactory.getLogger(PrimitiveUtil.class);

    /**
     * Object>方法。
     * @return static final Map<Class<?>, Object> primitiveTypeDefaults = new LinkedHashMap<Class<?>,类型返回值
     */
    /**
     * Object>方法。
     * @return static final Map<Class<?>, Object> primitiveTypeDefaults = new LinkedHashMap<Class<?>,类型返回值
     */
    public static final Map<Class<?>, Object> primitiveTypeDefaults = new LinkedHashMap<Class<?>, Object>() {
        {
            put(Byte.class, (byte) 0);
            put(Short.class, (short) 0);
            put(Integer.class, 0);
            put(Character.class, '0');
            put(Long.class, 0L);
            put(Float.class, 0F);
            put(Double.class, 0D);
            put(Boolean.class, false);
        }
    };

    /**
     * 原始类型的集合
     */
    private static final List<Class<?>> primitiveTypeList = new ArrayList<Class<?>>() {
        {
            add(byte.class);
            add(char.class);
            add(short.class);
            add(int.class);
            add(long.class);
            add(float.class);
            add(double.class);
            add(boolean.class);
        }
    };

    /**
     * 封装类型的集合
     */
    private static final List<Class<?>> primitiveWrapperTypeList = new ArrayList<Class<?>>() {
        {
            add(Byte.class);
            add(Character.class);
            add(Short.class);
            add(Integer.class);
            add(Long.class);
            add(Float.class);
            add(Double.class);
            add(Boolean.class);
        }
    };

    /**
     * 获得默认基础值
     */
    /**
     * defaultValue方法。
     *      * @param clazz Class?类型参数
     * @return static Object类型返回值
     */
    /**
     * defaultValue方法。
     *      * @param clazz Class?类型参数
     * @return static Object类型返回值
     */
    public static Object defaultValue(Class<?> clazz) {

        if(isPrimitive(clazz)){
            return primitiveTypeDefaults.get(getPrimitiveWrapper(clazz));
        }

        if(isPrimitiveWrapper(clazz)){
            return primitiveTypeDefaults.get(clazz);
        }

        return null;
    }

    /**
     * 判断是否是基本类型
     */
    /**
     * isPrimitive方法。
     *      * @param clazz ClassT类型参数
     * @return static <T> boolean类型返回值
     */
    /**
     * isPrimitive方法。
     *      * @param clazz ClassT类型参数
     * @return static <T> boolean类型返回值
     */
    public static <T> boolean isPrimitive(Class<T> clazz) {
        return primitiveTypeList.contains(clazz);
    }

    /**
     * 判断是否是基本类型的包装类
     */
    /**
     * isPrimitiveWrapper方法。
     *      * @param clazz ClassT类型参数
     * @return static <T> boolean类型返回值
     */
    /**
     * isPrimitiveWrapper方法。
     *      * @param clazz ClassT类型参数
     * @return static <T> boolean类型返回值
     */
    public static <T> boolean isPrimitiveWrapper(Class<T> clazz) {
        return primitiveWrapperTypeList.contains(clazz);
    }

    /**
     * 判断是否为普通类型 -> 既不是基本类型也不是基本类型的包装
     */
    /**
     * isGeneralType方法。
     *      * @param clazz ClassT类型参数
     * @return static <T> boolean类型返回值
     */
    /**
     * isGeneralType方法。
     *      * @param clazz ClassT类型参数
     * @return static <T> boolean类型返回值
     */
    public static <T> boolean isGeneralType(Class<T> clazz) {
        return (!isPrimitive(clazz)) && (!isPrimitiveWrapper(clazz));
    }

    /**
     * 得到包装类对应的基本类型
     */
    /**
     * getPrimitive方法。
     *      * @param clazz Class?类型参数
     * @return static Class<?>类型返回值
     */
    /**
     * getPrimitive方法。
     *      * @param clazz Class?类型参数
     * @return static Class<?>类型返回值
     */
    public static Class<?> getPrimitive(Class<?> clazz) {

        if(isPrimitive(clazz)){
            return clazz;
        }

        if (!isPrimitiveWrapper(clazz)) {
            String error = "the input class" + clazz.getName() + " is not wrapperType";
            log.error(error);
            throw new RuntimeException(error);
        }

        return primitiveTypeList.get(primitiveWrapperTypeList.indexOf(clazz));
    }

    /**
     * 得到基本类型对应的包装类型
     */
    /**
     * getPrimitiveWrapper方法。
     *      * @param clazz Class?类型参数
     * @return static Class<?>类型返回值
     */
    /**
     * getPrimitiveWrapper方法。
     *      * @param clazz Class?类型参数
     * @return static Class<?>类型返回值
     */
    public static Class<?> getPrimitiveWrapper(Class<?> clazz) {

        if(isPrimitiveWrapper(clazz)){
            return clazz;
        }

        if(isPrimitive(clazz)){
            return primitiveWrapperTypeList.get(primitiveTypeList.indexOf(clazz));
        } else {
            return clazz;
        }
    }
}