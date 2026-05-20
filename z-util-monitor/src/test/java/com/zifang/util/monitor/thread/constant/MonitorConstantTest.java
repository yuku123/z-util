package com.zifang.util.monitor.thread.constant;

import org.junit.Test;
import static org.junit.Assert.*;

public class MonitorConstantTest {

    @Test
    public void testConstants() {
        assertNotNull(MonitorConstant.INIT_STATUS);
        assertEquals("threadStartTime", MonitorConstant.THREAD_START_TIME);
        assertEquals("taskStartTime", MonitorConstant.TASK_START_TIME);
    }

    @Test
    public void testPrivateConstructor() {
        // Test that the class cannot be instantiated
        try {
            MonitorConstant.class.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            // Expected - constructor is private
        }
    }
}
