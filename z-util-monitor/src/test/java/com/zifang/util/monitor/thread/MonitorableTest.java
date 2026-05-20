package com.zifang.util.monitor.thread;

import com.zifang.util.monitor.thread.alarm.AlarmPolicy;
import com.zifang.util.monitor.thread.alarm.AlarmService;
import com.zifang.util.monitor.thread.alarm.LogAlarmService;
import org.junit.Test;
import static org.junit.Assert.*;

public class MonitorableTest {

    @Test
    public void testMonitorableInterfaceExists() {
        Monitorable monitorable = new Monitorable() {
            @Override
            public Status status() {
                Status status = new Status();
                status.setLevel(StatusLevel.OK);
                status.setStatus("Test");
                return status;
            }

            @Override
            public String componentName() {
                return "TestComponent";
            }

            @Override
            public void alarm() {
                // do nothing
            }
        };

        assertNotNull(monitorable.status());
        assertEquals("TestComponent", monitorable.componentName());
    }
}
