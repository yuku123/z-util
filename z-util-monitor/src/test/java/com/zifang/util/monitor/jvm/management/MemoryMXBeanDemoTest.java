package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MemoryMXBeanDemoTest类。
 */
public class MemoryMXBeanDemoTest {

    @Test
    /**
     * testMemoryMXBeanDemoExists方法。
     */
    public void testMemoryMXBeanDemoExists() {
        MemoryMXBeanDemo demo = new MemoryMXBeanDemo();
        assertNotNull(demo);
    }
}
