package com.zifang.util.monitor.jvm.management;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * ThreadMXBeanDemoTest类。
 */
public class ThreadMXBeanDemoTest {

    @Test
    /**
     * testThreadMXBeanDemoExists方法。
     */
    public void testThreadMXBeanDemoExists() {
        ThreadMXBeanDemo demo = new ThreadMXBeanDemo();
        assertNotNull(demo);
    }
}
