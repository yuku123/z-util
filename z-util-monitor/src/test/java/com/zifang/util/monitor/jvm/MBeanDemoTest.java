package com.zifang.util.monitor.jvm;

import org.junit.Test;
import static org.junit.Assert.*;

public class MBeanDemoTest {

    @Test
    public void testMBeanDemoExists() {
        MBeanDemo demo = new MBeanDemo();
        assertNotNull(demo);
    }
}
