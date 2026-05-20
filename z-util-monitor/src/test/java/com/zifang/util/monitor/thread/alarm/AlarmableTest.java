package com.zifang.util.monitor.thread.alarm;

import org.junit.Test;
import static org.junit.Assert.*;

public class AlarmableTest {

    @Test
    public void testAlarmableInterface() {
        Alarmable alarmable = new Alarmable() {
            @Override
            public void alarm() {
                // do nothing
            }
        };
        assertNotNull(alarmable);
    }
}
