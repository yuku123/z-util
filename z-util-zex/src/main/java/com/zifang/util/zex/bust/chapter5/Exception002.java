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
public class Exception002 {

    public static void main(String[] args) {
        int re = bar();
        System.out.println(re);
    }

    private static int bar() {
        try {
            System.out.println("处理---");
            return 5;
        } finally {
            System.out.println("finally");
        }
    }
}
