package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassLoadingMXBeanDemoTest {

    @Test
    public void testClassLoadingMXBeanDemoExists() {
        ClassLoadingMXBeanDemo demo = new ClassLoadingMXBeanDemo();
        assertNotNull(demo);
    }
}
