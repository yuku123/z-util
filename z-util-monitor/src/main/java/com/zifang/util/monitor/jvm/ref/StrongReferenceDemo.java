package com.zifang.util.monitor.jvm.ref;

/**
 * 强引用演示类。
 * <p>
 * 强引用是Java中最常见的引用类型，只要强引用存在，
 * JVM就不会对被引用的对象进行垃圾回收，即使内存不足也不会回收。
 *
 * @author lijing
 * @since 2015/11/24
 */
public class StrongReferenceDemo {

    /**
     * 主方法，演示强引用的特性。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
