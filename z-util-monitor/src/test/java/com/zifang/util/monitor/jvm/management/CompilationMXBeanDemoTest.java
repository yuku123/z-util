package com.zifang.util.monitor.jvm.management;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * CompilationMXBeanDemoTest类。
 */
public class CompilationMXBeanDemoTest {

    @Test
    /**
     * testCompilationMXBeanDemoExists方法。
     */
    public void testCompilationMXBeanDemoExists() {
        CompilationMXBeanDemo demo = new CompilationMXBeanDemo();
        assertNotNull(demo);
    }
}
