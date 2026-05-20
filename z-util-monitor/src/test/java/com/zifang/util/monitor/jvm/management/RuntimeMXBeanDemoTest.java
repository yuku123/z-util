package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

public class RuntimeMXBeanDemoTest {

    @Test
    public void testRuntimeMXBeanDemoExists() {
        RuntimeMXBeanDemo demo = new RuntimeMXBeanDemo();
        assertNotNull(demo);
    }
}
