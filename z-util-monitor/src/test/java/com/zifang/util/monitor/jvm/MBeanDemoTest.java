package com.zifang.util.monitor.jvm;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MBeanDemoTest类。
 */
public class MBeanDemoTest {

    @Test
    /**
     * testMBeanDemoExists方法。
     */
    public void testMBeanDemoExists() {
        MBeanDemo demo = new MBeanDemo();
        assertNotNull(demo);
    }
}
