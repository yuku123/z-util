package com.zifang.util.monitor.jvm.management;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;


/**
 * 用于 Java 虚拟机的内存信息接口。
 * <p>
 * 该MXBean提供查看堆内存和非堆内存使用情况的方法。
 *
 * @author zifang
 */
public class MemoryMXBeanDemo {


    /**
     * 获取指定JVM的内存信息。
     * <p>
     * 远程程序启动JVM参数配置开启远程监控：
     * <ul>
     *   <li>-Djava.rmi.server.hostname=192.168.10.105</li>
     *   <li>-Dcom.sun.management.jmxremote</li>
     *   <li>-Dcom.sun.management.jmxremote.port=9999</li>
     *   <li>-Dcom.sun.management.jmxremote.ssl=false</li>
     *   <li>-Dcom.sun.management.jmxremote.authenticate=false</li>
     * </ul>
     *
     * @return 远程JVM的MemoryMXBean实例，连接失败返回null
     */
    public static MemoryMXBean getRemoteMemoryMXBean() {
        String jmxURL = "service:jmx:rmi:///jndi/rmi://192.168.10.98:9999/jmxrmi";
        MBeanServerConnection mbs = null;
        try {
            JMXServiceURL address = new JMXServiceURL(jmxURL);
            JMXConnector connector = JMXConnectorFactory.connect(address);
            // 获取MBeanServerConnection
            mbs = connector.getMBeanServerConnection();
        } catch (IOException e) {
            System.out.println("jmx.url 连接异常！" + e.getMessage());
        }

        try {
            MemoryMXBean memoryMXBean = ManagementFactory.newPlatformMXBeanProxy(mbs,
                    ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
            return memoryMXBean;
        } catch (Exception e) {
        }
        return null;

    }

    /**
     * 获取当前虚拟机内存使用信息。
     *
     * @return 当前JVM的MemoryMXBean实例
     */
    public static MemoryMXBean getLocalMemoryMXBean() {
        return ManagementFactory.getMemoryMXBean();
    }

    /**
     * 打印内存使用情况。
     *
     * @param memoryUsage 内存使用情况对象
     */
    private static void printMemoryUsage(MemoryUsage memoryUsage) {
        if (memoryUsage != null) {
            // 返回已提交给 Java 虚拟机使用的内存量（以字节为单位）。
            System.out.println("Committed:" + memoryUsage.getCommitted());
            //返回 Java 虚拟机最初从操作系统请求用于内存管理的内存量（以字节为单位）。
            System.out.println("Init:" + memoryUsage.getInit());
            //  返回可以用于内存管理的最大内存量（以字节为单位）。
            System.out.println("Max:" + memoryUsage.getMax());
            //  返回已使用的内存量（以字节为单位）。
            System.out.println("Used:" + memoryUsage.getUsed());
        }
    }

    /**
     * 主方法，演示获取JVM内存信息。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        MemoryMXBean memoryMXBean = getRemoteMemoryMXBean();
//		MemoryMXBean memoryMXBean = getLocalMemoryMXBean();
        //返回用于对象分配的堆的当前内存使用量。
        System.out.println("---------------Heap Memory Memory Usage---------------");
        printMemoryUsage(memoryMXBean.getHeapMemoryUsage());
        //返回 Java 虚拟机使用的非堆内存的当前内存使用量。
        System.out.println("---------------NonHeap Memory Memory Usage---------------");
        printMemoryUsage(memoryMXBean.getNonHeapMemoryUsage());
        //返回其终止被挂起的对象的近似数目。
        System.out.println(memoryMXBean.getObjectPendingFinalizationCount());
        //测试内存系统的 verbose 输出是否已启用
        System.out.println(memoryMXBean.isVerbose());

    }

}