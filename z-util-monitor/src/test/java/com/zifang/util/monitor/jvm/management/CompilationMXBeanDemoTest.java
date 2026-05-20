package com.zifang.util.monitor.jvm.management;

import org.junit.Test;
import static org.junit.Assert.*;

public class CompilationMXBeanDemoTest {

    @Test
    public void testCompilationMXBeanDemoExists() {
        CompilationMXBeanDemo demo = new CompilationMXBeanDemo();
        assertNotNull(demo);
    }
}
