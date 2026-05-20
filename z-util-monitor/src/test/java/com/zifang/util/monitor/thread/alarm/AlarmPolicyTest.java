package com.zifang.util.monitor.thread.alarm;

import com.zifang.util.monitor.thread.Status;
import com.zifang.util.monitor.thread.StatusLevel;
import org.junit.Test;
import static org.junit.Assert.*;

public class AlarmPolicyTest {

    @Test
    public void testAlarmPolicyAbstract() {
        // Test concrete implementation
        AlarmPolicy policy = new AlarmPolicy() {
            @Override
            public boolean needAlarm(Status status) {
                return status.getLevel() == StatusLevel.ERROR;
            }
        };

        Status okStatus = new Status();
        okStatus.setLevel(StatusLevel.OK);
        assertFalse(policy.needAlarm(okStatus));

        Status errorStatus = new Status();
        errorStatus.setLevel(StatusLevel.ERROR);
        assertTrue(policy.needAlarm(errorStatus));
    }
}
