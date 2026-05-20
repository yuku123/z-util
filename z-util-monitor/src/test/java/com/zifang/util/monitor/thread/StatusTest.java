package com.zifang.util.monitor.thread;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StatusTest {

    private Status status;

    @Before
    public void setUp() {
        status = new Status();
    }

    @Test
    public void testDefaultStatus() {
        assertNull(status.getStatus());
        assertNull(status.getLevel());
    }

    @Test
    public void testSetAndGetStatus() {
        String expectedStatus = "OK";
        status.setStatus(expectedStatus);
        assertEquals(expectedStatus, status.getStatus());
    }

    @Test
    public void testSetAndGetLevel() {
        status.setLevel(StatusLevel.OK);
        assertEquals(StatusLevel.OK, status.getLevel());

        status.setLevel(StatusLevel.ERROR);
        assertEquals(StatusLevel.ERROR, status.getLevel());
    }

    @Test
    public void testToString() {
        status.setStatus("TestStatus");
        status.setLevel(StatusLevel.OK);
        String result = status.toString();
        assertTrue(result.contains("TestStatus"));
        assertTrue(result.contains("OK"));
    }
}
