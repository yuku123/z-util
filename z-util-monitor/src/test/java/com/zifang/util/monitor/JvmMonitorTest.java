package com.zifang.util.monitor;

import org.junit.Test;
import static org.junit.Assert.*;

public class JvmMonitorTest {

    @Test
    public void testJvmMonitorExists() {
        com.zifang.util.monitor.jvm.JvmMonitor monitor = new com.zifang.util.monitor.jvm.JvmMonitor();
        assertNotNull(monitor);
    }
}
