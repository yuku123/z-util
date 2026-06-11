package com.zifang.util.monitor.jvm.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.ref.ReferenceQueue;
import java.util.Map;

/**
 * 引用类型主测试类。
 * <p>
 * 演示Java中的强引用、软引用、弱引用、虚引用四种引用类型的行为差异，
 * 以及引用队列在引用回收中的作用。
 *
 * @author zifang
 */
public class RefMainThread {

    /**
     * 测试用内部类。
     */
    static class RefTestObj {
        private int id;

    /**
     * getId方法。
     * @return int类型返回值
     */
        public int getId() {
            return id;
        }

    /**
     * setId方法。
     *      * @param id int类型参数
     */
        public void setId(int id) {
            this.id = id;
        }

        @Override
    /**
     * hashCode方法。
     * @return int类型返回值
     */
        public int hashCode() {
            return super.hashCode();
        }

        @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
        public String toString() {
            return super.toString() + "[id=" + this.id + "]";
        }

        @Override
    /**
     * finalize方法。
     */
        protected void finalize() {
            System.out.println("Object [" + this.hashCode() + "][ id=" + this.id + "] come into finalize");
            try {
                super.finalize();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 主方法，演示四种引用类型的回收机制。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建三种不同的引用类型所需对象
        RefTestObj softRef = new RefTestObj();
        RefTestObj weakRef = new RefTestObj();
        RefTestObj phanRef = new RefTestObj();

        softRef.setId(1);
        weakRef.setId(2);
        phanRef.setId(3);

        ReferenceQueue<RefTestObj> softRefQueue = new ReferenceQueue<RefTestObj>();
        ReferenceQueue<RefTestObj> weakRefQueue = new ReferenceQueue<RefTestObj>();
        ReferenceQueue<RefTestObj> phanRefQueue = new ReferenceQueue<RefTestObj>();

        SoftReference<RefTestObj> softRefObj = new SoftReference<RefTestObj>(softRef, softRefQueue);
        WeakReference<RefTestObj> weakRefObj = new WeakReference<RefTestObj>(weakRef, weakRefQueue);
        PhantomReference<RefTestObj> phanRefObj = new PhantomReference<RefTestObj>(phanRef, phanRefQueue);

        // 打印正常情况下三种对象引用
        print(softRefObj);
        print(weakRefObj);
        print(phanRefObj);

        // 将对象清空
        softRef = null;
        weakRef = null;
        phanRef = null;

        // 打印引用队列及 get() 方法所能取到的对象自身
        if (softRefObj != null) {
            System.out.println("Soft Reference Object run get():" + softRefObj.get());
            System.out.println("Check soft queue:" + softRefQueue.poll());
        }

        if (weakRefObj != null) {
            System.out.println("Weak Reference Object run get():" + weakRefObj.get());
            System.out.println("Check weak queue:" + weakRefQueue.poll());
        }

        if (phanRefObj != null) {
            System.out.println("Phantom Reference Object run get():" + phanRefObj.get());
            System.out.println("Check Phantom queue:" + phanRefQueue.poll());
        }

        // 开始执行垃圾回收
        System.gc();
        System.runFinalization();

        // 检查队列，是否已经被加入队列，是否还能取回对象
        if (softRefObj != null) {
            System.out.println("Soft Reference Object run get():" + softRefObj.get());
            System.out.println("Check soft queue:" + softRefQueue.poll());
        }

        if (weakRefObj != null) {
            System.out.println("Weak Reference Object run get():" + weakRefObj.get());
            System.out.println("Check weak queue:" + weakRefQueue.poll());
        }

        if (phanRefObj != null) {
            System.out.println("Phantom Reference Object run get():" + phanRefObj.get());
            System.out.println("Check Phantom queue:" + phanRefQueue.poll());
        }

        // 对于虚引用对象，在经过多次 GC 之后， 才会加入到队列中去
        Reference<? extends RefTestObj> mynewphan = null;
        int refCount = 1;
        while (mynewphan == null) {
            mynewphan = phanRefQueue.poll();
            System.gc();
            System.runFinalization();
            if (mynewphan != null) {
                System.out.println("Check Phantom queue:" + mynewphan);
                System.out.println("Count for " + refCount + " times");
                break;
            }
            refCount++;
        }
    }

    /**
     * 打印引用对象及其所指向对象的信息。
     *
     * @param ref 引用对象
     */
    public static void print(Reference<RefTestObj> ref) {
        RefTestObj obj = ref.get();
        System.out.println("The Reference is " + ref.toString() + " and with object " + obj + " which is "
                + (obj == null ? "null" : "not null"));
    }
}