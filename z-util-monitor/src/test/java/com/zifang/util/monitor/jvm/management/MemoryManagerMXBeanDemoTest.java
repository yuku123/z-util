package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryManagerMXBeanDemoTest {

    @Test
    public void testMemoryManagerMXBeanDemoExists() {
        MemoryManagerMXBeanDemo demo = new MemoryManagerMXBeanDemo();
        assertNotNull(demo);
    }
}
