package com.zifang.util.zex.bust.chapter4.case4;

/**
 * Java编程知识整理类。
 * <p>
 * 此类整理了Java编程中的各类知识点。
 * 包含基础语法、并发、IO、设计模式等内容。
 *
 * @author zifang
 * @version 1.0
 */
class Human7 {
    void handle(int... d) {
        for (int dd : d) {
            System.out.println(dd);
        }
    }

    public static void main(String[] args) {
        Human7 human = new Human7();
        human.handle(1, 2, 3, 4);
    }
}
