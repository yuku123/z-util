package com.zifang.util.zex.bust.charpter12;

/**
 * Java编程知识整理类。
 * <p>
 * 此类整理了Java编程中的各类知识点。
 * 包含基础语法、并发、IO、设计模式等内容。
 *
 * @author zifang
 * @version 1.0
 */
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTest {

    public volatile static AtomicInteger i = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                while(i++ < 10000){
                    AtomicTest.i.incrementAndGet();
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                while(i++ < 10000){
                    AtomicTest.i.incrementAndGet();
                }
            }
        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println(AtomicTest.i);

    }
}
