package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

public class ThreadMXBeanDemoTest {

    @Test
    public void testThreadMXBeanDemoExists() {
        ThreadMXBeanDemo demo = new ThreadMXBeanDemo();
        assertNotNull(demo);
    }
}
