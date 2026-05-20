package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

public class OperatingSystemMXBeanDemoTest {

    @Test
    public void testOperatingSystemMXBeanDemoExists() {
        OperatingSystemMXBeanDemo demo = new OperatingSystemMXBeanDemo();
        assertNotNull(demo);
    }
}
