package com.zifang.util.core.lang.collection;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author zifang
 */
/**
 * Maps类。
 */
/**
 * Maps类。
 */
public class Maps {

    /**
     * 移除Map中key为null的元素
     *
     * @param map 待处理Map，不能为null
     * @throws NullPointerException 如果map为null
     */
    /**
     * removeNullKeys方法。
     *      * @param map MapK,类型参数
     * @return static <K, V> void类型返回值
     */
    /**
     * removeNullKeys方法。
     *      * @param map MapK,类型参数
     * @return static <K, V> void类型返回值
     */
    public static <K, V> void removeNullKeys(Map<K, V> map) {
        removeKeys(map, Objects::isNull);
    }

    /**
     * 移除Map中value为null的元素
     *
     * @param map 待处理Map，不能为null
     * @throws NullPointerException 如果map为null
     */
    /**
     * removeNullValues方法。
     *      * @param map MapK,类型参数
     * @return static <K, V> void类型返回值
     */
    /**
     * removeNullValues方法。
     *      * @param map MapK,类型参数
     * @return static <K, V> void类型返回值
     */
    public static <K, V> void removeNullValues(Map<K, V> map) {
        removeValues(map, Objects::isNull);
    }


    /**
     * 移除Map中符合predicate检测结果的key的元素
     *
     * @param map       待处理Map，不能为null
     * @param predicate 检验条件，用于判断key是否需要被移除
     * @throws NullPointerException 如果map或predicate为null
     */
    /**
     * removeKeys方法。
     *      * @param map MapK,类型参数
     * @param predicate PredicateK类型参数
     * @return static <K, V> void类型返回值
     */
    /**
     * removeKeys方法。
     *      * @param map MapK,类型参数
     * @param predicate PredicateK类型参数
     * @return static <K, V> void类型返回值
     */
    public static <K, V> void removeKeys(Map<K, V> map, Predicate<K> predicate) {
        map.entrySet().removeIf(entry -> predicate.test(entry.getKey()));
    }


    /**
     * 移除Map中符合predicate检测结果的value的元素
     *
     * @param map       待处理Map，不能为null
     * @param predicate 检验条件，用于判断value是否需要被移除
     * @throws NullPointerException 如果map或predicate为null
     */
    /**
     * removeValues方法。
     *      * @param map MapK,类型参数
     * @param predicate PredicateV类型参数
     * @return static <K, V> void类型返回值
     */
    /**
     * removeValues方法。
     *      * @param map MapK,类型参数
     * @param predicate PredicateV类型参数
     * @return static <K, V> void类型返回值
     */
    public static <K, V> void removeValues(Map<K, V> map, Predicate<V> predicate) {
        map.entrySet().removeIf(entry -> predicate.test(entry.getValue()));
    }

    /**
     * 移除Map中符合predicate检测结果的key和value的元素
     *
     * @param map       待处理Map，不能为null
     * @param predicate 检验条件，同时对key和value进行判断
     * @throws NullPointerException 如果map或predicate为null
     */
    /**
     * remove方法。
     *      * @param map MapK,类型参数
     * @param predicate PredicateMap.EntryK,类型参数
     * @return static <K, V> void类型返回值
     */
    /**
     * remove方法。
     *      * @param map MapK,类型参数
     * @param predicate PredicateMap.EntryK,类型参数
     * @return static <K, V> void类型返回值
     */
    public static <K, V> void remove(Map<K, V> map, Predicate<Map.Entry<K, V>> predicate) {
        map.entrySet().removeIf(predicate);
    }

    /**
     * 对给定的Map按照条件进行过滤，返回符合条件的元素组成的新Map
     *
     * @param map       待过滤的Map，不能为null
     * @param predicate 过滤条件，用于判断元素是否应被保留
     * @return Map<K, V> 符合条件的新Map，如果没有任何元素符合条件则返回空Map
     * @throws NullPointerException 如果map或predicate为null
     */
    /**
     * filter方法。
     *      * @param map MapK,类型参数
     * @param predicate PredicateMap.EntryK,类型参数
     * @return static <K, V> Map<K, V>类型返回值
     */
    /**
     * filter方法。
     *      * @param map MapK,类型参数
     * @param predicate PredicateMap.EntryK,类型参数
     * @return static <K, V> Map<K, V>类型返回值
     */
    public static <K, V> Map<K, V> filter(Map<K, V> map, Predicate<Map.Entry<K, V>> predicate) {
        Map<K, V> mapStore = new HashMap<>(map.size());
        remove(mapStore, predicate);
        return mapStore;
    }

    /**
     * 根据Set集合中的每个元素，分裂生成对应的Map对象
     *
     * @param set           待处理的Set集合，不能为null
     * @param acceptAsKey   将Set中每个元素转换为Map的key的函数，不能为null
     * @param acceptAsValue 将Set中每个元素转换为Map的value的函数，不能为null
     * @return Map<K, V> 根据给定规则生成的Map，key由acceptAsKey函数产生，value由acceptAsValue函数产生
     * @throws NullPointerException 如果set、acceptAsKey或acceptAsValue为null
     */
    /**
     * populateMap方法。
     *      * @param set SetU类型参数
     * @param acceptAsKey FunctionU,类型参数
     * @param acceptAsValue FunctionU,类型参数
     * @return static <U, K, V> Map<K, V>类型返回值
     */
    /**
     * populateMap方法。
     *      * @param set SetU类型参数
     * @param acceptAsKey FunctionU,类型参数
     * @param acceptAsValue FunctionU,类型参数
     * @return static <U, K, V> Map<K, V>类型返回值
     */
    public static <U, K, V> Map<K, V> populateMap(Set<U> set, Function<U, K> acceptAsKey, Function<U, V> acceptAsValue) {
        Map<K, V> map = new LinkedHashMap<>();
        for (U u : set) {
            K k = acceptAsKey.apply(u);
            V v = acceptAsValue.apply(u);
            map.put(k, v);
        }
        return map;
    }
}
