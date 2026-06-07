package com.zifang.util.core.lang.primitive;

import java.text.DecimalFormat;

/**
 * @author zifang
 */
/**
 * Doubles类。
 */
/**
 * Doubles类。
 */
public class Doubles {

    /**
     * 格式化一个float
     *
     * @param format 要格式化成的格式 such as #.00, #.#
     * @return 格式化后的字符串
     */
    /**
     * formatDouble方法。
     *      * @param f double类型参数
     * @param format String类型参数
     * @return static String类型返回值
     */
    /**
     * formatDouble方法。
     *      * @param f double类型参数
     * @param format String类型参数
     * @return static String类型返回值
     */
    public static String formatDouble(double f, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(f);
    }

}
