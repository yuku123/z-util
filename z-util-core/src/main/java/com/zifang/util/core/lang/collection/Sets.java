package com.zifang.util.core.lang.collection;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * @author zifang
 */
public class Sets {

    /**
     * 新建一个HashSet
     *
     * @param <T> 集合元素类型
     * @param ts  元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(T... ts) {
        return newHashSet(false, ts);
    }

    /**
     * 新建一个HashSet
     *
     * @param <T>      集合元素类型
     * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回 {@link HashSet}
     * @param ts       元素数组
     * @return HashSet对象
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(boolean isSorted, T... ts) {
        if (null == ts) {
            return isSorted ? new LinkedHashSet<>() : new HashSet<>();
        }
        int initialCapacity = Math.max((int) (ts.length / .75f) + 1, 16);
        final HashSet<T> set = isSorted ? new LinkedHashSet<>(initialCapacity) : new HashSet<>(initialCapacity);
        Collections.addAll(set, ts);
        return set;
    }

    /**
     * 计算两个集合的交集
     *
     * @return 交集集合
     */
    public static void intersection() {

    }

    /**
     * 计算两个集合的差集
     *
     * @return 差集集合
     */
    public static void difference() {

    }

    /**
     * 计算两个集合的并集
     *
     * @return 并集集合
     */
    public static void union() {

    }
}
