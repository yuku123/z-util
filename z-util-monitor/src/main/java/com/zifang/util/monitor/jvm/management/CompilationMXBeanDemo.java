package com.zifang.util.monitor.jvm.management;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;


/**
 * 用于 Java 虚拟机的编译系统的管理接口。
 * <p>
 * 该MXBean提供查看JIT编译器统计信息的方法。
 *
 * @author zifang
 */
/**
 * CompilationMXBeanDemo类。
 */
/**
 * CompilationMXBeanDemo类。
 */
public class CompilationMXBeanDemo {


    /**
     * 获取指定JVM的编译信息。
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
     * @return 远程JVM的CompilationMXBean实例，连接失败返回null
     */
    /**
     * getRemoteCompilationMXBean方法。
     * @return static CompilationMXBean类型返回值
     */
    /**
     * getRemoteCompilationMXBean方法。
     * @return static CompilationMXBean类型返回值
     */
    public static CompilationMXBean getRemoteCompilationMXBean() {
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
            CompilationMXBean compilationMXBean = ManagementFactory.newPlatformMXBeanProxy(mbs,
                    ManagementFactory.COMPILATION_MXBEAN_NAME, CompilationMXBean.class);
            return compilationMXBean;
        } catch (Exception e) {
        }
        return null;

    }

    /**
     * 获取当前虚拟机编译信息。
     *
     * @return 当前JVM的CompilationMXBean实例
     */
    /**
     * getLocalCompilationMXBean方法。
     * @return static CompilationMXBean类型返回值
     */
    /**
     * getLocalCompilationMXBean方法。
     * @return static CompilationMXBean类型返回值
     */
    public static CompilationMXBean getLocalCompilationMXBean() {
        return ManagementFactory.getCompilationMXBean();
    }

    /**
     * 主方法，演示获取JVM编译信息。
     *
     * @param args 命令行参数
     */
    /**
     * main方法。
     *      * @param args String[]类型参数
     * @return static void类型返回值
     */
    /**
     * main方法。
     *      * @param args String[]类型参数
     * @return static void类型返回值
     */
    public static void main(String[] args) {
        CompilationMXBean compilationMXBean = getRemoteCompilationMXBean();
//		CompilationMXBean compilationMXBean = getLocalCompilationMXBean();
        //返回即时 (JIT) 编译器的名称。
        System.out.println("JIT 编译器:" + compilationMXBean.getName());
        //返回在编译上花费的累积耗费时间的近似值（以毫秒为单位）。
        System.out.println("总编译时间:" + compilationMXBean.getTotalCompilationTime());
        //测试 Java 虚拟机是否支持监视编译时间。
        System.out.println("否支持监视编译时间:" + compilationMXBean.isCompilationTimeMonitoringSupported());

    }
}