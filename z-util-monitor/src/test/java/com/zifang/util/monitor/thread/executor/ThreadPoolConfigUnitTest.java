package com.zifang.util.monitor.thread.executor;

import com.zifang.util.monitor.thread.alarm.AlarmPolicy;
import com.zifang.util.monitor.thread.alarm.AlarmService;
import com.zifang.util.monitor.thread.alarm.LogAlarmService;
import com.zifang.util.monitor.thread.alarm.ThreadPoolOvertimeAlarmPolicy;
import com.zifang.util.monitor.thread.utility.TimeUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ThreadPoolConfigUnitTest {

    private ThreadPoolConfigUnit configUnit;

    @Before
    public void setUp() {
        configUnit = new ThreadPoolConfigUnit();
    }

    @Test
    public void testDefaultValues() {
        assertEquals(180000, configUnit.getThreadOvertimeThreshhold());
        assertEquals(10000, configUnit.getMonitorInterval());
    }

    @Test
    public void testPoolName() {
        configUnit.setPoolName("TestPool");
        assertEquals("TestPool", configUnit.getPoolName());
    }

    @Test
    public void testPoolSize() {
        configUnit.setPoolSize(10);
        assertEquals(10, configUnit.getPoolSize());
    }

    @Test
    public void testComputeType() {
        configUnit.setComputeType((byte) 1);
        assertEquals(1, configUnit.getComputeType());
    }

    @Test
    public void testTaskType() {
        configUnit.setTaskType(String.class);
        assertEquals(String.class, configUnit.getTaskType());
    }

    @Test
    public void testThreadOvertimeThreshhold() {
        configUnit.setThreadOvertimeThreshhold(60000);
        assertEquals(60000, configUnit.getThreadOvertimeThreshhold());
    }

    @Test
    public void testAlarmPolicy() {
        AlarmPolicy policy = new ThreadPoolOvertimeAlarmPolicy();
        configUnit.setAlarmPolicy(policy);
        assertEquals(policy, configUnit.getAlarmPolicy());
    }

    @Test
    public void testAlarmService() {
        AlarmService service = new LogAlarmService();
        configUnit.setAlarmService(service);
        assertEquals(service, configUnit.getAlarmService());
    }

    @Test
    public void testMonitorInterval() {
        configUnit.setMonitorInterval(20000);
        assertEquals(20000, configUnit.getMonitorInterval());
    }

    @Test
    public void testToString() {
        configUnit.setPoolName("TestPool");
        configUnit.setPoolSize(5);
        String result = configUnit.toString();
        assertTrue(result.contains("TestPool"));
        assertTrue(result.contains("5"));
    }
}
