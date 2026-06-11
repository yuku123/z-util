package com.zifang.util.monitor.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 多线程演示类。
 * <p>
 * 演示如何使用ThreadMXBean获取JVM中线程的信息，
 * 包括线程ID、线程名称等。
 *
 * @author zifang
 */
public class MultiThread extends Thread {

    /**
     * 主方法，演示获取JVM线程信息。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        //只要线程和线程堆栈的信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);

        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "]" + threadInfo.getThreadName());
        }
    }
}

//[4]Signal Dispatcher      分发处理发送给jvm信号的线程
//[3]Finalizer              调用finalize方法的线程
//[2]Reference Handler      清楚reference 线程
//[1]main                   main线程
