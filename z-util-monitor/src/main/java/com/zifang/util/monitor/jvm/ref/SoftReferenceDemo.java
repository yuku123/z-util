package com.zifang.util.monitor.jvm.ref;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

/**
 * 软引用演示类。
 * <p>
 * 软引用在"弱引用"中强度最高。当内存充足时，软引用指向的对象不会被回收；
 * 当内存不足时，JVM会根据情况决定是否回收软引用指向的对象。
 * 适用于实现内存敏感的缓存。
 *
 * @author zifang
 */
public class SoftReferenceDemo {

    /**
     * 测试用内部类。
     */
    static class Bean {
        private String name;
        private int age;

        public Bean(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

    }

    /**
     * 测试软引用的基本用法。
     */
    public static void test1() {
        SoftReference<Bean> softBean = new SoftReference<Bean>(new Bean("name", 10));
        Bean bean = softBean.get();
        System.out.println(bean.getName());// “name:10”

        System.gc();
        System.runFinalization();
        // 软引用所指向的对象会根据内存使用情况来决定是否回收，这里内存还充足，所以不会被回收。
        System.out.println(bean.getName());// “name:10”
    }

    /**
     * 测试大量软引用在内存不足时的回收行为。
     * <p>
     * JVM参数：-Xmx2m -Xms2m
     */
    public static void test2() {
        Reference<Bean>[] referent = new SoftReference[100000];
        for (int i = 0; i < referent.length; i++) {
            referent[i] = new SoftReference<Bean>(new Bean("mybean:" + i, 100));
        }
        System.out.println(referent[100].get());// “null”
    }

    /**
     * 主方法，执行软引用测试。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        test1();
        test2();
    }

}
