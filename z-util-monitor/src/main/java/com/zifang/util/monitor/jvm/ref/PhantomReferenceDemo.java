package com.zifang.util.monitor.jvm.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 虚引用演示类。
 * <p>
 * 虚引用是所有"弱引用"中最弱的引用类型。无法通过get()方法取得目标对象的强引用。
 * 虚引用主要用来跟踪对象被垃圾回收的状态，当对象被回收前，虚引用会被放入ReferenceQueue中。
 * 虚引用不会自动回收对象，需要客户端自行处理以防止内存溢出。
 *
 * @author zifang
 */
public class PhantomReferenceDemo {

    /**
     * 测试虚引用的基本用法。
     * <p>
     * 虚引用无法通过get()获取目标对象，对象被回收后会被放入引用队列。
     *
     * @throws InterruptedException 如果等待过程中被中断
     */
    public static void test1() throws InterruptedException {
        ReferenceQueue<String> refQueue = new ReferenceQueue<String>();
        PhantomReference<String> referent = new PhantomReference<String>("T", refQueue);
        System.out.println(referent.get());// null

        // 只有被垃圾回收后才把referent放进ReferenceQueue中
        System.out.println(refQueue.poll() == referent); // false
        System.gc();
        System.runFinalization();
        System.out.println(refQueue.poll() == referent); // true
    }

    /**
     * 测试虚引用在内存不足时的行为。
     * <p>
     * JVM参数：-Xmx2m -Xms2m
     * <p>
     * 注意：虚引用不会自动回收对象，当内存不足时会抛出OutOfMemoryError。
     * 此方法已被注释掉，因为直接运行会导致内存溢出。
     */
    public static void test2() {
//		Reference<Bean>[] referent = new PhantomReference[100000];
//		ReferenceQueue<Bean> queue = new ReferenceQueue<SoftReferenceDemo.Bean>();
//		for (int i = 0; i < referent.length; i++) {
//			referent[i] = new PhantomReference<SoftReferenceDemo.Bean>(new Bean("mybean:" + i, 100), queue);// throw
//																							// Exception
//		}
//
//		System.out.println(referent[100].get());
    }


    /**
     * 主方法，执行虚引用测试。
     *
     * @param args 命令行参数
     * @throws InterruptedException 如果等待过程中被中断
     */
    public static void main(String[] args) throws InterruptedException {
        test1();
        test2();
    }
}
