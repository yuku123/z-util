package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * GarbageCollectorMXBeanDemoTest类。
 */
public class GarbageCollectorMXBeanDemoTest {

    @Test
    /**
     * testGarbageCollectorMXBeanDemoExists方法。
     */
    public void testGarbageCollectorMXBeanDemoExists() {
        GarbageCollectorMXBeanDemo demo = new GarbageCollectorMXBeanDemo();
        assertNotNull(demo);
    }
}
