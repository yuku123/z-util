package com.zifang.util.monitor.thread;

import org.junit.Test;
import static org.junit.Assert.*;

public class StatusLevelTest {

    @Test
    public void testStatusLevelValues() {
        assertNotNull(StatusLevel.OK);
        assertNotNull(StatusLevel.ERROR);
    }

    @Test
    public void testStatusLevelCount() {
        assertEquals(2, StatusLevel.values().length);
    }

    @Test
    public void testStatusLevelOrdinal() {
        assertEquals(0, StatusLevel.OK.ordinal());
        assertEquals(1, StatusLevel.ERROR.ordinal());
    }
}
