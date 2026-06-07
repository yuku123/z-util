package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MemoryManagerMXBeanDemoTest类。
 */
public class MemoryManagerMXBeanDemoTest {

    @Test
    /**
     * testMemoryManagerMXBeanDemoExists方法。
     */
    public void testMemoryManagerMXBeanDemoExists() {
        MemoryManagerMXBeanDemo demo = new MemoryManagerMXBeanDemo();
        assertNotNull(demo);
    }
}
