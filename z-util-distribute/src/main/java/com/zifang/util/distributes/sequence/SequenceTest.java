package com.zifang.util.distributes.sequence;


import java.util.HashSet;
import java.util.Set;

/**
 * Sequence序列号生成器并发测试类
 * <p>
 * 测试在多线程并发场景下Sequence生成的ID是否唯一。
 * 启动两个线程分别使用不同的workerId生成ID，验证是否存在重复ID。
 *
 * @author zifang
 * @see Sequence
 */
/**
 * SequenceTest类。
 */
public class SequenceTest {

    /**
     * main方法。
     *      * @param args String[]类型参数
     * @return static void类型返回值
     */
    public static void main(String[] args) {
        Set<Long> set = new HashSet<Long>();
        final Sequence idWorker1 = new Sequence(0, 0);
        final Sequence idWorker2 = new Sequence(1, 0);
        Thread t1 = new Thread(new IdWorkThread(set, idWorker1));
        Thread t2 = new Thread(new IdWorkThread(set, idWorker2));
        t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class IdWorkThread implements Runnable {
        private Set<Long> set;
        private Sequence idWorker;

    /**
     * IdWorkThread方法。
     *      * @param set SetLong类型参数
     * @param idWorker Sequence类型参数
     */
        public IdWorkThread(Set<Long> set, Sequence idWorker) {
            this.set = set;
            this.idWorker = idWorker;
        }

        @Override
    /**
     * run方法。
     */
        public void run() {
            while (true) {
                long id = idWorker.nextId();
                if (!set.add(id)) {
                    System.out.println("duplicate:" + id);
                }
            }
        }
    }
}
