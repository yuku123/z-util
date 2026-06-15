package com.zifang.util.monitor.jvm.management;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * OperatingSystemMXBeanDemoTest类。
 */
public class OperatingSystemMXBeanDemoTest {

    @Test
    /**
     * testOperatingSystemMXBeanDemoExists方法。
     */
    public void testOperatingSystemMXBeanDemoExists() {
        OperatingSystemMXBeanDemo demo = new OperatingSystemMXBeanDemo();
        assertNotNull(demo);
    }
}
