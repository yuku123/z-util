package com.zifang.util.monitor.thread.alarm;

import org.junit.Test;
import static org.junit.Assert.*;

public class AlarmServiceTest {

    @Test
    public void testLogAlarmServiceAlarm() {
        AlarmService alarmService = new LogAlarmService();
        // Should not throw exception
        alarmService.alarm("test message");
        alarmService.alarm();
        alarmService.alarm("arg1", "arg2", "arg3");
    }

    @Test
    public void testLogAlarmServiceAlarmWithNull() {
        AlarmService alarmService = new LogAlarmService();
        // Should not throw exception
        alarmService.alarm((Object[]) null);
    }
}
