package com.zifang.util.monitor.thread.executor;

import com.zifang.util.monitor.thread.Monitorable;
import com.zifang.util.monitor.thread.Status;
import com.zifang.util.monitor.thread.StatusLevel;
import com.zifang.util.monitor.thread.alarm.LogAlarmService;
import org.junit.Test;
import org.junit.After;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

public class MonitorableExecutorTest {

    @Test
    public void testMonitorableExecutorInterface() {
        // Test that the interface extends Monitorable and Alarmable
        MonitorableExecutor executor = new FixedMonitorableExecutor(
                createConfigUnit(),
                new java.util.concurrent.LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory()
        );

        assertNotNull(executor);
        assertNotNull(executor.status());
        executor.shutdown(true);
    }

    private ThreadPoolConfigUnit createConfigUnit() {
        ThreadPoolConfigUnit unit = new ThreadPoolConfigUnit();
        unit.setPoolName("TestPool");
        unit.setPoolSize(2);
        unit.setAlarmService(new LogAlarmService());
        return unit;
    }
}
