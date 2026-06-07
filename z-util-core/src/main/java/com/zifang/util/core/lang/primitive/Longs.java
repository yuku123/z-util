package com.zifang.util.core.lang.primitive;

/**
 * @author zifang
 */
/**
 * Longs类。
 */
/**
 * Longs类。
 */
public class Longs {

    /**
     * byte数组 转 long
     */

    /**
     * of方法。
     *      * @param bytes byte[]类型参数
     * @return static long类型返回值
     */
    /**
     * of方法。
     *      * @param bytes byte[]类型参数
     * @return static long类型返回值
     */
    public static long of(byte[] bytes) {
        long longa = 0;
        for (int i = 0; i < bytes.length; i++) {
            // 移位和清零
            longa += ((long) (bytes[i] & 0xff) << i * 8);
        }

        return longa;
    }

}
