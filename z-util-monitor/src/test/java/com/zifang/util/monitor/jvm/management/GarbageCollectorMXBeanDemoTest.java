package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

public class GarbageCollectorMXBeanDemoTest {

    @Test
    public void testGarbageCollectorMXBeanDemoExists() {
        GarbageCollectorMXBeanDemo demo = new GarbageCollectorMXBeanDemo();
        assertNotNull(demo);
    }
}
