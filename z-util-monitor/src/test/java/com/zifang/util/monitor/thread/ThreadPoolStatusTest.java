package com.zifang.util.monitor.thread;

import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.*;

public class ThreadPoolStatusTest {

    private ThreadPoolStatus status;

    @Before
    public void setUp() {
        status = new ThreadPoolStatus();
    }

    @Test
    public void testDefaultValues() {
        assertNotNull(status.getStartTime());
        assertTrue(status.getStartTime() > 0);
        assertNotNull(status.getSubmitCount());
        assertNotNull(status.getStartCount());
        assertNotNull(status.getSucessCount());
        assertNotNull(status.getFailCount());
        assertNotNull(status.getLastStartTime());
        assertNotNull(status.getLastFinishTime());
        assertNotNull(status.getTotalTimeConsuming());
        assertNotNull(status.getAlarmTimes());
        assertNotNull(status.getThreadStatusMap());
        assertNotNull(status.getTaskStatusMap());
        assertEquals(10000, status.getMonitorInterval());
    }

    @Test
    public void testSubmitCountIncrement() {
        int before = status.getSubmitCount().get();
        status.getSubmitCount().incrementAndGet();
        assertEquals(before + 1, status.getSubmitCount().get());
    }

    @Test
    public void testStartCountIncrement() {
        int before = status.getStartCount().get();
        status.getStartCount().incrementAndGet();
        assertEquals(before + 1, status.getStartCount().get());
    }

    @Test
    public void testSucessCountIncrement() {
        int before = status.getSucessCount().get();
        status.getSucessCount().incrementAndGet();
        assertEquals(before + 1, status.getSucessCount().get());
    }

    @Test
    public void testFailCountIncrement() {
        int before = status.getFailCount().get();
        status.getFailCount().incrementAndGet();
        assertEquals(before + 1, status.getFailCount().get());
    }

    @Test
    public void testLastStartTimeUpdate() {
        long before = status.getLastStartTime().get();
        status.getLastStartTime().set(System.currentTimeMillis());
        assertTrue(status.getLastStartTime().get() >= before);
    }

    @Test
    public void testLastFinishTimeUpdate() {
        long before = status.getLastFinishTime().get();
        status.getLastFinishTime().set(System.currentTimeMillis());
        assertTrue(status.getLastFinishTime().get() >= before);
    }

    @Test
    public void testTotalTimeConsumingUpdate() {
        status.getTotalTimeConsuming().addAndGet(100);
        assertEquals(100, status.getTotalTimeConsuming().get());
    }

    @Test
    public void testAlarmTimesIncrement() {
        int before = status.getAlarmTimes().get();
        status.getAlarmTimes().incrementAndGet();
        assertEquals(before + 1, status.getAlarmTimes().get());
    }

    @Test
    public void testMonitorIntervalSetter() {
        status.setMonitorInterval(20000);
        assertEquals(20000, status.getMonitorInterval());
    }

    @Test
    public void testToString() {
        String result = status.toString();
        assertNotNull(result);
        assertTrue(result.contains("ThreadPoolStatus"));
    }
}
