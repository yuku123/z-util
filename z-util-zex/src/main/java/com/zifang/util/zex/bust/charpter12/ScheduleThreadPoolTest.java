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
import java.util.concurrent.*;

public class ScheduleThreadPoolTest {
    private static ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);

    public static void method1() {
        exec.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("1");
            }
        }, 2, TimeUnit.SECONDS);
    }

    public static void method2() {
        ScheduledFuture<String> future = exec.schedule(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Callable";
            }
        }, 4, TimeUnit.SECONDS);
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        method1();
        method2();
    }
}