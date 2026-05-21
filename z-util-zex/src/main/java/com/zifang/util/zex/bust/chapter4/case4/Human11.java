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
class Human11 {
    private String age;

    public void eat() {
        System.out.println("我要吃饭");
        goWc();
    }

    private void goWc() {
        System.out.println("吃完饭就上个厕所");
    }
}

class Coder1 extends Human11 {
    public void wirteCode() {
        System.out.println("愉快地写代码");
    }

    public static void main(String[] args) {
        Coder1 coder = new Coder1();
        coder.eat();// human的行为
        coder.wirteCode();// 张三的行为
    }
}