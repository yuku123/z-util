package com.zifang.util.monitor.jvm.ref;

import org.junit.Test;
import static org.junit.Assert.*;

public class RefMainThreadTest {

    @Test
    public void testRefMainThreadExists() {
        RefMainThread demo = new RefMainThread();
        assertNotNull(demo);
    }
}
