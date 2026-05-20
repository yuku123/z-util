package com.zifang.util.monitor.thread.task;

import com.zifang.util.monitor.thread.Monitorable;
import com.zifang.util.monitor.thread.Status;
import com.zifang.util.monitor.thread.StatusLevel;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MonitorTaskTest {

    @Test
    public void testMonitorTaskCreation() {
        List<Monitorable> monitorables = new ArrayList<>();
        MonitorTask task = new MonitorTask(monitorables);
        assertNotNull(task);
    }

    @Test
    public void testMonitorTaskWithMonitorables() {
        List<Monitorable> monitorables = new ArrayList<>();
        monitorables.add(new TestMonitorable("Test1"));
        monitorables.add(new TestMonitorable("Test2"));

        MonitorTask task = new MonitorTask(monitorables);
        assertNotNull(task);
    }

    @Test
    public void testMonitorTaskRun() {
        List<Monitorable> monitorables = new ArrayList<>();
        monitorables.add(new TestMonitorable("Test"));
        MonitorTask task = new MonitorTask(monitorables);
        task.run(); // Should not throw exception
    }

    private static class TestMonitorable implements Monitorable {
        private final String name;

        TestMonitorable(String name) {
            this.name = name;
        }

        @Override
        public Status status() {
            Status status = new Status();
            status.setLevel(StatusLevel.OK);
            status.setStatus("OK");
            return status;
        }

        @Override
        public String componentName() {
            return name;
        }

        @Override
        public void alarm() {
            // do nothing
        }
    }
}
