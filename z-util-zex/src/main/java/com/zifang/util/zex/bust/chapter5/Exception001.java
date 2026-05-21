package com.zifang.util.zex.bust.chapter5;

/**
 * Java编程知识整理类。
 * <p>
 * 此类整理了Java编程中的各类知识点。
 * 包含基础语法、并发、IO、设计模式等内容。
 *
 * @author zifang
 * @version 1.0
 */
public class Exception001 {
    public static void main(String[] args) {
        f1();
    }


    public static void f1() {
        int num1 = 1;
        int num2 = 0;
        int result = f2(num1, num2);
        System.out.println("result:" + result);
    }

    public static int f2(int num1, int num2) {
        return num1 / num2;
    }
}