package com.zifang.util.core.lang;

import com.zifang.util.core.lang.primitive.ByteUtil;
import com.zifang.util.core.lang.primitive.IntegerUtil;
import com.zifang.util.core.lang.primitive.LongUtil;
import com.zifang.util.core.lang.primitive.ShortUtil;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.zifang.util.core.lang.MapUtil.MAX_POWER_OF_TWO;

/**
 * @author: zifang
 * @time: 2021-10-25 18:59:00
 * @description: collection util
 * @version: JDK 1.8
 */
public class CollectionUtil {

    /**
     * isEmpty方法。
     *      * @param collection CollectionT类型参数
     * @return static <T> boolean类型返回值
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * isNotEmpty方法。
     *      * @param collection CollectionT类型参数
     * @return static <T> boolean类型返回值
     */
    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    /**
     * newHashSet方法。
     *      * @param expectedSize int类型参数
     * @return static <E> HashSet<E>类型返回值
     */
    public static <E> HashSet<E> newHashSet(int expectedSize) {
        return new HashSet<E>(MapUtil.capacity(expectedSize));
    }

    /**
     * newHashSet方法。
     * @return static <E> HashSet<E>类型返回值
     */
    public static <E> HashSet<E> newHashSet() {
        return newHashSet(16);
    }

