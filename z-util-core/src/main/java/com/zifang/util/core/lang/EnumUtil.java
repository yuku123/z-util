package com.zifang.util.core.lang;

import java.util.Arrays;
import java.util.Optional;

/**
 * 枚举工具类。
 * <p>
 * 提供枚举相关的常用操作方法。
 *
 * @author zifang
 */
/**
 * EnumUtil类。
 */
public class EnumUtil {

    /**
     * 根据枚举类获取指定名称的枚举实例。
     *
     * @param enumClass 枚举类
     * @param name      枚举名称（不区分大小写）
     * @param <E>       枚举类型
     * @return 对应的枚举实例，不存在则返回 Optional.empty()
     */
    /**
     * getEnumIgnoreCase方法。
     *      * @param enumClass ClassE类型参数
     * @param name String类型参数
     * @return static <E extends Enum<E>> Optional<E>类型返回值
     */
    public static <E extends Enum<E>> Optional<E> getEnumIgnoreCase(Class<E> enumClass, String name) {
        if (enumClass == null || name == null) {
            return Optional.empty();
        }
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * 安全获取枚举值，如果名称不匹配则返回默认值。
     *
     * @param enumClass    枚举类
     * @param name         枚举名称
     * @param defaultValue 默认值
     * @param <E>          枚举类型
     * @return 对应的枚举实例或默认值
     */
    /**
     * getEnumOrDefault方法。
     *      * @param enumClass ClassE类型参数
     * @param name String类型参数
     * @param defaultValue E类型参数
     * @return static <E extends Enum<E>> E类型返回值
     */
    public static <E extends Enum<E>> E getEnumOrDefault(Class<E> enumClass, String name, E defaultValue) {
        return getEnumIgnoreCase(enumClass, name).orElse(defaultValue);
    }

    /**
     * 获取枚举的所有名称。
     *
     * @param enumClass 枚举类
     * @param <E>       枚举类型
     * @return 枚举名称数组
     */
    /**
     * getEnumNames方法。
     *      * @param enumClass ClassE类型参数
     * @return static <E extends Enum<E>> String[]类型返回值
     */
    public static <E extends Enum<E>> String[] getEnumNames(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .toArray(String[]::new);
    }

    /**
     * 获取枚举的所有值。
     *
     * @param enumClass 枚举类
     * @param <E>       枚举类型
     * @return 枚举值数组
     */
    /**
     * getEnumValues方法。
     *      * @param enumClass ClassE类型参数
     * @return static <E extends Enum<E>> E[]类型返回值
     */
    public static <E extends Enum<E>> E[] getEnumValues(Class<E> enumClass) {
        return enumClass.getEnumConstants();
    }
}