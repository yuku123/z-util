package com.zifang.util.zex.bust.chapter8;

/**
 * Java编程知识整理类。
 * <p>
 * 此类整理了Java编程中的各类知识点。
 * 包含基础语法、并发、IO、设计模式等内容。
 *
 * @author zifang
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.List;

public class Handle {
    public static void main(String[] args) {
        List<? extends Number> numberList = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
            }
        };
        Number number = numberList.get(0);
        //Integer i = numberList.get(0);
    }
}