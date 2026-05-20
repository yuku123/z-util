package com.zifang.util.monitor;

import org.junit.Test;
import static org.junit.Assert.*;

public class ThreadMonitorTest {

    @Test
    public void testThreadMonitorExists() {
        com.zifang.util.monitor.thread.ThreadMonitor monitor = new com.zifang.util.monitor.thread.ThreadMonitor();
        assertNotNull(monitor);
    }
}
