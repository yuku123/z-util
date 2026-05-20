package com.zifang.util.monitor.thread.executor;

import com.zifang.util.monitor.thread.Monitorable;
import com.zifang.util.monitor.thread.MonitorManager;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

public class ExecutorManagerTest {

    @Test
    public void testExecutorManagerCreation() {
        List<ThreadPoolConfigUnit> configList = new ArrayList<>();
        ThreadPoolConfigUnit unit = new ThreadPoolConfigUnit();
        unit.setPoolName("TestPool");
        unit.setPoolSize(2);
        configList.add(unit);

        ExecutorManager manager = new ExecutorManager(configList);
        assertNotNull(manager);
        assertNotNull(manager.getExecutor("TestPool"));
        assertNotNull(manager.getMonitorManager());

        manager.getMonitorManager().shutdown(true);
    }

    @Test
    public void testExecutorManagerWithNullList() {
        ExecutorManager manager = new ExecutorManager(null);
        assertNotNull(manager);
    }

    @Test
    public void testExecutorManagerWithEmptyList() {
        ExecutorManager manager = new ExecutorManager(new ArrayList<>());
        assertNotNull(manager);
    }

    @Test
    public void testGetExecutor() {
        List<ThreadPoolConfigUnit> configList = new ArrayList<>();
        ThreadPoolConfigUnit unit = new ThreadPoolConfigUnit();
        unit.setPoolName("TestPool");
        unit.setPoolSize(2);
        configList.add(unit);

        ExecutorManager manager = new ExecutorManager(configList);
        assertNotNull(manager.getExecutor("TestPool"));
        assertNull(manager.getExecutor("NonExistent"));

        manager.getMonitorManager().shutdown(true);
    }

    @Test
    public void testConstants() {
        assertEquals(0xF, ExecutorManager.TASK_EXECUTE_TYPE_MASK);
        assertEquals(0x1, ExecutorManager.CPU_INTENSIVE);
        assertEquals(0x2, ExecutorManager.IO_INTENSIVE);
    }
}