    /**
     * containsInstance方法。
     *      * @param collection CollectionT类型参数
     * @param element Object类型参数
     * @return static <T> boolean类型返回值
     */
    public static <T> boolean containsInstance(Collection<T> collection, Object element) {
        if (collection != null) {
            for (Object candidate : collection) {
                if (candidate == element) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @author: zifang
     * @description: compare two collection
     * @time: 2021/12/18 16:46
     * @params: [source, target] request
     * @return: boolean response
     */
    public static <T> boolean equals(Collection<T> source, Collection<T> target) {
        return equals(source, target, Collection::hashCode);
    }

    /**
     * @author: zifang
     * @description: compare two collection use way of compute hashCode
     * @time: 2021/12/18 16:46
     * @params: [source, target, callback] request
     * @return: boolean response
     */
    public static <T> boolean equals(Collection<T> source, Collection<T> target,
                                     HashCallback<T> callback) {
        if (isEmpty(source) && isNotEmpty(target)) {
            return false;
        }
        if (isNotEmpty(source) && isEmpty(target)) {
            return false;
        }
        if (null == source && null == target) {
            return true;
        }
        if (source.size() != target.size()) {
            return false;
        }

        return callback.computeHashCode(source) == callback.computeHashCode(target);
    }

    /**
     * containsAny方法。
     *      * @param source CollectionT类型参数
     * @param candidates CollectionT类型参数
     * @return static <T> boolean类型返回值
     */
    public static <T> boolean containsAny(Collection<T> source, Collection<T> candidates) {
        return findFirstMatch(source, candidates) != null;
    }

    /**
     * random方法。
     *      * @param list ListT类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T random(List<T> list) {
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        int index = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(index);
    }

    /**
     * newArrayList方法。
     *      * @param elements E...类型参数
     * @return static <E> ArrayList<E>类型返回值
     */
    public static <E> ArrayList<E> newArrayList(E... elements) {
        if (null == elements) {
            throw new NullPointerException();
        }
        int arraySize = elements.length;
        ArrayList<E> list = new ArrayList<>(suitableCapacity(arraySize));
        Collections.addAll(list, elements);
        return list;
    }

    /**
     * newListArray方法。
     *      * @param elements E[]类型参数
     * @return static <E> ArrayList<E>类型返回值
     */
    public static <E> ArrayList<E> newListArray(E[] elements) {
        if (null == elements) {
            throw new NullPointerException();
        }
        return newArrayList(elements);
    }

    /**
     * suitableCapacity方法。
     *      * @param arraySize int类型参数
     * @return static int类型返回值
     */
    public static int suitableCapacity(int arraySize) {
        return IntegerUtil.saturatedCast(5 + arraySize + arraySize / 10);
    }


    /**
     * findFirstMatch方法。
     *      * @param source CollectionSOURCE类型参数
     * @param candidates CollectionE类型参数
     * @return static <SOURCE, E> E类型返回值
     */
    public static <SOURCE, E> E findFirstMatch(Collection<SOURCE> source, Collection<E> candidates) {
        if (isEmpty(source) || isEmpty(candidates)) {
            return null;
        }
        for (Object candidate : candidates) {
            if (source.contains(candidate)) {
                return (E) candidate;
            }
        }
        return null;
    }


    /**
     * findValueOfType方法。
     *      * @param collection CollectionT类型参数
     * @param type ClassT类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T findValueOfType(Collection<T> collection, Class<T> type) {
        if (isEmpty(collection)) {
            return null;
        }
        T value = null;
        for (Object element : collection) {
            if (type == null || type.isInstance(element)) {
                if (value != null) {
                    // More than one value found... no clear single value.
                    return null;
                }
                value = (T) element;
            }
        }
        return value;
    }


    /**
     * hasUniqueObject方法。
     *      * @param collection CollectionT类型参数
     * @return static <T> boolean类型返回值
     */
    public static <T> boolean hasUniqueObject(Collection<T> collection) {
        if (isEmpty(collection)) {
            return false;
        }
        boolean hasCandidate = false;
        Object candidate = null;
        for (Object elem : collection) {
            if (!hasCandidate) {
                hasCandidate = true;
                candidate = elem;
            } else if (candidate != elem) {
                return false;
            }
        }
        return true;
    }

    /**
     * findCommonElementType方法。
     *      * @param collection CollectionT类型参数
     * @return static <T> Class<?>类型返回值
     */
    public static <T> Class<?> findCommonElementType(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        Class<?> candidate = null;
        for (Object val : collection) {
            if (val != null) {
                if (candidate == null) {
                    candidate = val.getClass();
                } else if (candidate != val.getClass()) {
                    return null;
                }
            }
        }
        return candidate;
    }


    /**
     * firstElement方法。
     *      * @param set SetT类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T firstElement(Set<T> set) {
        if (isEmpty(set)) {
            return null;
        }
        if (set instanceof SortedSet) {
            return ((SortedSet<T>) set).first();
        }

        Iterator<T> it = set.iterator();
        T first = null;
        if (it.hasNext()) {
            first = it.next();
        }
        return first;
    }


    /**
     * firstElement方法。
     *      * @param list ListT类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T firstElement(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }


    /**
     * lastElement方法。
     *      * @param set SetT类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T lastElement(Set<T> set) {
        if (isEmpty(set)) {
            return null;
        }
        if (set instanceof SortedSet) {
            return ((SortedSet<T>) set).last();
        }

        // Full iteration necessary...
        Iterator<T> it = set.iterator();
        T last = null;
        while (it.hasNext()) {
            last = it.next();
        }
        return last;
    }


    /**
     * lastElement方法。
     *      * @param list ListT类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T lastElement(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    /**
     * toArray方法。
     *      * @param enumeration EnumerationE类型参数
     * @param array A[]类型参数
     * @return static <A, E extends A> A[]类型返回值
     */
    public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
        ArrayList<A> elements = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            elements.add(enumeration.nextElement());
        }
        return elements.toArray(array);
    }

    /**
     * toIterator方法。
     *      * @param enumeration EnumerationE类型参数
     * @return static <E> Iterator<E>类型返回值
     */
    public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
        return (enumeration != null ? new EnumerationIterator<>(enumeration)
                : Collections.emptyIterator());
    }

    @FunctionalInterface
/**
 * HashCallback接口。
 */
    public interface HashCallback<T> {

        int computeHashCode(Collection<T> collection);

    }

    private static class EnumerationIterator<E> implements Iterator<E> {

        private final Enumeration<E> enumeration;

    /**
     * EnumerationIterator方法。
     *      * @param enumeration EnumerationE类型参数
     */
        public EnumerationIterator(Enumeration<E> enumeration) {
            this.enumeration = enumeration;
        }

