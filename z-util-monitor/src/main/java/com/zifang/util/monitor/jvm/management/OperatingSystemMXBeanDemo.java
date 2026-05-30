package com.zifang.util.monitor.jvm.management;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;


/**
 * 用于 Java 虚拟机所在的操作系统系统的管理接口。
 * <p>
 * 该MXBean提供查看操作系统信息的方法，包括系统架构、处理器数量、系统负载等。
 *
 * @author zifang
 */
/**
 * OperatingSystemMXBeanDemo类。
 */
public class OperatingSystemMXBeanDemo {


    /**
     * 获取指定JVM所在的操作系统信息。
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
     * @return 远程JVM所在操作系统的OperatingSystemMXBean实例，连接失败返回null
     */
    /**
     * getRemoteOperatingSystemMXBean方法。
     * @return static OperatingSystemMXBean类型返回值
     */
    public static OperatingSystemMXBean getRemoteOperatingSystemMXBean() {
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
            OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.newPlatformMXBeanProxy(mbs,
                    ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
            return operatingSystemMXBean;
        } catch (Exception e) {
        }
        return null;

    }

    /**
     * 获取当前虚拟机所在的操作系统信息。
     *
     * @return 当前JVM所在操作系统的OperatingSystemMXBean实例
     */
    /**
     * getLocalOperatingSystemMXBean方法。
     * @return static OperatingSystemMXBean类型返回值
     */
    public static OperatingSystemMXBean getLocalOperatingSystemMXBean() {
        return ManagementFactory.getOperatingSystemMXBean();
    }

    /**
     * 主方法，演示获取JVM所在操作系统信息。
     *
     * @param args 命令行参数
     */
    /**
     * main方法。
     *      * @param args String[]类型参数
     * @return static void类型返回值
     */
    public static void main(String[] args) {
        OperatingSystemMXBean operatingSystemMXBean = getRemoteOperatingSystemMXBean();
//		OperatingSystemMXBean operatingSystemMXBean = getLocalOperatingSystemMXBean();
        // 返回操作系统的架构。
        System.out.println("Arch:" + operatingSystemMXBean.getArch());
        // 返回 Java 虚拟机可以使用的处理器数目。
        System.out.println("AvailableProcessors:" + operatingSystemMXBean.getAvailableProcessors());
        // 返回操作系统名称。
        System.out.println("Name:" + operatingSystemMXBean.getName());
        // 返回最后一分钟内系统加载平均值。
        System.out.println("SystemLoadAverage:" + operatingSystemMXBean.getSystemLoadAverage());
        // 返回操作系统的版本。
        System.out.println("Version:" + operatingSystemMXBean.getVersion());


    }

}