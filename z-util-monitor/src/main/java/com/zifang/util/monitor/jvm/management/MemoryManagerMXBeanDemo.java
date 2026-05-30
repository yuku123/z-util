package com.zifang.util.monitor.jvm.management;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryManagerMXBean;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * 用于 Java 虚拟机的内存管理接口。
 * <p>
 * 该MXBean提供查看内存管理器信息的方法。
 *
 * @author zifang
 */
/**
 * MemoryManagerMXBeanDemo类。
 */
public class MemoryManagerMXBeanDemo {


    /**
     * 获取指定JVM的内存管理信息。
     * <p>
     * 远程程序启动JVM参数配置开启远程监控：
     * <ul>
     *   <li>-Djava.rmi.server.hostname=192.168.10.105</li>
     *   <li>-Dcom.sun.management.jmxremote</li>
     *   <li>-Dcom.sun.management.jmxremote.port=9999</li>
     *   <li>-Dcom.sun.management.jmxremote.ssl=false</li>
     *   <li>-Dcom.sun.management.jmxremote.authenticate=false</li>
     * </ul>
     */
    /**
     * getRemoteMemoryManagerMXBean方法。
     * @return static void类型返回值
     */
    public static void getRemoteMemoryManagerMXBean() {
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
            ObjectName mmName = new ObjectName(ManagementFactory.MEMORY_MANAGER_MXBEAN_DOMAIN_TYPE + ",*");

            Set<ObjectName> mbeans = mbs.queryNames(mmName, null);
            if (mbeans != null) {

                Iterator<ObjectName> iterator = mbeans.iterator();
                while (iterator.hasNext()) {
                    ObjectName objName = iterator.next();
                    MemoryManagerMXBean mm = ManagementFactory.newPlatformMXBeanProxy(mbs, objName.getCanonicalName(),
                            MemoryManagerMXBean.class);
                    //返回此内存管理器管理的内存池名称。
                    String[] poolNames = mm.getMemoryPoolNames();
                    System.out.println(Arrays.toString(poolNames));
                    //返回表示此内存管理器的名称。
                    System.out.println("内存管理器的名称:" + mm.getName());

                }
            }

        } catch (Exception e) {
        }

    }

    /**
     * 获取当前虚拟机内存管理信息。
     */
    /**
     * getLocalMemoryManagerMXBean方法。
     * @return static void类型返回值
     */
    public static void getLocalMemoryManagerMXBean() {
        List<MemoryManagerMXBean> list = ManagementFactory.getMemoryManagerMXBeans();
        if (list != null) {
            for (MemoryManagerMXBean memoryManagerMXBean : list) {
                //返回此内存管理器管理的内存池名称。
                String[] poolNames = memoryManagerMXBean.getMemoryPoolNames();
                System.out.println(Arrays.toString(poolNames));
                //返回表示此内存管理器的名称。
                System.out.println("内存管理器的名称:" + memoryManagerMXBean.getName());
            }
        }
    }

    /**
     * 主方法，演示获取JVM内存管理信息。
     *
     * @param args 命令行参数
     */
    /**
     * main方法。
     *      * @param args String[]类型参数
     * @return static void类型返回值
     */
    public static void main(String[] args) {
        getRemoteMemoryManagerMXBean();
        getLocalMemoryManagerMXBean();
    }

}