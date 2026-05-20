package com.zifang.util.monitor;

import org.junit.Test;
import static org.junit.Assert.*;

public class NetMonitorTest {

    @Test
    public void testNetMonitorExists() {
        com.zifang.util.monitor.net.NetMonitor monitor = new com.zifang.util.monitor.net.NetMonitor();
        assertNotNull(monitor);
    }
}
