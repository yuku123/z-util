package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * RuntimeMXBeanDemoTest类。
 */
public class RuntimeMXBeanDemoTest {

    @Test
    /**
     * testRuntimeMXBeanDemoExists方法。
     */
    public void testRuntimeMXBeanDemoExists() {
        RuntimeMXBeanDemo demo = new RuntimeMXBeanDemo();
        assertNotNull(demo);
    }
}
