package com.zifang.util.monitor.jvm.management;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * 用于 Java 虚拟机的内存池的管理接口。
 * <p>
 * 该MXBean提供查看各内存池使用情况的方法，包括Eden Space、Survivor Space、Old Gen、Metaspace等。
 *
 * @author zifang
 */
public class MemoryPoolMXBeanDemo {


    /**
     * 获取指定JVM的内存池信息。
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
    public static void getRemoteMemoryPoolMXBean() {
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
            ObjectName poolName = new ObjectName(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + ",*");

            Set<ObjectName> mbeans = mbs.queryNames(poolName, null);
            if (mbeans != null) {

                Iterator<ObjectName> iterator = mbeans.iterator();
                while (iterator.hasNext()) {
                    ObjectName objName = iterator.next();
                    System.out.println("objName:" + objName.getCanonicalName());
                    MemoryPoolMXBean p = ManagementFactory.newPlatformMXBeanProxy(mbs, objName.getCanonicalName(),
                            MemoryPoolMXBean.class);
                    System.out.println("name:" + p.getName());

                    MemoryUsage usage = p.getCollectionUsage();
                    if (usage == null) {
                        continue;
                    }

                    if (objName.toString().indexOf("Eden Space") != -1) {
                        System.out.println("-----------Eden Space-----------------");
                    } else if (objName.toString().indexOf("Survivor Space") != -1) {
                        System.out.println("----------------------------");
                    } else if (objName.toString().indexOf("Code Cache") != -1) {
                        System.out.println("-----------Survivor Space-----------------");
                    } else if (objName.toString().indexOf("Perm Gen") != -1) {
                        System.out.println("-----------Perm Gen-----------------");
                    } else if (objName.toString().indexOf("Old Gen") != -1) {
                        System.out.println("-----------Old Gen-----------------");
                    } else if (objName.toString().indexOf("Metaspace") != -1) {
                        System.out.println("-----------Metaspace-----------------");
                    }

                    printMemoryUsage(usage);
                }
            }
        } catch (Exception e) {
        }

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
     * 获取当前虚拟机内存池信息。
     */
    public static void getLocalMemoryPoolMXBean() {
        List<MemoryPoolMXBean> list = ManagementFactory.getMemoryPoolMXBeans();
        if (list != null) {
            for (MemoryPoolMXBean memoryPoolMXBean : list) {
                MemoryUsage usage = memoryPoolMXBean.getUsage();
                if (usage == null) {
                    continue;
                }
                String objName = memoryPoolMXBean.getName();
                if (objName.indexOf("Eden Space") != -1) {
                    System.out.println("-----------Eden Space-----------------");
                } else if (objName.indexOf("Survivor Space") != -1) {
                    System.out.println("----------------------------");
                } else if (objName.indexOf("Code Cache") != -1) {
                    System.out.println("-----------Survivor Space-----------------");
                } else if (objName.indexOf("Perm Gen") != -1) {
                    System.out.println("-----------Perm Gen-----------------");
                } else if (objName.indexOf("Old Gen") != -1) {
                    System.out.println("-----------Old Gen-----------------");
                } else if (objName.indexOf("Metaspace") != -1) {
                    System.out.println("-----------Metaspace-----------------");
                }
                printMemoryUsage(usage);
            }
        }
    }

    /**
     * 主方法，演示获取JVM内存池信息。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
//		getRemoteMemoryPoolMXBean();
        getLocalMemoryPoolMXBean();
    }

}