package com.zifang.util.zex.bust.chapter6;

/**
 * Java编程知识整理类。
 * <p>
 * 此类整理了Java编程中的各类知识点。
 * 包含基础语法、并发、IO、设计模式等内容。
 *
 * @author zifang
 * @version 1.0
 */
import java.util.IdentityHashMap;

public class IdentityHashMapTest {
    static class A{

    }

    public static void main(String[] args) {
        A a = new A();
        IdentityHashMap<A,Integer> integerIdentityHashMap = new IdentityHashMap<>(4);
        integerIdentityHashMap.put(a,1);
        integerIdentityHashMap.put(new A(),1);
        integerIdentityHashMap.put(new A(),2);
        integerIdentityHashMap.put(new A(),3);


    }
}
