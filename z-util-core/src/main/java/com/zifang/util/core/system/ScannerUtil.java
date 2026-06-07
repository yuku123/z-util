package com.zifang.util.core.system;

import java.util.Scanner;
import java.util.function.Function;

/**
 * Scanner utility for reading input from standard input.
 *
 * @author zifang
 */
/**
 * ScannerUtil类。
 */
/**
 * ScannerUtil类。
 */
public class ScannerUtil {

    /**
     * Read input from standard input and apply transformation function.
     *
     * @param transformHandler function to transform each input line
     */
    /**
     * scanner方法。
     *      * @param transformHandler FunctionString,类型参数
     * @return static void类型返回值
     */
    /**
     * scanner方法。
     *      * @param transformHandler FunctionString,类型参数
     * @return static void类型返回值
     */
    public static void scanner(Function<String, String> transformHandler) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            System.out.println(transformHandler.apply(sc.next()));
        }
    }

    /**
     * Read a single line from standard input.
     *
     * @return the next line of input, or null if no more input
     */
    /**
     * nextLine方法。
     * @return static String类型返回值
     */
    /**
     * nextLine方法。
     * @return static String类型返回值
     */
    public static String nextLine() {
        Scanner sc = new Scanner(System.in);
        return sc.hasNextLine() ? sc.nextLine() : null;
    }

    /**
     * Read all remaining input as a single string.
     *
     * @return all remaining input
     */
    /**
     * readAll方法。
     * @return static String类型返回值
     */
    /**
     * readAll方法。
     * @return static String类型返回值
     */
    public static String readAll() {
        StringBuilder sb = new StringBuilder();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine()).append("\n");
        }
        return sb.toString();
    }
}