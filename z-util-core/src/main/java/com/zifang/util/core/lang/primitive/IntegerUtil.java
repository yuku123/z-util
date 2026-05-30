package com.zifang.util.core.lang.primitive;

/**
 * @author: zifang
 * @time: 2021-12-02 12:56:00
 * @description: integer util
 * @version: JDK 1.8
 */
/**
 * IntegerUtil类。
 */
public class IntegerUtil {

    /**
     * parseInteger方法。
     *      * @param object Object类型参数
     * @return static Integer类型返回值
     */
    public static Integer parseInteger(Object object) {
        if (null == object) {
            return null;
        }
        return Integer.parseInt(object.toString());
    }

    /**
     * parseIntegerOrDefault方法。
     *      * @param object Object类型参数
     * @param defaultValue int类型参数
     * @return static Integer类型返回值
     */
    public static Integer parseIntegerOrDefault(Object object, Integer defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        return Integer.parseInt(object.toString());
    }

    /**
     * saturatedCast方法。
     *      * @param value long类型参数
     * @return static int类型返回值
     */
    public static int saturatedCast(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return value < Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) value;
        }
    }

}
