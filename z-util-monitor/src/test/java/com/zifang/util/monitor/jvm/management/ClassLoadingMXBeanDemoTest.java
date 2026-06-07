package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ClassLoadingMXBeanDemoTest类。
 */
public class ClassLoadingMXBeanDemoTest {

    @Test
    /**
     * testClassLoadingMXBeanDemoExists方法。
     */
    public void testClassLoadingMXBeanDemoExists() {
        ClassLoadingMXBeanDemo demo = new ClassLoadingMXBeanDemo();
        assertNotNull(demo);
    }
}
