package com.zifang.util.monitor;

import org.junit.Test;
import static org.junit.Assert.*;

public class OsMonitorTest {

    @Test
    public void testOsMonitorExists() {
        com.zifang.util.monitor.os.OsMonitor monitor = new com.zifang.util.monitor.os.OsMonitor();
        assertNotNull(monitor);
    }
}
