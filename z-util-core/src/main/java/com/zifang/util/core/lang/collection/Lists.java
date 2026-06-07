package com.zifang.util.core.lang.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author zifang
 */
/**
 * Lists类。
 */
/**
 * Lists类。
 */
public class Lists {

    /**
     * 将多个元素变为List
     */
    /**
     * of方法。
     *      * @param t1 T...类型参数
     * @return static <T> List<T>类型返回值
     */
    /**
     * of方法。
     *      * @param t1 T...类型参数
     * @return static <T> List<T>类型返回值
     */
    public static <T> List<T> of(T... t1) {
        return Arrays.asList(t1);
    }

    /**
     * 传入一个字符串，传入分割符，得到子序列的List
     */
    /**
     * of方法。
     *      * @param content String类型参数
     * @param splitor String类型参数
     * @return static List<String>类型返回值
     */
    /**
     * of方法。
     *      * @param content String类型参数
     * @param splitor String类型参数
     * @return static List<String>类型返回值
     */
    public static List<String> of(String content, String splitor) {
        return Arrays.asList(content.split(splitor));
    }

    /**
     * 将迭代器的数据转换为List
     */
    /**
     * of方法。
     *      * @param iterable IterableE类型参数
     * @return static <E> List<E>类型返回值
     */
    /**
     * of方法。
     *      * @param iterable IterableE类型参数
     * @return static <E> List<E>类型返回值
     */
    public static <E> List<E> of(Iterable<E> iterable) {
        List<E> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    /**
     * 对一个数组进行过滤操作
     */
    /**
     * filter方法。
     *      * @param elements ListE类型参数
     * @param predicate PredicateE类型参数
     * @return static <E> List<E>类型返回值
     */
    /**
     * filter方法。
     *      * @param elements ListE类型参数
     * @param predicate PredicateE类型参数
     * @return static <E> List<E>类型返回值
     */
    public static <E> List<E> filter(List<E> elements, Predicate<E> predicate) {
        return elements.stream().filter(predicate).collect(Collectors.toList());
    }
}
