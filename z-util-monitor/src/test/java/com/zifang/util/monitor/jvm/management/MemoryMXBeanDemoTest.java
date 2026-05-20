package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryMXBeanDemoTest {

    @Test
    public void testMemoryMXBeanDemoExists() {
        MemoryMXBeanDemo demo = new MemoryMXBeanDemo();
        assertNotNull(demo);
    }
}
