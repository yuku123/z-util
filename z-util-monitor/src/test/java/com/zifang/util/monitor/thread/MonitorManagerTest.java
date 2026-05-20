package com.zifang.util.monitor.thread;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class MonitorManagerTest {

    private MonitorManager monitorManager;

    @Before
    public void setUp() {
        monitorManager = new MonitorManager();
    }

    @After
    public void tearDown() {
        if (monitorManager != null) {
            monitorManager.shutdown(false);
        }
    }

    @Test
    public void testMonitorManagerCreation() {
        assertNotNull(monitorManager);
    }

    @Test
    public void testAddAllMonitor() {
        List<Monitorable> monitorables = new ArrayList<>();
        Map<Long, List<Monitorable>> aggregation = new HashMap<>();
        aggregation.put(1000L, monitorables);

        monitorManager.addAllMonitor(aggregation);
        // Should not throw exception
    }

    @Test(expected = IllegalStateException.class)
    public void testAddAllMonitorTwice() {
        List<Monitorable> monitorables = new ArrayList<>();
        Map<Long, List<Monitorable>> aggregation = new HashMap<>();
        aggregation.put(1000L, monitorables);

        monitorManager.addAllMonitor(aggregation);
        monitorManager.addAllMonitor(aggregation); // Should throw exception
    }

    @Test
    public void testShutdown() {
        monitorManager.shutdown(false);
        monitorManager.shutdown(true);
        // Should not throw exception
    }
}