        @Override
    /**
     * hasNext方法。
     * @return boolean类型返回值
     */
        public boolean hasNext() {
            return this.enumeration.hasMoreElements();
        }

        @Override
    /**
     * next方法。
     * @return E类型返回值
     */
        public E next() {
            return this.enumeration.nextElement();
        }

        @Override
    /**
     * remove方法。
     */
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported");
        }
    }


    /**
     * mergePropertiesIntoMap方法。
     *      * @param props Properties类型参数
     * @param map MapK,类型参数
     * @return static <K, V> void类型返回值
     */
    public static <K, V> void mergePropertiesIntoMap(Properties props, Map<K, V> map) {
        if (props != null) {
            for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
                String key = (String) en.nextElement();
                Object value = props.get(key);
                if (value == null) {
                    // Allow for defaults fallback or potentially overridden accessor...
                    value = props.getProperty(key);
                }
                map.put((K) key, (V) value);
            }
        }
    }

    /**
     * newHashMap方法。
     *      * @param expectedSize int类型参数
     * @return static <K, V> HashMap<K, V>类型返回值
     */
    public static <K, V> HashMap<K, V> newHashMap(int expectedSize) {
        return new HashMap<>(capacity(expectedSize));
    }

    /**
     * capacity方法。
     *      * @param expectedSize int类型参数
     * @return static int类型返回值
     */
    protected static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            checkNonNegative(expectedSize, "expectedSize");
            return expectedSize + 1;
        }
        if (expectedSize < MAX_POWER_OF_TWO) {
            // This is the calculation used in JDK8 to resize when a putAll
            // happens; it seems to be the most conservative calculation we
            // can make.  0.75 is the default load factor.
            return (int) ((float) expectedSize / 0.75F + 1.0F);
        }
        return Integer.MAX_VALUE;
    }

    private static int checkNonNegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
        }
        return value;
    }

    /**
     * isEmpty方法。
     *      * @param map MapK,类型参数
     * @return static <K, V> boolean类型返回值
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * isNotEmpty方法。
     *      * @param map MapK,类型参数
     * @return static <K, V> boolean类型返回值
     */
    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    /**
     * parseValue方法。
     *      * @param map MapK,类型参数
     * @param key K类型参数
     * @return static <K, V> V类型返回值
     */
    public static <K, V> V parseValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return map.get(key);
    }

    /**
     * parseStringValue方法。
     *      * @param map MapK,类型参数
     * @param key K类型参数
     * @return static <K, V> String类型返回值
     */
    public static <K, V> String parseStringValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return StringUtil.parseString(map.get(key));
    }

    /**
     * parseByteValue方法。
     *      * @param map MapK,类型参数
     * @param key K类型参数
     * @return static <K, V> Byte类型返回值
     */
    public static <K, V> Byte parseByteValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return ByteUtil.parseByte(map.get(key));
    }

    /**
     * parseShortValue方法。
     *      * @param map MapK,类型参数
     * @param key K类型参数
     * @return static <K, V> Short类型返回值
     */
    public static <K, V> Short parseShortValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return ShortUtil.parseShort(map.get(key));
    }

    /**
     * parseIntegerValue方法。
     *      * @param map MapK,类型参数
     * @param key K类型参数
     * @return static <K, V> Integer类型返回值
     */
    public static <K, V> Integer parseIntegerValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return IntegerUtil.parseInteger(map.get(key));
    }


    /**
     * parseLongValue方法。
     *      * @param map MapK,类型参数
     * @param key K类型参数
     * @return static <K, V> Long类型返回值
     */
    public static <K, V> Long parseLongValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return LongUtil.parseLong(map.get(key));
    }

    /**
     * parseValueOrDefault方法。
     *      * @param map MapK,类型参数
     * @param key K类型参数
     * @param defaultValue V类型参数
     * @return static <K, V> V类型返回值
     */
    public static <K, V> V parseValueOrDefault(Map<K, V> map, K key, V defaultValue) {
        if (isEmpty(map)) {
            return defaultValue;
        }
        return map.get(key);
    }

}
