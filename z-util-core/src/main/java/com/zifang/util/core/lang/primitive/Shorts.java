package com.zifang.util.core.lang.primitive;

import java.util.Arrays;

/**
 * Short 工具类。
 * <p>
 * 提供 Short 类型的常用操作方法。
 *
 * @author zifang
 */
/**
 * Shorts类。
 */
/**
 * Shorts类。
 */
public class Shorts {

    private Shorts() {
    }

    /**
     * 将短整数数组用指定分隔符连接成字符串。
     */
    /**
     * join方法。
     *      * @param array short[]类型参数
     * @param delimiter String类型参数
     * @return static String类型返回值
     */
    /**
     * join方法。
     *      * @param array short[]类型参数
     * @param delimiter String类型参数
     * @return static String类型返回值
     */
    public static String join(short[] array, String delimiter) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(delimiter);
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

    /**
     * 将多个短整数数组合并为一个。
     */
    /**
     * concat方法。
     *      * @param arrays short[]...类型参数
     * @return static short[]类型返回值
     */
    /**
     * concat方法。
     *      * @param arrays short[]...类型参数
     * @return static short[]类型返回值
     */
    public static short[] concat(short[]... arrays) {
        int totalLength = 0;
        for (short[] arr : arrays) {
            if (arr != null) {
                totalLength += arr.length;
            }
        }
        short[] result = new short[totalLength];
        int index = 0;
        for (short[] arr : arrays) {
            if (arr != null) {
                System.arraycopy(arr, 0, result, index, arr.length);
                index += arr.length;
            }
        }
        return result;
    }

    /**
     * 反转短整数数组。
     */
    /**
     * reverse方法。
     *      * @param array short[]类型参数
     * @return static short[]类型返回值
     */
    /**
     * reverse方法。
     *      * @param array short[]类型参数
     * @return static short[]类型返回值
     */
    public static short[] reverse(short[] array) {
        if (array == null || array.length <= 1) {
            return array != null ? array : new short[0];
        }
        short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[array.length - 1 - i];
        }
        return result;
    }

    /**
     * 将短整数数组转换为字符串。
     */
    /**
     * toString方法。
     *      * @param array short[]类型参数
     * @return static String类型返回值
     */
    /**
     * toString方法。
     *      * @param array short[]类型参数
     * @return static String类型返回值
     */
    public static String toString(short[] array) {
        if (array == null || array.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(array[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 计算短整数数组的和。
     */
    /**
     * sum方法。
     *      * @param array short[]类型参数
     * @return static int类型返回值
     */
    /**
     * sum方法。
     *      * @param array short[]类型参数
     * @return static int类型返回值
     */
    public static int sum(short[] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        int sum = 0;
        for (short s : array) {
            sum += s;
        }
        return sum;
    }

    /**
     * 获取短整数数组中的最小值。
     */
    /**
     * min方法。
     *      * @param array short[]类型参数
     * @return static short类型返回值
     */
    /**
     * min方法。
     *      * @param array short[]类型参数
     * @return static short类型返回值
     */
    public static short min(short[] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        short min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    /**
     * 获取短整数数组中的最大值。
     */
    /**
     * max方法。
     *      * @param array short[]类型参数
     * @return static short类型返回值
     */
    /**
     * max方法。
     *      * @param array short[]类型参数
     * @return static short类型返回值
     */
    public static short max(short[] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        short max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    /**
     * 判断短整数数组是否包含指定值。
     */
    /**
     * contains方法。
     *      * @param array short[]类型参数
     * @param value short类型参数
     * @return static boolean类型返回值
     */
    /**
     * contains方法。
     *      * @param array short[]类型参数
     * @param value short类型参数
     * @return static boolean类型返回值
     */
    public static boolean contains(short[] array, short value) {
        if (array == null || array.length == 0) {
            return false;
        }
        for (short s : array) {
            if (s == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找短整数在数组中首次出现的索引。
     */
    /**
     * indexOf方法。
     *      * @param array short[]类型参数
     * @param value short类型参数
     * @return static int类型返回值
     */
    /**
     * indexOf方法。
     *      * @param array short[]类型参数
     * @param value short类型参数
     * @return static int类型返回值
     */
    public static int indexOf(short[] array, short value) {
        if (array == null || array.length == 0) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 将字符串解析为短整数。
     */
    /**
     * parseShort方法。
     *      * @param str String类型参数
     * @return static Short类型返回值
     */
    /**
     * parseShort方法。
     *      * @param str String类型参数
     * @return static Short类型返回值
     */
    public static Short parseShort(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            return Short.parseShort(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}