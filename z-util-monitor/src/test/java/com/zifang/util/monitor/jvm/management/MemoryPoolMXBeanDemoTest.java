package com.zifang.util.monitor.jvm.management;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * MemoryPoolMXBeanDemoTest类。
 */
public class MemoryPoolMXBeanDemoTest {

    @Test
    /**
     * testMemoryPoolMXBeanDemoExists方法。
     */
    public void testMemoryPoolMXBeanDemoExists() {
        MemoryPoolMXBeanDemo demo = new MemoryPoolMXBeanDemo();
        assertNotNull(demo);
    }
}
